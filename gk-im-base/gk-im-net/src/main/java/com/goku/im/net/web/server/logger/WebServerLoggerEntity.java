package com.goku.im.net.web.server.logger;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author zhaodx
 *
 */
public class WebServerLoggerEntity
{
	 private String message;
	 private ChannelHandlerContext context;
	 private Throwable cause;
	 
	public String getMessage() 
	{
		return message;
	}
	
	public void setMessage(String message) 
	{
		this.message = message;
	}
	
	public ChannelHandlerContext getContext() 
	{
		return context;
	}
	
	public void setContext(ChannelHandlerContext context) 
	{
		this.context = context;
	}

	public Throwable getCause()
	{
		return cause;
	}

	public void setCause(Throwable cause)
	{
		this.cause = cause;
	}
}