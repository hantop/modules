package com.fenlibao.dao.pms.idmt.rolepermission;

import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.rolepermission.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public interface RolePermissionMapper {

    /**
     * 根据角色ID获取权限树
     *
     * @param roleId
     * @return
     */
    List<PermitTreeNode> getPermitTreeByRoleId(Integer roleId);

    /**
     * 删除角色对应的权限
     *
     * @param roleId
     */
    void removeRolePermissions(@Param(value = "roleId") Integer roleId);

    /**
     * 新增角色的权限(多个)
     *
     * @param roleId
     * @param permissionsId
     */
    void addRolePermissions(
            @Param(value = "roleId") Integer roleId,
            @Param(value = "permissionsId") List<Integer> permissionsId);

    /**
     * 根据角色ID获取权限
     *
     * @param roleId
     * @return
     */
    List<RolePermissionVO> findRolePermissions(@Param(value = "roleId") Integer roleId);
}
