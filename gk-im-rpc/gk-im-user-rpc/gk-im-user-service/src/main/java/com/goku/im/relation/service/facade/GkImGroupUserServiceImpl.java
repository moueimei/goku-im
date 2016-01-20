package com.goku.im.relation.service.facade;

import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.im.pushservice.model.Notify;
import com.goku.im.relation.UserRelationCodeConst;
import com.goku.im.relation.common.GroupReplyType;
import com.goku.im.relation.common.GroupUserRole;
import com.goku.im.relation.entity.ImGroup;
import com.goku.im.relation.entity.ImGroupUser;
import com.goku.im.relation.model.GkImGroup;
import com.goku.im.relation.model.GkImGroupUser;
import com.goku.im.relation.push.AsyncNotifyPusher;
import com.goku.im.relation.redis.ImGroupRedis;
import com.goku.im.relation.redis.ImGroupUserRedis;
import com.goku.im.relation.service.GkImGroupService;
import com.goku.im.relation.service.GkImGroupUserService;
import com.goku.im.relation.service.ImGroupService;
import com.goku.im.relation.service.ImGroupUserService;
import com.goku.im.relation.util.UserManager;
import com.goku.user.model.GkUser;
import com.goku.user.service.GkUserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by moueimei on 15/12/2.
 */
@Service("gkImGroupUserService")
public class GkImGroupUserServiceImpl implements GkImGroupUserService {
    private static final Logger logger = LoggerFactory.getLogger(GkImGroupUserServiceImpl.class);

    /**
     * 群组成员上限
     */
    public final static int GROUP_USER_LIMIT = 500;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ImGroupService imGroupService;
    @Autowired
    ImGroupRedis imGroupRedis;
    @Autowired
    ImGroupUserService imGroupUserService;
    @Autowired
    ImGroupUserRedis imGroupUserRedis;
    @Autowired
    UserManager userManager;
    @Autowired
    GkUserService gkUserService;
    @Autowired
    AsyncNotifyPusher asyncNotifyPusher;
    @Autowired
    GkImGroupService gkImGroupService;

