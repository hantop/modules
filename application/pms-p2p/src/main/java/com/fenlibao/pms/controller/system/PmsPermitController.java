package com.fenlibao.pms.controller.system;

import com.alibaba.fastjson.JSON;
import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.pms.shiro.web.UserInfo;
import com.fenlibao.service.pms.idmt.permit.PmsPermitService;
import com.fenlibao.service.pms.idmt.rolepermission.RolePermissionService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lullaby on 2015/7/28.
 */
@RestController
@RequestMapping("system/pmspermit")
public class PmsPermitController {
	
	@Autowired
	PmsPermitService pmsPermitService;
	@Resource
	private RolePermissionService rolePermissionService;
	@Resource
	private UserDetailsService userDetailsService;

	@RequiresPermissions("systemRole:view")
	@RequestMapping(value = "permittreebyroleid")
	public String getPermitTreeByRoleId(
			@RequestParam(required = false) Integer roleId,
			@RequestParam(required = false) Integer parentId) {
		PermitTreeNode rootNode = null;
		// 角色拥有的权限
		List<PermitTreeNode> rolePermits = null;
		// 当前用户的角色对应的权限
		List<PermitTreeNode> currentPermits = null;
		// 角色拥有的权限ID
		List<Integer> rolePermitIds = new ArrayList<>();
		// 获取当前用户拥有的角色
		List<RoleVO> userRoles = userDetailsService.getUserRoles(UserInfo.getUsername());
		RoleVO roleVO = userRoles.get(0);
		// 当前用户为root角色
		if (roleVO.getParentId() == 0) {
			currentPermits = pmsPermitService.getNodes(null);
		} else {
			currentPermits = rolePermissionService.getPermitTreeByRoleId(roleVO.getId());
		}
		// 新增用户
		if (roleId == -1) {
			rootNode = currentPermits.get(0);
			buildPermitAndChild(rootNode, currentPermits);
		} else {
			rolePermits = rolePermissionService.getPermitTreeByRoleId(roleId);
			// 遍历父角色的所有权限，把子角色拥有的权限选中
			for (PermitTreeNode node : rolePermits) {
				rolePermitIds.add(node.getId());
			}
			// 将角色拥有的权限设为已选中
			for (PermitTreeNode node : currentPermits) {
				if (rolePermitIds.contains(node.getId())) {
					node.setChecked(true);
				}
			}
			rootNode = currentPermits.get(0);
			buildPermitAndChild(rootNode, currentPermits);
		}
		return JSON.toJSONString(rootNode);
	}

	/**
	 * 组装权限及其子权限
	 *
	 * @param permits
     */
	private void buildPermitAndChild(PermitTreeNode pPermit, List<PermitTreeNode> permits) {
		List<PermitTreeNode> children = pPermit.getChildren();
		if(children == null) {
			children = new ArrayList<>();
		}
		pPermit.setChildren(children);
		for (PermitTreeNode permit : permits) {
			if (pPermit.getId().equals(permit.getPid())) {
				children.add(permit);
				buildPermitAndChild(permit, permits);
			}
		}
	}

	@RequiresPermissions("permission:view")
    @RequestMapping
    public ModelAndView pmsPermitPage() {
        return new ModelAndView("system/pmspermit/index");
    }

	@RequiresPermissions("permission:view")
    @RequestMapping(value="permittree",method = RequestMethod.POST)
    public String permitTree(@RequestParam(required = false) Integer id){
    	List<PermitTreeNode> list=null;
    	if(id!=null){
    		if(id==-1){
    			id=null;
    		}
    		list=pmsPermitService.getPermitTree(id);
    	}
    	return JSON.toJSONString(list);
    }
    
    @RequiresPermissions("permission:commit")
    @RequestMapping(value="savenode",method = RequestMethod.POST)
    public Object saveNode(PermitTreeNode node){
    	PermitTreeNode conditon=new PermitTreeNode();
    	conditon.setCode(node.getCode());
    	List<PermitTreeNode> listOfCode=pmsPermitService.getNodes(conditon);
    	String returnContent="";
    	if(listOfCode.isEmpty()){
    		returnContent="{status:'OK',message:'保存成功'}";
    		pmsPermitService.saveNode(node);
    	}
    	else if(listOfCode.get(0).getId().equals(node.getId())){
    		returnContent="{status:'OK',message:'保存成功'}";
    		pmsPermitService.saveNode(node);
    	}
    	else{
    		returnContent="{status:'ERROR',message:'权限代码已存在'}";
    	}
    	return JSON.parse(returnContent);
    }
    
    @RequiresPermissions("permission:delete")
    @RequestMapping(value="deletenode",method = RequestMethod.POST)
    public Object deleteNode(@RequestParam(required = true) Integer id){
    	pmsPermitService.deleteNode(id);
    	return JSON.parse("{status:'OK',message:'删除成功'}");
    }

}
