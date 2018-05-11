package com.fenlibao.p2p.model.trade.entity.order;

import java.math.BigDecimal;

/** 
 * 放款订单
 */
public class T6505 {

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;

    /** 
     * 投资用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F03;

    /** 
     * 投标记录ID,参考T6250.F01
     */
    public int F04;

    /** 
     * 投标金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
}
