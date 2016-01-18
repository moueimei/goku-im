package com.goku.im.pushservice;

/**
 * Created by moueimei on 15/12/10.
 * 通知动作类型
 */
public class PushServiceNotifyTypeConst {
    /**
     * 申请加为好友
     */
    public final static String FRIEND_APPLY = "friend_apply";

    /**
     * 回复加为好友的申请(同意/拒绝)
     */
    public final static String FRIEND_REPLY = "friend_reply";

    /**
     * 删除好友
     */
    public final static String FRIEND_DELETE = "friend_delete";

    /**
     * 解散群组
     */
    public final static String GROUP_DISMISS = "group_dismiss";

    /**
     * 用户申请加入群组
     */
    public final static String GROUP_USER_APPLY = "group_user_apply";

    /**
     * 回复用户加入群组的申请
     */
    public final static String GROUP_USER_REPLY = "group_user_reply";

    /**
     * 删除群组用户
     */
    public final static String GROUP_USER_DELETE = "group_user_delete";

    /**
     * 群组用户退群
     */
    public final static String GROUP_USER_QUIT = "group_user_quit";

    /**
     * 添加好友入群
     */
    public final static String GROUP_USER_ADD = "group_user_add";

    /**
     * 新用户入群(一般是给群成员发送的通知)
     */
    public final static String GROUP_NEW_USER_ADD = "group_new_user_add";

    /**
     * 评论视频
     */
    public final static String USER_COMMENT_VIDEO = "user_comment_video";

    /**
     * 回复评论
     */
    public final static String USER_REPLY_COMMENT_VIDEO = "user_reply_comment_video";

    /**
     * 关注用户
     */
    public final static String USER_FOLLOW_USER = "user_follow_user";
}