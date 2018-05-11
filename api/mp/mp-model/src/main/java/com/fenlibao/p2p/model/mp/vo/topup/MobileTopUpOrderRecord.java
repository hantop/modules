package com.fenlibao.p2p.model.mp.vo.topup;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手机充值订单记录
 * @author junda.feng
 * @date 2016年2月17日-2-26
 */
public class MobileTopUpOrderRecord {

//	private String orderId;//订单编号
	private String parvalue;//充值面额，为整型的字符串 比如：50，100
	private Date createTime;//
	private String status;//充值状态(0:待提交1:充值中;2:充值成功;3:充值失败)
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
//	public String getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
	public String getParvalue() {
		return parvalue;
	}
	public void setParvalue(String parvalue) {
		this.parvalue = parvalue;
	}
	
}
