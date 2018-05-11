package com.fenlibao.service.pms.idmt.user;

import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsUserGroup;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.model.pms.idmt.user.form.UserForm;
import org.apache.ibatis.session.RowBounds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface UserDetailsService {

    /**
     * 根据角色ID获取用户
     *
     * @param roleId
     * @param bounds
     * @return
     */
    List<PmsUser> getUsersByRole(Integer roleId, RowBounds bounds);

    HttpResponse login(HttpServletRequest request, HttpServletResponse response);

    List<PmsUser> getUserList(PmsUser user, RowBounds bounds);

    PmsUser getPmsUser(PmsUser user);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    PmsUser findByUsername(String username);

    /**
     * 添加用户，在添加用户之前要求对密码进行盐值加密
     *
     * @param user
     * @return
     */
    Boolean createUser(PmsUser user);

    /**
     * 修改用户密码
     *
     * @param username 待修改的用户名
     * @return
     */
    Boolean changePassword(String username, String newPassword);


    Set<String> getUserRoles(int userId);

    Set<String> getUserPermissions(int userId);

    /**
     * 删除用户，返回删除的行数
     *
     * @param userIds
     * @return
     */
    int delUser(List<Integer> userIds);

    /**
     * 根据用户id获取用户信息
     *
     * @param id
     * @return
     */
    PmsUser getUserById(int id);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    boolean updateUser(PmsUser user);

    /**
     * 获取当前登录用户的角色
     *
     * @return
     */
    List<RoleVO> getUserRoles(String username);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    PmsGroup findGroupByUsername(String username);

    /**
     * 更新用户部门信息
     * @param userGroup
     * @return
     */
    boolean updateDepartment(PmsUserGroup userGroup);

    /**
     * 设置用户的角色
     * @param userId
     * @param roles
     * @return
     */
    boolean updateRoles(Integer userId, List<Integer> roles);

    /**
     * 获取登录账号所有的组织id
     * @param username
     * @return
     */
    List<PmsGroup> getUserOwnGroups(String username);

    /**
     * 获取登录账号的组织id
     * @param username
     * @return
     */
    Integer getUserGroup(String username);

    /**
     * 更新或者修改用户所在组织
     * @param user
     * @return
     */
    void saveOrUpdateUserGroup(UserForm user);

    /**
     * 是否离职
     * @param id
     * @return
     */
    void dimissionUser(Integer id);

    /**
     * 更新密码错误次数
     */
    int updateErrorNumber(PmsUser user);

    /**
     * 更新用户状态
     * @param user
     * @return
     */
    int updateStatus(PmsUser user);
}
