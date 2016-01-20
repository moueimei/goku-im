package com.goku.im.net.web.server.action.impl;


import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.context.RequestContext;

/**
 * @author moueimei
 */
public abstract class AbstractActionResolve implements ActionResolve {
    @Override
    public String getAction(RequestContext context) throws Exception {
        String postData = context.getPostData();
        if (null == postData || "".equals(postData))
            return null;

        return resolveAction(postData);
    }

    protected abstract String resolveAction(String content) throws Exception;
}