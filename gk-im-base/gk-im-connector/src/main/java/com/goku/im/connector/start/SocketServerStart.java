package com.goku.im.connector.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.handler.ConnectionCloseHandler;
import com.goku.im.connector.handler.ConnectionLogHandler;
import com.goku.im.connector.handler.ConnectionMessageHandler;
import com.goku.im.net.socket.server.conf.ServerConfig;
import com.goku.im.net.socket.server.main.SocketServer;

/**
 * Created by moueimei on 15/11/27.
 */
@Component
public class SocketServerStart {
    @Autowired
    ConnectionMessageHandler messageHandler;
    @Autowired
    ConnectionCloseHandler closeHandler;

    @Autowired
    ConnectionLogHandler connectionLogHandler;

    ServerConfig config = new ServerConfig();

    public void start() throws Exception {
        ///Socket Server config
        config.setKeepAlive(true);
        config.setReuseAddr(true);
        config.setReadIdleTime(60000);
        config.setConnTimeout(30000);
        config.setSoTimeout(450000);

        ///Socket Server 启动
        int port = GlobalConfig.PORT;
        SocketServer server = new SocketServer(port);
        server.setServerConfig(config);
        server.setWebServerLogger(connectionLogHandler);
        server.setExecutor(messageHandler);
        server.setCloser(closeHandler);
        System.out.println("im server start on " + port + ".");
        server.start();
    }
}