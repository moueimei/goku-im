package com.goku.im.connector.receiver;

import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.global.GlobalObject;
import com.goku.im.connector.global.common.InvokeSource;
import com.goku.im.connector.model.Notify;
import com.goku.im.connector.push.NotifyPusher;
import com.goku.im.connector.redis.ConnectorPushNotifyQueue;
import com.goku.im.framework.util.StringUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moueimei on 15/12/10.
 */
@Component
public class NotifyReceiveListener {
    private static final Logger logger = LoggerFactory.getLogger(NotifyReceiveListener.class);

    @Autowired
    ConnectorPushNotifyQueue pushNotifyQueue;
    @Autowired
    NotifyPusher notifyPusher;

    private boolean receive = true;
    private final static int RECEIVE_THREAD_COUNT = 2;

    public void start() {
        ///启动两个线程阻塞读取push notify queue
        Thread[] threads = getStartThreads();
        for (int i = 0; i < threads.length; i++) {
            threads[i].setName("connector_receive_notify_" + i);
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
                    ///从队列中获取通知
                    String notifyJson = pushNotifyQueue.pop(GlobalConfig.OWNER_DOMAIN);
                    if (StringUtil.isNullOrEmpty(notifyJson)) {
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
                PushNotifyTask task = new PushNotifyTask(notify);
                GlobalObject.NOTIFY_THREAD_POOL.execute(task);
            } catch (Exception e) {
                logger.error("at NotifyReceiveListener.ReceiveNotifyTask.handle throw an error." + e.getMessage(), e.getCause());
            }
        }
    }

    class PushNotifyTask implements Runnable {
        private Notify notify;

        public PushNotifyTask(Notify notify) {
            this.notify = notify;
        }

        @Override
        public void run() {
            try {
                notifyPusher.push(notify, InvokeSource.PushQueue);
            } catch (Exception e) {
                logger.error("at NotifyReceiveListener.PushNotifyTask.run throw an error." + e.getMessage(), e.getCause());
            }
        }
    }
}