package com.goku.im.relation.service;


import com.goku.im.relation.model.GkImGroup;
import com.goku.im.relation.model.GkImGroupUser;

import java.util.List;

/**
 * Created by moueimei on 15/12/1.
 */
public interface GkImGroupUserService
{
    /**
     * 申请加入群组
     * @param userId 申请加入用户ID
     * @param groupId 群组ID
     * @return
     */
    int apply(int userId, int groupId);

    /**
     * 申请加入群组回复
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 申请加入用户ID
     * @param groupId 群组ID
     * @param replyType 回复类型(同意/拒绝)
     * @return
     */
    int reply(int operatorId, int userId, int groupId, int replyType);

    /**
     * 删除群组成员
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 被删除用户ID
     * @param groupId 群组ID
     * @return
     */
    int delete(int operatorId, int userId, int groupId);

    /**
     * 用户主动退群
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return
     */
    int quit(int userId, int groupId);

    /**
     * 群主邀请用户入群(不需要验证)
     * @param operatorId 操作人ID(管理员/群主)
     * @param userId 被邀请用户ID
     * @param groupId 群组ID
     * @return
     */
    int invite(int operatorId, int userId, int groupId);

    /**
     * 判定用户是否为群组成员
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return
     */
    boolean isGroupUser(int userId, int groupId);

    /**
     * 获取群组成员列表
     * @param groupId 群组ID
     * @param page 页码
     * @param size 每页记录数
     * @return
     */
    List<GkImGroupUser> getGroupUsers(int groupId, int page, int size);

    /**
     * 获取群组成员总数
     * @param groupId 群组ID
     * @return
     */
    int getGroupUserCount(int groupId);

    /**
     * 获取用户的群组列表
     * @param userId 用户ID
     * @return
     */
    List<GkImGroup> getUserGroups(int userId);

    /**
     * 获取群组成员ID列表(为聊天专用)
     * @param groupId 群组ID
     * @return
     */
    List<Integer> getGroupUserIds(int groupId);
}
