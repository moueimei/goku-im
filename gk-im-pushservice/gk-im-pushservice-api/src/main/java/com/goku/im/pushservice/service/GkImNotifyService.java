package com.goku.im.pushservice.service;


import com.goku.im.pushservice.model.Notify;

/**
 * Created by moueimei on 15/12/10.
 */
public interface GkImNotifyService
{
    /**
     * 发送通知
     * @param notify 通知对象
     * @return
     */
    int push(Notify notify);
}