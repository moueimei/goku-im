package com.goku.im.relation.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by moueimei on 15/12/1.
 */
public class GkImGroupUser implements Serializable {
    /**
     * 群组ID
     */
    private Integer groupId = 0;

    /**
     * 用户ID
     */
    private Integer puId = 0;

    /**
     * 群成员身份(3: 群主  2:管理员  1:成员)
     */
    private Short role = 1;

    /**
     * 入群时间
     */
    private Date createTime = new Date();

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

    /**
     * @return 群组ID
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId 群组ID
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getPuId() {
        return puId;
    }

    public void setPuId(Integer puId) {
        this.puId = puId;
    }

    public Short getRole() {
        return role;
    }

    public void setRole(Short role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
