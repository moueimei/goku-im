package com.goku.im.connector.handler;

import com.goku.im.connector.logic.UserLogic;
import com.goku.im.net.socket.server.handler.SocketCloseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by moueimei on 15/11/26.
 * 客户端连接断开处理器
 */
@Component
public class ConnectionCloseHandler implements SocketCloseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionCloseHandler.class);

    @Autowired
    UserLogic userLogic;

    @Override
    public void handle(ChannelHandlerContext context) {
        if (null == context)
            return;

        Channel channel = context.channel();
        if (null == channel)
            return;

        try {
            userLogic.disconnect(channel);
        } catch (Exception e) {
            logger.error("at ConnectionCloseHandler.handle throw an error." + e.getCause().getMessage(), e.getCause());
        }
    }
}