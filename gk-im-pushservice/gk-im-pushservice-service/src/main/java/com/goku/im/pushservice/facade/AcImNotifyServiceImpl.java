package com.goku.im.pushservice.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tv.acframework.qos.collector.core.utils.StringUtils;
import tv.acfun.im.pushservice.PushServiceCodeConst;
import tv.acfun.im.pushservice.model.Notify;
import tv.acfun.im.pushservice.push.NotifyPusher;
import tv.acfun.im.pushservice.service.AcImNotifyService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by milo on 15/12/10.
 */
@Service("acImNotifyService")
public class AcImNotifyServiceImpl implements AcImNotifyService
{
    private static final Logger logger = LoggerFactory.getLogger(AcImNotifyServiceImpl.class);

    public final static ExecutorService NOTIFY_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    NotifyPusher notifyPusher;

    @Override
    public int push(Notify notify)
    {
        int fromUserId = notify.getFromUserId();
        List<Integer> toUserIds = notify.getToUserIds();
        String notifyType = notify.getNotifyType();

        if(0 == fromUserId
                || null == toUserIds
                || toUserIds.size() == 0
                || StringUtils.isBlank(notifyType))
            return PushServiceCodeConst.LOST_NECESSARY_PARAMETER;

        if(notifyType.length() > 128)
            return PushServiceCodeConst.PARAMETER_FORMAT_ERROR;

        ///使用线程池执行具体业务
        Runnable task = () ->
        {
            try
            {
                notifyPusher.push(notify);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        NOTIFY_THREAD_POOL.execute(task);

        return PushServiceCodeConst.SUCCESS;
    }
}