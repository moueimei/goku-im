package com.goku.im.relation.redis;

import tv.acfun.im.cas.user.model.AcUser;

/**
 * Created by milo on 15/12/2.
 */
public interface ImUserRedis
{
    /**
     * 缓存用户信息
     * @param userInfo
     * @throws Exception
     */
    void setUserInfo(AcUser userInfo) throws Exception;

    /**
     * 从缓存中获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    AcUser getUserInfo(int userId) throws Exception;
}