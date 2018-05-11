package com.fenlibao.p2p.model.entity.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fenlibao.p2p.model.enums.pay.RefundStatus;

/**
 * 
 * @author zcai
 * @date 2016年4月27日
 */
public class RefundOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date createTime;
	private Date submitTime;
	private Date finishedTime;
	private Integer consumeOrderId;
	private BigDecimal amount;
	private String sn;
	private RefundStatus status;
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
	public Integer getConsumeOrderId() {
		return consumeOrderId;
	}
	public void setConsumeOrderId(Integer consumeOrderId) {
		this.consumeOrderId = consumeOrderId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public RefundStatus getStatus() {
		return status;
	}
	public void setStatus(RefundStatus status) {
		this.status = status;
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
	
}
