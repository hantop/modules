package com.fenlibao.platform.dao.thirdparty;

import java.util.List;
import java.util.Map;

import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;

public interface BidInfoMapper {

	/**
	 * 更新借款标信息
	 */
	public List<BidInfoEntity> getBidInfoList(Map map);
	
	public int getTotalCount(Map<String,Object> map);
	
	public Double getTotalAmount(Map<String,Object> map);
	
	public List<BidInvestrecordsEntity> getBidInvestrecordsList(Map map);
	
	public int getTotalCountByIds(Map<String,Object> map);
	
	public List<BidInfoEntity> getBidInfoListByIds(Map map);
	
	/**
	 *网贷天眼
	 */
	List<BidInfoEntity> getBidInfoListWDTY(Map map);
	
	int getTotalCountWDTY(Map<String,Object> map);
	
	int getInvestrecordsCountWDTY(Map<String,Object> map);

	List<BidInfoEntity> getWDZJPrepaymentBidInfoList(Map<String,Object> map);

	int getWDZJPrepaymentTotalCount(Map<String,Object> map);

	int setWDZJPrepaymentBids(List<BidInfoEntity> list);
	
}
