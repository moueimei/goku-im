package com.goku.im.connector.redis;

import com.goku.im.relation.UserRelationRedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by moueimei on 15/12/7.
 */
@Repository
public class UserRelationRedis {
    @Autowired
    private Jedis jedis;

    public Boolean isFriend(int userId, int friendUserId) throws Exception {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_FRIEND_LIST_KEY_PREFIX, userId);
        if (jedis.exists(key)) {
            return jedis.sismember(key, String.valueOf(friendUserId));
        }
        return null;
    }

    public List<Integer> getGroupUserIds(int groupId) throws Exception {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_USER_LIST_KEY_PREFIX, groupId);

        Set<String> set = jedis.zrange(key, 0, -1);
        if (null == set || set.size() == 0)
            return null;

        List<Integer> userIdList = new ArrayList<>();
        set.forEach(userId -> userIdList.add(Integer.parseInt(userId)));
        return userIdList;
    }
}