    @Override
    public int apply(int userId, int groupId) {
        if (userId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
            if (null == gkImGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            boolean isGroupUser = isGroupUser(userId, groupId);
            if (isGroupUser) {
                ///该用户已是群组成员
                return UserRelationCodeConst.USER_IS_GROUP_USER;
            }

            int count = getGroupUserCount(groupId);
            if (count >= GROUP_USER_LIMIT) {
                ///已达群组成员上限
                return UserRelationCodeConst.GROUP_USER_ALREADY_LIMIT;
            }

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(userId);
            List<Integer> toUserIds = new ArrayList<>();
            toUserIds.add(gkImGroup.getCreatorId());
            notify.setToUserIds(toUserIds);
            JSONObject content = new JSONObject();
            content.put("groupId", groupId);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushGroupUserApply(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int reply(int operatorId, int userId, int groupId, int replyType) {
        if (operatorId <= 0 || userId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
            if (null == gkImGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            boolean isMaster = (operatorId == gkImGroup.getCreatorId());
            if (!isMaster) {
                return UserRelationCodeConst.GROUP_HANDLE_INSUFFICIENT_PERMISSIONS;
            }

            if (replyType == GroupReplyType.AGREE) {
                boolean isGroupUser = isGroupUser(userId, groupId);
                if (isGroupUser) {
                    ///该用户已是群组成员
                    return UserRelationCodeConst.USER_IS_GROUP_USER;
                }

                int count = getGroupUserCount(groupId);
                if (count >= GROUP_USER_LIMIT) {
                    ///已达群组成员上限
                    return UserRelationCodeConst.GROUP_USER_ALREADY_LIMIT;
                }

                ImGroupUser imGroupUser = existsRecord(userId, groupId);
                if (null != imGroupUser)    ///之前加入过该群组, 但被被删除过, 更新deleted字段为0
                {
                    imGroupUser.setDeleted(false);
                    imGroupUserService.updateByPrimaryKey(imGroupUser);
                } else           ///之前没有加入过该群组, 直接插入记录
                {
                    ///将群组成员添加到数据库
                    Date date = new Date();
                    imGroupUser = new ImGroupUser();
                    imGroupUser.setGroupId(groupId);
                    imGroupUser.setUserId(userId);
                    imGroupUser.setRole(GroupUserRole.MEMBER);
                    imGroupUser.setDeleted(false);
                    imGroupUser.setCreateTime(date);
                    imGroupUser.setUpdateTime(date);
                    imGroupUser.setCreateBy(String.valueOf(operatorId));
                    imGroupUserService.insertSelective(imGroupUser);
                }

                ///将群组成员添加到redis
                imGroupUserRedis.addUserIdToGroupUsers(groupId, userId, GroupUserRole.MEMBER);

                ///调用pushservice发送通知

                ///给申请入群的用户发送通知
                Notify userNotify = new Notify();
                userNotify.setFromUserId(operatorId);
                List<Integer> toUserIds = new ArrayList<>();
                toUserIds.add(userId);
                userNotify.setToUserIds(toUserIds);
                JSONObject userContent = new JSONObject();
                JSONObject jsonGroup = new JSONObject();
                jsonGroup.put("id", gkImGroup.getId());
                jsonGroup.put("groupName", gkImGroup.getGroupName());
                jsonGroup.put("icon", gkImGroup.getIcon());
                jsonGroup.put("intro", gkImGroup.getIntro());
                jsonGroup.put("createTime", dateFormat.format(gkImGroup.getCreateTime()));
                jsonGroup.put("creatorId", gkImGroup.getCreatorId());
                userContent.put("group", jsonGroup);
                userContent.put("replyType", replyType);
                userNotify.setContent(userContent.toString());
                asyncNotifyPusher.pushGroupUserReply(userNotify);

                ///给群里其他用户发送新用户入群通知
                List<Integer> exceptList = new ArrayList<>();
                exceptList.add(userId);

                Notify otherNotify = new Notify();
                otherNotify.setFromUserId(operatorId);
                JSONObject otherContent = new JSONObject();
                JSONObject jsonAddUser = new JSONObject();
                jsonAddUser.put("puId", userId);
                otherContent.put("groupId", groupId);
                otherContent.put("addUser", jsonAddUser);
                otherNotify.setContent(otherContent.toString());
                asyncNotifyPusher.pushGroupNewUserAdd(otherNotify, exceptList);
            }

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int delete(int operatorId, int userId, int groupId) {
        if (operatorId <= 0 || userId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
            if (null == gkImGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            boolean isMaster = (operatorId == gkImGroup.getCreatorId());
            if (!isMaster) {
                return UserRelationCodeConst.GROUP_HANDLE_INSUFFICIENT_PERMISSIONS;
            }

            boolean isGroupUser = isGroupUser(userId, groupId);
            if (!isGroupUser) {
                ///该用户不是群组成员
                return UserRelationCodeConst.USER_IS_NOT_GROUP_USER;
            }

            ///从数据库中删除群组用户(逻辑删除 deleted=1)
            ImGroupUser imGroupUser = new ImGroupUser();
            imGroupUser.setDeleted(true);
            imGroupUser.setUpdateTime(new Date());
            imGroupUser.setUpdateBy(String.valueOf(operatorId));
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("groupId", groupId);
            MapQuery mapQuery = MapQuery.create(params);
            imGroupUserService.updateByMapSelective(imGroupUser, mapQuery);

            ///从redis中删除群组用户
            imGroupUserRedis.deleteUserIdFromGroupUsers(groupId, userId);

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(operatorId);
            List<Integer> toUserIds = getGroupUserIds(groupId);
            toUserIds.add(userId);             ///因为userId在上面的方法中已经从redis里删除了,所以要重新添加一下
            notify.setToUserIds(toUserIds);
            JSONObject content = new JSONObject();
            content.put("groupId", groupId);
            content.put("deleteUserId", userId);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushGroupUserDelete(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int quit(int userId, int groupId) {
        if (userId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
            if (null == gkImGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            boolean isGroupUser = isGroupUser(userId, groupId);
            if (!isGroupUser) {
                ///该用户不是群组成员
                return UserRelationCodeConst.USER_IS_NOT_GROUP_USER;
            }

            if (userId == gkImGroup.getCreatorId())    ///如果是群主退群,则执行解散群操作
            {
                return gkImGroupService.dismiss(userId, groupId);
            }

            ///从数据库中删除群组用户(逻辑删除 deleted=1)
            ImGroupUser imGroupUser = new ImGroupUser();
            imGroupUser.setDeleted(true);
            imGroupUser.setUpdateTime(new Date());
            imGroupUser.setUpdateBy(String.valueOf(userId));
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("groupId", groupId);
            MapQuery mapQuery = MapQuery.create(params);
            imGroupUserService.updateByMapSelective(imGroupUser, mapQuery);

            ///从redis中删除群组用户
            imGroupUserRedis.deleteUserIdFromGroupUsers(groupId, userId);

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(userId);
            List<Integer> toUserIds = getGroupUserIds(groupId);
            toUserIds.add(userId);     ///因为userId在上面的方法中已经从redis里删除了,所以要重新添加一下
            notify.setToUserIds(toUserIds);
            JSONObject content = new JSONObject();
            content.put("groupId", groupId);
            content.put("deleteUserId", userId);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushGroupUserQuit(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int invite(int operatorId, int userId, int groupId) {
        if (operatorId <= 0 || userId <= 0 || groupId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
            if (null == gkImGroup) {
                return UserRelationCodeConst.GROUP_NOT_EXISTS;
            }

            boolean isMaster = (operatorId == gkImGroup.getCreatorId());
            if (!isMaster) {
                return UserRelationCodeConst.GROUP_HANDLE_INSUFFICIENT_PERMISSIONS;
            }

            boolean isGroupUser = isGroupUser(userId, groupId);
            if (isGroupUser) {
                ///该用户已是群组成员
                return UserRelationCodeConst.USER_IS_GROUP_USER;
            }

            int count = getGroupUserCount(groupId);
            if (count >= GROUP_USER_LIMIT) {
                ///已达群组成员上限
                return UserRelationCodeConst.GROUP_USER_ALREADY_LIMIT;
            }

            ImGroupUser imGroupUser = existsRecord(userId, groupId);
            if (null != imGroupUser)    ///之前加入过该群组, 但被被删除过, 更新deleted字段为0
            {
                imGroupUser.setDeleted(false);
                imGroupUserService.updateByPrimaryKey(imGroupUser);
            } else                        ///之前没有加入过该群组, 直接插入记录
            {
                ///将群组成员添加到数据库
                Date date = new Date();
                imGroupUser = new ImGroupUser();
                imGroupUser.setGroupId(groupId);
                imGroupUser.setUserId(userId);
                imGroupUser.setRole(GroupUserRole.MEMBER);
                imGroupUser.setDeleted(false);
                imGroupUser.setCreateTime(date);
                imGroupUser.setUpdateTime(date);
                imGroupUser.setCreateBy(String.valueOf(operatorId));
                imGroupUserService.insertSelective(imGroupUser);
            }

            ///将群组成员添加到redis
            imGroupUserRedis.addUserIdToGroupUsers(groupId, userId, GroupUserRole.MEMBER);

            ///调用pushservice发送通知

            ///给被邀请用户发送通知
            Notify notify = new Notify();
            notify.setFromUserId(operatorId);
            List<Integer> toUserIds = new ArrayList<>();
            toUserIds.add(userId);
            notify.setToUserIds(toUserIds);
            JSONObject content = new JSONObject();
            JSONObject jsonGroup = new JSONObject();
            jsonGroup.put("id", gkImGroup.getId());
            jsonGroup.put("groupName", gkImGroup.getGroupName());
            jsonGroup.put("icon", gkImGroup.getIcon());
            jsonGroup.put("intro", gkImGroup.getIntro());
            jsonGroup.put("createTime", dateFormat.format(gkImGroup.getCreateTime()));
            jsonGroup.put("creatorId", gkImGroup.getCreatorId());
            content.put("group", jsonGroup);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushGroupUserAdd(notify);

            ///给群里其他用户发送新用户入群通知
            List<Integer> exceptList = new ArrayList<>();
            exceptList.add(userId);

            Notify otherNotify = new Notify();
            otherNotify.setFromUserId(operatorId);
            JSONObject otherContent = new JSONObject();
            JSONObject jsonAddUser = new JSONObject();
            jsonAddUser.put("puId", userId);
            otherContent.put("groupId", groupId);
            otherContent.put("addUser", jsonAddUser);
            otherNotify.setContent(otherContent.toString());
            asyncNotifyPusher.pushGroupNewUserAdd(otherNotify, exceptList);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public boolean isGroupUser(int userId, int groupId) {
        if (userId <= 0 || groupId <= 0)
            return false;

        try {
            ///先创建redis 结构
            buildRedis(groupId);

            ///从redis中获取
            return imGroupUserRedis.isGroupMember(groupId, userId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return false;
        }
    }

    @Override
    public List<GkImGroupUser> getGroupUsers(int groupId, int page, int size) {
        if (groupId <= 0)
            return null;

        try {
            ///先创建redis 结构
            buildRedis(groupId);

            Map<String, Object> params = new HashMap<>();
            params.put("groupId", groupId);
            params.put("deleted", 0);
            MapQuery mapQuery = MapQuery.create(params);
            List<ImGroupUser> list = imGroupUserService.selectByQuery(mapQuery);
            if (null == list || list.size() == 0)
                return null;

            List<GkImGroupUser> userList = new ArrayList<>();
            GkImGroupUser groupUser;
            GkUser gkUser;
            for (ImGroupUser imGroupUser : list) {
                groupUser = new GkImGroupUser();
                groupUser.setPuId(imGroupUser.getUserId());
                groupUser.setGroupId(groupId);
                groupUser.setRole(imGroupUser.getRole());
                groupUser.setCreateTime(imGroupUser.getCreateTime());

                gkUser = userManager.getInfo(imGroupUser.getUserId());
                if (null != gkUser) {
                    groupUser.setNick(gkUser.getNick());
                    groupUser.setPhoto(gkUser.getPhoto());
                    groupUser.setSex(gkUser.getSex());
                }
                userList.add(groupUser);
            }
            return userList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public List<GkImGroup> getUserGroups(int userId) {
        if (userId <= 0)
            return null;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("deleted", 0);
            MapQuery mapQuery = MapQuery.create(params);
            List<ImGroupUser> groupUsers = imGroupUserService.selectByQuery(mapQuery);
            if (null == groupUsers || groupUsers.size() == 0)
                return null;

            int arrLen = groupUsers.size();
            Map<Integer, Date> joinTimeMap = new HashMap<>();
            int[] ids = new int[arrLen];
            for (int i = 0; i < arrLen; i++) {
                ids[i] = groupUsers.get(i).getGroupId();
                joinTimeMap.put(groupUsers.get(i).getGroupId(), groupUsers.get(i).getCreateTime());
            }

            params.clear();
            params.put("ids", ids);
            params.put("deleted", 0);
            mapQuery = MapQuery.create(params);
            List<ImGroup> list = imGroupService.selectByGroupIds(mapQuery);
            if (null == list || list.size() == 0)
                return null;

            List<GkImGroup> groupList = new ArrayList<>();
            GkImGroup gkImGroup;
            for (ImGroup imGroup : list) {
                gkImGroup = new GkImGroup();
                gkImGroup.setId(imGroup.getId());
                gkImGroup.setGroupName(imGroup.getGroupName());
                gkImGroup.setIcon(imGroup.getIcon());
                gkImGroup.setIntro(imGroup.getIntro());
                gkImGroup.setCreatorId(imGroup.getCreatorId());
                gkImGroup.setCreateTime(imGroup.getCreateTime());
                gkImGroup.setUserCount(getGroupUserCount(imGroup.getId()));

                ///获取加入群组时间
                Date joinTime = new Date();
                if (joinTimeMap.containsKey(imGroup.getId()))
                    joinTime = joinTimeMap.get(imGroup.getId());
                gkImGroup.setJoinTime(joinTime);
                groupList.add(gkImGroup);
            }
            return groupList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public List<Integer> getGroupUserIds(int groupId) {
        if (groupId <= 0)
            return null;

        try {
            ///先创建redis 结构
            buildRedis(groupId);

            Set<String> set = imGroupUserRedis.getGroupUsers(groupId, 0L, -1L);
            if (null == set || set.size() == 0)
                return null;

            List<Integer> userIdList = new ArrayList<>();
            set.forEach(userId -> userIdList.add(Integer.parseInt(userId)));
            return userIdList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public int getGroupUserCount(int groupId) {
        if (groupId <= 0)
            return 0;

        try {
            ///先创建redis 结构
            buildRedis(groupId);

            return imGroupUserRedis.getGroupUserCount(groupId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return 0;
        }
    }

    private ImGroupUser existsRecord(int userId, int groupId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        MapQuery mapQuery = MapQuery.create(params);
        List<ImGroupUser> list = imGroupUserService.selectByQuery(mapQuery);
        if (null == list || list.size() == 0)
            return null;

        return list.get(0);
    }

    /**
     * 创建redis 群组成员列表(redis会过期,所以第一次访问时,需要创建)
     *
     * @param groupId 群组ID
     * @throws Exception
     */
    private void buildRedis(int groupId) throws Exception {
        boolean exists = imGroupUserRedis.exists(groupId);
        if (exists)
            return;

        ///从数据库中加载
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("deleted", 0);
        MapQuery mapQuery = MapQuery.create(params);
        List<ImGroupUser> list = imGroupUserService.selectByQuery(mapQuery);
        if (null != list && list.size() > 0) {
            for (ImGroupUser imGroupUser : list)
                imGroupUserRedis.addUserIdToGroupUsers(groupId, imGroupUser.getUserId(), imGroupUser.getRole());
        }
    }
}