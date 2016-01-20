package com.goku.im.relation.redis;

import java.util.Set;

/**
 * Created by moueimei on 15/12/1.
 */
public interface ImUserFriendRedis
{
    /**
     * 将好友ID添加到用户的好友列表中
     * @param userId 用户ID
     * @param friendUserId 好友ID
     * @throws Exception
     */
    void addUserIdToFriends(int userId, int friendUserId) throws Exception;

    /**
     * 将好友ID从用户的好友列表中删除
     * @param userId 用户ID
     * @param friendUserId 好友ID
     * @throws Exception
     */
    void deleteUserIdFromFriends(int userId, int friendUserId) throws Exception;

    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return
     * @throws Exception
     */
    Set<String> getFriends(int userId) throws Exception;

    /**
     * 获取用户的好友总数
     * @param userId 用户ID
     * @return
     * @throws Exception
     */
    int getFriendCount(int userId) throws Exception;

    /**
     * 判定是否为好友关系
     * @param userId
     * @param friendId
     * @return
     * @throws Exception
     */
    boolean isFriend(int userId, int friendId) throws Exception;

    /**
     * 判定redis Key 是否存在
     * @param userId
     * @return
     * @throws Exception
     */
    boolean exists(int userId) throws Exception;
}