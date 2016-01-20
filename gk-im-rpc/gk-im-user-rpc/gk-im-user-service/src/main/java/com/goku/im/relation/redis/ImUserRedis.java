package com.goku.im.relation.redis;

import com.goku.user.model.GkUser;

/**
 * Created by moueimei on 15/12/2.
 */
public interface ImUserRedis {
    /**
     * 缓存用户信息
     *
     * @param userInfo
     * @throws Exception
     */
    void setUserInfo(GkUser userInfo) throws Exception;

    /**
     * 从缓存中获取用户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    GkUser getUserInfo(int userId) throws Exception;
}