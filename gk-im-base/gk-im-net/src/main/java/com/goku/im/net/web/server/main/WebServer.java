package com.goku.im.net.web.server.main;

import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.conf.ServerConfig;
import com.goku.im.net.web.server.logger.WebServerLogger;
import com.goku.im.net.web.server.proxy.ActionExecutor;
import com.goku.im.net.web.server.proxy.ActionLoader;
import com.goku.im.net.web.server.proxy.impl.ActionDispatcher;
import com.goku.im.net.web.server.proxy.impl.ActionLoaderFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Iterator;
import java.util.Map;

/**
 * @author moueimei
 */
public class WebServer {
    private int port;
    private String scanPackagePath;
    private Class<?> closeHandlerClass;
    private ActionResolve websocketActionResolve;
    private ActionResolve httpActionResolve;
    private WebServerLogger serverLogger;
    private ServerConfig serverConfig;

    public WebServer(int port) {
        this.port = port;
    }

    public void setScanPackagePath(String scanPackagePath) {
        this.scanPackagePath = scanPackagePath;
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

    public void start() throws Exception {
        initActionMap();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup);
        server.channel(NioServerSocketChannel.class);
        if (null == serverConfig)
            serverConfig = new ServerConfig();
        ///配置server
        server.option(ChannelOption.SO_BACKLOG, serverConfig.getBacklog());
        server.option(ChannelOption.SO_TIMEOUT, serverConfig.getSoTimeout());
        server.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverConfig.getConnTimeout());
        server.option(ChannelOption.SO_REUSEADDR, serverConfig.isReuseAddr());
        server.option(ChannelOption.SO_KEEPALIVE, serverConfig.isKeepAlive());

        WebServerInitializer initializer = new WebServerInitializer();
        initializer.setWebSocketCloseHandlerClass(closeHandlerClass);
        initializer.setWebSocketActionResolve(websocketActionResolve);
        initializer.setHttpActionResolve(httpActionResolve);
        initializer.setWebServerLogger(serverLogger);
        initializer.setServerConfig(serverConfig);
        server.childHandler(initializer);

        Channel channel = null;
        ChannelFuture future = null;
        try {
            future = server.bind(port);
            future = future.sync();
            channel = future.channel();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void initActionMap() throws Exception {
        if (null == scanPackagePath || "".equals(scanPackagePath))
            throw new Exception("scanPackagePath can not be null.");

        ActionLoader loader = ActionLoaderFactory.getActionLoader();
        if (null == loader)
            throw new Exception("action load type dosen't set");

        ActionDispatcher dispatcher = ActionDispatcher.getInstance();
        Map<String, ActionExecutor> actionMap = loader.load(scanPackagePath);
        if (null == actionMap || actionMap.size() == 0)
            throw new Exception("action map can not be null.");

        Iterator<String> iterator = actionMap.keySet().iterator();
        String url;
        ActionExecutor executor;
        while (iterator.hasNext()) {
            url = iterator.next();
            executor = actionMap.get(url);
            dispatcher.regiester(url, executor);
        }
    }
}