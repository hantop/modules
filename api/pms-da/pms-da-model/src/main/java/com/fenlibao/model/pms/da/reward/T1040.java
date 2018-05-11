package com.fenlibao.model.pms.da.reward;

import java.sql.Timestamp;

public class T1040 {
	/**
	 * 自增ID
	 */
    private Integer F01;
    /**
     * 发送类型
     */
    private Integer F02;
    /**
     * 短信内容
     */
    private String F03;
    /**
     * 创建时间
     */
    private Timestamp F04;
    /**
     * 发送状态,W:未发送，Z:正在发送,Y:已发送 
     */
    private String F05;
    /**
     * 过期时间
     */
    private Timestamp F06;
    /**
     * 发送者用户ID，参考S61.T6110.F01
     */
    private Integer F07;
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
     * 发送类型
     */
	public Integer getF02() {
		return F02;
	}
    /**
     * 发送类型
     */
	public void setF02(Integer f02) {
		F02 = f02;
	}
    /**
     * 短信内容
     */
	public String getF03() {
		return F03;
	}
    /**
     * 短信内容
     */
	public void setF03(String f03) {
		F03 = f03;
	}
    /**
     * 创建时间
     */
	public Timestamp getF04() {
		return F04;
	}
    /**
     * 创建时间
     */
	public void setF04(Timestamp f04) {
		F04 = f04;
	}
    /**
     * 发送状态,W:未发送，Z:正在发送,Y:已发送 
     */
	public String getF05() {
		return F05;
	}
    /**
     * 发送状态,W:未发送，Z:正在发送,Y:已发送 
     */
	public void setF05(String f05) {
		F05 = f05;
	}
    /**
     * 过期时间
     */
	public Timestamp getF06() {
		return F06;
	}
    /**
     * 过期时间
     */
	public void setF06(Timestamp f06) {
		F06 = f06;
	}
    /**
     * 发送者用户ID，参考S61.T6110.F01
     */
	public Integer getF07() {
		return F07;
	}
    /**
     * 发送者用户ID，参考S61.T6110.F01
     */
	public void setF07(Integer f07) {
		F07 = f07;
	} 
    
    
}
