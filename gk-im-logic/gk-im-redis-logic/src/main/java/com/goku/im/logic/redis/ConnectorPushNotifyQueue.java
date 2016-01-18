package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by milo on 15/12/10.
 * 通知推送队列,每个connector会对应一个(组)推送队列, 由connector读取队列的消息并推送至客户端
 */
@Repository
public class ConnectorPushNotifyQueue {
    @Autowired
    private Jedis jedis;

    public void push(String destConnectorDomain, String notify) throws Exception {
        String key = RedisKeyConst.makeKey(RedisKeyConst.CONNECTOR_PUSH_NOTIFY_QUEUE_KEY_PREFIX, destConnectorDomain);
        jedis.lpush(key, notify);
    }
}