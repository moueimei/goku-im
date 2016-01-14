package com.goku.im.connector.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.goku.im.connector.global.GlobalObject;
import com.goku.im.connector.model.Message;
import com.goku.im.connector.service.MessageService;

/**
 * Created by milo on 15/11/29.
 */
@Component
public class MessageLogic
{
    private static final Logger logger = LoggerFactory.getLogger(MessageLogic.class);

    @Autowired
    MessageService messageService;

    public void sendMessage(final Message message)
    {
        Runnable task = () ->
        {
            try
            {
                messageService.sendMessage(message);
            }
            catch(Exception e)
            {
                logger.error("at MessageLogic.sendMessage throw an error. " + e.getMessage(), e.getCause());
            }
        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }

    public void ackPush(final int userId, final String messageId)
    {
        Runnable task = () ->
        {
            try
            {
                messageService.ackPush(userId, messageId);
            }
            catch(Exception e)
            {
                logger.error("at MessageLogic.ackPush throw an error. " + e.getMessage(), e.getCause());
            }
        };
        GlobalObject.MESSAGE_THREAD_POOL.execute(task);
    }
}