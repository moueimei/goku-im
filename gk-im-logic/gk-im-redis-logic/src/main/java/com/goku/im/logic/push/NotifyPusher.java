package com.goku.im.logic.push;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.acframework.config.core.util.StringUtils;
import tv.acfun.im.logic.model.Notify;
import tv.acfun.im.logic.redis.*;

/**
 * Created by milo on 15/12/10.
 * 负责将通知保存到持久层的待处理通知队列以及推送至connector的notify push queue中
 */
@Component
public class NotifyPusher
{
    @Autowired
    UserRegisterRedis userRegisterRedis;
    @Autowired
    OffLineNotifyRedis offLineNotifyRedis;
    @Autowired
    ConnectorPushNotifyQueue pushNotifyQueue;
    @Autowired
    DataBaseNotifyQueue dataBaseNotifyQueue;

    /**
     * 发送消息
     * @param notify 消息对象
     * @throws Exception
     */
    public void push(Notify notify) throws Exception
    {
        int userId = notify.getToUserId();
        String notifyId = notify.getNotifyId();

        ///构建push通知
        JSONObject json = notify.toJson();

        ///根据userID获取用户连接的connector domain
        String connectorDomain = userRegisterRedis.getConnectorDomainByUserId(userId);
        if(StringUtils.isNullOrEmpty(connectorDomain))
        {
            ///客户端不在线, 记录离线消息
            offLineNotifyRedis.add(userId, notify.getCreateTime(), notifyId);
        }
        else
        {
            ///将消息保存到connector对应的push notify queue中
            pushNotifyQueue.push(connectorDomain, json.toString());
        }

        ///将消息添加到持久层的待处理消息队列中
        dataBaseNotifyQueue.push(json.toString());
    }
}
