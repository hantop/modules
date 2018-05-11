package com.fenlibao.p2p.service.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.model.dm.entity.FundAccount;
import com.fenlibao.p2p.model.dm.enums.FundAccountType;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.service.dm.hx.HXUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zcai on 2016/10/9.
 */
@Service
public class HXUserServiceImpl implements HXUserService {

    @Resource
    private HXUserDao hxUserDao;


    @Override
    public void saveEAccount(int userId, String eAccount) {
        hxUserDao.saveEAccount(userId, eAccount);
    }

    @Override
    public HXAccountInfo getAccountInfo(int userId) {
        return hxUserDao.getAccountInfo(userId);
    }

    @Override
    public void createFundAcount(List<FundAccount> accounts) {
        hxUserDao.createFundAcount(accounts);
    }

    @Override
    public FundAccount getUserFundAccount(int userId, FundAccountType accountType) {
        return hxUserDao.getUserFundAccount(userId, accountType);
    }
}
