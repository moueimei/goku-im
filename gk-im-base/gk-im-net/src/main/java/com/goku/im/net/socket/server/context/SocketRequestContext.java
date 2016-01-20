package com.goku.im.net.socket.server.context;

import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author moueimei
 */
public class SocketRequestContext {
    private SocketAddress address;
    private Object requestData;
    private ChannelHandlerContext context;

    public SocketAddress getAddress() {
        return address;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }
}