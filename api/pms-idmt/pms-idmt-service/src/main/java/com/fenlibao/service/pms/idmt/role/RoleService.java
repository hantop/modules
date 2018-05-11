package com.fenlibao.service.pms.idmt.role;

import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 角色Service
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public interface RoleService {
    /**
     * 获取所有角色
     *
     * @return
     */
    List<RoleVO> getRoles();

    /**
     * 获取所有角色
     *
     * @return
     * @param bounds
     */
    List<Role> findAllRoles(RowBounds bounds);

    /**
     * 根据ID查询角色
     *
     * @param roleId
     * @return
     */
    Role findRoleById(Integer roleId);

    /**
     * 根据角色名称查询角色是否存在
     *
     * @param roleName
     * @return
     */
    boolean hasRole(String roleName);

    /**
     * 根据角色名称查询角色
     *
     * @param roleName
     * @param bounds
     * @return
     */
    List<Role> findRolesLikeName(String roleName, RowBounds bounds);

    /**
     * 更新角色的父角色ID
     * @param id
     * @param parentId
     *
     */
    void updateRoleParent(Integer id, Integer parentId);

    /**
     * 更新角色
     *
     * @param role
     */
    void updateRole(Role role);

    /**
     * 新增角色
     *
     * @param role
     *
     */
    void addRole(Role role);

    /**
     * 修改角色
     *  @param role
     *
     */
    void modifyRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId
     */
    void removeRole(Integer roleId);

    /**
     * 根据用户名获取角色信息
     * @param username
     * @return
     */
    List<RoleVO> findUserRolesByUsername(String username);
}
