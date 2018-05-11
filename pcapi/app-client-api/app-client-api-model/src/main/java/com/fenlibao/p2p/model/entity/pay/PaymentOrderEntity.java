package com.fenlibao.p2p.model.entity.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fenlibao.p2p.model.enums.pay.PaymentOrderStatus;

/**
 * 支付订单实体
 * @author zcai
 * @date 2016年4月20日
 */
public class PaymentOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date createTime; //创建时间
	private Date submitTime; //提交时间
	private Date finishedTime;//完成时间
	private Integer userId;//用户ID
	private BigDecimal amount;//金额
	private PaymentOrderStatus status;//订单状态
	private Integer channelCode;//支付通道编码
	private String sn;//流水号
	
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
	public PaymentOrderStatus getStatus() {
		return status;
	}
	public void setStatus(PaymentOrderStatus status) {
		this.status = status;
	}
	public Integer getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
}
