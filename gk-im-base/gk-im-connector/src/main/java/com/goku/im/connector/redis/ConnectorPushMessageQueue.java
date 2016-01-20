package com.goku.im.connector.redis;

import com.goku.im.connector.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


/**
 * Created by moueimei on 15/11/26.
 * 推送队列,每个connector会对应一个(组)推送队列, 由connector读取队列的消息并推送至客户端
 */
@Repository
public class ConnectorPushMessageQueue
{
    @Autowired
    private Jedis jedis;

    public String pop(String destConnectorDomain) throws Exception
    {
        String key = RedisKeyConst.makeKey(RedisKeyConst.CONNECTOR_PUSH_MESSAGE_QUEUE_KEY_PREFIX, destConnectorDomain);
        return jedis.rpop(key);
    }
}