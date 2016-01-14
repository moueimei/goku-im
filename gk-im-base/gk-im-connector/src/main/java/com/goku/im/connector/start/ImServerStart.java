package com.goku.im.connector.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.util.NetworkUtil;

/**
 * Created by milo on 15/12/8.
 */
public class ImServerStart
{
    @Value("${imserver.port}")
    int imServerPort;
    @Value("${token.expire_days}")
    int tokenExpireDays;
    @Value("${appId}")
    String appId;
    @Value("${appKey}")
    String appKey;
    @Value("${ether.flag}")
    String etherFlag;

    @Autowired
    PushMessageQueueListenerStart pushQueueListenerStart;
    @Autowired
    PushNotifyQueueListenerStart notifyQueueListenerStart;
    @Autowired
    SocketServerStart socketServerStart;

    public void start() throws Exception
    {
        ///初始化配置信息
        initConfig();

        ///启动push msg queue监听器
        pushQueueListenerStart.start();

        ///启动push notify queue监听器
        notifyQueueListenerStart.start();

        ///启动connector server
        socketServerStart.start();
    }

    private void initConfig()
    {
        GlobalConfig.PORT = imServerPort;
        GlobalConfig.TOKEN_EXPIRE_DAYS = tokenExpireDays;
        GlobalConfig.APP_ID = appId;
        GlobalConfig.APP_KEY = appKey;
        GlobalConfig.ETHER_FLAG = etherFlag;
        GlobalConfig.OWNER_DOMAIN = NetworkUtil.getEtherNetAddress();
        System.out.println("server ether ip is: " + GlobalConfig.OWNER_DOMAIN);
    }
}
