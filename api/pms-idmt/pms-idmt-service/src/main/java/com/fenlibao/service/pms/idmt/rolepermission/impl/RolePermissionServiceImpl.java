package com.fenlibao.service.pms.idmt.rolepermission.impl;

import com.fenlibao.dao.pms.idmt.rolepermission.RolePermissionMapper;
import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.rolepermission.vo.RolePermissionVO;
import com.fenlibao.service.pms.idmt.role.RoleService;
import com.fenlibao.service.pms.idmt.rolepermission.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Resource
    private RoleService roleService;
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermitTreeNode> getPermitTreeByRoleId(Integer roleId) {
        return rolePermissionMapper.getPermitTreeByRoleId(roleId);
    }

    @Override
    public List<RolePermissionVO> findRolePermissions(Integer roleId) {
        return rolePermissionMapper.findRolePermissions(roleId);
    }

    @Transactional
    @Override
    public void updateRoleAndPermissions(Role role, List<Integer> permissionsId) {
        // 删除角色的权限
        rolePermissionMapper.removeRolePermissions(role.getId());
        // 新增角色的权限
        rolePermissionMapper.addRolePermissions(role.getId(), permissionsId);
        // 更新角色
        roleService.updateRole(role);
    }

    @Transactional
    @Override
    public void addRoleAndPermissions(Role role, List<Integer> permissionsId) {
        roleService.addRole(role);
        rolePermissionMapper.addRolePermissions(role.getId(), permissionsId);
    }

    @Transactional
    @Override
    public void removeRoleAndPermissions(Integer roleId) {
        // 删除角色对应的权限
        rolePermissionMapper.removeRolePermissions(roleId);
        // 删除角色
        roleService.removeRole(roleId);
    }
}
