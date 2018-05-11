package com.fenlibao.platform.model.thirdparty.entity.bid;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提供给第三方平台的投标记录-- flb.v_bid_investrecords
 * 
 * @author junda.feng
 * @date 2016年5月28日
 * 
 */
public class BidInvestrecordsEntity {

    private Integer projectId;//项目主键,t6230.F01
    private Integer subscribeUserName;//投标人ID,参考t6250.F03
    private BigDecimal amount;//投标金额,参考t6250.F04
    private Date addDate;//'投标时间,参考t6250.F06',
    private String type;//参考t6250.F09，F：手动 S：自动'
    
	public Integer getSubscribeUserName() {
		return subscribeUserName;
	}
	public void setSubscribeUserName(Integer subscribeUserName) {
		this.subscribeUserName = subscribeUserName;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
    

}
