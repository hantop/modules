package com.fenlibao.p2p.model.trade.entity.pay;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.pay.T6130_F09;
import com.fenlibao.p2p.model.trade.enums.pay.T6130_F16;

/** 
 * 用户提现申请
 */
public class T6130{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 银行卡ID,参考T6114.F01
     */
    public int F03;

    /** 
     * 提现金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 应收手续费
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 实收手续费
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 创建时间
     */
    public Timestamp F08;

    /** 
     * 状态,DSH:待审核;DFK:待放款;YFK:已放款;TXSB:提现失败;
     */
    public T6130_F09 F09;

    /** 
     * 审核人ID,参考T7110.F01
     */
    public int F10;

    /** 
     * 审核时间
     */
    public Timestamp F11;

    /** 
     * 审核意见
     */
    public String F12;

    /** 
     * 放款人ID,参考T7110.F01
     */
    public int F13;

    /** 
     * 放款时间
     */
    public Timestamp F14;

    /** 
     * 放款意见
     */
    public String F15;

    /** 
     * 是否已到账,F:否;S:是;
     */
    public T6130_F16 F16;

}
