package com.goku.im.logic.push;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tv.acframework.config.core.util.StringUtils;
import tv.acfun.im.logic.global.MessageTypeConst;
import tv.acfun.im.logic.model.Message;
import tv.acfun.im.logic.redis.*;

/**
 * Created by milo on 15/11/27.
 * 负责将消息保存到持久层的待处理消息队列以及推送至connector的msg push queue中
 */
@Component
public class MessagePusher
{
    @Autowired
    UserRegisterRedis userRegisterRedis;
    @Autowired
    OffLinePrivateMessageRedis offLinePrivateMessageRedis;
    @Autowired
    OffLineGroupMessageRedis offLineGroupMessageRedis;
    @Autowired
    ConnectorPushMessageQueue pushMessageQueue;
    @Autowired
    DataBaseMessageQueue dataBaseMessageQueue;

    /**
     * 发送消息
     * @param message 消息对象
     * @throws Exception
     */
    public void push(Message message) throws Exception
    {
        int userId = message.getToUserId();
        String messageId = message.getMessageId();

        ///根据userID获取用户连接的connector domain
        String connectorDomain = userRegisterRedis.getConnectorDomainByUserId(userId);

        ///构建push消息
        JSONObject json = message.toJson();

        ///如果客户端不在线, 记录离线消息
        if(StringUtils.isNullOrEmpty(connectorDomain))
        {
            int messageType = message.getMessageType();
            if(messageType == MessageTypeConst.PRIVATE)  ///添加私聊离线消息
                offLinePrivateMessageRedis.add(userId, message.getCreateTime(), messageId);
            else if(messageType == MessageTypeConst.GROUP)  ///添加群聊离线消息
                offLineGroupMessageRedis.add(userId, message.getCreateTime(), messageId);
        }
        else
        {
            ///添加到connector对应的push msg queue中
            pushMessageQueue.push(connectorDomain, json.toString());
        }

        ///将消息添加到持久层的待处理消息队列中
        dataBaseMessageQueue.push(json.toString());
    }
}