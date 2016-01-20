package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/12/10.
 * 数据库待处理通知队列,负责存储用户触发的动作类通知,由logic集群负责添加,后端persistence集群读取并存入数据库
 */
@Repository
public class DataBaseNotifyQueue {
    @Autowired
    private Jedis jedis;

    public void push(String notify) throws Exception {
        String key = RedisKeyConst.DATABASE_NOTIFY_QUEUE_KEY;
        jedis.lpush(key, notify);
    }
}