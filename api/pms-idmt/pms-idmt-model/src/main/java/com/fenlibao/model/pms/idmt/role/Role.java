package com.fenlibao.model.pms.idmt.role;

import java.util.Date;

/**
 * 角色
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public class Role {
    private Integer id;
    private Integer parentId;// 父角色id
    private String name;// 角色名称
    private String description;// 角色描述
    private Integer sort;// 排序
    private Date createTime;// 创建时间

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
