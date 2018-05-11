package com.fenlibao.dao.pms.idmt.role;

import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.role.vo.UserRoleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 角色Mapper
 *
 * Created by chenzhixuan on 2016/1/21.
 */
public interface RoleMapper {

    /**
     * 根据父角色ID获取角色
     *
     * @param parentId
     * @return
     */
    List<RoleVO> getRolesByParentId(Integer parentId);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<RoleVO> getRoles();

    /**
     * 根据角色数量
     *
     * @param roleName
     * @return
     */
    int getCountByName(String roleName);

    /**
     * 更新角色的父角色ID
     * @param id
     * @param parentId
     */
    void updateRoleParent(@Param("id") Integer id, @Param("parentId") Integer parentId);

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
     */
    void addRole(Role role);

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
     * 根据角色名称查询角色
     *
     * @param roleName
     * @param bounds
     * @return
     */
    List<Role> findRoleByName(@Param(value = "name") String roleName, RowBounds bounds);

    /**
     * 根据用户名获取角色信息
     * @param username
     * @return
     */
    List<RoleVO> findUserRolesByUsername(String username);

    /**
     * 根据角色ID删除角色
     *
     * @param roleId
     */
    void removeRole(Integer roleId);
    /**
     * 删除用户角色信息
     * @param userId
     */
    void deleteUserRole(Integer userId);

    /**
     * 设置用户角色
     * @param roleList
     * @return
     */
    int addUserRoles(List<UserRoleVO> roleList);
}
