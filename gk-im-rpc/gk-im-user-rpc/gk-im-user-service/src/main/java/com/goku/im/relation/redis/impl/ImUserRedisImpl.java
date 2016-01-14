package com.goku.im.relation.redis.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import tv.acfun.im.cas.user.model.AcUser;
import tv.acfun.im.relation.UserRelationRedisKeyConst;
import tv.acfun.im.relation.redis.ImUserRedis;

/**
 * Created by milo on 15/12/2.
 */
@Repository("imUserRedis")
public class ImUserRedisImpl implements ImUserRedis
{
    private final static int KEY_EXPIRE_SECONDS = 3600;

    @Autowired
    Jedis jedis;

    /**
     * 缓存用户信息
     * @param acUser
     * @throws Exception
     */
    public void setUserInfo(AcUser acUser) throws Exception
    {
        int userId = acUser.getPuId();
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_INFO_KEY_PREFIX, userId);
        JSONObject json = new JSONObject();
        json.put("puId", userId);
        json.put("nick", acUser.getNick() == null ? "" : acUser.getNick());
        json.put("photo", acUser.getPhoto() == null ? "" : acUser.getPhoto());
        json.put("sex", acUser.getSex() == null ? "" : acUser.getSex());
        jedis.setex(key, KEY_EXPIRE_SECONDS, json.toString());
    }

    /**
     * 从缓存中获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    public AcUser getUserInfo(int userId) throws Exception
    {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_INFO_KEY_PREFIX, userId);
        String value = jedis.get(key);
        if(null == value)
            return null;

        try
        {
            JSONObject json = new JSONObject(value);
            AcUser acUser = new AcUser();
            acUser.setPuId(json.optInt("puId", 0));
            acUser.setNick(json.optString("nick", ""));
            acUser.setPhoto(json.optString("photo", ""));
            acUser.setSex(json.optString("sex", ""));
            return acUser;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
