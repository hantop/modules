package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;


/**
 * 投资信息--通用字段
 * @author junda.feng
 * @date 2016年6月1日
 * 
 */
public class BaseBidInvestrecordsVo<M> {

//    private String subscribeUserName;//投标人ID
    private Double amount;//投标金额
    private Double validAmount;//'如平台无’投标金额’和’有效金额’之分，则’投标金额’和’有效金额’一样',
    private String addDate;//'投标时间
    private Integer status;//投标状态
    private Integer type;//0：手动 1：自动'
    
    
    
    @SuppressWarnings("hiding")
	protected  <M> BaseBidInvestrecordsVo(BidInvestrecordsEntity records) throws Throwable{
    	this.amount=records.getAmount().doubleValue();
    	this.validAmount=records.getAmount().doubleValue();
    	this.addDate=DateUtil.getDateTime(records.getAddDate());
    	this.status=1;
    	this.type="F".equals(records.getType())?0:1;
    			
    }
    
    
    
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
    

}
