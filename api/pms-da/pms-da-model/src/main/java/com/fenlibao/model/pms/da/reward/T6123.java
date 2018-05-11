package com.fenlibao.model.pms.da.reward;

import java.sql.Timestamp;

public class T6123 {
	/**
	 * 自增ID
	 */
	private Integer F01;

	/**
	 * 接收用户ID,参考T6110.F01
	 */
	private Integer F02;

	/**
	 * 标题
	 */
	private String F03;

	/**
	 * 发送时间
	 */
	private Timestamp F04;

	/**
	 * 状态,WD:未读;YD:已读;SC:删除;
	 */
	private String F05;

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
	 * 接收用户ID,参考T6110.F01
	 */
	public Integer getF02() {
		return F02;
	}

	/**
	 * 接收用户ID,参考T6110.F01
	 */
	public void setF02(Integer f02) {
		F02 = f02;
	}

	/**
	 * 标题
	 */
	public String getF03() {
		return F03;
	}

	/**
	 * 标题
	 */
	public void setF03(String f03) {
		F03 = f03;
	}

	/**
	 * 发送时间
	 */
	public Timestamp getF04() {
		return F04;
	}

	/**
	 * 发送时间
	 */
	public void setF04(Timestamp f04) {
		F04 = f04;
	}
	
	/**
	 * 状态,WD:未读;YD:已读;SC:删除;
	 */
	public String getF05() {
		return F05;
	}

	/**
	 * 状态,WD:未读;YD:已读;SC:删除;
	 */
	public void setF05(String f05) {
		F05 = f05;
	}

}
