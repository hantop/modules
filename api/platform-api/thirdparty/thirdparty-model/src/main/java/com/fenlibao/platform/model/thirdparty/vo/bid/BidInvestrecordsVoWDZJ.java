package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;


/**
 * 投资信息--网贷之家
 * @author junda.feng
 * @date 2016年5月30日
 * 
 */
public class BidInvestrecordsVoWDZJ extends BaseBidInvestrecordsVo<BidInvestrecordsVoWDZJ>{
	private String subscribeUserName;//投标人ID
	public BidInvestrecordsVoWDZJ(BidInvestrecordsEntity records)
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
