package com.fenlibao.pms.controller.system;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.idmt.role.Role;
import com.fenlibao.model.pms.idmt.role.form.RoleEditForm;
import com.fenlibao.model.pms.idmt.role.form.RoleForm;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.shiro.web.UserInfo;
import com.fenlibao.service.pms.idmt.role.RoleService;
import com.fenlibao.service.pms.idmt.rolepermission.RolePermissionService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lullaby on 2015/7/20.
 */
@RestController
@RequestMapping("system/pmsrole")
public class PmsRoleController {
    private static final Logger LOG = LoggerFactory.getLogger(PmsRoleController.class);
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserDetailsService userDetailsService;

    @RequiresPermissions("systemRole:view")
    @RequestMapping(value = "members", method = RequestMethod.GET)
    public
    @ResponseBody
    Object
    findMembers(Integer roleId,
                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<PmsUser> list = userDetailsService.getUsersByRole(roleId, bounds);
        PageInfo<PmsUser> paginator = new PageInfo<>(list);
        return paginator;
    }

    @RequiresPermissions("systemRole:view")
    @RequestMapping(value="roletree",method = RequestMethod.POST)
    public Object roleTree() {
        // 根节点
        RoleVO root = null;
        // 获取当前用户拥有的角色
        List<RoleVO> userRoles = userDetailsService.getUserRoles(UserInfo.getUsername());
        if (userRoles != null && userRoles.size() > 0) {
            root = userRoles.get(0);
            // 获取所有角色
            List<RoleVO> roles = roleService.getRoles();
            if(roles != null && roles.size() > 0) {
                buildRoleAndChild(root, roles);
            }
        }
        return JSON.toJSONString(root);
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

    @RequiresPermissions("systemRole:delete")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public HttpResponse deleteRole(@PathVariable(value = "id") Integer id) {
        HttpResponse response = new HttpResponse();
        if (id > 0) {
            try {
                // 删除角色及角色的权限
                rolePermissionService.removeRoleAndPermissions(id);
            } catch (Exception e) {
                LOG.error("PmsRoleController.deleteRole:", e.getMessage());
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "move", method = RequestMethod.PUT)
    public HttpResponse updateRoleParent(@RequestParam Integer id, @RequestParam Integer parentId) {
        HttpResponse response = new HttpResponse();
        if (id != null && parentId != null) {
            try {
                roleService.updateRoleParent(id, parentId);
            } catch (Exception e) {
                LOG.error("PmsRoleController.updateRoleParent:", e.getMessage());
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

    @RequiresPermissions("systemRole:commit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public HttpResponse addOrUpdateRole(@ModelAttribute(value = "roleEditForm") RoleEditForm roleEditForm) {
        HttpResponse response = new HttpResponse();
        if (StringUtils.isNotBlank(roleEditForm.getRoleName())
                && roleEditForm.getPermitIds() != null
                && roleEditForm.getPermitIds().size() > 0) {
            Role role = new Role();
            role.setParentId(roleEditForm.getParentId());
            role.setName(roleEditForm.getRoleName());
            role.setDescription(roleEditForm.getRoleDescription());
            role.setSort(roleEditForm.getSort());
            try {
                Integer editId = null;
                HashMap<String, Object> data;
                // 新增
                if (roleEditForm.getId() == null) {
                    // 根据角色名称查询角色是否存在
                    boolean hasRole = roleService.hasRole(roleEditForm.getRoleName());
                    if (hasRole) {
                        response.setCodeMessage(ResponseEnum.ROLE_ALREADYEXISTS.getCode(), ResponseEnum.ROLE_ALREADYEXISTS.getMessage());
                    } else {
                        // 新增角色及其权限
                        role.setId(0);
                        rolePermissionService.addRoleAndPermissions(role, roleEditForm.getPermitIds());
                        editId = role.getId();
                    }
                }
                // 更新
                else {
                    // 更新角色及其权限
                    role.setId(roleEditForm.getId());
                    rolePermissionService.updateRoleAndPermissions(role, roleEditForm.getPermitIds());
                    editId = role.getId();
                }
                data = new HashMap<>();
                data.put("editId", editId);
                response.setData(data);
            } catch (Exception e) {
                LOG.error("PmsRoleController.addOrUpdateRole:", e.getMessage());
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

    @RequiresPermissions("systemRole:view")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView findPmsroles(RoleForm roleForm,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        ModelAndView mav = new ModelAndView("system/pmsrole/index");
        RowBounds bounds = new RowBounds(page, limit);
        String name = roleForm.getName();
        List<Role> roles;
        if (StringUtils.isEmpty(name)) {
            roles = roleService.findAllRoles(bounds);
        } else {
            roles = roleService.findRolesLikeName(name, bounds);
        }
        PageInfo<Role> paginator = new PageInfo<>(roles);
        return mav.addObject("list", roles)
                .addObject("roleForm", roleForm)
                .addObject("paginator", paginator);
    }

}
