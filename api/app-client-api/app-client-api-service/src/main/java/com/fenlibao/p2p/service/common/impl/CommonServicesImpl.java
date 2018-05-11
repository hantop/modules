package com.fenlibao.p2p.service.common.impl;

import com.fenlibao.p2p.dao.common.CommonDao;
import com.fenlibao.p2p.service.common.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * kris [fengjunda@fenlibao.com]
 * 2017年05月26日  16:58
 */
@Service
public class CommonServicesImpl implements CommonServices{
    @Autowired
    CommonDao commonDao;

    @Override
    public Map platformStatictis() {
        return commonDao.platformStatictis();
    }
}
