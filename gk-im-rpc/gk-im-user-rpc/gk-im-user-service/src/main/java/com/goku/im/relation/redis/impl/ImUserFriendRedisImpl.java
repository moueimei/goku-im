package com.goku.im.relation.redis.impl;

import com.goku.im.relation.UserRelationRedisKeyConst;
import com.goku.im.relation.redis.ImUserFriendRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by moueimei on 15/12/1.
 */
@Repository("imUserFriendRedis")
public class ImUserFriendRedisImpl implements ImUserFriendRedis
{
    private final static int KEY_EXPIRE_SECONDS = 24 * 60 * 60;

    @Autowired
    Jedis jedis;

    @Override
    public void addUserIdToFriends(int userId, int friendUserId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        jedis.sadd(key, String.valueOf(friendUserId));
        jedis.expire(key, KEY_EXPIRE_SECONDS);
    }

    @Override
    public void deleteUserIdFromFriends(int userId, int friendUserId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        jedis.srem(key, String.valueOf(friendUserId));
    }

    @Override
    public Set<String> getFriends(int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        return jedis.smembers(key);
    }

    @Override
    public int getFriendCount(int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        Long count = jedis.scard(key);
        if(null == count)
            return 0;

        return count.intValue();
    }

    @Override
    public boolean isFriend(int userId, int friendId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        return jedis.sismember(key, String.valueOf(friendId));
    }

    @Override
    public boolean exists(int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        return jedis.exists(key);
    }
}