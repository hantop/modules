package com.fenlibao.model.pms.idmt.role.form;

import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class RoleEditForm {
    private Integer id;
    private Integer parentId;
    private String roleName;
    private String roleDescription;
    private Integer sort;
    private List<Integer> permitIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Integer> getPermitIds() {
        return permitIds;
    }

    public void setPermitIds(List<Integer> permitIds) {
        this.permitIds = permitIds;
    }
}