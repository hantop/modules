package com.fenlibao.p2p.service.pms.user.impl;

import com.fenlibao.p2p.dao.pms.user.PmsUserDao;
import com.fenlibao.p2p.model.entity.pms.user.PmsUser;
import com.fenlibao.p2p.service.pms.user.PmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/12.
 */
@Service
public class PmsUserServiceImpl implements PmsUserService {
    @Autowired
    private PmsUserDao pmsUserDao;

    @Override
    public int getUserId(String operatorUsername) {
        PmsUser pmsUser = pmsUserDao.getUserByUsername(operatorUsername);
        return pmsUser.getId();
    }
}
