package com.fenlibao.p2p.service.xinwang.bank.impl;

import com.fenlibao.p2p.dao.xinwang.bank.XWBankDao;
import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;
import com.fenlibao.p2p.service.xinwang.bank.XWBankService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @date 2017/6/2 14:23
 */
@Service
public class XWBankServiceImpl implements XWBankService {
    @Resource
    private XWBankDao bankDao;

    @Override
    public XWBankInfo getBankInfo(String bankCode) {
        return StringUtils.isEmpty(bankCode)?new XWBankInfo():bankDao.getBankInfo(bankCode);
    }
}
