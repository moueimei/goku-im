package com.goku.im.pushservice.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by moueimei on 15/12/10.
 */
public class Notify implements Serializable
{
    private String notifyId;
    private int fromUserId;
    private List<Integer> toUserIds;
    private String content;
    private String notifyType;
    private long createTime = System.currentTimeMillis();

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public List<Integer> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<Integer> toUserIds) {
        this.toUserIds = toUserIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
