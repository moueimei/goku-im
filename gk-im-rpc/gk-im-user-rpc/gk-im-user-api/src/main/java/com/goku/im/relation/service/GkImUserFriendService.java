package com.goku.im.relation.service;


import com.goku.im.relation.model.GkImUserFriend;

import java.util.List;

/**
 * Created by moueimei on 15/12/1.
 */
public interface GkImUserFriendService
{
    /**
     * 申请加为好友(如A申请加B为好友, A是fromUserId, B是toUserId)
     * @param fromUserId 申请的用户ID
     * @param toUserId 被申请的用户ID
     * @return
     */
    int apply(int fromUserId, int toUserId);

    /**
     * 申请加为好友回复(如B回复A的加好友申请, B是fromUserId, A是toUserId)
     * @param fromUserId 被申请的用户ID
     * @param toUserId 申请的用户ID
     * @param replyType 回复类型(同意/拒绝)
     * @return
     */
    int reply(int fromUserId, int toUserId, int replyType);

    /**
     * 删除好友
     * @param userId 用户ID
     * @param friendUserId 好友ID
     * @return
     */
    int delete(int userId, int friendUserId);

    /**
     * 判定是否为好友(判定friendId 是否为userId的好友)
     * @param userId
     * @param friendId
     * @return
     */
    boolean isFriend(int userId, int friendId);

    /**
     * 获取指定用户的好友ID列表
     * @param userId 用户ID
     * @return
     */
    List<GkImUserFriend> getFriends(int userId);

    /**
     * 获取好友总数
     * @param userId 用户ID
     * @return
     */
    int getFriendCount(int userId);
}
