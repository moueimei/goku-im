package com.goku.im.connector.handler.action.impl;

import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.goku.im.connector.global.ReturnCodeConst;
import com.goku.im.connector.global.ReturnHelper;
import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import com.goku.im.connector.logic.UserLogic;
import com.goku.im.framework.util.StringUtil;

/**
 * Created by milo on 15/11/26.
 * 连接请求处理器
 * 连接请求由客户端发送
 */
@Controller
public class RequestConnectHandler extends AbstractActionHandler
{
    @Autowired
    UserLogic userLogic;

    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception
    {
        ReturnValue value = new ReturnValue();
        String userToken = json.optString("token", null);
        String ackAction = ActionAckTypeConst.ACK_CONNECT;

        if(StringUtil.isNullOrEmpty(userToken) || "null".equals(userToken.toLowerCase()))
        {
            ///缺少必要参数
            return ReturnHelper.lostNecessaryParameter(ackAction);
        }

        if(userToken.length() > 256)
        {
            ///返回参数格式错误
            return ReturnHelper.parameterFormatError(ackAction);
        }

        ///根据user token 从redis中获取用户ID
        int userId = userLogic.getUserIdByUserToken(userToken);
        if(0 == userId)
        {
            ///用户token已过期,需要重新登录
            userLogic.logout(userToken);

            ///2. 返回用户token已过期
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.USER_TOKEN_HAS_EXPIRE);
            value.setMessage("用户token已过期");
            return value;
        }

        userLogic.connect(channel, userId);

        ///返回结果
        return ReturnHelper.success(ackAction, null);
    }
}