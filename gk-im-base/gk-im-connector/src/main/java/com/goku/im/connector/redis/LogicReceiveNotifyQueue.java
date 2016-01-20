package com.goku.im.connector.redis;

import com.goku.im.connector.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/12/10.
 */
@Repository
public class LogicReceiveNotifyQueue {
    @Autowired
    private Jedis jedis;

    public void push(String notify) throws Exception {
        String key = RedisKeyConst.LOGIC_RECEIVE_NOTIFY_QUEUE_KEY;
        jedis.lpush(key, notify);
    }
}