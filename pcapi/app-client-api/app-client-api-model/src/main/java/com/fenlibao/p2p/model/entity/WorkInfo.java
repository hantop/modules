package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人工作记录(t6143)
 */
public class WorkInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;//ID
	private Integer userId;// 用户ID
	private String state;// 工作状态,ZZ:在职;LZ:离职
	private String company;// 单位名称
	private String position;// 职位
	private String email;// 工作邮箱
	private Integer cityId;// 工作城市,参考T5119.F01
	private String companyAddr;// 工作地址
	private String companyType;// 公司类别
	private String companyIndustry;// 公司行业
	private String companySize;// 公司规模
	private Date startDate;// 工作开始时间
	private Date endDate;// 工作结束时间
	private String income;// 工作收入
	
	

	public static long getSerialVersionUID() {
		return serialVersionUID;
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



	public String getState() {
		if(state==null){
			state="zz";
		}
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}



	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		this.position = position;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public Integer getCityId() {
		return cityId;
	}



	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}



	public String getCompanyAddr() {
		return companyAddr;
	}



	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}



	public String getCompanyType() {
		return companyType;
	}



	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}



	public String getCompanyIndustry() {
		return companyIndustry;
	}



	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}



	public String getCompanySize() {
		return companySize;
	}



	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public String getIncome() {
		return income;
	}



	public void setIncome(String income) {
		this.income = income;
	}




	
}
