package com.fenlibao.model.pms.idmt.rolepermission.vo;

import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;

import java.util.Date;
import java.util.List;

/**
 * 角色的权限
 * Created by Administrator on 2016/1/21.
 */
public class RolePermissionVO {
    private RoleVO role;
    private List<PermitVO> permits;
    private Date createTime;

    public RoleVO getRole() {
        return role;
    }

    public void setRole(RoleVO role) {
        this.role = role;
    }

    public List<PermitVO> getPermits() {
        return permits;
    }

    public void setPermits(List<PermitVO> permits) {
        this.permits = permits;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
