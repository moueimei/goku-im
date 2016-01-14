package com.goku.im.net.web.server.proxy;

import tv.acfun.im.net.web.server.context.RequestContext;

/**
 * action interface 
 * @author milozhao
 *
 */
public interface ActionExecutor
{
	public String execute(RequestContext context);
}