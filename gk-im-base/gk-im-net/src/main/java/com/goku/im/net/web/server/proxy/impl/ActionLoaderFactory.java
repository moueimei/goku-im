package com.goku.im.net.web.server.proxy.impl;

import tv.acfun.im.net.web.server.proxy.ActionLoader;

/**
 * 
 * @author zhaodx
 *
 */
public class ActionLoaderFactory
{
	public static ActionLoader getActionLoader()
	{
		return new AnnotationActionLoader();
	}
}