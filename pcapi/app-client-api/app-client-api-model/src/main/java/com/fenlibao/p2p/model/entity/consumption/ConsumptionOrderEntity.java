package com.fenlibao.p2p.model.entity.consumption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费订单实体
 * @author zcai
 * @date 2016年4月20日
 */
public class ConsumptionOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date createTime;//创建时间
	private Date submitTime;//提交时间
	private Date finishedTime;//完成时间
	private Integer userId;//用户ID
	private BigDecimal amount;//金额
//	private ConsumptionOrderStatus status;//状态
	private Integer typeCode;//类型编码(flb.consumption_type)
	private Integer paymentOrderId;//支付订单ID
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getPaymentOrderId() {
		return paymentOrderId;
	}
	public void setPaymentOrderId(Integer paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

}
