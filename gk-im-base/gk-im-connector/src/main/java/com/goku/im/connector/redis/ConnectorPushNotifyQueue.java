package com.goku.im.connector.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import com.goku.im.connector.global.RedisKeyConst;

/**
 * Created by milo on 15/12/10.
 */
@Repository
public class ConnectorPushNotifyQueue
{
    @Autowired
    private Jedis jedis;

    public String pop(String destConnectorDomain) throws Exception
    {
        String key = RedisKeyConst.makeKey(RedisKeyConst.CONNECTOR_PUSH_NOTIFY_QUEUE_KEY_PREFIX, destConnectorDomain);
        return jedis.rpop(key);
    }
}