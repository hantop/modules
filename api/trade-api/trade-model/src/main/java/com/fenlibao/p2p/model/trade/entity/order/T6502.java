package com.fenlibao.p2p.model.trade.entity.order;

import java.math.BigDecimal;

/**
 * 充值订单
 * Created by zcai on 2016/11/1.
 */
public class T6502 {

    /**
     * 订单号,参考T6501.F01
     */
    public int F01;
    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;
    /**
     * 充值金额
     */
    public BigDecimal F03;
    /**
     * 应收手续费
     */
    public BigDecimal F04;
    /**
     * 实收手续费
     */
    public BigDecimal F05;
    /**
     * 银行卡号
     */
    public String F06;
    /**
     * 支付公司代号
     */
    public int F07;
    /**
     * 流水单号,充值成功时记录
     */
    public String F08;

    public T6502() {
        this.F03 = BigDecimal.ZERO;
        this.F04 = BigDecimal.ZERO;
        this.F05 = BigDecimal.ZERO;
    }


}
