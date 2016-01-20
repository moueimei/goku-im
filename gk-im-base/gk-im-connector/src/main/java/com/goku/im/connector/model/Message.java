package com.goku.im.connector.model;

import com.goku.im.connector.global.common.MessageContentTypeConst;
import com.goku.im.connector.global.common.MessageTypeConst;
import org.json.JSONObject;

/**
 * Created by moueimei on 15/11/26.
 * 消息实体类
 */
public class Message {
    private String messageId;
    private int messageType = MessageTypeConst.PRIVATE;
    private int fromUserId;
    private int toUserId;
    private String content;
    private int contentType;
    private int groupId;
    private long createTime = System.currentTimeMillis();

    public Message() {
    }

    public Message(JSONObject json) {
        this.messageId = json.optString("msgId", "");
        this.messageType = json.optInt("messageType", MessageTypeConst.PRIVATE);
        this.fromUserId = json.optInt("fromUserId", 0);
        this.toUserId = json.optInt("toUserId", 0);
        this.content = json.optString("content", "");
        this.contentType = json.optInt("contentType", MessageContentTypeConst.TEXT);
        this.groupId = json.optInt("groupId", 0);
        this.createTime = json.optLong("createTime", System.currentTimeMillis());
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("msgId", messageId);
        json.put("messageType", messageType);
        json.put("fromUserId", fromUserId);
        json.put("toUserId", toUserId);
        json.put("content", content);
        json.put("contentType", contentType);
        json.put("groupId", groupId);
        json.put("createTime", createTime);
        return json;
    }
}