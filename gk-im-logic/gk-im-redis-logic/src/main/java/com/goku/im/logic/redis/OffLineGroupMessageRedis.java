package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/12/12.
 * 离线群组消息队列, 负责存储掉线用户的未读群组消息
 */
@Repository
public class OffLineGroupMessageRedis {
    @Autowired
    Jedis jedis;

    public void add(int userId, long createTime, String messageId) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.OFFLINE_GROUP_MESSAGE_LIST_KEY_PREFIX, userId);
        jedis.zadd(key, createTime, messageId);
    }

    public void delete(int userId, String... messageIds) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.OFFLINE_GROUP_MESSAGE_LIST_KEY_PREFIX, userId);
        jedis.zrem(key, messageIds);
    }
}