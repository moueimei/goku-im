package com.goku.im.relation;

/**
 * Created by moueimei on 15/12/8.
 */
public class UserRelationCodeConst {
    /**
     * 成功
     */
    public final static int SUCCESS = 0;

    /**
     * 客户端缺少必要参数
     */
    public final static int LOST_NECESSARY_PARAMETER = 401;

    /**
     * 参数格式错误
     */
    public final static int PARAMETER_FORMAT_ERROR = 402;

    /**
     * 客户端无效操作
     */
    public final static int INVALID_OPERATION = 403;

    /**
     * 客户端非法请求
     */
    public final static int INVALID_REQUEST = 404;

    /**
     * 服务端错误
     */
    public final static int SERVER_ERROR = 500;

    /**
     * 双方已经是好友关系
     */
    public final static int WE_ARE_FRIEND = 701;

    /**
     * 双方不是好友关系
     */
    public final static int WE_ARE_NOT_FRIEND = 704;

    /**
     * 用户创建的群已达上限
     */
    public final static int USER_CREATE_GROUP_ALREADY_LIMIT = 711;

    /**
     * 用户已是群成员
     */
    public final static int USER_IS_GROUP_USER = 712;

    /**
     * 用户不是群成员
     */
    public final static int USER_IS_NOT_GROUP_USER = 713;

    /**
     * 群成员已达上限
     */
    public final static int GROUP_USER_ALREADY_LIMIT = 714;

    /**
     * 群组不存在
     */
    public final static int GROUP_NOT_EXISTS = 715;

    /**
     * 群组操作无权限(解散群组)
     */
    public final static int GROUP_HANDLE_INSUFFICIENT_PERMISSIONS = 716;
}