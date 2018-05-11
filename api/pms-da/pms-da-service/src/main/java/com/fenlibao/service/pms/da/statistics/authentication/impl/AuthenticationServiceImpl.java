package com.fenlibao.service.pms.da.statistics.authentication.impl;

import com.fenlibao.dao.pms.da.statistics.authentication.AuthenticationMapper;
import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationForm;
import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationInfo;
import com.fenlibao.service.pms.da.statistics.authentication.AuthenticationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/30.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Resource
    private AuthenticationMapper authenticationMapper;

    @Override
    public List<AuthenticationInfo> getAuthenticationList(Map<String,Object> param, RowBounds bounds) {
        return authenticationMapper.getAuthenticationList(param,bounds);
    }

    @Override
    public AuthenticationInfo getAuthenticationTotal(Map<String,Object> param) {
        return authenticationMapper.getAuthenticationTotal(param);
    }
}
