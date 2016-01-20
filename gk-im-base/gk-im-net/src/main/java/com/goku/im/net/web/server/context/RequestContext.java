package com.goku.im.net.web.server.context;

import java.net.SocketAddress;
import java.util.Map;

/**
 * @author moueimei
 */
public interface RequestContext {
    String getRequestUrl();

    String getParameters(String key);

    Map<String, String> getParamMap();

    SocketAddress getRemoteAddress();

    String getPostData();
}