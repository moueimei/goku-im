package com.goku.im.relation.service.facade;

import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.im.pushservice.model.Notify;
import com.goku.im.relation.UserRelationCodeConst;
import com.goku.im.relation.common.GroupUserRole;
import com.goku.im.relation.entity.ImGroup;
import com.goku.im.relation.entity.ImGroupUser;
import com.goku.im.relation.model.GkImGroup;
import com.goku.im.relation.push.AsyncNotifyPusher;
import com.goku.im.relation.redis.ImGroupRedis;
import com.goku.im.relation.redis.ImGroupUserRedis;
import com.goku.im.relation.service.GkImGroupService;
import com.goku.im.relation.service.ImGroupService;
import com.goku.im.relation.service.ImGroupUserService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by moueimei on 15/12/2.
 */
@Service("gkImGroupService")
public class GkImGroupServiceImpl implements GkImGroupService {
    private static final Logger logger = LoggerFactory.getLogger(GkImGroupServiceImpl.class);

    /**
     * 用户创建群数上限
     */
    public final static int USER_CREATE_GROUP_LIMIT = 10;

    @Autowired
    ImGroupService imGroupService;
    @Autowired
    ImGroupUserRedis imGroupUserRedis;
    @Autowired
    ImGroupUserService imGroupUserService;
    @Autowired
    ImGroupRedis imGroupRedis;
    @Autowired
    AsyncNotifyPusher asyncNotifyPusher;

