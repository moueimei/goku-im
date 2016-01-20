package com.goku.im.connector.global.common;

/**
 * Created by moueimei on 15/11/26.
 */
public class ActionAckTypeConst {
    /**
     * 登录回执(由服务端发送)
     */
    public final static String ACK_LOGIN = "ackLogin";

    /**
     * 发送消息回执(由服务端发送)
     */
    public final static String ACK_SEND_MSG = "ackSendMsg";

    /**
     * 推送消息回执(由客户端发送)
     */
    public final static String ACK_PUSH_MSG = "ackPushMsg";

    /**
     * TCP心跳应答(由服务端发送)
     */
    public final static String ACK_HEART_BEAT_PONG = "pong";

    /**
     * 客户端连接应答(由服务端发送)
     */
    public final static String ACK_CONNECT = "ackConnect";

    /**
     * 客户端登出应答(由服务端发送)
     */
    public final static String ACK_LOGOUT = "ackLogout";
}