package com.fenlibao.lianpay.v_1_0.vo;

/**
 * 退款异步通知bean
 * @author zcai
 * @date 2016年4月27日
 */
public class RefundCallBackVO {
	
	public static final String sta_refund_SUCCESS = "2"; //退款成功
	public static final String sta_refund_FAILURE = "3"; //退款失败
	
	private String oid_partner;
	private String sign_type;
	private String sign;
	private String no_refund; //商户退款流水号
	private String dt_refund; //商户退款时间
	private String money_refund; //退款金额
	private String oid_refundno; //连连银通退款流水号
	private String sta_refund; //退款状态 
	private String settle_date; //清算日期
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNo_refund() {
		return no_refund;
	}
	public void setNo_refund(String no_refund) {
		this.no_refund = no_refund;
	}
	public String getDt_refund() {
		return dt_refund;
	}
	public void setDt_refund(String dt_refund) {
		this.dt_refund = dt_refund;
	}
	public String getMoney_refund() {
		return money_refund;
	}
	public void setMoney_refund(String money_refund) {
		this.money_refund = money_refund;
	}
	public String getOid_refundno() {
		return oid_refundno;
	}
	public void setOid_refundno(String oid_refundno) {
		this.oid_refundno = oid_refundno;
	}
	public String getSta_refund() {
		return sta_refund;
	}
	public void setSta_refund(String sta_refund) {
		this.sta_refund = sta_refund;
	}
	public String getSettle_date() {
		return settle_date;
	}
	public void setSettle_date(String settle_date) {
		this.settle_date = settle_date;
	}
	@Override
	public String toString() {
		return "RefundCallBackVO [oid_partner=" + oid_partner + ", sign_type=" + sign_type + ", sign=" + sign
				+ ", no_refund=" + no_refund + ", dt_refund=" + dt_refund + ", money_refund=" + money_refund
				+ ", oid_refundno=" + oid_refundno + ", sta_refund=" + sta_refund + ", settle_date=" + settle_date
				+ "]";
	}
	
}
