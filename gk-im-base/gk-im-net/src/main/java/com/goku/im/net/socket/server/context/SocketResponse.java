package com.goku.im.net.socket.server.context;

/**
 * Created by moueimei on 15/11/29.
 */
public interface SocketResponse {
    String toJsonString();

    boolean needCloseChannel();
}