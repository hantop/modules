package com.fenlibao.p2p.model.mp.entity.topup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 手机充值订单实体
 * @author yangzengcai
 * @date 2016年2月19日
 */
public class MobileTopupOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6986559134515012243L;

	private Integer id;
	private Integer userId;
	private String phoneNum;
	private String parvalueCode;//充值面额编码
	private BigDecimal userPayAmount;//用户支付给平台金额
	private BigDecimal platformPayAmount;//平台支付给第三方的金额
	private Integer p_id;//用户积分抵扣消费id(关联MP_MEMBER_POINTS_USE_INFO)
	private Integer status;//充值状态(0:待提交1:充值中;2:充值成功;3:充值失败)
	private Date createTime;
	private Date endTime; //充值完成时间
	private String orderNum;//订单编号
	private Integer topUpChannel;//充值渠道
	private String integralCode;//使用积分编码
	private Integer integralQty;//使用积分数量
	private String thirdpartyOrdernum;//第三方订单编号
	
	private Integer consumptionOrderId;//消费订单ID（flb.consumption_order.id）
	
	public Integer getConsumptionOrderId() {
		return consumptionOrderId;
	}
	public void setConsumptionOrderId(Integer consumptionOrderId) {
		this.consumptionOrderId = consumptionOrderId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getParvalueCode() {
		return parvalueCode;
	}
	public void setParvalueCode(String parvalueCode) {
		this.parvalueCode = parvalueCode;
	}
	public BigDecimal getUserPayAmount() {
		return userPayAmount;
	}
	public void setUserPayAmount(BigDecimal userPayAmount) {
		this.userPayAmount = userPayAmount;
	}
	public BigDecimal getPlatformPayAmount() {
		return platformPayAmount;
	}
	public void setPlatformPayAmount(BigDecimal platformPayAmount) {
		this.platformPayAmount = platformPayAmount;
	}
	public Integer getP_id() {
		return p_id;
	}
	public void setP_id(Integer p_id) {
		this.p_id = p_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getTopUpChannel() {
		return topUpChannel;
	}
	public void setTopUpChannel(Integer topUpChannel) {
		this.topUpChannel = topUpChannel;
	}
	public String getIntegralCode() {
		return integralCode;
	}
	public void setIntegralCode(String integralCode) {
		this.integralCode = integralCode;
	}
	public Integer getIntegralQty() {
		return integralQty;
	}
	public void setIntegralQty(Integer integralQty) {
		this.integralQty = integralQty;
	}
	public String getThirdpartyOrdernum() {
		return thirdpartyOrdernum;
	}
	public void setThirdpartyOrdernum(String thirdpartyOrdernum) {
		this.thirdpartyOrdernum = thirdpartyOrdernum;
	}
	
}
