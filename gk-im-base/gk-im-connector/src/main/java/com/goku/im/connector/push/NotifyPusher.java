package com.goku.im.connector.push;

import com.goku.im.connector.global.GlobalConfig;
import com.goku.im.connector.global.common.ActionRequestTypeConst;
import com.goku.im.connector.global.common.InvokeSource;
import com.goku.im.connector.model.Notify;
import com.goku.im.connector.model.User;
import com.goku.im.connector.redis.DataBaseNotifyQueue;
import com.goku.im.connector.redis.LogicReceiveNotifyQueue;
import com.goku.im.connector.redis.OffLineNotifyRedis;
import com.goku.im.connector.service.UserService;
import com.goku.im.connector.util.ChannelManager;
import com.goku.im.framework.util.StringUtil;
import io.netty.channel.Channel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by moueimei on 15/12/10.
 * 负责将通知推送至客户端的具体实现
 */
@Component
public class NotifyPusher {
    private static final Logger logger = LoggerFactory.getLogger(NotifyPusher.class);

    @Autowired
    UserService userService;
    @Autowired
    DataBaseNotifyQueue dataBaseNotifyQueue;
    @Autowired
    LogicReceiveNotifyQueue logicReceiveNotifyQueue;
    @Autowired
    OffLineNotifyRedis offLineNotifyRedis;

    /**
     * 发送消息
     *
     * @param notify 消息对象
     * @param source 调用来源
     * @throws Exception
     */
    public void push(Notify notify, InvokeSource source) throws Exception {
        ///根据userID获取用户连接的connector domain
        int userId = notify.getToUserId();
        String connectorDomain = userService.getConnectorDomainByUserId(userId);

        ///推送流程:
        /// 1. 客户端不在线
        ///     a. 执行logic集群的业务逻辑
        ///         1). 如果客户端此时在线, 则由logic集群将通知添加到目标客户端对应的connector的 push notify queue中
        ///         2). 如果客户端此时掉线, 则由logic集群负责保存离线通知并添加到im_db_notify_queue
        /// 2. 客户端在线且在当前connector
        ///     a. 通道可用: 跳过logic集群的业务逻辑,将通知直接发送至客户端
        ///     b. 通道不可用: 跳过logic集群的业务逻辑, 将通知保存到离线通知列表
        ///     c. 判断调用来源
        ///         1). 如果是connector调用(也就是目标用户在当前的connector上),则需要将通知保存到im_db_notify_queue
        ///         2). 如果是pushqueue调用(通过logic推送过来), 则不需要保存通知,因为logic集群已经保存通知到im_db_notify_queue了
        /// 3. 客户端在线但不在当前connector
        ///     a. 执行logic集群的业务逻辑
        ///         1). 如果客户端此时在线, 则由logic集群将通知添加到目标客户端对应的connector的 push notify queue中
        ///         2). 如果客户端此时掉线, 则由logic集群负责保存离线通知并添加到im_db_notify_queue

        StringBuilder strLog = new StringBuilder();
        if (StringUtil.isNullOrEmpty(connectorDomain)) {
            ///将消息保存到logic集群的receive notify queue
            logicReceiveNotifyQueue.push(notify.toJson().toString());

            ///记录日志
            strLog.append("[connector is null or empty, send notify to logic] ");
            strLog.append("send userId: " + userId + " ");
            strLog.append("send notify: " + notify.toJson().toString());
            logger.info(strLog.toString());
            return;
        }

        ///如果目标用户在本机,则直接发送,反之,则添加到logic对应的receive notify queue中
        if (connectorDomain.equals(GlobalConfig.OWNER_DOMAIN)) {
            Channel channel = ChannelManager.getChannel(userId);
            if (null != channel && channel.isOpen()) {
                ///构建push通知
                JSONObject json = buildPushJson(notify);

                ///发送消息
                channel.writeAndFlush(json.toString());

                ///记录日志
                strLog.append("[connector is owner and channel is open, send notify to user] ");
                strLog.append("send userId: " + userId + " ");
                strLog.append("send notify: " + notify.toJson().toString());
                logger.info(strLog.toString());
            } else {
                ///将通道从本机jvm移除
                ChannelManager.remove(userId);

                ///客户端已断开连接, 记录离线消息
                String notifyId = notify.getNotifyId();
                offLineNotifyRedis.add(userId, notify.getCreateTime(), notifyId);

                ///记录日志
                strLog.append("[connector is owner but channel is closed, send notify to offline] ");
                strLog.append("send userId: " + userId + " ");
                strLog.append("send notify: " + notify.toJson().toString());
                logger.info(strLog.toString());
            }

            if (source == InvokeSource.Connector) {
                ///保存到im_db_notify_queue
                dataBaseNotifyQueue.push(notify.toJson().toString());
            }
        } else {
            ///将消息保存到logic集群的receive notify queue
            logicReceiveNotifyQueue.push(notify.toJson().toString());

            ///记录日志
            strLog.append("[connector is not owner, send notify to logic] ");
            strLog.append("send userId: " + userId + " ");
            strLog.append("send notify: " + notify.toJson().toString());
        }
    }

    /**
     * 创建推送给客户端的json对象
     *
     * @param notify
     * @return
     * @throws Exception
     */
    private JSONObject buildPushJson(Notify notify) throws Exception {
        ///获取发送消息的用户信息
        JSONObject jsonFromUser = new JSONObject();
        jsonFromUser.put("puId", notify.getFromUserId());
        User user = userService.getUserInfo(notify.getFromUserId());
        if (null != user) {
            jsonFromUser.put("nick", user.getNickName());
            jsonFromUser.put("photo", user.getAvatar());
            jsonFromUser.put("sex", user.getGender());
        }

        JSONObject json = new JSONObject();
        json.put("action", ActionRequestTypeConst.PUSH_NOTIFY);
        json.put("notifyType", notify.getNotifyType());
        json.put("fromUser", jsonFromUser);
        json.put("createTime", notify.getCreateTime());

        String content = notify.getContent();
        if (!StringUtil.isNullOrEmpty(content))
            json.put("content", new JSONObject(content));
        else
            json.put("content", "");

        return json;
    }
}
