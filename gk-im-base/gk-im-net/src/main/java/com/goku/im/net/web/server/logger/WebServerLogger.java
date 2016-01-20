package com.goku.im.net.web.server.logger;

/**
 * @author moueimei
 */
public interface WebServerLogger {
    public void info(WebServerLoggerEntity entity);

    public void warning(WebServerLoggerEntity entity);

    public void error(WebServerLoggerEntity entity);
}