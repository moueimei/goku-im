package com.goku.im.net.web.server.proxy;


import com.goku.im.net.web.server.context.RequestContext;

/**
 * action interface
 *
 * @author moueimei
 */
public interface ActionExecutor {
    public String execute(RequestContext context);
}