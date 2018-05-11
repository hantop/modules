package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.dao.BaseDao;
import com.fenlibao.p2p.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;

/**
 * Created by Lullaby on 2015/9/11.
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Resource
    private BaseDao baseDao;

    @Override
    public Connection getConnection() {
        return baseDao.getConnection();
    }

}
