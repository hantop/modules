package com.fenlibao.p2p.model.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 已到账收益
 * @author yangzengcai
 * @date 2015年11月9日
 */
public class ArrivalEarningsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 346126731278141962L;

	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	/**
	 * 收益类型
	 */
	private Integer type;
	/**
	 * 标类型ID
	 */
	private Integer bidTypeId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 债权ID 
	 */
	private Integer ZQ_id;
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getBidTypeId() {
		return bidTypeId;
	}
	public void setBidTypeId(Integer bidTypeId) {
		this.bidTypeId = bidTypeId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getZQ_id() {
		return ZQ_id;
	}
	public void setZQ_id(Integer zQ_id) {
		ZQ_id = zQ_id;
	}
	
}
