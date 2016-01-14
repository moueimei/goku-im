package com.goku.im.connector.global;

import org.json.JSONArray;
import org.json.JSONObject;
import com.goku.im.framework.util.StringUtil;
import com.goku.im.net.socket.server.context.SocketResponse;

/**
 * Created by milo on 15/11/26.
 */
public class ReturnValue implements SocketResponse
{
    private String action;
    private Integer code;
    private String message;
    private Object info;
    private boolean needClose = false;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setInfo(Object info)
    {
        this.info = info;
    }

    public void setNeedClose(boolean needClose) {
        this.needClose = needClose;
    }

    @Override
    public String toJsonString()
    {
        JSONObject json = new JSONObject();
        try
        {
            if(!StringUtil.isNullOrEmpty(action))
                json.put("action", action);
            if(null != code)
                json.put("code", code);
            if(!StringUtil.isNullOrEmpty(message))
                json.put("message", message);
            if(null != info)
            {
                if(info instanceof JSONObject)
                    json.put("info", (JSONObject) info);
                else if(info instanceof JSONArray)
                    json.put("info", (JSONArray) info);
            }
            return json.toString();
        }
        catch(Exception e)
        {
            return "{\"code\" : 500}";
        }
    }

    @Override
    public boolean needCloseChannel()
    {
        return needClose;
    }
}