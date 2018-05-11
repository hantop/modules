package com.fenlibao.service.pms.idmt.rolepermission;

import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.rolepermission.vo.RolePermissionVO;

import java.util.List;

/**
 * 角色与权限Service
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public interface RolePermissionService {

    /**
     * 根据角色ID获取权限树
     *
     * @param roleId
     * @return
     */
    List<PermitTreeNode> getPermitTreeByRoleId(Integer roleId);

    /**
     * 根据角色ID获取权限
     *
     * @param roleId
     * @return
     */
    List<RolePermissionVO> findRolePermissions(Integer roleId);

    /**
     * 更新角色及其权限
     *
     * @param role
     * @param permissionsId
     */
    void updateRoleAndPermissions(Role role, List<Integer> permissionsId);

    /**
     * 新增角色及其权限
     *
     * @param role
     * @param permissionsId
     */
    void addRoleAndPermissions(Role role, List<Integer> permissionsId);

    /**
     * 删除角色及角色的权限
     *
     * @param roleId
     */
    void removeRoleAndPermissions(Integer roleId);
}
