package com.goku.im.logic.start;

import com.goku.im.logic.receive.MessageReceiveListener;
import com.goku.im.logic.receive.NotifyReceiveListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by moueimei on 15/12/12.
 * Logic集群启动类
 */
public class LogicStart {

    @Autowired
    MessageReceiveListener messageReceiveListener;

    @Autowired
    NotifyReceiveListener notifyReceiveListener;

    public void start() {
        ///启动待处理消息队列监听器
        messageReceiveListener.start();
        ///启动待处理通知队列监听器
        notifyReceiveListener.start();
    }
}