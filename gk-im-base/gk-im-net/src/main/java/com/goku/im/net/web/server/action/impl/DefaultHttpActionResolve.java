package com.goku.im.net.web.server.action.impl;


import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.context.RequestContext;
import com.goku.im.net.web.server.context.impl.HttpRequestContext;


/**
 * @author moueimei
 */
public class DefaultHttpActionResolve implements ActionResolve {
    @Override
    public String getAction(RequestContext context) throws Exception {
        HttpRequestContext ctx = (HttpRequestContext) context;
        String url = ctx.getRequestUrl();
        if (null == url || "".equals(url))
            return null;

        String action = url;
        int pos = action.indexOf("?");
        if (pos > 0)
            action = action.substring(0, pos);

        if (action.startsWith("/"))
            action = action.substring(1, action.length());
        return action;
    }
}