package com.goku.im.logic.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import com.goku.im.logic.global.RedisKeyConst;

/**
 * Created by milo on 15/12/10.
 * 待处理通知队列, 由pushservice集群将通知添加到该队列
 */
@Repository
public class LogicReceiveNotifyQueue {
    @Autowired
    private Jedis jedis;

    public String pop() throws Exception {
        String key = RedisKeyConst.LOGIC_RECEIVE_NOTIFY_QUEUE_KEY;
        return jedis.rpop(key);
    }
}