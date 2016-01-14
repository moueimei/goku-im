package com.goku.im.relation.redis.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import tv.acfun.im.relation.UserRelationRedisKeyConst;
import tv.acfun.im.relation.entity.ImGroup;
import tv.acfun.im.relation.redis.ImGroupRedis;

import java.util.Date;

/**
 * Created by milo on 15/12/2.
 */
@Repository("imGroupRedis")
public class ImGroupRedisImpl implements ImGroupRedis
{
    private final static int KEY_EXPIRE_SECONDS = 3600;

    @Autowired
    Jedis jedis;

    @Override
    public void setGroupInfo(ImGroup imGroup) throws Exception
    {
        int groupId = imGroup.getId();
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_INFO_KEY_PREFIX, groupId);
        JSONObject json = new JSONObject();
        json.put("group_id", groupId);
        json.put("group_name", imGroup.getGroupName() == null ? "" : imGroup.getGroupName());
        json.put("icon", imGroup.getIcon() == null ? "" : imGroup.getIcon());
        json.put("intro", imGroup.getIntro() == null ? "" : imGroup.getIntro());
        json.put("creator_id", imGroup.getCreatorId());
        json.put("create_time", imGroup.getCreateTime().getTime());
        json.put("create_by", imGroup.getCreateBy() == null ? "" : imGroup.getCreateBy());
        jedis.setex(key, KEY_EXPIRE_SECONDS, json.toString());
    }

    @Override
    public ImGroup getGroupInfo(int groupId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_INFO_KEY_PREFIX, groupId);
        String value = jedis.get(key);
        if(null == value)
            return null;

        JSONObject json = new JSONObject(value);
        ImGroup imGroup = new ImGroup();
        imGroup.setId(json.optInt("group_id", 0));
        imGroup.setGroupName(json.optString("group_name", ""));
        imGroup.setIcon(json.optString("icon", ""));
        imGroup.setIntro(json.optString("intro", ""));
        imGroup.setDeleted(false);
        imGroup.setCreatorId(json.optInt("creator_id", 0));
        long createTime = json.optLong("create_time", System.currentTimeMillis());
        imGroup.setCreateTime(new Date(createTime));
        imGroup.setCreateBy(json.optString("create_by", ""));
        return imGroup;
    }

    @Override
    public void delete(int groupId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.GROUP_INFO_KEY_PREFIX, groupId);
        jedis.del(key);
    }
}