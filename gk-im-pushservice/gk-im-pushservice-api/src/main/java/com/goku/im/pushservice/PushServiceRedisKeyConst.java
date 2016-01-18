package com.goku.im.pushservice;

/**
 * Created by moueimei on 15/12/10.
 */
public class PushServiceRedisKeyConst {
    /**
     * 接收通知队列(logic集群从该队列中读取待处理的通知,然后负责推送至connector对应的 im_push_notify_queue中)
     * 结构: List
     * 示例: lpush im_receive_notify_queue
     */
    public final static String LOGIC_RECEIVE_NOTIFY_QUEUE_KEY = "im_receive_notify_queue";
}
