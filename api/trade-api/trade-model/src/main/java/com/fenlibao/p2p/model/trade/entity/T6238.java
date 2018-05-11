package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;

/** 
 * 标的费率
 */
public class T6238 {

    private static final long serialVersionUID = 1L;

    /** 
     * 标的ID,参考T6230.F01
     */
    public int F01;

    /** 
     * 成交服务费率
     */
    public BigDecimal F02 = BigDecimal.ZERO;

    /** 
     * 投资管理费率
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 逾期罚息利率
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /** 
     * 投资奖励利率
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 借款人利率
     */
    public BigDecimal F06 = BigDecimal.ZERO;
    
    /** 
     * 逾期手续费率
     */
    public BigDecimal F07 = BigDecimal.ZERO;
    
    /** 
     * 代收手续费
     */
    public BigDecimal F08 = BigDecimal.ZERO;
}
