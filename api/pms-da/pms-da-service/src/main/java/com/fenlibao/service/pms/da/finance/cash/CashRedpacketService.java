package com.fenlibao.service.pms.da.finance.cash;

import com.fenlibao.model.pms.da.finance.CashRedpacket;
import com.fenlibao.model.pms.da.finance.form.CashRedpacketForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Bogle on 2016/1/13.
 */
public interface CashRedpacketService {

    /**
     * 分页获取现金红包
     * @param cashRedpacketForm
     * @param bounds
     * @return
     */
    Map.Entry<BigDecimal,List<CashRedpacket>> findCashListByPager(CashRedpacketForm cashRedpacketForm, RowBounds bounds);

    List<CashRedpacket> findAllCashList(CashRedpacketForm cashRedpacketForm);

    BigDecimal sumCashMoney(CashRedpacketForm cashRedpacketForm);
}
