package com.goku.im.relation.service;

/**
 * Created by moueimei on 15/12/8.
 */
public interface UserRelationAdapter {

    /****************************************群组接口****************************************/
    /**
     * 创建群组
     * @param userId 创建者ID
     * @param groupName 群组名称
     * @param icon 群组封面
     * @param intro 群组介绍
     * @return json
     */
    String createGroup(int userId, String groupName, String icon, String intro);

    /**
     * 编辑群组信息
     * @param userId 创建者ID
     * @param groupId 群组ID
     * @param groupName 群组名称
     * @param icon 群组封面
     * @param intro 群组介绍
     * @return json
     */
    String editGroup(int userId, int groupId, String groupName, String icon, String intro);

    /**
     * 解散群组
     * @param operatorId 操作用户ID
     * @param groupId 群组ID
     * @return json
     */
    String dismissGroup(int operatorId, int groupId);

    /**
     * 获取群组信息
     * @param groupId 群组ID
     * @return json
     */
    String getGroupInfo(int groupId);

    /**
     * 判定用户是否为群主
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return json
     */
    String isGroupMaster(int userId, int groupId);

    /****************************************群组成员接口****************************************/

    /**
     * 申请加入群组
     * @param userId 申请加入用户ID
     * @param groupId 群组ID
     * @return json
     */
    String applyJoinGroup(int userId, int groupId);

    /**
     * 申请加入群组回复
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 申请加入用户ID
     * @param groupId 群组ID
     * @param replyType 回复类型(同意/拒绝)
     * @return json
     */
    String replyJoinGroup(int operatorId, int userId, int groupId, int replyType);

    /**
     * 删除群组成员
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 被删除用户ID
     * @param groupId 群组ID
     * @return json
     */
    String deleteGroupUser(int operatorId, int userId, int groupId);

    /**
     * 用户主动退群
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return json
     */
    String quitGroup(int userId, int groupId);

    /**
     * 群主邀请用户入群(不需要验证)
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 被邀请用户ID
     * @param groupId 群组ID
     * @return json
     */
    String inviteFriendJoinGroup(int operatorId, int userId, int groupId);

    /**
     * 判定用户是否为群组成员
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return json
     */
    String isGroupUser(int userId, int groupId);

    /**
     * 获取群组成员列表
     * @param groupId 群组ID
     * @param page 页码
     * @param size 每页记录数
     * @return json
     */
    String getGroupUsers(int groupId, int page, int size);

    /**
     * 获取群组成员总数
     * @param groupId 群组ID
     * @return json
     */
    String getGroupUserCount(int groupId);

    /**
     * 获取用户群组列表
     * @param userId 用户ID
     * @return json
     */
    String getUserGroups(int userId);

    /****************************************好友接口****************************************/

    /**
     * 申请加为好友(如A申请加B为好友, A是fromUserId, B是toUserId)
     * @param fromUserId 申请的用户ID
     * @param toUserId 被申请的用户ID
     * @return json
     */
    String applyFriend(int fromUserId, int toUserId);

    /**
     * 申请加为好友回复(如B回复A的加好友申请, B是fromUserId, A是toUserId)
     * @param fromUserId 被申请的用户ID
     * @param toUserId 申请的用户ID
     * @param replyType 回复类型(同意/拒绝)
     * @return json
     */
    String replyFriend(int fromUserId, int toUserId, int replyType);

    /**
     * 删除好友
     * @param userId 用户ID
     * @param friendUserId 好友ID
     * @return json
     */
    String deleteFriend(int userId, int friendUserId);

    /**
     * 判定是否为好友(判定friendId 是否为userId的好友)
     * @param userId
     * @param friendId
     * @return json
     */
    String isFriend(int userId, int friendId);

    /**
     * 获取指定用户的好友ID列表
     * @param userId 用户ID
     * @return json
     */
    String getFriends(int userId);

    /**
     * 获取好友总数
     * @param userId 用户ID
     * @return json
     */
    String getFriendCount(int userId);
}