package com.goku.im.connector.service;

import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.model.User;
import com.goku.im.connector.redis.UserRegisterRedis;
import com.goku.im.connector.redis.UserRelationRedis;
import com.goku.im.connector.util.ChannelManager;
import com.goku.im.relation.service.GkImGroupUserService;
import com.goku.im.relation.service.GkImUserFriendService;
import com.goku.user.model.GkUser;
import com.goku.user.service.GkUserService;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by moueimei on 15/11/27.
 */
@Service
public class UserService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRegisterRedis userRegisterStore;
    @Autowired
    UserRelationRedis userRelationRedis;
    @Autowired
    GkUserService gkUserService;
    @Autowired
    GkImGroupUserService gkImGroupUserService;
    @Autowired
    GkImUserFriendService gkImUserFriendService;

    /**
     * 登录
     *
     * @param channel
     * @param userId
     * @param userToken
     * @throws Exception
     */
    public void login(Channel channel, int userId, String userToken) throws Exception {
        ///登录成功需要如下操作
        ///1. 保存用户token和用户ID的对应关系, 需要设置过期时间(redis)
        userRegisterStore.setUserIdByUserToken(userId, userToken);

        ///2. 保存用户和connectorIP的对应关系(redis)
        String domain = GlobalConfig.OWNER_DOMAIN;
        userRegisterStore.setConnectorDomainByUserId(userId, domain);

        ///3. 保存用户的TCP通道(jvm)
        ChannelManager.add(userId, channel);
    }

    /**
     * 登出
     *
     * @param userId
     * @param userToken
     * @throws Exception
     */
    public void logout(int userId, String userToken) throws Exception {
        ///登出需要如下操作
        ///1. 删除用户token和用户ID的对应关系(redis)
        userRegisterStore.deletedUserIdByUserToken(userToken);

        ///2. 删除用户和connectorIP的对应关系(redis)
        userRegisterStore.deleteConnectorDomainByUserId(userId);

        ///3. 删除用户的TCP通道(jvm),并将通道关闭
        ///只移除jvm中用户和通道的对应关系, 关闭通道在底层框架执行(通过needCloseChannel参数)
        ChannelManager.removeWithoutClose(userId);
    }

    /**
     * 连接
     *
     * @param channel
     * @param userId
     * @throws Exception
     */
    public void connect(Channel channel, int userId) throws Exception {
        ///连接成功需要如下操作
        ///1. 保存用户和connectorIP的对应关系(redis)
        String domain = GlobalConfig.OWNER_DOMAIN;
        userRegisterStore.setConnectorDomainByUserId(userId, domain);

        ///2. 保存用户的TCP通道(jvm)
        ChannelManager.add(userId, channel);
    }

    /**
     * 断开连接
     *
     * @param channel
     * @throws Exception
     */
    public void disconnect(Channel channel) throws Exception {
        ///断开连接需要如下操作
        ///1. 不删除用户和token的对应关系, 因为断开网络不代表登出
        ///2. 不删除用户和connectorIP的对应关系,如果用户再次连接connector时,会将redis记录刷新的
        ///3. 只做关闭通道操作,这样用户在获取通道时,会判断通道是否打开,如果通道关闭则会移除
        if (null != channel && channel.isOpen())
            channel.close();
    }

    /**
     * 获取用户对应的connector domain
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public String getConnectorDomainByUserId(int userId) throws Exception {
        return userRegisterStore.getConnectorDomainByUserId(userId);
    }

    /**
     * 获取token对应的用户ID
     *
     * @param userToken
     * @return
     * @throws Exception
     */
    public int getUserIdByUserToken(String userToken) throws Exception {
        return userRegisterStore.getUserIdByUserToken(userToken);
    }

    /**
     * 设置token和用户ID的对应关系
     *
     * @param userId
     * @param userToken
     * @throws Exception
     */
    public void setUserIdByUserToken(int userId, String userToken) throws Exception {
        userRegisterStore.setUserIdByUserToken(userId, userToken);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfo(int userId) {
        ///先从本地缓存获取,如果获取不到,再从duboo接口获取,之后存入本地缓存
        try {
            User user = userRegisterStore.getUserInfo(userId);
            if (null != user)
                return user;

            GkUser gkUser = gkUserService.findUserByPUId(userId);
            if (null == gkUser)
                return null;

            user = new User();
            user.setUserId(userId);
            user.setNickName(gkUser.getNick());
            user.setAvatar(gkUser.getPhoto());
            user.setGender(gkUser.getSex());

            userRegisterStore.setUserInfo(user);
            return user;
        } catch (Exception e) {
            logger.error("at UserService.getUserInfo throw an error." + e.getMessage(), e.getCause());
            return null;
        }
    }

    /**
     * 获取群组用户列表
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public List<Integer> getGroupUserIds(int groupId) throws Exception {
        ///先从公共缓存获取,如果获取不到,再从duboo接口获取, 但不存入本地缓存
        List<Integer> userIds = userRelationRedis.getGroupUserIds(groupId);
        if (null == userIds || userIds.size() == 0)  ///公共缓存没有,从dubbo获取
            userIds = gkImGroupUserService.getGroupUserIds(groupId);

        return userIds;
    }

    /**
     * 判定两个用户是否为好友关系
     *
     * @param fromUserId
     * @param toUserId
     * @return
     * @throws Exception
     */
    public boolean isFriend(int fromUserId, int toUserId) throws Exception {
        ///先从公共缓存获取,如果获取不到,再从duboo接口获取, 但不存入本地缓存
        Boolean isFriend = userRelationRedis.isFriend(fromUserId, toUserId);
        if (null == isFriend)
            isFriend = gkImUserFriendService.isFriend(fromUserId, toUserId);

        return isFriend.booleanValue();
    }
}