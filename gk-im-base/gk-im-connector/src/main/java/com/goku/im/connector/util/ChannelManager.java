package com.goku.im.connector.util;

import com.goku.im.connector.global.GlobalObject;
import io.netty.channel.Channel;

/**
 * Created by moueimei on 15/11/26.
 * 用户对应的connector通道管理器
 */
public class ChannelManager {
    public static void add(int userId, Channel channel) {
        if (null == channel || !channel.isOpen())
            return;

        ///检查之前是否有保存用户的通道,如果有,先将之前的通道移除并关闭,再保存用户当前的TCP通道
        Channel originalChannel = GlobalObject.UID_CHANNEL_MAP.get(userId);
        if (null == originalChannel) {
            ///保存用户的TCP通道
            GlobalObject.UID_CHANNEL_MAP.put(userId, channel);
        } else {
            if (!originalChannel.equals(channel)) {
                remove(userId);
                GlobalObject.UID_CHANNEL_MAP.put(userId, channel);
            }
        }
    }

    public static void remove(int userId) {
        ///删除用户的TCP通道, 并将通道关闭
        Channel channel = GlobalObject.UID_CHANNEL_MAP.get(userId);
        if (null != channel && channel.isOpen()) {
            channel.close();
        }
        GlobalObject.UID_CHANNEL_MAP.remove(userId);
    }

    public static void removeWithoutClose(int userId) {
        ///只移除jvm中用户和通道的对应关系,不关闭通道(因为要向客户端发送登出应答)
        GlobalObject.UID_CHANNEL_MAP.remove(userId);
    }

    public static Channel getChannel(int userId) {
        return GlobalObject.UID_CHANNEL_MAP.get(userId);
    }
}