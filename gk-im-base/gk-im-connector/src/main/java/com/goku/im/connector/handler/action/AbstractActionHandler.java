package com.goku.im.connector.handler.action;

import com.goku.im.connector.global.ReturnValue;
import com.goku.im.connector.model.Atom;
import io.netty.channel.Channel;
import org.json.JSONObject;


/**
 * Created by moueimei on 15/11/27.
 */
public abstract class AbstractActionHandler implements ActionHandler {
    @Override
    public ReturnValue exec(Channel channel, JSONObject json) throws Exception {
        return handle(channel, json);
    }

    protected abstract ReturnValue handle(Channel channel, JSONObject json) throws Exception;

    protected Atom parseAtom(String strAtom) {
        Atom atom = new Atom();
        return atom;
    }
}