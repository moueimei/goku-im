package com.goku.im.pushservice.service;

import tv.acfun.im.pushservice.model.Notify;

/**
 * Created by milo on 15/12/10.
 */
public interface AcImNotifyService
{
    /**
     * 发送通知
     * @param notify 通知对象
     * @return
     */
    int push(Notify notify);
}