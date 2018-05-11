package com.fenlibao.pms.controller.system;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsUserGroup;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.model.pms.idmt.user.form.UserForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.shiro.web.UserInfo;
import com.fenlibao.service.pms.idmt.error.AccountException;
import com.fenlibao.service.pms.idmt.group.PmsGroupService;
import com.fenlibao.service.pms.idmt.role.RoleService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lullaby on 2015/7/20.
 */
@RestController
@RequestMapping("system/pmsuser")
public class PmsUserController {
    private static final Logger LOG = LoggerFactory.getLogger(PmsUserController.class);
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private RoleService roleService;

    @Resource
    private PmsGroupService pmsGroupService;

    @RequiresPermissions("systemUser:view")
    @RequestMapping
    public ModelAndView pmsuserlist(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            PmsUser user) {
        RowBounds bounds = new RowBounds(page, limit);
        Subject principals = SecurityUtils.getSubject();
        String username = (String) principals.getPrincipal();
        List<PmsGroup> groups = userDetailsService.getUserOwnGroups(username);
        user.setGroups(groups);
        List<PmsUser> list = userDetailsService.getUserList(user,bounds);
        PageInfo<PmsUser> paginator = new PageInfo<>(list);
        return new ModelAndView("system/pmsuser/index")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("user", user);
    }

    @RequiresPermissions("systemUser:delete")
    @RequestMapping(value = "/delUser", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity deleteParent(@RequestBody @NotEmpty List<Integer> userIds, BindingResult result) {
        int count = userDetailsService.delUser(userIds);
        Map<String, Serializable> body = new HashMap<>();
        boolean flag = count > 0 ? true : false;
        if (count < 0) {
            body.put("message", "存在渠道信息，不能删除");
        } else if (count == 0) {
            body.put("message", "删除失败，请联系管理员");
        } else {
            body.put("message", "删除成功");
        }
        body.put("success", flag);

        return new ResponseEntity<>(body, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * 删除营销报表数据
     *
     * @param id
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/dimissionUser", method = RequestMethod.POST)
    public Map<String, Object> dimissionUser(
            @RequestParam(required = false, defaultValue = "0") int id
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            userDetailsService.dimissionUser(id);
            resultMap.put("codeStatus","success");
        }catch (Exception e){
            resultMap.put("codeStatus","error");
        }

        return resultMap;
    }

    @RequiresPermissions(value={"systemUser:create","systemUser:edit"},logical=Logical.OR)
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
    public ModelAndView saveOrUpdate(@RequestParam(required = false, defaultValue = "0") int id) {
        PmsUser user = new UserForm();
        PmsGroup groupMsg = null;
        ModelAndView view  = new ModelAndView("system/pmsuser/user-edit");

        Integer groupId = getGroupTreeId();
        List<PmsGroup> groupTree = pmsGroupService.findAll(groupId,new RowBounds());
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            user = userDetailsService.getUserById(id);
            groupMsg = pmsGroupService.getGroupByUserId(id);
            UserForm tmp = new UserForm();
            BeanUtils.copyProperties(user, tmp);
            user = tmp;
        }
        view.addObject("user", user).addObject("title", title).addObject("groupTree", groupTree).addObject("groupMsg",groupMsg);
        return view;
    }

    private Integer getGroupTreeId() {
        Subject principals = SecurityUtils.getSubject();
        String username = (String) principals.getPrincipal();
        Integer groupId = userDetailsService.getUserGroup(username);
        return groupId;
    }

    @RequiresPermissions(value={"systemUser:create","systemUser:edit"},logical=Logical.OR)
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ModelAndView saveOrUpdate(@Valid @ModelAttribute("user") UserForm user, BindingResult result) {
        String title = "新增";
        if (user.getId() > 0) {
            title = "编辑";
        }

        if (result.hasErrors() && (user.getId() > 0 && result.getFieldErrors().size() > 2)) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("system/pmsuser/user-edit")
                    .addObject("title", title);
        }
        if (!user.getPassword().equals(user.getRepassword())) {
            LOG.error("密码和重复密码不一致");
            result.rejectValue("repassword", "user", "密码和重复密码不一致");
            return new ModelAndView("system/pmsuser/user-edit")
                    .addObject("title", title);
        }
        ModelAndView modelAndView = new ModelAndView();
        boolean flag = false;
        int code = 0;
        if (user.getId() > 0) {
            flag = userDetailsService.updateUser(user);
        } else {
            try {
                flag = userDetailsService.createUser(user);
            } catch (AccountException e) {
                if (e.getCode() == -1) {
                    LOG.error("用户名已经存在");
                    modelAndView.addObject("error", "登陆账号已经存在");
                    code = -1;
                }
            }
        }

        userDetailsService.saveOrUpdateUserGroup(user);

        String url = "redirect:/system/pmsuser/saveOrUpdate";
        if (!flag) {
            url = "system/pmsuser/user-edit";
            modelAndView.addObject("code", code);
        }
        if (flag) {
            user.setRepassword(null);
            modelAndView.addObject("success", flag);
        }
        modelAndView.setViewName(url);
        return modelAndView.addObject("title", title);
    }

