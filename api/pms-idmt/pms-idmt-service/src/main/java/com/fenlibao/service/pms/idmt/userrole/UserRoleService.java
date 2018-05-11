package com.fenlibao.service.pms.idmt.userrole;

import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;

import java.util.List;

/**
 * 用户与角色Service
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public interface UserRoleService {
    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<RoleVO> findRolesByUserId(Integer userId);

    /**
     * 根据角色ID查询用户ID
     *
     * @param roleId
     * @return
     */
    List<Integer> findUserIdsByRoleId(Integer roleId);

    /**
     * 根据角色ID查询用户
     *
     * @param roleId
     * @return
     */
    List<PmsUser> findUsersByRoleId(Integer roleId);
}
