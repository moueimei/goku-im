package com.goku.im.pushservice.redis;

import com.goku.im.pushservice.PushServiceRedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/12/10.
 * 待处理通知队列, 由pushservice集群将通知添加到该队列
 */
@Repository
public class LogicReceiveNotifyQueue {
    @Autowired
    private Jedis jedis;

    public void push(String notify) throws Exception {
        String key = PushServiceRedisKeyConst.LOGIC_RECEIVE_NOTIFY_QUEUE_KEY;
        jedis.lpush(key, notify);
    }
}