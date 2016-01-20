package com.goku.im.net.web.server.main;

import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.conf.ServerConfig;
import com.goku.im.net.web.server.handler.WebServerHandler;
import com.goku.im.net.web.server.logger.WebServerLogger;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author moueimei
 */
public class WebServerInitializer extends ChannelInitializer<SocketChannel> {
    private Class<?> closeHandlerClass;
    private ActionResolve websocketActionResolve;
    private ActionResolve httpActionResolve;
    private ServerConfig serverConfig;
    private WebServerLogger serverLogger;

    public WebServerInitializer() {
    }

    public void setWebSocketCloseHandlerClass(Class<?> handlerClass) {
        this.closeHandlerClass = handlerClass;
    }

    public void setWebSocketActionResolve(ActionResolve actionResolve) {
        this.websocketActionResolve = actionResolve;
    }

    public void setHttpActionResolve(ActionResolve actionResolve) {
        this.httpActionResolve = actionResolve;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setWebServerLogger(WebServerLogger serverLogger) {
        this.serverLogger = serverLogger;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(serverConfig.getMaxPostSize()));

        long readIdleTime = serverConfig.getReadIdleTime();
        long writeIdleTime = serverConfig.getWriteIdleTime();
        long allIdleTime = serverConfig.getAllIdleTime();
        if (readIdleTime > 0 || writeIdleTime > 0 || allIdleTime > 0) {
            IdleStateHandler idleStateHandler = new IdleStateHandler(readIdleTime, writeIdleTime, allIdleTime, TimeUnit.MILLISECONDS);
            pipeline.addLast("idleState", idleStateHandler);
        }

        ChannelInboundHandlerAdapter handler = new WebServerHandler();
        if (null != closeHandlerClass)
            ((WebServerHandler) handler).setWebSocketCloseHandlerClass(closeHandlerClass);
        if (null != websocketActionResolve)
            ((WebServerHandler) handler).setWebSocketActionResolve(websocketActionResolve);
        if (null != httpActionResolve)
            ((WebServerHandler) handler).setHttpActionResolve(httpActionResolve);
        if (null != serverLogger)
            ((WebServerHandler) handler).setWebServerLogger(serverLogger);
        pipeline.addLast("handler", handler);
    }
}