package com.goku.im.relation.entity;

import java.io.Serializable;
import java.util.Date;

public class ImGroup implements Serializable {
    private static final long serialVersionUID = 190702359725344106L;

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
     * @return 群组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName 
	 *            群组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return 群组封面
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 
	 *            群组封面
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 群组介绍
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @param intro 
	 *            群组介绍
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @return 群创建者ID
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId 
	 *            群创建者ID
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
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