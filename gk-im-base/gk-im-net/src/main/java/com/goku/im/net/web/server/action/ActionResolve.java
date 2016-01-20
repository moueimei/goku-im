package com.goku.im.net.web.server.action;


import com.goku.im.net.web.server.context.RequestContext;

/**
 * @author moueimei
 */
public interface ActionResolve {
    public String getAction(RequestContext context) throws Exception;
}