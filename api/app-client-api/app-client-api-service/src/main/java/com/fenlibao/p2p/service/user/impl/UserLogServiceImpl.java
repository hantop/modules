package com.fenlibao.p2p.service.user.impl;


import com.fenlibao.p2p.dao.user.UserLogDao;
import com.fenlibao.p2p.model.entity.UserLog;
import com.fenlibao.p2p.service.user.UserLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by chen on 2016/8/25.
 */
@Service
public class UserLogServiceImpl implements UserLogService {

    @Resource
    private UserLogDao userLogDao;

    @Override
    public void addUserLog(UserLog userLog) throws Exception {
       this.userLogDao.addUserLog(userLog);
    }
}
