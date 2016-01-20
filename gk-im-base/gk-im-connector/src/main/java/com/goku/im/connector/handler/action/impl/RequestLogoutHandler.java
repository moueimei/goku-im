package com.goku.im.connector.handler.action.impl;

import com.goku.im.connector.global.ReturnHelper;
import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import com.goku.im.connector.logic.UserLogic;
import com.goku.im.framework.util.StringUtil;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Created by moueimei on 15/11/27.
 * 登出请求处理器
 * 登出请求由客户端发送
 */
@Controller
public class RequestLogoutHandler extends AbstractActionHandler {
    @Autowired
    UserLogic userLogic;

    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception {
        String userToken = json.optString("token", null);
        String ackAction = ActionAckTypeConst.ACK_LOGOUT;

        if (StringUtil.isNullOrEmpty(userToken)) {
            ///返回缺少必要参数
            return ReturnHelper.lostNecessaryParameter(ackAction);
        }

        if (userToken.length() > 256) {
            ///返回参数格式错误
            return ReturnHelper.parameterFormatError(ackAction);
        }

        ///执行用户登出操作
        userLogic.logout(userToken);

        ///返回结果
        return ReturnHelper.success(ackAction, null, true);
    }
}