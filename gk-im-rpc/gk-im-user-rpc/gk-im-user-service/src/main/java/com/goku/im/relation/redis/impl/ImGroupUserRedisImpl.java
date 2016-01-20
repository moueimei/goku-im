package com.goku.im.relation.redis.impl;

import com.goku.im.relation.UserRelationRedisKeyConst;
import com.goku.im.relation.redis.ImGroupUserRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by moueimei on 15/12/1.
 */
@Repository("imGroupUserRedis")
public class ImGroupUserRedisImpl implements ImGroupUserRedis
{
    private final static int KEY_EXPIRE_SECONDS = 24 * 60 * 60;

    @Autowired
    Jedis jedis;

    @Override
    public void addUserIdToGroupUsers(int groupId, int userId, int role) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        jedis.zadd(key, role, String.valueOf(userId));
        jedis.expire(key, KEY_EXPIRE_SECONDS);
    }

    @Override
    public void deleteUserIdFromGroupUsers(int groupId, int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        jedis.zrem(key, String.valueOf(userId));
    }

    @Override
    public void deleteGroupUsers(int groupId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        jedis.del(key);
    }

    @Override
    public Set<String> getGroupUsers(int groupId, long start, long end) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        return jedis.zrevrange(key, start, end);
    }

    @Override
    public int getGroupUserCount(int groupId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        Long count = jedis.zcard(key);
        if(null == count)
            return 0;

        return count.intValue();
    }

    @Override
    public boolean isGroupMember(int groupId, int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        Long rank = jedis.zrank(key, String.valueOf(userId));
        return null != rank;
    }

    /**
     * 判定redis Key 是否存在
     * @param groupId
     * @return
     * @throws Exception
     */
    public boolean exists(int groupId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);
        return jedis.exists(key);
    }
}