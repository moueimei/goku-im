package com.goku.im.connector.model;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by moueimei on 15/11/26.
 */
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private int userId;
    private String nickName;
    private String avatar;
    private String gender;
    private String userToken;
    private int userType;

    public User() {
    }

    public User(JSONObject json) {
        try {
            this.userId = json.optInt("puId", 0);
            this.nickName = json.optString("nick", "");
            this.avatar = json.optString("photo", "");
            this.gender = json.optString("sex", "");
        } catch (Exception e) {
            logger.error("at User.User throw an error." + e.getMessage(), e.getCause());
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("puId", userId);
            json.put("nick", nickName == null ? "" : nickName);
            json.put("photo", avatar == null ? "" : avatar);
            json.put("sex", gender == null ? "" : gender);
            return json;
        } catch (Exception e) {
            logger.error("at User.toJSON throw an error." + e.getMessage(), e.getCause());
            return null;
        }
    }
}