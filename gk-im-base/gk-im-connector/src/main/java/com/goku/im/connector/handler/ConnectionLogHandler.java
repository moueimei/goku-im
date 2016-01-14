package com.goku.im.connector.handler;

import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.goku.im.net.socket.server.logger.ServerLogger;
import com.goku.im.net.socket.server.logger.ServerLoggerEntity;

/**
 * Created by milo on 15/12/10.
 */
@Component
public class ConnectionLogHandler extends LoggingHandler implements ServerLogger
{
    private static final Logger logger = LoggerFactory.getLogger(ConnectionLogHandler.class);

    @Override
    public void info(ServerLoggerEntity entity)
    {
        logger.info(entity.getMessage());
    }

    @Override
    public void warning(ServerLoggerEntity entity) {

    }

    @Override
    public void error(ServerLoggerEntity entity)
    {
        logger.error(entity.getMessage(), entity.getCause());
    }
}