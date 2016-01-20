package com.goku.im.connector.handler.action.impl;

import com.goku.im.connector.global.ReturnCodeConst;
import com.goku.im.connector.global.ReturnHelper;
import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.global.common.MessageContentTypeConst;
import com.goku.im.connector.global.common.MessageTypeConst;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import com.goku.im.connector.logic.MessageLogic;
import com.goku.im.connector.logic.UserLogic;
import com.goku.im.connector.model.Message;
import com.goku.im.framework.util.StringUtil;
import com.goku.im.relation.UserRelationCodeConst;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Created by moueimei on 15/11/26.
 * 发送消息请求处理器
 * 发送消息请求由客户端发送
 */
@Controller
public class RequestSendMessageHandler extends AbstractActionHandler {
    @Autowired
    MessageLogic messageLogic;
    @Autowired
    UserLogic userLogic;

    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception {
        ReturnValue value = new ReturnValue();
        String messageId = json.optString("msgId", null);
        String userToken = json.optString("token", null);
        String content = json.optString("content", null);
        int contentType = json.optInt("contentType", MessageContentTypeConst.TEXT);
        int messageType = json.optInt("msgType", MessageTypeConst.PRIVATE);
        int to = json.optInt("to", 0);
        String ackAction = ActionAckTypeConst.ACK_SEND_MSG;

        if (StringUtil.isNullOrEmpty(userToken)
                || StringUtil.isNullOrEmpty(messageId)
                || StringUtil.isNullOrEmpty(content)) {
            ///返回缺少必要参数
            return ReturnHelper.lostNecessaryParameter(ackAction);
        }

        if (userToken.length() > 256
                || messageId.length() > 128
                || content.length() > 2048
                || to <= 0L) {
            ///返回参数格式错误
            return ReturnHelper.parameterFormatError(ackAction);
        }

        ///根据user token 从redis中获取用户ID
        int fromUserId = userLogic.getUserIdByUserToken(userToken);
        if (0 == fromUserId) {
            ///用户token已过期,需要重新登录
            userLogic.logout(userToken);

            ///2. 返回用户token已过期
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.USER_TOKEN_HAS_EXPIRE);
            value.setMessage("用户token已过期");
            return value;
        }

        int toUserId = 0;
        int groupId = 0;
        if (messageType == MessageTypeConst.PRIVATE) {
            toUserId = to;
            boolean isFriend = userLogic.isFriend(fromUserId, toUserId);
            if (!isFriend) {
                ///双方不是好友
                value.setAction(ackAction);
                value.setCode(UserRelationCodeConst.WE_ARE_NOT_FRIEND);
                value.setMessage("双方不是好友");
                return value;
            }
        } else if (messageType == MessageTypeConst.GROUP)
            groupId = to;

        ///构建message对象
        Message message = new Message();
        message.setMessageId(messageId);
        message.setMessageType(messageType);
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setGroupId(groupId);
        message.setContent(content);
        message.setContentType(contentType);
        messageLogic.sendMessage(message);

        ///返回结果
        JSONObject info = new JSONObject();
        info.put("msgId", messageId);
        return ReturnHelper.success(ackAction, info);
    }
}