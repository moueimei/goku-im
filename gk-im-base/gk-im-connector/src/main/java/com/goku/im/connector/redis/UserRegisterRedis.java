package com.goku.im.connector.redis;

import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.global.RedisKeyConst;
import com.goku.im.connector.model.User;
import com.goku.im.framework.util.StringUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


/**
 * Created by moueimei on 15/11/26.
 * 用于处理用户token和用户ID以及connectorIP的对应关系
 */
@Repository
public class UserRegisterRedis {
    @Autowired
    private Jedis jedis;

    /**
     * 设置token和用户ID的对应关系
     *
     * @param userId
     * @param userToken
     * @throws Exception
     */
    public void setUserIdByUserToken(int userId, String userToken) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_TOKEN_KEY_PREFIX, userToken);
        int expireSeconds = GlobalConfig.TOKEN_EXPIRE_DAYS * 24 * 60 * 60;
        jedis.setex(key, expireSeconds, String.valueOf(userId));
    }

    /**
     * 删除token和用户ID的对应关系
     *
     * @param userToken
     * @throws Exception
     */
    public void deletedUserIdByUserToken(String userToken) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_TOKEN_KEY_PREFIX, userToken);
        jedis.del(key);
    }

    /**
     * 根据token获取用户ID
     *
     * @param userToken 用户登录token
     * @return
     * @throws Exception
     */
    public int getUserIdByUserToken(String userToken) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_TOKEN_KEY_PREFIX, userToken);
        String strUserId = jedis.get(key);
        if (!StringUtil.isInteger(strUserId))
            return 0;

        return Integer.parseInt(strUserId);
    }

    /**
     * 设置用户ID和connector domain的对应关系
     *
     * @param userId 用户ID
     * @param domain 本机connector的domain
     * @throws Exception
     */
    public void setConnectorDomainByUserId(int userId, String domain) throws Exception {
        ///保存用户和connectorIP的对应关系
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_CONNECTOR_KEY_PREFIX, userId);
        jedis.set(key, domain);
    }

    /**
     * 删除用户ID和connector domain的对应关系
     *
     * @param userId 用户ID
     * @throws Exception
     */
    public void deleteConnectorDomainByUserId(int userId) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_CONNECTOR_KEY_PREFIX, userId);
        jedis.del(key);
    }

    /**
     * 获取用户的connector domain
     *
     * @param userId 用户ID
     * @return
     * @throws Exception
     */
    public String getConnectorDomainByUserId(int userId) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_CONNECTOR_KEY_PREFIX, userId);
        return jedis.get(key);
    }

    /**
     * 缓存用户信息
     *
     * @param userInfo
     * @throws Exception
     */
    public void setUserInfo(User userInfo) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_INFO_KEY_PREFIX, userInfo.getUserId());
        JSONObject json = userInfo.toJSON();
        if (null != json) {
            jedis.setex(key, 3600, json.toString());
        }
    }

    /**
     * 从缓存中获取用户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public User getUserInfo(int userId) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.USER_INFO_KEY_PREFIX, userId);
        String value = jedis.get(key);
        if (StringUtil.isNullOrEmpty(value))
            return null;

        try {
            JSONObject json = new JSONObject(value);
            return new User(json);
        } catch (Exception e) {
            return null;
        }
    }
}