package com.goku.im.connector.global.common;

/**
 * Created by moueimei on 15/12/16.
 * 调用来源
 */
public enum InvokeSource {
    /**
     * 由Connector调用
     */
    Connector,

    /**
     * 由PushQueue调用
     */
    PushQueue
}