package com.fenlibao.service.pms.idmt.userrole.impl;

import com.fenlibao.dao.pms.idmt.role.RoleMapper;
import com.fenlibao.dao.pms.idmt.userrole.UserRoleMapper;
import com.fenlibao.model.pms.idmt.role.vo.RoleVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.service.pms.idmt.userrole.UserRoleService;

import javax.annotation.Resource;
import java.util.List;

public class UserRoleServiceImpl implements UserRoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<RoleVO> findRolesByUserId(Integer userId) {
        return userRoleMapper.findRolesByUserId(userId);
    }

    @Override
    public List<Integer> findUserIdsByRoleId(Integer roleId) {
        return null;
    }

    @Override
    public List<PmsUser> findUsersByRoleId(Integer roleId) {
        return null;
    }
}
