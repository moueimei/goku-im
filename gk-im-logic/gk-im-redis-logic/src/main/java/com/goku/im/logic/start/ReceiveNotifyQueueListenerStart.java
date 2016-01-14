package com.goku.im.logic.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.acfun.im.logic.receive.NotifyReceiveListener;

/**
 * Created by milo on 15/12/12.
 * 待处理通知队列监听器
 */
@Component
public class ReceiveNotifyQueueListenerStart
{
    @Autowired
    NotifyReceiveListener notifyReceiveListener;

    public void start()
    {
        notifyReceiveListener.start();
    }
}