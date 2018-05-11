package com.fenlibao.p2p.service.common.impl;

import com.fenlibao.p2p.dao.common.LockDao;
import com.fenlibao.p2p.service.common.LockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @date 2017/8/22 15:45
 */
@Service
public class LockServiceImpl implements LockService {
    @Resource
    private LockDao lockDao;

    @Override
    public void createLock(String requestNo, String status) {
        lockDao.createLock(requestNo, status);
    }
}
