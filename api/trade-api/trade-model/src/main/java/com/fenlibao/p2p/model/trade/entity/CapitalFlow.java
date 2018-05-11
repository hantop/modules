package com.fenlibao.p2p.model.trade.entity
;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.T6102_F10;

/** 
 * 资金流水
 */
public class CapitalFlow {

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public Integer F01;

    /** 
     * 资金账号ID,参考T6101.F01
     */
    public Integer F02;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public Integer F03;

    /** 
     * 对方账户ID,参考T6101.F01
     */
    public Integer F04;

    /** 
     * 创建时间
     */
    public Timestamp F05;

    /** 
     * 收入
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 支出
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 余额
     */
    public BigDecimal F08 = BigDecimal.ZERO;

    /** 
     * 备注
     */
    public String F09;

    /** 
     * 对账状态,WDZ:未对账;YDZ:已对账;
     */
    public T6102_F10 F10;

    /** 
     * 对账时间
     */
    public Timestamp F11;
    
    /**
     * 标ID
     */
    public Integer F12;

}
