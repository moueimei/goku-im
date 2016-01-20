package com.goku.im.net.socket.server.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author moueimei
 */
public interface SocketCloseHandler {
    void handle(ChannelHandlerContext context);
}