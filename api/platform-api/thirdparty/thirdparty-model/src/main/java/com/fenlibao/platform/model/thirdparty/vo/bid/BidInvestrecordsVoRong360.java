package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;


/**
 * 投资信息--融360
 * @author junda.feng
 * @date 2016年6月7日
 * 
 */
public class BidInvestrecordsVoRong360 extends BaseBidInvestrecordsVo<BidInvestrecordsVoRong360>{
	private String subscribeUserName;//投标人ID
	public BidInvestrecordsVoRong360(BidInvestrecordsEntity records)
			throws Throwable {
		super(records);
		this.subscribeUserName=StringHelper.encode(StringHelper.deviation(records.getSubscribeUserName()));
	}
	public String getSubscribeUserName() {
		return subscribeUserName;
	}
	public void setSubscribeUserName(String subscribeUserName) {
		this.subscribeUserName = subscribeUserName;
	}
	
}
