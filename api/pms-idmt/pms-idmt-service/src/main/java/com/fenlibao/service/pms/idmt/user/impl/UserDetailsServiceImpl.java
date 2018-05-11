package com.fenlibao.service.pms.idmt.user.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.redis.RedisPrefix;
import com.fenlibao.dao.pms.idmt.group.PmsGroupMapper;
import com.fenlibao.dao.pms.idmt.role.RoleMapper;
import com.fenlibao.dao.pms.idmt.user.PmsUserMapper;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsUserGroup;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.role.vo.UserRoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.model.pms.idmt.user.form.UserForm;
import com.fenlibao.p2p.common.util.http.CookieUtil;
import com.fenlibao.service.pms.idmt.error.AccountException;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private PmsUserMapper pmsUserMapper;

    @Resource
    private PasswordHelper passwordHelper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PmsGroupMapper pmsGroupMapper;

    public void setPasswordHelper(PasswordHelper passwordHelper) {
        this.passwordHelper = passwordHelper;
    }

    @Override
    public List<PmsUser> getUsersByRole(Integer roleId, RowBounds bounds) {
        return pmsUserMapper.getUsersByRole(roleId, bounds);
    }

    public HttpResponse login(HttpServletRequest request, HttpServletResponse response) {
        HttpResponse resp = new HttpResponse();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captcha = request.getParameter("captcha");
        String session = CookieUtil.getCookieByName(request, Config.get("cookie.session.id"));
        Subject currentUser = SecurityUtils.getSubject();
        /**
         * 这都是什么鬼
         * @TODO 待优化
         */
        if (!currentUser.isAuthenticated()) {
            if (!StringUtils.isAnyBlank(username, password)) {
                //if (StringUtils.isNotBlank(captcha)) {
                if (StringUtils.isNotBlank(session)) {
                    //if (isCaptchaCorrect(captcha, session)) {
                    try {
                        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                        currentUser.login(token);
                        logger.info("[" + username + "] sign in successfully");
                        Cookie cookie = new Cookie(Config.get("cookie.session.name"), username);
                        cookie.setPath(Config.get("cookie.valid.path"));
                        response.addCookie(cookie);
                    } catch (Exception e) {
                        logger.error("[Login Exception]");
                        resp.setCodeMessage(ResponseEnum.WRONG_USERNAME_PASSWORD.getCode(), ResponseEnum.WRONG_USERNAME_PASSWORD.getMessage());
                    }
                        /*} else {
                            resp.setCodeMessage(ResponseEnum.WRONG_CAPTCHA.getCode(), ResponseEnum.WRONG_CAPTCHA.getMessage());
                        }*/
                } else {
                    resp.setCodeMessage(ResponseEnum.EMPTY_CLIENT_SESSION.getCode(), ResponseEnum.EMPTY_CLIENT_SESSION.getMessage());
                }
               /* } else {
                    resp.setCodeMessage(ResponseEnum.EMPTY_CAPTCHA.getCode(), ResponseEnum.EMPTY_CAPTCHA.getMessage());
                }*/
            } else {
                resp.setCodeMessage(ResponseEnum.EMPTY_USERNAME_PASSWORD.getCode(), ResponseEnum.EMPTY_USERNAME_PASSWORD.getMessage());
            }
        }
        return resp;
    }

    private boolean isCaptchaCorrect(String captcha, String session) {
        try (Jedis jedis = RedisFactory.getResource()) {
            String correct = jedis.get(RedisPrefix.LOGIN_CAPTCHA.getPrefix().concat(session));
            if (StringUtils.isNotEmpty(correct) && correct.equals(captcha)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public List<PmsUser> getUserList(PmsUser user, RowBounds bounds) {
        return pmsUserMapper.getUserList(user, bounds);
    }

    @Override
    public PmsUser getPmsUser(PmsUser user) {
        passwordHelper.encryptPassword(user,user.getSalt());
        return pmsUserMapper.getPmsUser(user);
    }

    /*****************************************************************************************************************************************************/
    @Override
    public PmsUser findByUsername(String username) {
        return pmsUserMapper.findByUsername(username);
    }

    @Override
    public Boolean createUser(PmsUser user) {
        if(existsUsername(user.getUsername())) {
            throw new AccountException(-1,"用户名已存在");
        }
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        this.passwordHelper.encryptPassword(user);
        int count = this.pmsUserMapper.saveUser(user);
        return count > 0;
    }

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    private boolean existsUsername(String username) {
        PmsUser user = this.pmsUserMapper.findByUsername(username);
        return user != null;
    }

    @Override
    public Boolean changePassword(String username, String newPassword) {
        PmsUser user = new PmsUser(username,newPassword);
        this.passwordHelper.encryptPassword(user);
        return this.pmsUserMapper.updatePassword(user) > 0;
    }

    /**
     * 根据list删除用户
     * @param userIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delUser(List<Integer> userIds) {
        int count = pmsUserMapper.delUser(userIds);
        pmsUserMapper.delRoles(userIds);
        return count;
    }

    @Override
    public PmsUser getUserById(int id) {
        return pmsUserMapper.getUserById(id);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(PmsUser user) {
        if(user.getPassword() == null || "".equals(user.getPassword().trim())) {
            user.setPassword(null);
        } else {
            this.passwordHelper.encryptPassword(user);
        }
        return pmsUserMapper.updateUser(user) > 0 ? true : false;
    }

    @Override
    public List<RoleVO> getUserRoles(String username) {
        List<RoleVO> roles = this.roleMapper.findUserRolesByUsername(username);
        return roles;
    }

    @Override
    public PmsGroup findGroupByUsername(String username) {
        return pmsGroupMapper.findGroupByUsername(username);
    }

    @Override
    @Transactional
    public boolean updateDepartment(PmsUserGroup userGroup) {
        pmsGroupMapper.deleteUserDepartment(userGroup.getUserId());//删除之前的部门
        return pmsGroupMapper.addUserDepartment(userGroup) > 0 ? true : false;
    }

    @Override
    @Transactional
    public boolean updateRoles(Integer userId, List<Integer> roles) {
        roleMapper.deleteUserRole(userId);//删除之前的角色
        List<UserRoleVO> roleList = new ArrayList<>();
        for(Integer roleId : roles) {
            UserRoleVO userRoleVO = new UserRoleVO(userId,roleId,new Date());
            roleList.add(userRoleVO);
        }
        return roleMapper.addUserRoles(roleList) > 0 ? true : false;
    }

    /*****************************************************************************************************************************************************/

    public Set<String> getUserRoles(int userId) {
        Set<String> roles = new HashSet<>();
       // Set<String> roles = pmsUserMapper.getUserRoleNames(userId);
        List<Role> userRoles = pmsUserMapper.findRoles(userId);
        if(!userRoles.isEmpty()){
            for (Role role:userRoles) {
                roles.add(role.getName());
            }
        }
        return roles;
    }

    public Set<String> getUserPermissions(int userId) {
        Set<String> permissions = new HashSet();
        List<Role> userRoles = pmsUserMapper.findRoles(userId);
        List<Integer> roleIds = new ArrayList<>();
        for (Role role:userRoles) {
            roleIds.add(role.getId());
        }
        if(!roleIds.isEmpty()){
        	List<PermitVO> rolePermissions = pmsUserMapper.findPermissions(roleIds.toArray());
            for (PermitVO permission:rolePermissions) {
                if(permission != null && !StringUtils.isEmpty(permission.getCode())) {
                        permissions.add(permission.getCode());
                }
            }
        }
    //   Set<String> permissions = pmsUserMapper.getUserPermissions(userId);
        return permissions;
    }

    @Override
    public List<PmsGroup> getUserOwnGroups(String username) {
        List<PmsGroup> groups = new ArrayList<>();
        if(!StringUtils.isEmpty(username)){
            Integer userGroupId = pmsUserMapper.getUserOwnGroups(username);
            PmsGroup pg = new PmsGroup();
            pg.setId(userGroupId);
            groups = pmsUserMapper.getAllGroups(pg);
            groups.add(pg);
        }
        return groups;
    }

    @Override
    public Integer getUserGroup(String username) {
        return pmsUserMapper.getUserOwnGroups(username);
    }

    @Override
    @Transactional
    public void saveOrUpdateUserGroup(UserForm user) {
        Map<String,Object> param = new HashMap<>();
        if(user != null){
            param.put("userId",user.getId());
            param.put("groupId",user.getGroupId());
        }
        pmsUserMapper.saveOrUpdateUserGroup(param);
    }

    @Override
    public void dimissionUser(Integer id) {
        pmsUserMapper.dimissionUser(id);
    }

    @Override
    @Transactional
    public int updateErrorNumber(PmsUser user) {
        return pmsUserMapper.updateErrorNumber(user) > 0 ? 1 : 0;
    }

    @Override
    public int updateStatus(PmsUser user) {
        return pmsUserMapper.updateStatus(user) > 0 ? 1 : 0;
    }
}
