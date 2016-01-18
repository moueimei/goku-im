package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


/**
 * Created by milo on 15/11/26.
 * 用于处理用户和connectorIP的对应关系
 */
@Repository
public class UserRegisterRedis {
    @Autowired
    private Jedis jedis;

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
}