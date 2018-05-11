package com.fenlibao.p2p.model.mp.vo.topup;

/**
 * 用于劲峰手机充值回调
 * @author yangzengcai
 * @date 2016年2月18日
 */
public class MobileTopUpCallbackVO {

	private String R0_biztype;//业务类型
	private String R1_agentcode;//代理商编号
	private String R2_mobile;//手机号
	private String R3_parvalue;//充值金额
	private String R4_trxamount;//实际扣款金额
	private String R5_productcode;//充值产品
	private String R6_requestid;//充值订单号
	private String R7_trxid;//代理商系统订单号
	/**
	 * 充值结果： 2表示成功    3表示失败 等  返回页面为空白页面时，不要重复提交该笔订单。请与劲峰优品技术支持联系。 （0表示排队中，1表示充值中、2表示充值成功、3表示失败、5表示退款状态、6退款成功，7部分退款）
	 */
	private String R8_returncode;//返回结果
	private String R9_extendinfo;//扩展信息，通知时将原样返回。如使用中文，请注意转码
	private String R10_trxDate;//充值时间
	private String hmac;//签名信息hmac(R0~R10,key)
	
	public String getR0_biztype() {
		return R0_biztype;
	}
	public void setR0_biztype(String r0_biztype) {
		R0_biztype = r0_biztype;
	}
	public String getR1_agentcode() {
		return R1_agentcode;
	}
	public void setR1_agentcode(String r1_agentcode) {
		R1_agentcode = r1_agentcode;
	}
	public String getR2_mobile() {
		return R2_mobile;
	}
	public void setR2_mobile(String r2_mobile) {
		R2_mobile = r2_mobile;
	}
	public String getR3_parvalue() {
		return R3_parvalue;
	}
	public void setR3_parvalue(String r3_parvalue) {
		R3_parvalue = r3_parvalue;
	}
	public String getR4_trxamount() {
		return R4_trxamount;
	}
	public void setR4_trxamount(String r4_trxamount) {
		R4_trxamount = r4_trxamount;
	}
	public String getR5_productcode() {
		return R5_productcode;
	}
	public void setR5_productcode(String r5_productcode) {
		R5_productcode = r5_productcode;
	}
	public String getR6_requestid() {
		return R6_requestid;
	}
	public void setR6_requestid(String r6_requestid) {
		R6_requestid = r6_requestid;
	}
	public String getR7_trxid() {
		return R7_trxid;
	}
	public void setR7_trxid(String r7_trxid) {
		R7_trxid = r7_trxid;
	}
	public String getR8_returncode() {
		return R8_returncode;
	}
	public void setR8_returncode(String r8_returncode) {
		R8_returncode = r8_returncode;
	}
	public String getR9_extendinfo() {
		return R9_extendinfo;
	}
	public void setR9_extendinfo(String r9_extendinfo) {
		R9_extendinfo = r9_extendinfo;
	}
	public String getR10_trxDate() {
		return R10_trxDate;
	}
	public void setR10_trxDate(String r10_trxDate) {
		R10_trxDate = r10_trxDate;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	
	@Override
	public String toString() {
		return "MobileTopUpCallbackVO [R0_biztype=" + R0_biztype + ", R1_agentcode=" + R1_agentcode + ", R2_mobile="
				+ R2_mobile + ", R3_parvalue=" + R3_parvalue + ", R4_trxamount=" + R4_trxamount + ", R5_productcode="
				+ R5_productcode + ", R6_requestid=" + R6_requestid + ", R7_trxid=" + R7_trxid + ", R8_returncode="
				+ R8_returncode + ", R9_extendinfo=" + R9_extendinfo + ", R10_trxDate=" + R10_trxDate + ", hmac=" + hmac
				+ "]";
	}
	
	
}
