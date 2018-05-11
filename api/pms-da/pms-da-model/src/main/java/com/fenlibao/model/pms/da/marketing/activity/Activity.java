package com.fenlibao.model.pms.da.marketing.activity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 活动设置
 * @author Administrator
 *
 */
public class Activity implements Serializable{
	
	private int id;
	
	@NotNull(message = "活动编码不能为空")
	private String code;
	
	@NotNull(message = "活动名称不能为空")
	private String name;
	
	private Date timeStart;
	
	private Date timeEnd;
	
	private String status;
	
	private List<ActivityRateCoupon> activityRateCoupon;
	
	//@NotNull(message = "奖励类型不能为空")
	private List<Integer> couponIds;
	
	public List<ActivityRateCoupon> getActivityRateCoupon() {
		return activityRateCoupon;
	}

	public void setActivityRateCoupon(List<ActivityRateCoupon> activityRateCoupon) {
		this.activityRateCoupon = activityRateCoupon;
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

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Integer> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Integer> couponIds) {
		this.couponIds = couponIds;
	}

}
