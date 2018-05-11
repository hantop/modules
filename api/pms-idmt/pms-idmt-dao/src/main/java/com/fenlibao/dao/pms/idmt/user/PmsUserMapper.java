package com.fenlibao.dao.pms.idmt.user;

import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PmsUserMapper {

    /**
     * 根据角色ID获取用户
     *
     * @param roleId
     * @param bounds
     * @return
     */
    List<PmsUser> getUsersByRole(@Param(value = "roleId") Integer roleId, RowBounds bounds);

    PmsUser getPmsUser(PmsUser user);

    /**
     * 获取user by List信息
     * @param user
     *@param bounds  @return
     */
    List<PmsUser> getUserList(PmsUser user, RowBounds bounds);

    Set<String> getUserRoleNames(int userId);

    Set<String> getUserPermissions(int userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    PmsUser findByUsername(String username);

    /**
     * 序列化用户信息
     * @param user
     * @return
     */
    int saveUser(PmsUser user);

    /**
     * 密码修改
     * @param user
     * @return
     */
    int updatePassword(PmsUser user);

    /**
     * 密码错误次数
     * @param user
     * @return
     */
    int updateErrorNumber(PmsUser user);

    /**
     * 更新用户状态
     * @param user
     * @return
     */
    int updateStatus(PmsUser user);
    /**
     * 删除用户信息
     * @param userIds
     * @return
     */
    int delUser(List<Integer> userIds);

    /**
     * 删除角色信息
     * @param userIds
     * @return
     */
    int delRoles(List<Integer> userIds);

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    PmsUser getUserById(int id);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int updateUser(PmsUser user);

    /**
     * 更新用户信息
     * @int 用户id
     * @return
     */
    List<Role> findRoles(int userId);

    /**
     * 权限查询
     * @return
     * @param
     */
    List<PermitVO> findPermissions(Object[] roleIds);

    /**
     * 获取组织id
     * @param username
     * @return
     */
    Integer getUserOwnGroups(String username);

    /**
     * 根据组织id 获取所有组织
     * @param pmsGroup
     * @return
     */
    List<PmsGroup> getAllGroups(PmsGroup pmsGroup);

    /**
     * 更新或者修改用户所在组织
     * @param param
     * @return
     */
    void saveOrUpdateUserGroup(Map<String, Object> param);

    /**
     * 是否离职
     * @param id
     * @return
     */
    void dimissionUser(Integer id);
}
