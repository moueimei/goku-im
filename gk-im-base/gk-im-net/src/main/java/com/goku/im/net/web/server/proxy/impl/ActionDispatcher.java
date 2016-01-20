package com.goku.im.net.web.server.proxy.impl;


import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.context.RequestContext;
import com.goku.im.net.web.server.proxy.ActionExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moueimei
 */
public class ActionDispatcher {
    private final static ActionDispatcher DISPATCHER = new ActionDispatcher();
    private final static Map<String, ActionExecutor> ACTION_MAP = new HashMap<String, ActionExecutor>();
    private ActionResolve actionResolve;

    private ActionDispatcher() {
    }

    public static ActionDispatcher getInstance() {
        return DISPATCHER;
    }

    public void setActionResolve(ActionResolve actionResolve) {
        this.actionResolve = actionResolve;
    }

    public boolean regiester(String url, ActionExecutor executor) {
        if (null == url || "".equals(url))
            return false;
        if (null == executor)
            return false;

        ACTION_MAP.put(url, executor);
        return true;
    }

    public String execute(RequestContext context) throws Exception {
        if (null == actionResolve)
            return null;

        String action = actionResolve.getAction(context);
        if (null == action || "".equals(action))
            return null;

        if (!ACTION_MAP.containsKey(action))
            return null;

        ActionExecutor executor = ACTION_MAP.get(action);
        if (null == executor)
            return null;
        try {
            return executor.execute(context);
        } catch (Exception e) {
            throw e;
        }
    }
}