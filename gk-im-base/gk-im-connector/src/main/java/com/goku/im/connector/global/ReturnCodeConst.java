package com.goku.im.connector.global;

/**
 * Created by moueimei on 15/11/29.
 */
public class ReturnCodeConst {
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
     * 获取用户登录token失败
     */
    public final static int USER_TOKEN_GET_FAIL = 601;

    /**
     * 无效的用户ID
     */
    public final static int INVALID_USER_ID = 602;

    /**
     * 用户token已过期
     */
    public final static int USER_TOKEN_HAS_EXPIRE = 603;

    /**
     * 用户名密码错误
     */
    public final static int USER_PASSWORD_ERROR = 604;

    /**
     * 账号不存在
     */
    public final static int ACCOUNT_NOT_EXITSTS = 605;

    /**
     * 未绑定手机号
     */
    public final static int USER_NOT_BIND_PHONE = 606;

    /**
     * 登录失败
     */
    public final static int LOGIN_FAIL = 607;

}