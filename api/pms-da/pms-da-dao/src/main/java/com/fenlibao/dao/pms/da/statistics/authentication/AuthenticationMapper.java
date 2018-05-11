package com.fenlibao.dao.pms.da.statistics.authentication;

import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 用户个人资产投资统计
 * Created by Louis Wang on 2015/11/6.
 */
public interface AuthenticationMapper {

    List<AuthenticationInfo> getAuthenticationList(Map<String,Object> param, RowBounds bounds);

    AuthenticationInfo getAuthenticationTotal(Map<String,Object> param);
}
