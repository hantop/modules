package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class T6101 {

	/**
	 * 账户ID,自增
	 */
	private Integer F01;

	/**
	 * 用户ID,参考T6110.F01
	 */
	private Integer F02;

	/**
	 * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
	 */
	private String F03;

	/**
	 * 资金账号
	 */
	private String F04;

	/**
	 * 账户名称
	 */
	private String F05;

	/**
	 * 余额
	 */
	private BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 最后更新时间
	 */
	private Timestamp F07;

	
	/**
	 * 账户ID,自增
	 */
	public Integer getF01() {
		return F01;
	}

	/**
	 * 账户ID,自增
	 */
	public void setF01(Integer f01) {
		F01 = f01;
	}

	/**
	 * 用户ID,参考T6110.F01
	 */
	public Integer getF02() {
		return F02;
	}

	/**
	 * 用户ID,参考T6110.F01
	 */
	public void setF02(Integer f02) {
		F02 = f02;
	}

	/**
	 * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
	 */
	public String getF03() {
		return F03;
	}

	/**
	 * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
	 */
	public void setF03(String f03) {
		F03 = f03;
	}

	/**
	 * 资金账号
	 */
	public String getF04() {
		return F04;
	}

	/**
	 * 资金账号
	 */
	public void setF04(String f04) {
		F04 = f04;
	}

	/**
	 * 账户名称
	 */
	public String getF05() {
		return F05;
	}

	/**
	 * 账户名称
	 */
	public void setF05(String f05) {
		F05 = f05;
	}

	/**
	 * 余额
	 */
	public BigDecimal getF06() {
		return F06;
	}

	/**
	 * 余额
	 */
	public void setF06(BigDecimal f06) {
		F06 = f06;
	}

	/**
	 * 最后更新时间
	 */
	public Timestamp getF07() {
		return F07;
	}

	/**
	 * 最后更新时间
	 */
	public void setF07(Timestamp f07) {
		F07 = f07;
	}

}