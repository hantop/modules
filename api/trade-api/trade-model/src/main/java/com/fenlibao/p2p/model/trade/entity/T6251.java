package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.fenlibao.p2p.model.trade.enums.T6251_F08;

/** 
 * 标债权记录
 */
public class T6251 {

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 债权编码
     */
    public String F02;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F03;

    /** 
     * 债权人ID,参考T6110.F01
     */
    public int F04;

    /** 
     * 购买价格
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 原始债权金额
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 持有债权金额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 是否正在转让,S:是;F:否;
     */
    public T6251_F08 F08;

    /** 
     * 创建日期
     */
    public Date F09;

    /** 
     * 起息日期
     */
    public Date F10;

    /** 
     * 投资记录ID,参考T6250.F01
     */
    public int F11;

    /** 
     * 债权转让订单Id
     */
    public int F12;

}
