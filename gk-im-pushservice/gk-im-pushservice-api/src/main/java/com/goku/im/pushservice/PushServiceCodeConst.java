package com.goku.im.pushservice;

/**
 * Created by moueimei on 15/12/10.
 */
public class PushServiceCodeConst {
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
}
