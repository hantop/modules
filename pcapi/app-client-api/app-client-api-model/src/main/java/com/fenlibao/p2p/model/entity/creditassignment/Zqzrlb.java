package com.fenlibao.p2p.model.entity.creditassignment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6260_F07;

/**
 * 线上债权转让列表
 *
 */
public class Zqzrlb implements Serializable{

	 private static final long serialVersionUID = 1L;
	 
	    /** 
	     * 债权ID,参考T6251.F01
	     */
	    public int F01;

	    /** 
	     * 转让价格
	     */
	    public BigDecimal F02 = new BigDecimal(0);

	    /** 
	     * 已转债权
	     */
	    public BigDecimal F03 = new BigDecimal(0);

	    /** 
	     * 创建时间
	     */
	    public Timestamp F04;

	    /** 
	     * 结束时间
	     */
	    public Timestamp F05;

	    /** 
	     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
	     */
	    public T6260_F07 F06;

	    /** 
	     * 转让手续费率
	     */
	    public BigDecimal F07 = new BigDecimal(0);

	    /** 
	     * 债权人ID,参考T6110.F01
	     */
	    public int F08;

	    /** 
	     * 购买价格
	     */
	    public BigDecimal F09 = new BigDecimal(0);

	    /** 
	     * 原始债权金额
	     */
	    public BigDecimal F10 = new BigDecimal(0);

	    /** 
	     * 持有债权金额
	     */
	    public BigDecimal F11 = new BigDecimal(0);

	    /** 
	     * 借款标题
	     */
	    public String F12;

	    /** 
	     * 借款标类型ID,参考T6211.F01
	     */
	    public int F13;

	    /** 
	     * 年化利率
	     */
	    public BigDecimal F14 = new BigDecimal(0);

	    /** 
	     * 借款周期,单位:月
	     */
	    public int F15;

	    /** 
	     * 信用等级,参考T5124.F01
	     */
	    public int F16;
	    
	    /**
	     * 筹款到期时间
	     */
	    public Timestamp F17;
	    /**
	     * 债权编号
	     */
	    public String F18;
	   
	    /**
	     * 债权转让的折扣率
	     */
	    public BigDecimal F19 = new BigDecimal(0);
	    
	    /**
	     * 标的放款时间
	     */
	    public Timestamp F20;
	    /**
	     * 还款总期数
	     */
	    public int F22;
	    /**
	     * 剩余期数
	     */
	    public int F23;
	    /** 
	     * 标ID
	     */
	    public int F24;
	    /** 
	     * 债权申请ID
	     */
	    public int F25;
	    
	    /** 
	     * 标的资产类型
	     */
	    public String F26;
	    
	    /** 
	     * 标的描述
	     */
	    public String F27;
	    
}
