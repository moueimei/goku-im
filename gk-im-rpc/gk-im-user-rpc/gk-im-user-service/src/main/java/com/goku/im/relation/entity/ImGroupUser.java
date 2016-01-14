package com.goku.im.relation.entity;

import java.io.Serializable;
import java.util.Date;

public class ImGroupUser implements Serializable {
    private static final long serialVersionUID = 525146006576774367L;

    private Integer id;

    /**
     * 群组ID
     */
    private Integer groupId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 群成员身份(3: 群主  2:管理员  1:成员)
     */
    private Short role;

    /**
     * 群组删除状态(0:正常  1:删除)
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
     * @return 群组ID
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId 
	 *            群组ID
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
     * @return 群成员身份(3: 群主  2:管理员  1:成员)
     */
    public Short getRole() {
        return role;
    }

    /**
     * @param role 
	 *            群成员身份(3: 群主  2:管理员  1:成员)
     */
    public void setRole(Short role) {
        this.role = role;
    }

    /**
     * @return 群组删除状态(0:正常  1:删除)
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted 
	 *            群组删除状态(0:正常  1:删除)
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