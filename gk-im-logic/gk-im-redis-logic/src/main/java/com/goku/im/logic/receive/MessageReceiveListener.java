package com.goku.im.logic.receive;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.acframework.config.core.util.StringUtils;
import tv.acfun.im.logic.global.GlobalObject;
import tv.acfun.im.logic.model.Message;
import tv.acfun.im.logic.push.MessagePusher;
import tv.acfun.im.logic.redis.LogicReceiveMessageQueue;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by milo on 15/11/27.
 * 从receive msg queue中读取消息, 并交给MessageHandler来异步处理
 */
@Component
public class MessageReceiveListener
{
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiveListener.class);

    @Autowired
    LogicReceiveMessageQueue receiveMessageQueue;
    @Autowired
    MessagePusher messagePusher;

    private boolean receive = true;
    private final static int RECEIVE_THREAD_COUNT = 2;

    public void start()
    {
        ///启动两个线程阻塞读取push queue
        Thread[] threads = getStartThreads();
        for(int i=0; i<threads.length; i++)
        {
            threads[i].setName("logic_receive_message_" + i);
            threads[i].start();
        }
    }

    private Thread[] getStartThreads()
    {
        Thread[] threads = new Thread[RECEIVE_THREAD_COUNT];
        for(int i=0; i<RECEIVE_THREAD_COUNT; i++)
        {
            Thread thread = new Thread(new ReceiveMessageTask());
            threads[i] = thread;
        }
        return threads;
    }

    class ReceiveMessageTask implements Runnable
    {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void run()
        {
            try
            {
                while(receive)
                {
                    ///从队列中获取消息
                    String messageJson = receiveMessageQueue.pop();
                    if(StringUtils.isNullOrEmpty(messageJson)) {
                        Thread.sleep(1);
                        continue;
                    }

                    ///处理消息
                    handle(messageJson);
                }
            }
            catch(Exception e)
            {
                logger.error("at MessageReceiveListener.ReceiveNotifyTask.run throw an error." + e.getMessage(), e.getCause());
            }
        }

        private void handle(String messageJson)
        {
            try
            {
                ///记录日志
                StringBuilder strLog = new StringBuilder();
                strLog.append("[" + dateFormat.format(new Date()) + "] ");
                strLog.append("MessageReceiveListener receive message :" + messageJson);
                logger.info(strLog.toString());

                ///执行任务
                JSONObject json = new JSONObject(messageJson);
                Message message = new Message(json);
                MessageHandler task = new MessageHandler(message);
                GlobalObject.HANDLE_MESSAGE_THREAD_POOL.execute(task);
            }
            catch (Exception e)
            {
                logger.error("at MessageReceiveListener.ReceiveNotifyTask.handle throw an error." + e.getMessage(), e.getCause());
            }
        }
    }

    class MessageHandler implements Runnable
    {
        private Message message;

        public MessageHandler(Message message)
        {
            this.message = message;
        }

        @Override
        public void run()
        {
            try
            {
                messagePusher.push(message);
            }
            catch(Exception e)
            {
                logger.error("at MessageReceiveListener.MessageHandler.run throw an error." + e.getMessage(), e.getCause());
            }
        }
    }
}