package com.fenlibao.model.pms.idmt.role.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public class RoleVO {
    private Integer id;
    private String name;// 角色名称
    private String description;// 角色描述
    private Date createTime;// 创建时间
    @JSONField(name="pId")
    private Integer parentId;// 父节点id
    private Integer sort;// 顺序
    private List<RoleVO> children;// 子节点
    private String isParent;
    private boolean checked;//是否选中

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<RoleVO> getChildren() {
        return children;
    }

    public void setChildren(List<RoleVO> children) {
        this.children = children;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleVO roleVO = (RoleVO) o;

        return !(id != null ? !id.equals(roleVO.id) : roleVO.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
