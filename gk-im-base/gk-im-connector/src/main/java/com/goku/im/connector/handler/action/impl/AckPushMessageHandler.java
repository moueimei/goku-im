package com.goku.im.connector.handler.action.impl;

import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import com.goku.im.connector.logic.MessageLogic;
import com.goku.im.connector.util.TokenManager;
import com.goku.im.framework.util.StringUtil;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Created by moueimei on 15/11/26.
 * 推送消息应答处理器
 * 推送消息应答由客户端发送
 */
@Controller
public class AckPushMessageHandler extends AbstractActionHandler {
    @Autowired
    MessageLogic messageLogic;

    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception {
        String userToken = json.optString("token", null);
        int code = json.optInt("code", -1);
        JSONObject info = json.optJSONObject("info");

        if (!StringUtil.isNullOrEmpty(userToken)) {
            if (code == 0 && null != info) {
                String messageId = info.optString("msgId", null);
                if (!StringUtil.isNullOrEmpty(messageId)) {
                    ///客户端应答的消息不需要从register store获取用户ID,直接从user token中解析即可
                    int userId = TokenManager.getUserIdByUserToken(userToken);
                    if (userId > 0) {
                        messageLogic.ackPush(userId, messageId);
                    }
                }
            }
        }

        ///不需要再向客户端发送消息
        return null;
    }
}