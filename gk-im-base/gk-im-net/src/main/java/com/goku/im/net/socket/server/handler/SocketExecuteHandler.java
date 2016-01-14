package com.goku.im.net.socket.server.handler;

import tv.acfun.im.net.socket.server.context.SocketRequestContext;
import tv.acfun.im.net.socket.server.context.SocketResponse;

public interface SocketExecuteHandler
{
	SocketResponse execute(SocketRequestContext context);
}