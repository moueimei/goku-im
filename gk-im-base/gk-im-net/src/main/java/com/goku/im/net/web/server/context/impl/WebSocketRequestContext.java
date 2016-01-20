package com.goku.im.net.web.server.context.impl;

import com.goku.im.net.web.server.context.RequestContext;
import com.goku.im.net.web.server.parse.PostDataParser;
import com.goku.im.net.web.server.parse.impl.TextPostDataParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author moueimei
 */
public class WebSocketRequestContext implements RequestContext {
    private Map<String, String> paramMap = new HashMap<String, String>();
    private WebSocketFrame frame;
    private SocketAddress remoteAddress;
    private Charset charset = Charset.forName("UTF-8");
    private String postData;
    private String requestUrl;
    private ChannelHandlerContext channelContext;

    public WebSocketRequestContext(WebSocketFrame frame) throws Exception {
        this.frame = frame;
        if (null == frame)
            throw new Exception("web server receive empty frame.");

        initParam();
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getParameters(String key) {
        return paramMap.get(key);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public WebSocketFrame getFrame() {
        return this.frame;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getPostData() {
        return this.postData;
    }

    public ChannelHandlerContext getChannelContext() {
        return channelContext;
    }

    public void setChannelContext(ChannelHandlerContext channelContext) {
        this.channelContext = channelContext;
    }

    private void initParam() throws Exception {
        try {
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame txtFrame = (TextWebSocketFrame) frame;
                this.postData = txtFrame.content().toString(charset);
                PostDataParser parser = new TextPostDataParser();
                if (null != parser) {
                    Map<String, String> postDataMap = parser.parse(postData);
                    if (null != postDataMap)
                        paramMap.putAll(postDataMap);
                }
            }
        } catch (Exception e) {
            throw new Exception("web server parser data throw an error. message:" + e.getMessage());
        }
    }
}