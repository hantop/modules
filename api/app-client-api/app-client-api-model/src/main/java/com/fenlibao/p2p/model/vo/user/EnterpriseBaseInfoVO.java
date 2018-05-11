package com.fenlibao.p2p.model.vo.user;

/**
 * 企业基础信息
 * @author zcai
 * @date 2016年4月22日
 */
public class EnterpriseBaseInfoVO {

	private Integer userId; //用户ID,参考T6110.F01
	private String code; //企业编号
	private String license; //营业执照登记注册号,唯一
	private String name; //企业名称
	private Integer registerYear; //注册年份
	private String corporate; //法人
	private String idCardNo; //法人身份证号,加密存储
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRegisterYear() {
		return registerYear;
	}
	public void setRegisterYear(Integer registerYear) {
		this.registerYear = registerYear;
	}
	public String getCorporate() {
		return corporate;
	}
	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
}
