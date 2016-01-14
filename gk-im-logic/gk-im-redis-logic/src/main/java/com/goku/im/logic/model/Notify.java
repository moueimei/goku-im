package com.goku.im.logic.model;

import org.json.JSONObject;

/**
 * Created by milo on 15/12/10.
 * 通知实体类
 */
public class Notify
{
    private String notifyId;
    private int fromUserId;
    private int toUserId;
    private String content;
    private String notifyType;
    private long createTime = System.currentTimeMillis();

    public Notify()
    {}

    public Notify(JSONObject json)
    {
        this.notifyId = json.optString("notifyId", "");
        this.fromUserId = json.optInt("fromUserId", 0);
        this.toUserId = json.optInt("toUserId", 0);
        this.content = json.optString("content", "");
        this.notifyType = json.optString("notifyType", "");
        this.createTime = json.optLong("createTime", System.currentTimeMillis());
    }

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

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
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

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("notifyId", notifyId);
        json.put("fromUserId", fromUserId);
        json.put("toUserId", toUserId);
        json.put("content", content);
        json.put("notifyType", notifyType);
        json.put("createTime", createTime);
        return json;
    }
}
