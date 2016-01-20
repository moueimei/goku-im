package com.goku.im.relation.redis.impl;

import com.goku.im.relation.UserRelationRedisKeyConst;
import com.goku.im.relation.redis.ImUserRedis;
import com.goku.user.model.GkUser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


/**
 * Created by moueimei on 15/12/2.
 */
@Repository("imUserRedis")
public class ImUserRedisImpl implements ImUserRedis {
    private final static int KEY_EXPIRE_SECONDS = 3600;

    @Autowired
    Jedis jedis;

    /**
     * 缓存用户信息
     *
     * @param gkUser
     * @throws Exception
     */
    public void setUserInfo(GkUser gkUser) throws Exception {
        int userId = gkUser.getPuId();
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_INFO_KEY_PREFIX, userId);
        JSONObject json = new JSONObject();
        json.put("puId", userId);
        json.put("nick", gkUser.getNick() == null ? "" : gkUser.getNick());
        json.put("photo", gkUser.getPhoto() == null ? "" : gkUser.getPhoto());
        json.put("sex", gkUser.getSex() == null ? "" : gkUser.getSex());
        jedis.setex(key, KEY_EXPIRE_SECONDS, json.toString());
    }

    /**
     * 从缓存中获取用户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public GkUser getUserInfo(int userId) throws Exception {
        String key = UserRelationRedisKeyConst.makeKey(UserRelationRedisKeyConst.USER_INFO_KEY_PREFIX, userId);
        String value = jedis.get(key);
        if (null == value)
            return null;

        try {
            JSONObject json = new JSONObject(value);
            GkUser gkUser = new GkUser();
            gkUser.setPuId(json.optInt("puId", 0));
            gkUser.setNick(json.optString("nick", ""));
            gkUser.setPhoto(json.optString("photo", ""));
            gkUser.setSex(json.optString("sex", ""));
            return gkUser;
        } catch (Exception e) {
            return null;
        }
    }
}
