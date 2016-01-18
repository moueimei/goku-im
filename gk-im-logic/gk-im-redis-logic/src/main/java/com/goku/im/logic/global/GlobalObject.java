package com.goku.im.logic.global;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by milo on 15/11/26.
 * 全局公共对象
 */
public class GlobalObject {
    /**
     * 处理接收到的消息的线程池
     */
    public final static ExecutorService HANDLE_MESSAGE_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 处理接收到的通知的线程池
     */
    public final static ExecutorService HANDLE_NOTIFY_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
}