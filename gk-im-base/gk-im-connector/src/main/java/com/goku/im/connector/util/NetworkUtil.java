package com.goku.im.connector.util;


import com.goku.im.connector.global.GlobalConfig;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

/**
 * Created by moueimei on 15/12/5.
 * 网络工具
 */
public class NetworkUtil {
    /**
     * 获取本机内网IP
     *
     * @return IP地址
     */
    public static String getEtherNetAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            String etherAddress = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                String netName = netInterface.getName();
                if (netName.toLowerCase().equals(GlobalConfig.ETHER_FLAG)) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            etherAddress = ip.getHostAddress();
                            break;
                        }
                    }
                    break;
                }
            }
            return etherAddress;
        } catch (Exception e) {
            ///如果出现异常,则使用 192.168 加上随机后两位的方式
            Random rnd = new Random();
            int third = rnd.nextInt(100);
            int four = rnd.nextInt(200);
            return "192.168." + third + "." + four;
        }
    }
}