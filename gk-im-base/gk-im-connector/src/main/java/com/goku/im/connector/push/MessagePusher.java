package com.goku.im.connector.push;

import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.global.common.ActionRequestTypeConst;
import com.goku.im.connector.global.common.InvokeSource;
import com.goku.im.connector.global.common.MessageTypeConst;
import com.goku.im.connector.model.Message;
import com.goku.im.connector.model.User;
import com.goku.im.connector.redis.DataBaseMessageQueue;
import com.goku.im.connector.redis.LogicReceiveMessageQueue;
import com.goku.im.connector.redis.OffLineGroupMessageRedis;
import com.goku.im.connector.redis.OffLinePrivateMessageRedis;
import com.goku.im.connector.service.UserService;
import com.goku.im.connector.util.ChannelManager;
import com.goku.im.framework.util.StringUtil;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by moueimei on 15/11/27.
 * 负责将消息推送至客户端的具体实现
 */
@Component
public class MessagePusher {
    private static final Logger logger = LoggerFactory.getLogger(MessagePusher.class);

    @Autowired
    UserService userService;
    @Autowired
    DataBaseMessageQueue dataBaseMessageQueue;
    @Autowired
    LogicReceiveMessageQueue logicReceiveMessageQueue;
    @Autowired
    OffLinePrivateMessageRedis offLinePrivateMessageRedis;
    @Autowired
    OffLineGroupMessageRedis offLineGroupMessageRedis;

    /**
     * 发送消息
     *
     * @param message 消息对象
     * @param source  调用来源
     * @throws Exception
     */
    public void push(Message message, InvokeSource source) throws Exception {
        ///根据userID获取用户连接的connector domain
        int userId = message.getToUserId();
        String connectorDomain = userService.getConnectorDomainByUserId(userId);

        ///推送流程:
        /// 1. 客户端不在线
        ///     a. 执行logic集群的业务逻辑
        ///         1). 如果客户端此时在线, 则由logic集群将消息添加到目标客户端对应的connector的 push msg queue中
        ///         2). 如果客户端此时掉线, 则由logic集群负责保存离线消息并添加到im_db_msg_queue
        /// 2. 客户端在线且在当前connector
        ///     a. 通道可用: 跳过logic集群的业务逻辑,将消息直接发送至客户端
        ///     b. 通道不可用: 跳过logic集群的业务逻辑, 将消息保存到离线消息列表
        ///     c. 判断调用来源
        ///         1). 如果是connector调用(也就是目标用户在当前的connector上),则需要将消息保存到im_db_msg_queue
        ///         2). 如果是pushqueue调用(通过logic推送过来), 则不需要保存消息,因为logic集群已经保存消息到im_db_msg_queue了
        /// 3. 客户端在线但不在当前connector
        ///     a. 执行logic集群的业务逻辑
        ///         1). 如果客户端此时在线, 则由logic集群将消息添加到目标客户端对应的connector的 push msg queue中
        ///         2). 如果客户端此时掉线, 则由logic集群负责保存离线消息并添加到im_db_msg_queue

        StringBuilder strLog = new StringBuilder();

        ///如果客户端不在线, 记录离线消息
        if (StringUtil.isNullOrEmpty(connectorDomain)) {
            ///将消息保存到logic集群的 receive msg queue
            logicReceiveMessageQueue.push(message.toJson().toString());

            ///记录日志
            strLog.append("[connector is null or empty, send message to logic] ");
            strLog.append("send userId: " + userId + " ");
            strLog.append("send message: " + message.toJson().toString());
            logger.info(strLog.toString());
            return;
        }

        ///如果目标用户在本机,则直接发送,反之,则添加到logic集群对应的receive msg queue中
        if (connectorDomain.equals(GlobalConfig.OWNER_DOMAIN)) {
            Channel channel = ChannelManager.getChannel(userId);
            if (null != channel && channel.isOpen()) {
                ///构建push消息
                JSONObject json = buildPushJson(message);

                ///发送消息
                channel.writeAndFlush(json.toString());

                ///记录日志
                strLog.append("[connector is owner and channel is open, send message to user] ");
                strLog.append("send userId: " + userId + " ");
                strLog.append("send message: " + message.toJson().toString());
                logger.info(strLog.toString());
            } else {
                ///将通道从本机jvm移除
                ChannelManager.remove(userId);

                ///保存到离线消息
                int messageType = message.getMessageType();
                String messageId = message.getMessageId();
                if (messageType == MessageTypeConst.PRIVATE)  ///添加私聊离线消息
                    offLinePrivateMessageRedis.add(userId, message.getCreateTime(), messageId);
                else if (messageType == MessageTypeConst.GROUP)  ///添加群聊离线消息
                    offLineGroupMessageRedis.add(userId, message.getCreateTime(), messageId);

                ///记录日志
                strLog.append("[connector is owner but channel is closed, send message to offline] ");
                strLog.append("send userId: " + userId + " ");
                strLog.append("send message: " + message.toJson().toString());
                logger.info(strLog.toString());
            }

            if (source == InvokeSource.Connector) {
                ///保存到im_db_msg_queue
                dataBaseMessageQueue.push(message.toJson().toString());
            }
        } else {
            ///将消息保存到logic集群的 receive msg queue
            logicReceiveMessageQueue.push(message.toJson().toString());

            ///记录日志
            strLog.append("[connector is not owner, send message to logic] ");
            strLog.append("send userId: " + userId + " ");
            strLog.append("send message: " + message.toJson().toString());
            logger.info(strLog.toString());
        }
    }

    /**
     * 创建推送给客户端的json对象
     *
     * @param message
     * @return
     * @throws Exception
     */
    private JSONObject buildPushJson(Message message) throws Exception {
        int fromId = message.getFromUserId();
        if (message.getMessageType() == MessageTypeConst.GROUP)
            fromId = message.getGroupId();

        JSONObject json = new JSONObject();
        json.put("action", ActionRequestTypeConst.PUSH_MSG);
        json.put("msgId", message.getMessageId());
        json.put("msgType", message.getMessageType());
        json.put("fromId", fromId);
        JSONObject jsonFromUser = new JSONObject();
        jsonFromUser.put("puId", message.getFromUserId());
        ///获取发送消息的用户信息
        User user = userService.getUserInfo(message.getFromUserId());
        if (null != user) {
            jsonFromUser.put("nick", user.getNickName());
            jsonFromUser.put("photo", user.getAvatar());
            jsonFromUser.put("sex", user.getGender());
        }
        json.put("fromUser", jsonFromUser);
        json.put("to", message.getToUserId());
        json.put("content", message.getContent());
        json.put("contentType", message.getContentType());
        json.put("createTime", message.getCreateTime());

        return json;
    }
}