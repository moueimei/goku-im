package com.goku.im.connector.handler.action;

import com.goku.im.connector.global.ReturnValue;
import io.netty.channel.Channel;
import org.json.JSONObject;

/**
 * Created by moueimei on 15/11/26.
 */
public interface ActionHandler {
    ReturnValue exec(Channel channel, JSONObject json) throws Exception;
}