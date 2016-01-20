package com.goku.im.relation.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by moueimei on 15/12/1.
 */
public class GkImGroup implements Serializable {
    private Integer id;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 群组封面
     */
    private String icon;

    /**
     * 群组介绍
     */
    private String intro;

    /**
     * 群创建者ID
     */
    private Integer creatorId;

    /**
     * 群创建时间
     */
    private Date createTime;

    /**
     * 当前用户加入该群的时间
     */
    private Date joinTime;

    /**
     * 群组成员总数
     */
    private int userCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
