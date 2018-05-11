package com.fenlibao.p2p.model.trade.entity;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.T6252_F09;

/** 
 * 标还款记录
 */
public class T6252 {

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 付款用户ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 收款用户ID,参考T6110.F01
     */
    public int F04;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F05;

    /** 
     * 期号
     */
    public int F06;

    /** 
     * 金额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 应还日期
     */
    public Date F08;
    
    /** 
     * 状态,WH:未还;YH:已还;
     */
    public T6252_F09 F09;

    /** 
     * 实际还款时间
     */
    public Timestamp F10;

    /** 
     * 债权ID,参考T6251.F01
     */
    public int F11;
    
}
