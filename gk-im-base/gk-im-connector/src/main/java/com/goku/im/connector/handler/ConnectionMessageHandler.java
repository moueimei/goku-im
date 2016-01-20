package com.goku.im.connector.handler;

import com.goku.im.connector.global.ReturnHelper;
import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.handler.action.ActionHandler;
import com.goku.im.connector.handler.action.ActionHandlerFactory;
import com.goku.im.net.socket.server.context.SocketRequestContext;
import com.goku.im.net.socket.server.context.SocketResponse;
import com.goku.im.net.socket.server.handler.SocketExecuteHandler;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by moueimei on 15/11/26.
 */
@Component
public class ConnectionMessageHandler implements SocketExecuteHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionCloseHandler.class);

    @Autowired
    ActionHandlerFactory handlerFactory;

    public SocketResponse execute(SocketRequestContext context) {
        Object requestData = context.getRequestData();
        Channel channel = context.getContext().channel();
        String action = null;
        JSONObject json;

        try {
            json = new JSONObject(requestData.toString());
        } catch (Exception e) {
            ///非法请求
            return ReturnHelper.invalidRequest(null);
        }

        StringBuilder logMessage = new StringBuilder();
        try {
            action = json.optString("action", null);
            if (null == action) {
                ///无效操作
                return ReturnHelper.invalidOperation(action);
            }

            ActionHandler handler = handlerFactory.getHandler(action);
            if (null == handler) {
                ///无效操作
                return ReturnHelper.invalidOperation(action);
            }

            logMessage.append(action + " | ");
            logMessage.append(json.toString() + " | ");

            long startTime = System.currentTimeMillis();
            ReturnValue value = handler.exec(channel, json);
            long endTime = System.currentTimeMillis();

            ///记录请求日志
            ///日志格式: action | costtime | request | response
            /// action: 请求动作
            /// costtime: 本次请求所花费时间
            /// request: 客户端请求的数据
            /// response: 服务端响应的数据

            logMessage.append((endTime - startTime) + " | ");

            String strValue = "null";
            if (null != value)
                strValue = value.toJsonString();
            logMessage.append(strValue);
            logger.info(logMessage.toString());

            ///返回响应结果
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("at ConnectionMessageHandler.execute throw an error. request:" + logMessage.toString() + "  error message:" + e.getMessage(), e.getCause());
            return ReturnHelper.ServerError(action);
        }
    }
}