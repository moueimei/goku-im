package com.goku.im.connector.start;

import com.goku.im.connector.receiver.NotifyReceiveListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by moueimei on 15/12/10.
 * 通知推送队列监听器
 */
@Component
public class PushNotifyQueueListenerStart {
    @Autowired
    NotifyReceiveListener listener;

    public void start() throws Exception {
        listener.start();
    }
}