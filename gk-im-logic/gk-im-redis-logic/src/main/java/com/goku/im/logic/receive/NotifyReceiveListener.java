package com.goku.im.logic.receive;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.goku.im.logic.global.GlobalObject;
import com.goku.im.logic.model.Notify;
import com.goku.im.logic.push.NotifyPusher;
import com.goku.im.logic.redis.LogicReceiveNotifyQueue;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moueimei on 15/12/10.
 * 从receive notify queue中读取消息, 并交给NotifyHandler来异步处理
 */
@Component
public class NotifyReceiveListener {
    private static final Logger logger = LoggerFactory.getLogger(NotifyReceiveListener.class);

    @Autowired
    LogicReceiveNotifyQueue receiveNotifyQueue;
    @Autowired
    NotifyPusher notifyPusher;

    private boolean receive = true;
    private final static int RECEIVE_THREAD_COUNT = 2;

    public void start() {
        ///启动两个线程阻塞读取push notify queue
        Thread[] threads = getStartThreads();
        for (int i = 0; i < threads.length; i++) {
            threads[i].setName("logic_receive_notify_" + i);
            threads[i].start();
        }
    }

    private Thread[] getStartThreads() {
        Thread[] threads = new Thread[RECEIVE_THREAD_COUNT];
        for (int i = 0; i < RECEIVE_THREAD_COUNT; i++) {
            Thread thread = new Thread(new ReceiveNotifyTask());
            threads[i] = thread;
        }
        return threads;
    }

    class ReceiveNotifyTask implements Runnable {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void run() {
            try {
                while (receive) {
                    ///从队列中获取消息
                    String notifyJson = receiveNotifyQueue.pop();
                    if (StringUtils.isEmpty(notifyJson)) {
                        Thread.sleep(1);
                        continue;
                    }
                    ///处理通知
                    handle(notifyJson);
                }
            } catch (Exception e) {
                logger.error("at NotifyReceiveListener.ReceiveNotifyTask.run throw an error." + e.getMessage(), e.getCause());
            }
        }

        private void handle(String notifyJson) {
            try {
                ///记录日志
                StringBuilder strLog = new StringBuilder();
                strLog.append("[" + dateFormat.format(new Date()) + "] ");
                strLog.append("NotifyReceiveListener receive notify :" + notifyJson);
                logger.info(strLog.toString());

                ///执行任务
                JSONObject json = new JSONObject(notifyJson);
                Notify notify = new Notify(json);
                NotifyHandler task = new NotifyHandler(notify);
                GlobalObject.HANDLE_NOTIFY_THREAD_POOL.execute(task);
            } catch (Exception e) {
                logger.error("at NotifyReceiveListener.ReceiveNotifyTask.handle throw an error." + e.getMessage(), e.getCause());
            }
        }
    }

    class NotifyHandler implements Runnable {
        private Notify notify;

        public NotifyHandler(Notify notify) {
            this.notify = notify;
        }

        @Override
        public void run() {
            try {
                notifyPusher.push(notify);
            } catch (Exception e) {
                logger.error("at NotifyReceiveListener.NotifyHandler.run throw an error." + e.getMessage(), e.getCause());
            }
        }
    }
}