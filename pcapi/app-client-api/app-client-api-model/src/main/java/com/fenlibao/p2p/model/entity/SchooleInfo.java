package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户学校信息(T6142)
 */
public class SchooleInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;//ID
	private Integer userId;//用户ID
	private String schoole="";//学校名称
	private Integer enrollmentYear=new Date().getYear();//入学年份
	private String major="";//专业
	private String situation;//在校情况

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


	public String getSchoole() {
		return schoole;
	}

	public void setSchoole(String schoole) {
		this.schoole = schoole;
	}



	public Integer getEnrollmentYear() {
		return enrollmentYear;
	}


	public void setEnrollmentYear(Integer enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}


	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}





	
}
