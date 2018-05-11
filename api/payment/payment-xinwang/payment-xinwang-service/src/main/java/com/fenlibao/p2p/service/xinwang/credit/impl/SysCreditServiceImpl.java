package com.fenlibao.p2p.service.xinwang.credit.impl;

import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysTransferInfo;
import com.fenlibao.p2p.service.xinwang.credit.SysCreditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @date 2017/6/1 14:45
 */
@Service
public class SysCreditServiceImpl implements SysCreditService {
    @Resource
    private SysCreditDao sysCreditDao;

    @Override
    public SysTransferInfo getTransferInfo(int creditId) {
        return sysCreditDao.getTransferInfo(creditId);
    }

    @Override
    public SysTransferInfo getInfoByOrder(int orderId) {
        return sysCreditDao.getTransferInfoByOrder(orderId);
    }

    @Override
    public BaseCreditInfo getBaseCreditInfo(int creditId) {
        return sysCreditDao.getBaseCreditInfo(creditId);
    }
}
