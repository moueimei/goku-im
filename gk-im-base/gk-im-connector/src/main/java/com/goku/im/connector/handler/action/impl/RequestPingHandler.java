package com.goku.im.connector.handler.action.impl;

import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;


/**
 * Created by moueimei on 15/11/26.
 * 心跳请求(ping)处理器
 * 心跳请求由客户端发送
 */
@Controller
public class RequestPingHandler extends AbstractActionHandler {
    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception {
        ReturnValue value = new ReturnValue();
        value.setAction(ActionAckTypeConst.ACK_HEART_BEAT_PONG);
        return value;
    }
}