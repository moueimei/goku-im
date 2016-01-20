package com.goku.im.connector.service;

import com.goku.im.connector.global.common.InvokeSource;
import com.goku.im.connector.global.common.MessageTypeConst;
import com.goku.im.connector.model.Message;
import com.goku.im.connector.push.MessagePusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by moueimei on 15/11/27.
 * 发送消息包括: 推送本机消息, 保存到push queue, 保存到msg queue
 */
@Service
public class MessageService {
    @Autowired
    UserService userService;
    @Autowired
    MessagePusher messagePusher;

    public void sendMessage(Message message) throws Exception {
        int messageType = message.getMessageType();
        switch (messageType) {
            case MessageTypeConst.PRIVATE: {
                messagePusher.push(message, InvokeSource.Connector);
                break;
            }
            case MessageTypeConst.GROUP: {
                int groupId = message.getGroupId();
                List<Integer> userIds = userService.getGroupUserIds(groupId);
                if (null == userIds || userIds.size() == 0)
                    break;

                for (int userId : userIds) {
                    if (userId == message.getFromUserId())  ///消息的发送者不进行推送
                        continue;

                    message.setToUserId(userId);
                    messagePusher.push(message, InvokeSource.Connector);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 推送应答
     *
     * @throws Exception
     */
    public void ackPush(int userId, String messageId) throws Exception {
        ///如果客户端返回了推送应答,证明客户端已成功收到push消息,将离线消息从队列中删除
        ///暂时不做处理
    }
}
