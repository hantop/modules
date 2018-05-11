package com.fenlibao.service.pms.da.bidtype.impl;

import com.fenlibao.dao.pms.da.bidType.BidTypeMapper;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BidTypeServiceImpl implements BidTypeService{

	@Resource
	private BidTypeMapper bidTypeMapper;

	@Override
	public List<BidType> getBidTypes(List<String> excludeCodes) {
		List<BidType> allUsedBidType = bidTypeMapper.getBidTypes(excludeCodes);
		return allUsedBidType;
	}
	
	@Override
	public List<BidType> getAllUsedBidType() {
		return this.bidTypeMapper.getAllUsedBidType();
	}

	@Override
	public List<String> getBidTypesByRedPacketId(Integer redPacketId) {
		return this.bidTypeMapper.getBidTypesByRedPacketId(redPacketId);
	}

	@Override
	public List<Integer> getBidTypeIdsByRedPacketId(Integer redPacketId) {
		return this.bidTypeMapper.getBidTypeIdsByRedPacketId(redPacketId);
	}

	@Override
	public List<Integer> getBidTypeIdsByRateCouponId(int rateCouponId) {
		return this.bidTypeMapper.getBidTypeIdsByRateCouponId(rateCouponId);
	}

}