    @Override
    public Map<String, Object> create(int userId, String groupName, String icon, String intro) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userId <= 0 || (StringUtils.isNotEmpty(groupName) && groupName.length() > 128)) {
            resultMap.put("code", UserRelationCodeConst.PARAMETER_FORMAT_ERROR);
            return resultMap;
        }

        try {
            int groupCount = getGroupCount(userId);
            if (groupCount >= USER_CREATE_GROUP_LIMIT) {
                resultMap.put("code", UserRelationCodeConst.USER_CREATE_GROUP_ALREADY_LIMIT);
                return resultMap;
            }

            ///将群组添加到数据库
            Date date = new Date();
            ImGroup imGroup = new ImGroup();
            imGroup.setGroupName(groupName);
            imGroup.setIcon(icon);
            imGroup.setIntro(intro);
            imGroup.setCreatorId(userId);
            imGroup.setDeleted(false);
            imGroup.setCreateTime(date);
            imGroup.setUpdateTime(date);
            imGroup.setCreateBy(String.valueOf(userId));
            imGroupService.insertSelective(imGroup);

            int groupId = imGroup.getId();
            ///将群主添加到数据库
            ImGroupUser imGroupUser = new ImGroupUser();
            imGroupUser.setGroupId(groupId);
            imGroupUser.setUserId(userId);
            imGroupUser.setRole(GroupUserRole.MASTER);
            imGroupUser.setDeleted(false);
            imGroupUser.setCreateTime(date);
            imGroupUser.setUpdateTime(date);
            imGroupUser.setCreateBy(String.valueOf(userId));
            imGroupUserService.insertSelective(imGroupUser);

            ///将群组添加到redis中
            imGroupRedis.setGroupInfo(imGroup);

            ///将群主添加到redis的群成员列表中(按照角色排序)
            imGroupUserRedis.addUserIdToGroupUsers(groupId, userId, GroupUserRole.MASTER);

            resultMap.put("code", UserRelationCodeConst.SUCCESS);
            resultMap.put("groupId", groupId);
            resultMap.put("createTime", date);
            return resultMap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            resultMap.put("code", UserRelationCodeConst.SERVER_ERROR);
            return resultMap;
        }
    }

    @Override
    public int edit(int userId, int groupId, String groupName, String icon, String intro) {
        if (userId <= 0 || groupId <= 0 || groupName.length() > 128) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            ImGroup imGroup = getInfoByRedis(groupId);
            if (null == imGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            if (userId != imGroup.getCreatorId()) {
                return UserRelationCodeConst.GROUP_HANDLE_INSUFFICIENT_PERMISSIONS;
            }

            imGroup.setGroupName(groupName);
            imGroup.setIcon(icon);
            imGroup.setIntro(intro);
            imGroup.setUpdateTime(new Date());
            imGroup.setUpdateBy(String.valueOf(userId));

            ///将群组更新到数据库
            imGroupService.updateByPrimaryKey(imGroup);
            ///将群组更新到redis
            imGroupRedis.setGroupInfo(imGroup);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int dismiss(int operatorId, int groupId) {
        if (operatorId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            ImGroup imGroup = getInfoByRedis(groupId);
            if (null == imGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            int creatorId = imGroup.getCreatorId();
            if (operatorId != creatorId) {
                return UserRelationCodeConst.GROUP_HANDLE_INSUFFICIENT_PERMISSIONS;
            }

            ///删除群组
            imGroup.setDeleted(true);
            imGroup.setUpdateTime(new Date());
            imGroup.setUpdateBy(String.valueOf(operatorId));
            imGroupService.updateByPrimaryKey(imGroup);

            ///删除数据库中群组成员
            ImGroupUser imGroupUser = new ImGroupUser();
            imGroupUser.setDeleted(true);
            imGroupUser.setUpdateTime(new Date());
            imGroupUser.setUpdateBy(String.valueOf(operatorId));
            MapQuery mapQuery = MapQuery.create("groupId", groupId);
            imGroupUserService.updateByMapSelective(imGroupUser, mapQuery);

            ///删除redis中群组信息
            imGroupRedis.delete(groupId);

            ///删除redis中群组成员列表
            Set<String> groupUserIds = imGroupUserRedis.getGroupUsers(groupId, 0L, -1L);
            imGroupUserRedis.deleteGroupUsers(groupId);

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(operatorId);
            JSONObject content = new JSONObject();
            content.put("groupId", groupId);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushGroupDismiss(notify, groupUserIds);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public GkImGroup getInfo(int groupId) {
        long start = System.currentTimeMillis();
        if (groupId <= 0)
            return null;

        try {
            ImGroup imGroup = getInfoByRedis(groupId);
            if (null == imGroup || imGroup.getDeleted())
                return null;

            GkImGroup gkImGroup = new GkImGroup();
            gkImGroup.setId(imGroup.getId());
            gkImGroup.setGroupName(imGroup.getGroupName());
            gkImGroup.setIcon(imGroup.getIcon());
            gkImGroup.setIntro(imGroup.getIntro());
            gkImGroup.setCreatorId(imGroup.getCreatorId());
            gkImGroup.setCreateTime(imGroup.getCreateTime());
            System.out.println("GKImGroupServiceImpl.getInfo(" + groupId + ") cost time :" + (System.currentTimeMillis() - start));
            return gkImGroup;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public boolean isMaster(int userId, int groupId) {
        try {
            ImGroup imGroup = getInfoByRedis(groupId);
            if (null == imGroup)
                return false;

            int creatorId = imGroup.getCreatorId();
            return creatorId == userId;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return false;
        }
    }

    @Override
    public List<GkImGroup> getGroupsByIds(List<Integer> groupIds) {
        if (null == groupIds || groupIds.size() == 0)
            return null;

        List<GkImGroup> groupList = new ArrayList<>();
        GkImGroup gkImGroup;
        for (int groupId : groupIds) {
            gkImGroup = getInfo(groupId);
            groupList.add(gkImGroup);
        }
        return groupList;
    }

    /**
     * 获取用户创建的群数
     *
     * @param userId 用户ID
     */
    private int getGroupCount(int userId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("creatorId", userId);
        params.put("deleted", 0);
        MapQuery mapQuery = MapQuery.create(params);
        return imGroupService.countByQuery(mapQuery);
    }

    /**
     * 从redis中获取群组信息, 如果redis中已过期,则从数据库中获取,并存入redis中
     *
     * @param groupId 群组ID
     * @return 群组实体
     * @throws Exception
     */
    private ImGroup getInfoByRedis(int groupId) throws Exception {
        ImGroup imGroup = imGroupRedis.getGroupInfo(groupId);
        if (null != imGroup)
            return imGroup;

        ///从数据库中加载
        imGroup = imGroupService.selectByPrimaryKey(groupId);
        if (null == imGroup || imGroup.getDeleted())
            return null;

        imGroupRedis.setGroupInfo(imGroup);
        return imGroup;
    }
}