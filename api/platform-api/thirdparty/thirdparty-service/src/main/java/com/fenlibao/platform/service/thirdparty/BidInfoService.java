package com.fenlibao.platform.service.thirdparty;

import java.util.List;
import java.util.Map;

import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;

public interface BidInfoService {

	/**
	 *借款标信息
	 */
	List<BidInfoEntity> getBidInfoList(Integer page,Integer pageSize,Map<String,Object> map);
	
	int getTotalCount(Map<String,Object> map);
	Double getTotalAmount(Map<String,Object> map);
	
	List<BidInvestrecordsEntity> getBidInvestrecordsList(Integer page,Integer pageSize,int projectId);
	
	/**
	 *融360
	 */
	int getTotalCountByIds(String[] sb);
	
	List<BidInfoEntity> getBidInfoListByIds(String[] sb);
	
	/**
	 *网贷天眼
	 */
	List<BidInfoEntity> getBidInfoListWDTY(Integer page,Integer pageSize,Map<String,Object> map);
	
	int getTotalCountWDTY(Map<String,Object> map);
	
	int getInvestrecordsCountWDTY(Map<String,Object> map);

	/**
	 * 网贷之家超过100万的标的全部提前还款
	 * @param page
	 * @param pageSize
	 * @param map
	 * @return
	 */
	List<BidInfoEntity> getWDZJPrepaymentBidInfoList(Integer page,Integer pageSize,Map<String,Object> map);

	/**
	 * 网贷之家超过100万的标的全部提前还款 总数
	 * @param map
	 * @return
	 */
	int getWDZJPrepaymentTotalCount(Map<String,Object> map);

	int setWDZJPrepaymentBids(List<BidInfoEntity> list) throws  Exception;
}
