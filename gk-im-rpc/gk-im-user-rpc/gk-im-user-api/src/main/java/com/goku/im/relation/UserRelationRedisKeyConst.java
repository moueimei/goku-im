package com.goku.im.relation;

/**
 * Created by moueimei on 15/12/8.
 */
public class UserRelationRedisKeyConst {
    /**
     * 用户好友列表
     * 结构: Set
     * 示例: sadd user_friend_{$user_id} {$friend_user_id}
     */
    public final static String USER_FRIEND_LIST_KEY_PREFIX = "im_user_friend_";

    /**
     * 群组成员列表
     * 结构: ZSet
     * 示例: zadd im_group_user_list_{$group_id} {$role} {$user_id}
     */
    public final static String GROUP_USER_LIST_KEY_PREFIX = "im_group_user_list_";

    /**
     * 用户信息
     * 结构: String
     * 示例: get im_user_info_{$user_id}
     */
    public final static String USER_INFO_KEY_PREFIX = "im_user_info_";

    /**
     * 群组信息
     * 结构: String
     * 示例: get im_group_info_{$group_id}
     */
    public final static String GROUP_INFO_KEY_PREFIX = "im_group_info_";

    /**
     * 构建Redis Key
     *
     * @param prefix    前缀
     * @param parameter 参数
     * @return
     */
    public static String makeKey(String prefix, Object parameter) {
        return prefix.concat(parameter.toString());
    }
}