package com.fenlibao.service.pms.idmt.role.impl;

import com.fenlibao.dao.pms.idmt.role.RoleMapper;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.service.pms.idmt.role.RoleService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<RoleVO> getRoles() {
        return roleMapper.getRoles();
    }

    @Override
    public List<Role> findAllRoles(RowBounds bounds) {
        return roleMapper.findAllRoles(bounds);
    }

    @Override
    public Role findRoleById(Integer roleId) {
        return roleMapper.findRoleById(roleId);
    }

    @Override
    public boolean hasRole(String roleName) {
        return roleMapper.getCountByName(roleName) == 0 ? false : true;
    }

    @Override
    public List<Role> findRolesLikeName(String roleName, RowBounds bounds) {
        return roleMapper.findRoleByName(roleName, bounds);
    }

    @Override
    public void updateRoleParent(Integer id, Integer parentId) {
        roleMapper.updateRoleParent(id, parentId);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }

    @Override
    public void addRole(Role role) {
        roleMapper.addRole(role);
    }

    @Override
    public void modifyRole(Role role) {

    }

    @Override
    public void removeRole(Integer roleId) {
        roleMapper.removeRole(roleId);
    }

    @Override
    public List<RoleVO> findUserRolesByUsername(String username) {
        return this.roleMapper.findUserRolesByUsername(username);
    }
}
