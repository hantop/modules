package com.fenlibao.platform.service.thirdparty.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fenlibao.platform.dao.thirdparty.BidInfoMapper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;
import com.fenlibao.platform.service.thirdparty.BidInfoService;

public class BidInfoServiceImpl implements BidInfoService {

	@Inject
	private BidInfoMapper bidInfoMapper;

	@Override
	public List<BidInfoEntity> getBidInfoList(Integer page,Integer pageSize,Map<String,Object> map) {
		if(map==null)map=new HashMap<>();
		map.put("page", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return bidInfoMapper.getBidInfoList(map);
	}

	@Override
	public int getTotalCount(Map<String,Object> map) {
		return bidInfoMapper.getTotalCount(map);
	}

	@Override
	public List<BidInvestrecordsEntity> getBidInvestrecordsList(Integer page,
			Integer pageSize,int projectId) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("projectId", projectId);
		map.put("page", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return bidInfoMapper.getBidInvestrecordsList(map);
	}

	@Override
	public List<BidInfoEntity> getBidInfoListByIds(String[] ids) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ids", ids);
		return bidInfoMapper.getBidInfoListByIds(map);
	}

	@Override
	public int getTotalCountByIds(String[] ids) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ids", ids);
		return bidInfoMapper.getTotalCountByIds(map);
	}

	@Override
	public List<BidInfoEntity> getBidInfoListWDTY(Integer page,
			Integer pageSize, Map<String, Object> map) {
		if(map==null)map=new HashMap<>();
		map.put("page", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return bidInfoMapper.getBidInfoListWDTY( map);
	}

	@Override
	public int getTotalCountWDTY(Map<String, Object> map) {
		return bidInfoMapper.getTotalCountWDTY(map);
	}

	@Override
	public Double getTotalAmount(Map<String, Object> map) {
		return bidInfoMapper.getTotalAmount(map);
	}

	@Override
	public int getInvestrecordsCountWDTY(Map<String, Object> map) {
		return bidInfoMapper.getInvestrecordsCountWDTY(map);
	}

	@Override
	public List<BidInfoEntity> getWDZJPrepaymentBidInfoList(Integer page, Integer pageSize, Map<String, Object> map) {
		if(map==null)map=new HashMap<>();
		map.put("page", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		return bidInfoMapper.getWDZJPrepaymentBidInfoList(map);
	}

	@Override
	public int getWDZJPrepaymentTotalCount(Map<String, Object> map) {
		return bidInfoMapper.getWDZJPrepaymentTotalCount(map);
	}

	@Override
	public int setWDZJPrepaymentBids(List<BidInfoEntity> list) throws  Exception{
		return bidInfoMapper.setWDZJPrepaymentBids(list);
	}

}
