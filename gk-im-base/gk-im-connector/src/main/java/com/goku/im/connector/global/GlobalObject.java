package com.goku.im.connector.global;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by moueimei on 15/11/26.
 * 全局公共对象
 */
public class GlobalObject {
    /**
     * uid对应的tcp通道集合
     */
    public final static ConcurrentHashMap<Integer, Channel> UID_CHANNEL_MAP = new ConcurrentHashMap<>();

    public final static ExecutorService MESSAGE_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public final static ExecutorService NOTIFY_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
}