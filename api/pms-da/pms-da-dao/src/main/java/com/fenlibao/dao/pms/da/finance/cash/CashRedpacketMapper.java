package com.fenlibao.dao.pms.da.finance.cash;

import com.fenlibao.model.pms.da.finance.CashRedpacket;
import com.fenlibao.model.pms.da.finance.form.CashRedpacketForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bogle on 2016/1/13.
 */
public interface CashRedpacketMapper {


    /**
     * 分页获取现金红包
     * @param cashRedpacketForm
     * @param bounds
     * @return
     */
    List<CashRedpacket> findCashListByPager(CashRedpacketForm cashRedpacketForm, RowBounds bounds);

    BigDecimal sumCashMoney(CashRedpacketForm cashRedpacketForm);

    List<CashRedpacket> findAllCashList(CashRedpacketForm cashRedpacketForm);

}
