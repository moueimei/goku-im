package com.goku.im.net.socket.server.logger;

/**
 * @author moueimei
 */
public interface ServerLogger {
    public void info(ServerLoggerEntity entity);

    public void warning(ServerLoggerEntity entity);

    public void error(ServerLoggerEntity entity);
}