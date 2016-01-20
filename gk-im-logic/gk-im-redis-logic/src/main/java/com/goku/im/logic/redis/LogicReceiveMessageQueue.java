package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/12/12.
 * 待处理消息队列, 由connector集群将接收的消息添加到该队列
 */
@Repository
public class LogicReceiveMessageQueue {
    @Autowired
    private Jedis jedis;

    public String pop() throws Exception {
        String key = RedisKeyConst.LOGIC_RECEIVE_MESSAGE_QUEUE_KEY;
        return jedis.rpop(key);
    }
}