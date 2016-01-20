package com.goku.im.connector.global;

/**
 * Created by moueimei on 15/11/26.
 * 全局配置
 */
public class GlobalConfig {
    /**
     * 启动端口
     */
    public static int PORT = 6000;

    /**
     * 本机IP(内网)
     */
    public static String OWNER_DOMAIN;

    /**
     * 用户token有效期
     */
    public static int TOKEN_EXPIRE_DAYS = 10;

    /**
     * 应用ID,用于区别客户端app
     */
    public static String APP_ID = "im-client";

    /**
     * 应用私钥key
     */
    public static String APP_KEY = "im";

    /**
     * 内网网卡标识
     */
    public static String ETHER_FLAG = "en0";
}