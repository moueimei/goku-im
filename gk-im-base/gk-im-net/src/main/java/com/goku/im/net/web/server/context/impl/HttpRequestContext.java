package com.goku.im.net.web.server.context.impl;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import tv.acfun.im.net.web.server.context.RequestContext;
import tv.acfun.im.net.web.server.parse.PostDataParser;
import tv.acfun.im.net.web.server.parse.impl.TextPostDataParser;

import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpRequestContext implements RequestContext
{
	private Map<String, String> paramMap = new HashMap<String, String>();
	private HttpRequest request;
	private SocketAddress remoteAddress;
	private Charset charset = Charset.forName("UTF-8");
	private String postData;
	
	public HttpRequestContext(HttpRequest request) throws Exception
	{
		this.request = request;
		if(null == request)
			throw new Exception("HttpRequest can not be null.");
		
		initParam();
	}
	
	public String getRequestUrl()
	{
		return request.getUri();
	}
	
	public String getHeaders(String key)
	{
		return this.request.headers().get(key);
	}
	
	public HttpMethod getMethod()
	{
		return this.request.getMethod();
	}
	
	public Set<String> getHeaderNames()
	{
		return this.request.headers().names();
	}
	
	public String getParameters(String key)
	{
		return paramMap.get(key);
	}
	
	public Map<String, String> getParamMap() 
	{
		return paramMap;
	}
	
	public HttpRequest getRequest()
	{
		return this.request;
	}

	public SocketAddress getRemoteAddress()
	{
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}
	
	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}
	
	public String getPostData()
	{
		return this.postData;
	}

	private void initParam() throws Exception
	{
		try 
		{
			QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri(), charset);
			if(null != decoderQuery)
			{
	            Map<String, List<String>> queryDataMap = decoderQuery.parameters();
	            if(null != queryDataMap)
	            {
		            for (Entry<String, List<String>> entry: queryDataMap.entrySet())
		            	paramMap.put(entry.getKey(), entry.getValue().get(0));
	            }
			}
			
            if(request.getMethod().equals(HttpMethod.POST))
            {
	            PostDataParser parser = new TextPostDataParser();
	            if(null != parser)
	            {
		            	DefaultFullHttpRequest fullReq = (DefaultFullHttpRequest)request;
		            	postData = getPostData(fullReq);
		            	Map<String, String> postDataMap = parser.parse(postData);
		            	if(null != postDataMap)
		            		paramMap.putAll(postDataMap);
	            }
            }
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	private String getPostData(DefaultFullHttpRequest request)
	{
		ByteBuf buffer = request.content();
		if(null == buffer)
			return null;
		
		return buffer.toString(charset);
	}
}
