package com.goku.im.net.web.server.proxy.impl;

import tv.acfun.im.net.web.server.proxy.ActionExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 
 * @author zhaodx
 *
 */
public class ActionInvocationHandler implements InvocationHandler 
{
	private ActionExecutor executor;
	
	public ActionInvocationHandler(ActionExecutor executor)
	{
		this.executor = executor;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable
	{
		Object obj = method.invoke(executor, args);
		return obj;
	}
}