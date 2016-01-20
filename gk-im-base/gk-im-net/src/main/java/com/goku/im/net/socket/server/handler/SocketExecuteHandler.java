package com.goku.im.net.socket.server.handler;


import com.goku.im.net.socket.server.context.SocketRequestContext;
import com.goku.im.net.socket.server.context.SocketResponse;

public interface SocketExecuteHandler {
    SocketResponse execute(SocketRequestContext context);
}