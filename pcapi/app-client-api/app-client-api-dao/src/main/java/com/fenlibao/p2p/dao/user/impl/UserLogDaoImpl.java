package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.user.UserLogDao;
import com.fenlibao.p2p.model.entity.UserLog;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcai on 2016/8/25.
 */
@Repository
public class UserLogDaoImpl extends BaseDao implements UserLogDao {

    public UserLogDaoImpl() {
        super("UserSecurityMapper");
    }

    @Override
    public void addUserLog(UserLog userLog) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userLog.getUserId());

        map.put("ip",userLog.getIp());
        map.put("conduct",userLog.getConduct());

        map.put("status",userLog.getStatus());
        map.put("remarks",userLog.getRemarks());
        map.put("requestStr",userLog.getRequestStr());
        sqlSession.insert(MAPPER + "addUserLog", map);
    }
}
