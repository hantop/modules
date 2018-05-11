package com.fenlibao.service.pms.da.statistics.invest.impl;

import com.fenlibao.dao.pms.da.statistics.invest.RegMapper;
import com.fenlibao.model.pms.da.statistics.invest.Register;
import com.fenlibao.model.pms.da.statistics.invest.form.RegisterForm;
import com.fenlibao.service.pms.da.statistics.invest.RegService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bogle on 2015/12/30.
 */
@Service
public class RegServiceImpl implements RegService {

    @Autowired
    private RegMapper regMapper;

    @Override
    public List<Register> findRegList(RegisterForm register, RowBounds bounds) {
        return regMapper.findRegList(register, bounds);
    }

    @Override
    public List<Register> findRegList(RegisterForm register) {
        return regMapper.findAllRegList(register);
    }

    @Override
    public BigDecimal getInvestMoneyTotal(RegisterForm register) {
        return regMapper.getInvestMoneyTotal(register);
    }

    @Override
    public BigDecimal getInvestSumTotal(RegisterForm register) {
        return regMapper.getInvestSumTotal(register);
    }

    @Override
    public RegisterForm getMoneyTotal(RegisterForm register) {
        return regMapper.getMoneyTotal(register);
    }
}
