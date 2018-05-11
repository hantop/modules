package com.fenlibao.dao.pms.idmt.userrole;

import com.fenlibao.model.pms.idmt.role.vo.RoleVO;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public interface UserRoleMapper {
    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<RoleVO> findRolesByUserId(Integer userId);
}
