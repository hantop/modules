package com.fenlibao.model.pms.idmt.group;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by Bogle on 2016/2/1.
 */
public class PmsUserGroup implements Serializable {

    private Integer id;
    @Min(value = 1,message = "用户不能为空")
    private Integer userId;
    @Min(value = 1,message = "部门不能为空")
    private Integer groupId;

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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