    @RequiresPermissions("systemUser:setRole")
    @RequestMapping(value = "/settingUserRole", method = RequestMethod.GET)
    public ModelAndView settingUserRole(Integer userId, String username, ModelAndView modelAndView) {
        modelAndView.setViewName("system/pmsuser/settingUserRole");
        modelAndView.addObject("targetUser", userDetailsService.getUserById(userId));//设置目标用户
        List<RoleVO> childrens = new ArrayList<>();
        // 根节点
        RoleVO root = null;
        // 获取当前用户拥有的角色
        List<RoleVO> userRoles = userDetailsService.getUserRoles(UserInfo.getUsername());
        // 目标用户拥有的角色
        List<RoleVO> targetRoles = userDetailsService.getUserRoles(username);

        if (userRoles != null && userRoles.size() > 0) {
            root = userRoles.get(0);
            // 获取所有角色
            List<RoleVO> roles = roleService.getRoles();
            // 选中目标用户拥有的角色
            for (RoleVO role : roles) {
                if(targetRoles.contains(role)) {
                    role.setChecked(true);
                }
            }
            if(roles != null && roles.size() > 0) {
                buildRoleAndChild(root, roles);
            }

            // 获取根节点的所有下一级节点
            for (RoleVO c : root.getChildren()) {
                childrens.add(c);
            }
        }
        modelAndView.addObject("roles", JSON.toJSONString(childrens));// 当前登陆的用户对应的角色(不包括本角色)
        return modelAndView.addObject("userId", userId);
    }

    /**
     * 组装角色及其子权限
     *
     * @param list
     */
    private void buildRoleAndChild(RoleVO root, List<RoleVO> list) {
        List<RoleVO> children = root.getChildren();
        Integer id = root.getId();
        Integer childrenParentId;
        if (children == null) {
            children = new ArrayList<>();
            root.setChildren(children);
        }
        for (RoleVO roleVO : list) {
            childrenParentId = roleVO.getParentId();
            if (id == childrenParentId) {
                children.add(roleVO);
                buildRoleAndChild(roleVO, list);
            }
        }
    }

    @RequiresPermissions("systemUser:setRole")
    @RequestMapping(value = "settingUserRole", method = RequestMethod.POST)
    public HttpResponse settingUserRole(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "roleIds") List<Integer> roleIds) {
        HttpResponse response = new HttpResponse();
        try {
            boolean flag = userDetailsService.updateRoles(userId, roleIds);
            if (!flag) {
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        } catch (Exception e) {
            LOG.error("PmsUserController.settingUserRole:", e.getMessage());
            response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("systemUser:setDep")
    @RequestMapping(value = "/settingDepartment", method = RequestMethod.GET)
    public ModelAndView settingDepartment(Integer userId, String username, ModelAndView modelAndView) {
        modelAndView.setViewName("system/pmsuser/setting-department");
        Map<String, Object> targetInfo = new HashMap<>();
        targetInfo.put("targetUser", userDetailsService.getUserById(userId));
        targetInfo.put("targetGroup", userDetailsService.findGroupByUsername(username));//当前登陆的用户角色
        return modelAndView.addObject("targetInfo", targetInfo).addObject("groups", userDetailsService.findGroupByUsername(UserInfo.getUsername())).addObject("id", userId);
    }

    @RequiresPermissions("systemUser:setDep")
    @RequestMapping(value = "/settingDepartment", method = RequestMethod.POST)
    public ModelAndView settingDepartment(PmsUserGroup userGroup, ModelAndView modelAndView) {
        boolean flag = userDetailsService.updateDepartment(userGroup);
        String viewNam = "system/pmsuser/settingDepartment";
        modelAndView.addObject("success", flag);
        if (flag) {
            viewNam = "redirect:/" + viewNam;
        }
        modelAndView.setViewName(viewNam);
        modelAndView.addObject("userId", userGroup.getUserId());
        modelAndView.addObject("username", UserInfo.getUsername());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public void changePassword(@RequestParam(required = true) String username,
                               @RequestParam(required = true) String password) {
        userDetailsService.changePassword(username, password);
    }

    @ResponseBody
    @RequestMapping(value = "/needChangePassword", method = RequestMethod.POST)
    public String needChangePassword(@RequestParam(required = true) String username) {
        PmsUser user = userDetailsService.findByUsername(username);
        return user.getLastChangepwdTime() == null ? "true" : "false";
    }
    
    @ResponseBody
    @RequestMapping(value = "/passwordIsRight", method = RequestMethod.POST)
    public String needChangePassword(@RequestParam(required = true) String username,@RequestParam(required = true) String password) {
    	String salt = userDetailsService.findByUsername(username).getSalt();
    	PmsUser temp=new PmsUser(username,password);
    	temp.setSalt(salt);
    	PmsUser user = userDetailsService.getPmsUser(temp);
    	return user!= null ? "true" : "false";
    }
    
}
