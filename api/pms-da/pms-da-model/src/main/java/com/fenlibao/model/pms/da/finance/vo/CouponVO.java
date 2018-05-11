package com.fenlibao.model.pms.da.finance.vo;

import java.math.BigDecimal;
/**
 * 加息券
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public class CouponVO {
    private Integer couponId;// 加息劵ID
    private String couponCode;// 加息劵代码
    private String remark;// 加息劵来源(备注)
    private BigDecimal range;// 加息劵幅度
    private BigDecimal minInvestMoney;// 加息劵最小投资金额
    private BigDecimal maxInvestMoney;// 加息劵最大投资金额
	private String limitMoney;// 加息劵限额(最小至最大值)
    private Integer effectDay;// 加息劵有效期
    private Integer sendNumber;// 发送数量
    private Integer activeCount;// 激活数量
    private BigDecimal forcastMoney;// 预计产生成本
    private BigDecimal actualMoney;// 实际产生成本
    private BigDecimal inversionMoney;// 转化投资额
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getRange() {
		return range;
	}
	public void setRange(BigDecimal range) {
		this.range = range;
	}
	public BigDecimal getMinInvestMoney() {
		return minInvestMoney;
	}
	public void setMinInvestMoney(BigDecimal minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}
	public BigDecimal getMaxInvestMoney() {
		return maxInvestMoney;
	}
	public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
		this.maxInvestMoney = maxInvestMoney;
	}
	//投资限额
	public String getLimitMoney() {
		return minInvestMoney + "-" + maxInvestMoney;
	}
	public Integer getEffectDay() {
		return effectDay;
	}
	public void setEffectDay(Integer effectDay) {
		this.effectDay = effectDay;
	}
	public Integer getSendNumber() {
		return sendNumber;
	}
	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}
	public Integer getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}
	public BigDecimal getForcastMoney() {
		return forcastMoney;
	}
	public void setForcastMoney(BigDecimal forcastMoney) {
		this.forcastMoney = forcastMoney;
	}
	public BigDecimal getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}
	public BigDecimal getInversionMoney() {
		return inversionMoney;
	}
	public void setInversionMoney(BigDecimal inversionMoney) {
		this.inversionMoney = inversionMoney;
	}

}
