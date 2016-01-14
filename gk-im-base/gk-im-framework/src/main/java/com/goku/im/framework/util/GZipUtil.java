package com.goku.im.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author zhaodx
 *
 */
public class GZipUtil
{
	private static String encode = "utf-8";

	/**
	 * 字符串压缩为字节数组
	 * @param str
	 * @return
	 */
	public static byte[] compressToByte(String str)
	{
		return compressToByte(str, encode);
	}

	/**
	 * 字符串压缩为字节数组
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static byte[] compressToByte(String str, String encoding)
	{
		if (null == str|| str.length() == 0)
			return null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try 
		{
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	/*
	 * 字符串压缩为字节数组
	 */
	public static byte[] compress(byte[] bytes)
	{
		if (null == bytes|| bytes.length == 0)
			return null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try 
		{
			gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 字节数组解压缩后返回字符串
	 * @param bytes
	 * @return
	 */
	public static String uncompressToString(byte[] bytes)
	{
		if (null == bytes|| bytes.length == 0)
			return null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try 
		{
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0)
				out.write(buffer, 0, n);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return out.toString();
	}

	/**
	 * 字节数组解压缩后返回字符串
	 * @param bytes
	 * @param encoding
	 * @return
	 */
	public static String uncompressToString(byte[] bytes, String encoding)
	{
		if (null == bytes || bytes.length == 0)
			return null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try 
		{
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0)
				out.write(buffer, 0, n);
			
			return out.toString(encoding);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String uncompressToString(InputStream is, String encode)
	{
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
	    try
	    {
	    	gzip = new GZIPInputStream(is);
	        baos = new ByteArrayOutputStream();
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = gzip.read(buf)) != -1)
	            baos.write(buf, 0, len);
	        byte[] b = baos.toByteArray();
	        baos.flush();
	        
	        return new String(b, encode);
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	    	try
	    	{
	    		if(null != baos)
	    			baos.close();
	    		if(null != gzip)
	    			gzip.close();
	    	}
	    	catch(Exception e)
	    	{}
	    }
	    return null;
	}
}