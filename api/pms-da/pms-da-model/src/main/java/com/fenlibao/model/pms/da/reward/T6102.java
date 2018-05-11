package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 资金流水
 */
public class T6102 {

	/**
	 * 自增ID
	 */
	private Integer F01;

	/**
	 * 资金账号ID,参考T6101.F01
	 */
	private Integer F02;

	/**
	 * 交易类型ID,参考T5122.F01
	 */
	private Integer F03;

	/**
	 * 对方账户ID,参考T6101.F01
	 */
	private Integer F04;

	/**
	 * 创建时间
	 */
	private Timestamp F05;

	/**
	 * 收入
	 */
	private BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	private BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	private BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 备注
	 */
	private String F09;

	/**
	 * 对账状态,WDZ:未对账;YDZ:已对账;
	 */
	private String F10;

	/**
	 * 对账时间
	 */
	private Timestamp F11;

	/**
	 * 标ID
	 */
	private Integer F12;

	/**
	 * 自增ID
	 */
	public Integer getF01() {
		return F01;
	}

	/**
	 * 自增ID
	 */
	public void setF01(Integer f01) {
		F01 = f01;
	}

	/**
	 * 资金账号ID,参考T6101.F01
	 */
	public Integer getF02() {
		return F02;
	}

	/**
	 * 资金账号ID,参考T6101.F01
	 */
	public void setF02(Integer f02) {
		F02 = f02;
	}

	/**
	 * 交易类型ID,参考T5122.F01
	 */
	public Integer getF03() {
		return F03;
	}

	/**
	 * 交易类型ID,参考T5122.F01
	 */
	public void setF03(Integer f03) {
		F03 = f03;
	}

	/**
	 * 对方账户ID,参考T6101.F01
	 */
	public Integer getF04() {
		return F04;
	}

	/**
	 * 对方账户ID,参考T6101.F01
	 */
	public void setF04(Integer f04) {
		F04 = f04;
	}

	/**
	 * 创建时间
	 */
	public Timestamp getF05() {
		return F05;
	}

	/**
	 * 创建时间
	 */
	public void setF05(Timestamp f05) {
		F05 = f05;
	}

	/**
	 * 收入
	 */
	public BigDecimal getF06() {
		return F06;
	}

	/**
	 * 收入
	 */
	public void setF06(BigDecimal f06) {
		F06 = f06;
	}

	/**
	 * 支出
	 */
	public BigDecimal getF07() {
		return F07;
	}

	/**
	 * 支出
	 */
	public void setF07(BigDecimal f07) {
		F07 = f07;
	}

	/**
	 * 余额
	 */
	public BigDecimal getF08() {
		return F08;
	}

	/**
	 * 余额
	 */
	public void setF08(BigDecimal f08) {
		F08 = f08;
	}

	/**
	 * 备注
	 */
	public String getF09() {
		return F09;
	}

	/**
	 * 备注
	 */
	public void setF09(String f09) {
		F09 = f09;
	}

	/**
	 * 对账状态,WDZ:未对账;YDZ:已对账;
	 */
	public String getF10() {
		return F10;
	}

	/**
	 * 对账状态,WDZ:未对账;YDZ:已对账;
	 */
	public void setF10(String f10) {
		F10 = f10;
	}

	/**
	 * 对账时间
	 */
	public Timestamp getF11() {
		return F11;
	}

	/**
	 * 对账时间
	 */
	public void setF11(Timestamp f11) {
		F11 = f11;
	}

	/**
	 * 标ID
	 */
	public Integer getF12() {
		return F12;
	}

	/**
	 * 标ID
	 */
	public void setF12(Integer f12) {
		F12 = f12;
	}

}