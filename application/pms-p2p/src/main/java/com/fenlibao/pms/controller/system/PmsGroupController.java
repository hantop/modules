package com.fenlibao.pms.controller.system;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsGroupForm;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.idmt.group.PmsGroupService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构管理
 * Created by Lullaby on 2015/7/20.
 */
@RestController
@RequestMapping("system/pmsgroup")
public class PmsGroupController {

    @Resource
    private PmsGroupService pmsGroupService;
    @Resource
    private UserDetailsService userDetailsService;

    @RequiresPermissions("organization:view")
    @RequestMapping
    public ModelAndView pmsGroupPage(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        ModelAndView view = new ModelAndView("system/group/index");
        Subject principals = SecurityUtils.getSubject();
        String username = (String) principals.getPrincipal();
        Integer groupId = userDetailsService.getUserGroup(username);
        RowBounds bounds = new RowBounds(page, limit);
        List<PmsGroup> pmsGroupTree = pmsGroupService.findAll(groupId,new RowBounds()) ;
        List<PmsGroup> pmsGroupList = pmsGroupService.findAll(groupId,bounds) ;
        PageInfo<PmsGroup> paginator = new PageInfo<>(pmsGroupList);
        view.addObject("pmsGroupList", pmsGroupList);
        view.addObject("pmsGroupTree", pmsGroupTree);
        view.addObject("paginator", paginator);
        return view;
    }

    @RequiresPermissions("organization:create")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> add(PmsGroupForm pmsGroupForm) {

        Map<String, Object> resultMap = new HashMap<>();
        try {
             pmsGroupService.addPmsGroup(pmsGroupForm);
            resultMap.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg","error");
        }
        return resultMap;
    }

    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(PmsGroupForm pmsGroupForm) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            pmsGroupService.updatePmsGroup(pmsGroupForm);
            resultMap.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg","error");
        }

        return resultMap;
    }

    @RequiresPermissions("organization:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> delete(@RequestParam(required=true) int id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            pmsGroupService.deletePmsGroup(id);
            resultMap.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg","error");
        }

        return resultMap;
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/findNode", method = RequestMethod.POST)
    public String findNodeForm(@RequestParam(required=false) int nodeId) {
        Map<String, Object> resultMap = new HashMap<>();
        PmsGroup pmsGroup = pmsGroupService.findNode(nodeId);
        String pmsGroupJson = JSON.toJSONString(pmsGroup);
        return pmsGroupJson;
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/users", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView pmsGroupViewUsers(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                     @RequestParam(required=true) int id) {
        ModelAndView view = new ModelAndView("system/group/users");
        Subject principals = SecurityUtils.getSubject();
        String username = (String) principals.getPrincipal();
        Integer groupId = userDetailsService.getUserGroup(username);
        RowBounds bounds = new RowBounds(page, limit);
        List<PmsGroup> pmsGroupTree = pmsGroupService.findAll(groupId, new RowBounds()) ;
    //    List<PmsUser> pmsUsersList = pmsGroupService.findGroupUsers(id,bounds) ;
    //    PageInfo<PmsUser> paginator = new PageInfo<>(pmsUsersList);
    //    view.addObject("pmsUsersList", pmsUsersList);
        view.addObject("pmsGroupTree", pmsGroupTree);
        view.addObject("groupId",id);
    //    view.addObject("paginator", paginator);
        return view;
    }

    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public
    @ResponseBody
    Object rechargeList(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int pageNum,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            @RequestParam(required=false) int id,
            @RequestParam(required=false) String dimission
    ) {

        RowBounds bounds = new RowBounds(pageNum + 1, limit);
        List<PmsUser> pmsUsersList = pmsGroupService.findGroupUsers(id,dimission,bounds) ;
        PageInfo<PmsUser> paginator = new PageInfo<>(pmsUsersList);
        return paginator;
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/maintain", method = RequestMethod.POST)
    public String showMaintainForm(@RequestParam(required=false) int id) {

        PmsGroup pmsGroup = pmsGroupService.findNode(id);
        String pmsGroupStr = JSON.toJSONString(pmsGroup);
        return pmsGroupStr;
    }

    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ModelAndView showTree() {
        ModelAndView view = new ModelAndView("system/group/tree");
    //    view.addObject("pmsGroupList", pmsGroupService.findAll(bounds));
        return view;
    }
}
