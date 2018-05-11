package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;


/**
 * 投资信息--多赚
 * @author junda.feng
 * @date 2016年5月30日
 * 
 */
public class BidInvestrecordsVoDZ extends BaseBidInvestrecordsVo<BidInvestrecordsVoDZ>{
	private String subUserName;//投标人ID
	public BidInvestrecordsVoDZ(BidInvestrecordsEntity records)
			throws Throwable {
		super(records);
		this.subUserName=StringHelper.encode(StringHelper.deviation(records.getSubscribeUserName()));
	}
	public String getSubUserName() {
		return subUserName;
	}
	public void setSubUserName(String subUserName) {
		this.subUserName = subUserName;
	}
	
}
