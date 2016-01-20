package com.goku.im.connector;

import com.goku.im.connector.start.PushMessageQueueListenerStart;
import com.goku.im.connector.start.SocketServerStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by moueimei on 15/11/27.
 */
public class ConnectorServer {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorServer.class);

    public static void main(String[] args) {
        try {
            /*********************************PushQueue Listenner*********************************/
            PushMessageQueueListenerStart listenerStart = new PushMessageQueueListenerStart();
            listenerStart.start();


            /*********************************Scoket Server*********************************/
            SocketServerStart serverStart = new SocketServerStart();
            serverStart.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("socket server start error.", e.getCause());
        }
    }
}