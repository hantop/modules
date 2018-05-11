package com.fenlibao.model.pms.da.marketing.activity.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fenlibao.model.pms.da.marketing.activity.ActivityRateCoupon;

public class ActivityForm {
	
	private int id;
	
	@NotNull(message = "活动编码不能为空")
	private String code;
	
	@NotNull(message = "活动名称不能为空")
	private String name;
	
	@NotNull(message = "活动开始时间不能为空")
	private String timeStart;
	
	@NotNull(message = "活动结束时间不能为空")
	private String timeEnd;
	
	//@NotNull(message = "奖励类型不能为空")
	private List<ActivityRateCoupon> couponList;
	
	private List<Integer> couponIds;
	
	private String status;//活动状态
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ActivityRateCoupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<ActivityRateCoupon> couponList) {
		this.couponList = couponList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public List<Integer> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Integer> couponIds) {
		this.couponIds = couponIds;
	}
	
}
