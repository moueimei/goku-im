package com.goku.im.relation.service.facade;

import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.im.pushservice.model.Notify;
import com.goku.im.relation.UserRelationCodeConst;
import com.goku.im.relation.common.FriendReplyType;
import com.goku.im.relation.entity.ImUserFriend;
import com.goku.im.relation.model.GkImUserFriend;
import com.goku.im.relation.push.AsyncNotifyPusher;
import com.goku.im.relation.redis.ImUserFriendRedis;
import com.goku.im.relation.service.GkImUserFriendService;
import com.goku.im.relation.service.ImUserFriendService;
import com.goku.im.relation.util.UserManager;
import com.goku.user.model.GkUser;
import com.goku.user.service.GkUserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by moueimei on 15/12/1.
 */
@Service("gkImUserFriendService")
public class GkImUserFriendServiceImpl implements GkImUserFriendService {
    private static final Logger logger = LoggerFactory.getLogger(GkImUserFriendServiceImpl.class);

    @Autowired
    ImUserFriendService imUserFriendService;
    @Autowired
    ImUserFriendRedis imUserFriendRedis;
    @Autowired
    GkUserService gkUserService;
    @Autowired
    UserManager userManager;
    @Autowired
    AsyncNotifyPusher asyncNotifyPusher;

    @Override
    public int apply(int fromUserId, int toUserId) {
        if (fromUserId <= 0 || toUserId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            boolean isFriend = isFriend(fromUserId, toUserId);
            if (isFriend) {
                ///双方已经是好友关系
                return UserRelationCodeConst.WE_ARE_FRIEND;
            }

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(fromUserId);
            List<Integer> toUserIds = new ArrayList<>();
            toUserIds.add(toUserId);
            notify.setToUserIds(toUserIds);
            asyncNotifyPusher.pushFriendApply(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int reply(int fromUserId, int toUserId, int replyType) {
        if (fromUserId <= 0 || toUserId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            ///如果同意加为好友
            if (replyType == FriendReplyType.AGREE) {
                boolean isFriend = isFriend(fromUserId, toUserId);
                //boolean isFriend = false;
                if (isFriend) {
                    ///双方已经是好友关系
                    return UserRelationCodeConst.WE_ARE_FRIEND;
                }

                Date createTime = new Date();
                ImUserFriend imUserFriend1 = existsRecord(fromUserId, toUserId);
                if (null != imUserFriend1)    ///之前加过好友, 但被被删除过, 更新deleted字段为0
                {
                    imUserFriend1.setDeleted(false);
                    imUserFriendService.updateByPrimaryKey(imUserFriend1);
                } else {

                    imUserFriend1 = new ImUserFriend();
                    imUserFriend1.setUserId(fromUserId);
                    imUserFriend1.setFriendId(toUserId);
                    imUserFriend1.setDeleted(false);
                    imUserFriend1.setCreateTime(createTime);
                    imUserFriend1.setUpdateTime(createTime);
                    imUserFriend1.setCreateBy(String.valueOf(fromUserId));
                    imUserFriend1.setUpdateBy("");
                    ///添加到数据库
                    imUserFriendService.insertSelective(imUserFriend1);
                }

                ImUserFriend imUserFriend2 = existsRecord(toUserId, fromUserId);
                if (null != imUserFriend2)    ///之前加过好友, 但被被删除过, 更新deleted字段为0
                {
                    imUserFriend2.setDeleted(false);
                    imUserFriendService.updateByPrimaryKey(imUserFriend2);
                } else {
                    imUserFriend2 = new ImUserFriend();
                    imUserFriend2.setUserId(toUserId);
                    imUserFriend2.setFriendId(fromUserId);
                    imUserFriend2.setDeleted(false);
                    imUserFriend2.setCreateTime(createTime);
                    imUserFriend2.setUpdateTime(createTime);
                    imUserFriend2.setCreateBy(String.valueOf(fromUserId));
                    imUserFriend2.setUpdateBy("");
                    ///添加到数据库
                    imUserFriendService.insertSelective(imUserFriend2);
                }

                ///添加到redis
                imUserFriendRedis.addUserIdToFriends(fromUserId, toUserId);
                imUserFriendRedis.addUserIdToFriends(toUserId, fromUserId);
            }

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(fromUserId);
            List<Integer> toUserIds = new ArrayList<>();
            toUserIds.add(toUserId);
            notify.setToUserIds(toUserIds);
            JSONObject content = new JSONObject();
            content.put("replyType", replyType);
            notify.setContent(content.toString());
            asyncNotifyPusher.pushFriendReply(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public int delete(int userId, int friendUserId) {
        if (userId <= 0 || friendUserId <= 0) {
            return UserRelationCodeConst.PARAMETER_FORMAT_ERROR;
        }

        try {
            boolean isFriend = isFriend(userId, friendUserId);
            if (!isFriend) {
                return UserRelationCodeConst.WE_ARE_NOT_FRIEND;
            }

            ///从数据库中删除两个用户的好友关系(逻辑删除 deleted=1)
            ImUserFriend imUserFriend = new ImUserFriend();
            imUserFriend.setDeleted(true);
            imUserFriend.setUpdateTime(new Date());
            imUserFriend.setUpdateBy(String.valueOf(userId));

            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("friendId", friendUserId);
            MapQuery mapQuery = MapQuery.create(params);
            imUserFriendService.updateByMapSelective(imUserFriend, mapQuery);

            params.clear();
            params.put("userId", friendUserId);
            params.put("friendId", userId);
            mapQuery = MapQuery.create(params);
            imUserFriendService.updateByMapSelective(imUserFriend, mapQuery);

            ///从redis中删除两个用户的好友关系
            imUserFriendRedis.deleteUserIdFromFriends(userId, friendUserId);
            imUserFriendRedis.deleteUserIdFromFriends(friendUserId, userId);

            ///调用pushservice发送通知
            Notify notify = new Notify();
            notify.setFromUserId(userId);
            List<Integer> toUserIds = new ArrayList<>();
            toUserIds.add(friendUserId);
            notify.setToUserIds(toUserIds);
            asyncNotifyPusher.pushFriendDelete(notify);

            return UserRelationCodeConst.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return UserRelationCodeConst.SERVER_ERROR;
        }
    }

    @Override
    public boolean isFriend(int userId, int friendUserId) {
        if (userId <= 0 || friendUserId <= 0)
            return false;

        try {
            ///先创建redis 结构
            buildRedis(userId);

            ///从redis中获取
            return imUserFriendRedis.isFriend(userId, friendUserId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return false;
        }
    }

    @Override
    public List<GkImUserFriend> getFriends(int userId) {
        if (userId <= 0)
            return null;

        try {
            ///先创建redis 结构
            buildRedis(userId);

            Set<String> set = imUserFriendRedis.getFriends(userId);
            if (null == set || set.size() == 0)
                return null;

            List<GkImUserFriend> userList = new ArrayList<>();
            GkImUserFriend gkImUserFriend;
            GkUser gkUser;
            for (String friendId : set) {
                gkImUserFriend = new GkImUserFriend();
                gkImUserFriend.setPuId(Integer.parseInt(friendId));

                gkUser = userManager.getInfo(Integer.parseInt(friendId));
                if (null != gkUser) {
                    gkImUserFriend.setNick(gkUser.getNick());
                    gkImUserFriend.setPhoto(gkUser.getPhoto());
                    gkImUserFriend.setSex(gkUser.getSex());
                }
                userList.add(gkImUserFriend);
            }
            return userList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public int getFriendCount(int userId) {
        if (userId <= 0)
            return 0;

        try {
            ///先创建redis 结构
            buildRedis(userId);

            return imUserFriendRedis.getFriendCount(userId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return 0;
        }
    }

    private ImUserFriend existsRecord(int userId, int friendUserId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("friendId", friendUserId);
        MapQuery mapQuery = MapQuery.create(params);
        List<ImUserFriend> list = imUserFriendService.selectByQuery(mapQuery);
        if (null == list || list.size() == 0)
            return null;

        return list.get(0);
    }

    /**
     * 创建redis 用户好友列表(redis会过期,所以第一次访问时,需要创建)
     *
     * @param userId 用户ID
     * @throws Exception
     */
    private void buildRedis(int userId) throws Exception {
        boolean exists = imUserFriendRedis.exists(userId);
        if (exists)
            return;

        ///从数据库中加载
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("deleted", 0);
        MapQuery mapQuery = MapQuery.create(params);

        List<ImUserFriend> list = imUserFriendService.selectByQuery(mapQuery);
        if (null != list && list.size() > 0) {
            for (ImUserFriend imUserFriend : list)
                imUserFriendRedis.addUserIdToFriends(userId, imUserFriend.getFriendId());
        }
    }
}
