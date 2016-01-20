package com.goku.im.connector.global;

/**
 * Created by moueimei on 15/11/29.
 */
public class ReturnHelper {
    /**
     * 成功
     *
     * @param action 动作
     * @param info   数据
     * @return
     */
    public static ReturnValue success(String action, Object info) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.SUCCESS);
        value.setMessage("OK");
        if (null != info)
            value.setInfo(info);

        return value;
    }

    /**
     * 成功
     *
     * @param action 动作
     * @param info   数据
     * @return
     */
    public static ReturnValue success(String action, Object info, boolean needCloseChannel) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.SUCCESS);
        value.setMessage("OK");
        value.setNeedClose(needCloseChannel);
        if (null != info)
            value.setInfo(info);

        return value;
    }

    /**
     * 缺少必要参数
     *
     * @param action
     * @return
     */
    public static ReturnValue lostNecessaryParameter(String action) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.LOST_NECESSARY_PARAMETER);
        value.setMessage("缺少必要参数");

        return value;
    }

    /**
     * 参数格式错误
     *
     * @param action
     * @return
     */
    public static ReturnValue parameterFormatError(String action) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.PARAMETER_FORMAT_ERROR);
        value.setMessage("参数格式错误");

        return value;
    }

    /**
     * 无效操作
     *
     * @param action
     * @return
     */
    public static ReturnValue invalidOperation(String action) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.INVALID_OPERATION);
        value.setMessage("无效操作");

        return value;
    }

    /**
     * 非法请求
     *
     * @param action
     * @return
     */
    public static ReturnValue invalidRequest(String action) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.INVALID_REQUEST);
        value.setMessage("非法请求");

        return value;
    }

    /**
     * 服务端错误
     *
     * @param action
     * @return
     */
    public static ReturnValue ServerError(String action) {
        ReturnValue value = new ReturnValue();
        value.setAction(action);
        value.setCode(ReturnCodeConst.SERVER_ERROR);
        value.setMessage("服务端错误");

        return value;
    }
}