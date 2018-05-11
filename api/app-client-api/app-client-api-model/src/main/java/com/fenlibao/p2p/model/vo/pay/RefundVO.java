package com.fenlibao.p2p.model.vo.pay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款VO
 * @author zcai
 * @date 2016年4月27日
 */
public class RefundVO {

	private Integer id;
	private Date createTime;
	private BigDecimal amount;
	private String paymentOrderSn; //支付订单sn(flb.payment_order.sn)
	private String refundOrderSn;//退款订单流水号
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPaymentOrderSn() {
		return paymentOrderSn;
	}
	public void setPaymentOrderSn(String paymentOrderSn) {
		this.paymentOrderSn = paymentOrderSn;
	}
	
	public String getRefundOrderSn() {
		return refundOrderSn;
	}
	public void setRefundOrderSn(String refundOrderSn) {
		this.refundOrderSn = refundOrderSn;
	}
	@Override
	public String toString() {
		return "RefundVO [id=" + id + ", createTime=" + createTime + ", amount=" + amount + ", paymentOrderSn="
				+ paymentOrderSn + ", refundOrderSn=" + refundOrderSn + "]";
	}
	
}
