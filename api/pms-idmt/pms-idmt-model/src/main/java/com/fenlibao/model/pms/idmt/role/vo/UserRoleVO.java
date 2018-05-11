package com.fenlibao.model.pms.idmt.role.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bogle on 2016/2/1.
 */
public class UserRoleVO implements Serializable {

    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Date createTime;

    public UserRoleVO() {
    }

    public UserRoleVO(Integer userId, Integer roleId, Date createTime) {
        this.userId = userId;
        this.roleId = roleId;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
