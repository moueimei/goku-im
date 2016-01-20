package com.goku.im.connector.logic;

import com.goku.im.connector.global.GlobalObject;
import com.goku.im.connector.service.UserService;
import com.goku.im.connector.util.TokenManager;
import com.goku.user.service.LoginService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by moueimei on 15/11/29.
 */
@Component
public class UserLogic {
    private static final Logger logger = LoggerFactory.getLogger(UserLogic.class);

    @Autowired
    UserService userService;
    @Autowired
    LoginService loginService;

    public void login(final Channel channel, final int userId, final String userToken) {
        Runnable task = () ->
        {
            try {
                userService.login(channel, userId, userToken);
            } catch (Exception e) {
                logger.error("at UserLogic.login throw an error. " + e.getMessage(), e.getCause());
            }

        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }

    public void logout(final String userToken) {
        Runnable task = () ->
        {
            try {
                ///根据user token获取用户ID
                int userId = TokenManager.getUserIdByUserToken(userToken);
                if (userId <= 0)
                    return;

                ///调用dubbo服务告知用户登出
                loginService.logout(userToken);

                ///执行用户退出流程
                userService.logout(userId, userToken);
            } catch (Exception e) {
                logger.error("at UserLogic.logout throw an error. " + e.getMessage(), e.getCause());
            }
        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }

    public void connect(final Channel channel, final int userId) {
        Runnable task = () ->
        {
            try {
                userService.connect(channel, userId);
            } catch (Exception e) {
                logger.error("at UserLogic.connect throw an error. " + e.getMessage(), e.getCause());
            }
        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }

    public void disconnect(final Channel channel) {
        Runnable task = () ->
        {
            try {
                userService.disconnect(channel);
            } catch (Exception e) {
                logger.error("at UserLogic.disconnect throw an error. " + e.getMessage(), e.getCause());
            }
        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }

    public int getUserIdByUserToken(String userToken) throws Exception {
        ///先从redis中根据user token获取用户ID
        int userId = userService.getUserIdByUserToken(userToken);
        if (0 == userId) {
            ///如果缓存已过期,则到dubbo服务器去验证,如果验证通过,并把结果存入缓存
            boolean isValid = loginService.validate(userToken);
            if (isValid) {
                ///通过userToken获取用户ID
                userId = TokenManager.getUserIdByUserToken(userToken);
                ///如果验证成功,把user token存入缓存
                userService.setUserIdByUserToken(userId, userToken);
            }
        }
        return userId;
    }

    public boolean isFriend(int fromUserId, int toUserId) throws Exception {
        return userService.isFriend(fromUserId, toUserId);
    }
}