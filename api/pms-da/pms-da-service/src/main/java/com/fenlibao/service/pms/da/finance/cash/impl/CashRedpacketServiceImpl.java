package com.fenlibao.service.pms.da.finance.cash.impl;

import com.fenlibao.dao.pms.da.finance.cash.CashRedpacketMapper;
import com.fenlibao.model.pms.da.finance.CashRedpacket;
import com.fenlibao.model.pms.da.finance.form.CashRedpacketForm;
import com.fenlibao.service.pms.da.finance.cash.CashRedpacketService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bogle on 2016/1/13.
 */
@Service
public class CashRedpacketServiceImpl implements CashRedpacketService {

    @Autowired
    private CashRedpacketMapper cashRedpacketMapper;

    @Override
    public Map.Entry<BigDecimal,List<CashRedpacket>> findCashListByPager(CashRedpacketForm cashRedpacketForm, RowBounds bounds) {
        List<CashRedpacket> list = this.cashRedpacketMapper.findCashListByPager(cashRedpacketForm,bounds);
        BigDecimal sumMoney = this.sumCashMoney(cashRedpacketForm);
        Map.Entry<BigDecimal,List<CashRedpacket>> entry = new  AbstractMap.SimpleEntry(sumMoney,list);
        return entry;
    }

    @Override
    public List<CashRedpacket> findAllCashList(CashRedpacketForm cashRedpacketForm) {
        return this.cashRedpacketMapper.findAllCashList(cashRedpacketForm);
    }

    @Override
    public BigDecimal sumCashMoney(CashRedpacketForm cashRedpacketForm) {
        return this.cashRedpacketMapper.sumCashMoney(cashRedpacketForm);
    }
}
