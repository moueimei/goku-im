package com.goku.im.connector.global.common;

/**
 * Created by moueimei on 15/11/26.
 */
public class ActionRequestTypeConst {
    /**
     * 登录请求(由客户端发送)
     */
    public final static String LOGIN = "login";

    /**
     * 发送消息请求(由客户端发送)
     */
    public final static String SEND_MSG = "sendMsg";

    /**
     * 推送消息请求(由服务端发送)
     */
    public final static String PUSH_MSG = "pushMsg";

    /**
     * 推送通知请求(由服务端发送)
     */
    public final static String PUSH_NOTIFY = "pushActNotify";

    /**
     * 重新连接(由客户端发送)
     */
    public final static String CONNECTION = "connect";

    /**
     * TCP心跳(由客户端发送)
     */
    public final static String HEART_BEAT_PING = "ping";

    /**
     * 登出请求(由客户端发送)
     */
    public final static String LOGOUT = "logout";
}