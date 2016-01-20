package com.goku.im.connector.model;

/**
 * Created by moueimei on 15/11/27.
 */
public class Atom {
    /**
     * 推广渠道(安智/安卓/豌豆荚等)
     */
    private String channelCode;
    /**
     * 平台(android, ios, wp7等)
     */
    private String platform;
    /**
     * 网络类型(wifi,2G,3G,4G)
     */
    private String connNet;
    /**
     * 客户端版本号
     */
    private String clientVersion;
    /**
     * 设备类型(ios6,ios6s, xiaomi等)
     */
    private String deviceType;
    /**
     * IOS设备token(ios专用,用来连接APNS推送离线消息)
     */
    private String iosToken;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getConnNet() {
        return connNet;
    }

    public void setConnNet(String connNet) {
        this.connNet = connNet;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIosToken() {
        return iosToken;
    }

    public void setIosToken(String iosToken) {
        this.iosToken = iosToken;
    }
}