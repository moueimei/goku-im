package com.goku.im.relation.redis;

import java.util.Set;

/**
 * Created by moueimei on 15/12/1.
 */
public interface ImGroupUserRedis
{
    /**
     * 将用户ID添加到群组用户列表中
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param role 用户角色
     * @throws Exception
     */
    void addUserIdToGroupUsers(int groupId, int userId, int role) throws Exception;

    /**
     * 将用户ID从群组用户列表中删除
     * @param groupId 群组ID
     * @param userId 用户ID
     * @throws Exception
     */
    void deleteUserIdFromGroupUsers(int groupId, int userId) throws Exception;

    /**
     * 删除群组用户列表
     * @param groupId 群组ID
     * @throws Exception
     */
    void deleteGroupUsers(int groupId) throws Exception;

    /**
     * 获取群组用户列表
     * @param groupId 群组ID
     * @param start 记录起始位置
     * @param end 记录截止位置
     * @return
     * @throws Exception
     */
    Set<String> getGroupUsers(int groupId, long start, long end) throws Exception;

    /**
     * 获取群组的成员总数
     * @param groupId 群组ID
     * @return
     * @throws Exception
     */
    int getGroupUserCount(int groupId) throws Exception;

    /**
     * 判定是否为好友关系
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return
     * @throws Exception
     */
    boolean isGroupMember(int groupId, int userId) throws Exception;

    /**
     * 判定redis Key 是否存在
     * @param groupId
     * @return
     * @throws Exception
     */
    boolean exists(int groupId) throws Exception;
}