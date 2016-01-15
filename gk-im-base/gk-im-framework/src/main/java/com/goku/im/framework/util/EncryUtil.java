package com.goku.im.framework.util;

import java.security.MessageDigest;

/**
 * 
 * @author zhaodx
 *
 */
public class EncryUtil
{
	/**
	 * MD5加密
	 */
	public static String md5(String value)
    {
        try
        {
            byte[] res = value.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5".toUpperCase());
            byte[] result=md.digest(res);
            for(int i=0;i<result.length;i++)
            {
                md.update(result[i]);
            }
            byte[] hash=md.digest();
            StringBuffer d=new StringBuffer("");
            for(int i=0;i<hash.length;i++)
            {
                int v=hash[i] & 0xFF;
                if(v<16) d.append("0");
                    d.append(Integer.toString(v,16).toUpperCase()+"");
            }
            return d.toString();
        }
        catch(Exception e)
        {
            return "";
        }
    }
}