package com.goku.im.relation.model;

import java.io.Serializable;

/**
 * Created by moueimei on 15/12/1.
 */
public class GkImUserFriend implements Serializable{
    /**
     * 用户ID
     */
    private Integer puId = 0;

    /**
     * 用户昵称
     */
    private String nick;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 用户性别
     */
    private String sex;

    public Integer getPuId() {
        return puId;
    }

    public void setPuId(Integer puId) {
        this.puId = puId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}