package com.fenlibao.service.pms.da.bidtype;

import com.fenlibao.model.pms.da.bidType.BidType;

import java.util.List;

public interface BidTypeService {

	/**
	 * 查询标类型
	 *
	 * @param excludeCodes 排除列表
	 * @return
	 */
	List<BidType> getBidTypes(List<String> excludeCodes);

	List<BidType> getAllUsedBidType();

	/**
	 * 查询返现券对应的标的类型限制
	 * @param redPacketId
	 * @return
	 */
	List<String> getBidTypesByRedPacketId(Integer redPacketId);
	
	List<Integer> getBidTypeIdsByRedPacketId(Integer redPacketId);

    List<Integer> getBidTypeIdsByRateCouponId(int rateCouponId);
}
