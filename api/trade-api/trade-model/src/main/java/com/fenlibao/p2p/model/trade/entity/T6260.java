package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.T6260_F07;

/** 
 * 债权转让申请
 */
public class T6260 {

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 债权ID,参考T6251.F01
     */
    public int F02;

    /** 
     * 转让价格
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 转让债权
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 创建时间
     */
    public Timestamp F05;

    /** 
     * 结束日期
     */
    public Date F06;

    /** 
     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
     */
    public T6260_F07 F07;

    /** 
     * 转让手续费率
     */
    public BigDecimal F08 = BigDecimal.ZERO;

    /** 
     * 折扣=转让价格/(转让债权+债权已过天数的收益)
     */
    public BigDecimal F09 = BigDecimal.ZERO;
}
