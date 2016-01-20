package com.goku.im.connector.handler.action.impl;


import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.global.ReturnCodeConst;
import com.goku.im.connector.global.ReturnHelper;
import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.global.common.ActionAckTypeConst;
import com.goku.im.connector.handler.action.AbstractActionHandler;
import com.goku.im.connector.logic.UserLogic;
import com.goku.im.framework.util.StringUtil;
import com.goku.user.GkUserConst;
import com.goku.user.service.LoginService;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Created by moueimei on 15/11/26.
 * 登录请求处理器
 * 登录请求由客户端发送
 */
@Controller
public class RequestLoginHandler extends AbstractActionHandler {
    @Autowired
    UserLogic userLogic;
    @Autowired
    LoginService loginService;

    @Override
    protected ReturnValue handle(Channel channel, JSONObject json) throws Exception {
        ReturnValue value = new ReturnValue();
        String atom = json.optString("atom", null);
        String account = json.optString("account", null);
        String password = json.optString("password", null);
        String userType = json.optString("user_type", null);
        String ackAction = ActionAckTypeConst.ACK_LOGIN;

        if (StringUtil.isNullOrEmpty(atom)
                || StringUtil.isNullOrEmpty(account)
                || StringUtil.isNullOrEmpty(password)
                || StringUtil.isNullOrEmpty(userType)) {
            ///返回缺少必要参数
            return ReturnHelper.lostNecessaryParameter(ackAction);
        }

        if (atom.length() > 128
                || account.length() > 64
                || password.length() > 64
                || userType.length() > 16) {
            ///返回参数格式错误
            return ReturnHelper.parameterFormatError(ackAction);
        }

        ///调用dubbo服务进行用户登录
        String appId = GlobalConfig.APP_ID;
        String appKey = GlobalConfig.APP_KEY;
        Map<String, String> map = loginService.login(account, password, appId, appKey, userType, channel.remoteAddress().toString());
        if (null == map) {
            ///登录失败
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.LOGIN_FAIL);
            value.setMessage("登录失败");
            return value;
        }

        String key = GkUserConst.LOGIN_MAP_CODE;

        String strCode = map.get(key);
        if (!StringUtil.isInteger(strCode)) {
            ///登录失败
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.LOGIN_FAIL);
            value.setMessage("登录失败");
            return value;
        }

        int code = Integer.parseInt(strCode);
        if (code == GkUserConst.ERROR_PASSWORD_CODE) {
            ///密码错误
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.USER_PASSWORD_ERROR);
            value.setMessage("密码错误");
            return value;
        } else if (code == GkUserConst.ERROR_ACCOUNT_CODE) {
            ///账号不存在
            value.setAction(ackAction);
            value.setCode(ReturnCodeConst.ACCOUNT_NOT_EXITSTS);
            value.setMessage("账号不存在");
            return value;
        } else if (code == GkUserConst.ERROR_ACCOUNT_BIND_CODE) {
            ///未绑定手机号,暂不处理
            code = GkUserConst.SUCCESS_CODE;
        }

        if (code == GkUserConst.SUCCESS_CODE) {
            String userToken = map.get("token");
            String strUserId = map.get("userID");

            ///如果登录成功, 则记录用户token对应的用户ID以及connector机器IP
            if (StringUtil.isNullOrEmpty(userToken) || !StringUtil.isInteger(strUserId)) {
                ///oauth token获取失败
                value.setAction(ackAction);
                value.setCode(ReturnCodeConst.USER_TOKEN_GET_FAIL);
                value.setMessage("token获取失败");
                return value;
            }

            int userId = Integer.parseInt(strUserId);
            if (userId <= 0) {
                ///非法的用户ID
                value.setAction(ackAction);
                value.setCode(ReturnCodeConst.INVALID_USER_ID);
                value.setMessage("非法的用户ID");
                return value;
            }

            ///执行登录流程
            userLogic.login(channel, userId, userToken);

            ///返回结果
            JSONObject info = new JSONObject();
            info.put("token", userToken);
            info.put("puId", userId);
            return ReturnHelper.success(ackAction, info);
        }

        ///返回结果
        value.setAction(ackAction);
        value.setCode(ReturnCodeConst.LOGIN_FAIL);
        value.setMessage("登录失败");
        return value;
    }
}
