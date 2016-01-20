package com.goku.im.relation.entity;

import java.io.Serializable;
import java.util.Date;

public class ImUserFriend implements Serializable {
    private static final long serialVersionUID = 113960015898453287L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 好友用户ID
     */
    private Integer friendId;

    /**
     * 好友删除状态(0:正常  1:删除)
     */
    private Boolean deleted;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId 
	 *            用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 好友用户ID
     */
    public Integer getFriendId() {
        return friendId;
    }

    /**
     * @param friendId 
	 *            好友用户ID
     */
    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    /**
     * @return 好友删除状态(0:正常  1:删除)
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted 
	 *            好友删除状态(0:正常  1:删除)
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}