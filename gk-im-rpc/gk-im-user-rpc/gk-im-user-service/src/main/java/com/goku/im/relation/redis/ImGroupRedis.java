package com.goku.im.relation.redis;


import com.goku.im.relation.entity.ImGroup;

/**
 * Created by moueimei on 15/12/2.
 */
public interface ImGroupRedis
{
    /**
     * 缓存群组信息
     * @param imGroup
     * @throws Exception
     */
    void setGroupInfo(ImGroup imGroup) throws Exception;

    /**
     * 从缓存中获取群组信息
     * @param groupId 群组ID
     * @return
     * @throws Exception
     */
    ImGroup getGroupInfo(int groupId) throws Exception;

    /**
     * 删除群组缓存
     * @param groupId 群组ID
     * @throws Exception
     */
    void delete(int groupId) throws Exception;
}