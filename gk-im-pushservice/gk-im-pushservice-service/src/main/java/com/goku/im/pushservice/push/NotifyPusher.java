package com.goku.im.pushservice.push;

import com.goku.im.pushservice.model.Notify;
import com.goku.im.pushservice.redis.LogicReceiveNotifyQueue;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by moueimei on 15/12/10.
 */
@Component
public class NotifyPusher {
    @Autowired
    LogicReceiveNotifyQueue logicReceiveNotifyQueue;

    /**
     * 发送消息
     *
     * @param notify 通知对象
     * @throws Exception
     */
    public void push(Notify notify) throws Exception {
        ///获取notify ID
        String notifyId = getUUID();
        notify.setNotifyId(notifyId);
        notify.setCreateTime(System.currentTimeMillis());

        List<Integer> toUserIds = notify.getToUserIds();
        for (int toUserId : toUserIds) {
            ///构建push消息
            JSONObject json = buildPushJson(notify, toUserId);

            ///将通知添加到logic集群对应的 receive notify queue中
            logicReceiveNotifyQueue.push(json.toString());
        }
    }

    /**
     * 创建推送给connector的json对象
     *
     * @param notify
     * @return
     * @throws Exception
     */
    private JSONObject buildPushJson(Notify notify, int toUserId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("notifyId", notify.getNotifyId());
        json.put("fromUserId", notify.getFromUserId());
        json.put("toUserId", toUserId);
        json.put("content", notify.getContent() == null ? "" : notify.getContent());
        json.put("notifyType", notify.getNotifyType());
        json.put("createTime", notify.getCreateTime());
        json.put("createBy", notify.getFromUserId());
        return json;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        StringBuffer sb = new StringBuffer(s.substring(0, 8));
        sb.append(s.substring(9, 13));
        sb.append(s.substring(14, 18));
        sb.append(s.substring(19, 23));
        sb.append(s.substring(24));
        return sb.toString();
    }
}
