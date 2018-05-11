package com.fenlibao.service.pms.da.statistics.authentication;

import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/30.
 */

public interface AuthenticationService {

    //获取查询时间的实名列表
    List<AuthenticationInfo> getAuthenticationList(Map<String,Object> param, RowBounds bounds);

    //获取查询时间的实名总计
    AuthenticationInfo getAuthenticationTotal(Map<String,Object> param);
}
