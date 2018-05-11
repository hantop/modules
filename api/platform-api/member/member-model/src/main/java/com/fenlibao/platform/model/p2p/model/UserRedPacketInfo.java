package com.fenlibao.platform.model.p2p.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserRedPacketInfo {
	private Integer id; //自增id
    private Integer hbId;   //红包Id
    private Integer userId; //用户Id
    private String type;    //红包活动类型
    private BigDecimal hbBalance = BigDecimal.ZERO;   //红包金额
    private BigDecimal conditionBalance = BigDecimal.ZERO;  //投资金额数
    private Integer effectDay;// 红包有效天数
    private String status;  //红包状态
    private Timestamp timestamp;    //红包活动截止使用日期
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getHbId() {
		return hbId;
	}
	public void setHbId(Integer hbId) {
		this.hbId = hbId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getHbBalance() {
		return hbBalance;
	}
	public void setHbBalance(BigDecimal hbBalance) {
		this.hbBalance = hbBalance;
	}
	public BigDecimal getConditionBalance() {
		return conditionBalance;
	}
	public void setConditionBalance(BigDecimal conditionBalance) {
		this.conditionBalance = conditionBalance;
	}
	public Integer getEffectDay() {
		return effectDay;
	}
	public void setEffectDay(Integer effectDay) {
		this.effectDay = effectDay;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
}
