package com.goku.im.connector.handler.action;

import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.global.common.ActionRequestTypeConst;
import com.goku.im.connector.handler.action.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by moueimei on 15/11/26.
 * 动作处理器工厂
 */
@Component
public class ActionHandlerFactory {
    @Autowired
    RequestLoginHandler requestLoginHandler;
    @Autowired
    RequestLogoutHandler requestLogoutHandler;
    @Autowired
    RequestSendMessageHandler requestSendMessageHandler;
    @Autowired
    AckPushMessageHandler ackPushMessageHandler;
    @Autowired
    RequestConnectHandler requestConnectionHandler;
    @Autowired
    RequestPingHandler requestPingHandler;

    public ActionHandler getHandler(String action) {
        if (action.equals(ActionRequestTypeConst.LOGIN))
            return requestLoginHandler;
        else if (action.equals(ActionRequestTypeConst.LOGOUT))
            return requestLogoutHandler;
        else if (action.equals(ActionRequestTypeConst.SEND_MSG))
            return requestSendMessageHandler;
        else if (action.equals(ActionRequestTypeConst.CONNECTION))
            return requestConnectionHandler;
        else if (action.equals(ActionRequestTypeConst.HEART_BEAT_PING))
            return requestPingHandler;
        else if (action.equals(ActionAckTypeConst.ACK_PUSH_MSG))
            return ackPushMessageHandler;

        return null;
    }
}
