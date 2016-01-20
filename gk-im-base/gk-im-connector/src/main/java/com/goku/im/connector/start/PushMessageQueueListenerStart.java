package com.goku.im.connector.start;

import com.goku.im.connector.receiver.MessageReceiveListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by moueimei on 15/11/27.
 * 消息推送队列监听器
 */
@Component
public class PushMessageQueueListenerStart {
    @Autowired
    MessageReceiveListener listener;

    public void start() throws Exception {
        listener.start();
    }
}