package com.fenlibao.p2p.model.trade.entity.order;

import java.math.BigDecimal;

/**
 * 提现订单
 * Created by zcai on 2016/11/28.
 */
public class T6503 {

    /**
     * 订单ID,参考T6501.F01
     */
    public int F01;
    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;
    /**
     * 提现金额
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
     * 支付公司代码
     */
    public int F07;
    /**
     * 流水单号,提现成功时填入
     */
    public String F08;
    /**
     * 提现申请记录ID,参考T6130.F01
     */
    public int F09;

    public T6503() {
        this.F03 = BigDecimal.ZERO;
        this.F04 = BigDecimal.ZERO;
        this.F05 = BigDecimal.ZERO;
    }

}
