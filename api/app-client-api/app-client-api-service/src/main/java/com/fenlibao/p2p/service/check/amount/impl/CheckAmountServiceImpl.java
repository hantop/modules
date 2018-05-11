package com.fenlibao.p2p.service.check.amount.impl;

import com.fenlibao.p2p.dao.check.amount.CheckAmountDao;
import com.fenlibao.p2p.model.check.amount.XwAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.check.amount.CheckAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
@Service
public class CheckAmountServiceImpl implements CheckAmountService {


    @Autowired
    private CheckAmountDao checkAmountDao;

    @Override
    public List<XwAccount> getAllXwUsers() {
        Map<String, Object> params = new HashMap<>();
        params.put("userRole", UserRole.INVESTOR.getCode());
        return checkAmountDao.getAllXwUsers(params);
    }

    @Override
    public void saveUserAmountError(int userId, String platformUserNo, BigDecimal localWlzh, BigDecimal availableAmount, BigDecimal localSdzh, BigDecimal freezeAmount, BigDecimal xwBalance) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("platformUserNo", platformUserNo);
        params.put("localWlzh", localWlzh);
        params.put("xwWlzh", availableAmount);
        params.put("wlzhSub", localWlzh.subtract(availableAmount));
        params.put("localSdzh", localSdzh);
        params.put("xwSdzh", freezeAmount);
        params.put("sdzhSub", localSdzh.subtract(freezeAmount));
        params.put("xwBalance", xwBalance);
        checkAmountDao.saveUserAmountError(params);
    }
}
