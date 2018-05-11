package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: BidInfoDaoImpl 
 * @Description: 标基本信息查询dao
 * @author: laubrence
 * @date: 2016-3-3 下午4:33:42  
 */
@Repository
public class BidInfoDaoImpl implements BidInfoDao {
	
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "BidInfoMapper.";
    
    public BorrowerInfo getBorrowerInfo(int bidId) {
        return sqlSession.selectOne(MAPPER + "getBorrowerInfo", bidId);
	}

	@Override
	public BidBaseInfo getBidBaseInfoByUser(int userId, int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidBaseInfoByUser", map);	
	}

	@Override
	public BidBaseInfo getBidBaseInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidBaseInfo", map);
	}

	@Override
	public BidExtendInfo getBidExtendInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidExtendInfo", map);
	}

	@Override
	public BidExtendInfo getBidAllInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidAllInfo", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatus, int minDays, int maxDays, PageBounds pageBounds) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidType", bidType);
		map.put("bidStatus", bidStatus);
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);

		return sqlSession.selectList(MAPPER + "getBidInfoList", map, pageBounds);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoOrderByList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bidType", bidType);
		map.put("bidStatus", bidStatuses);
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}

		return sqlSession.selectList(MAPPER + "getBidInfoOrderByList", map, pageBounds);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bidType", bidType);
		map.put("bidStatus", bidStatuses);
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}

		return sqlSession.selectList(MAPPER + "getBidInfoAndHisBidList", map, pageBounds);
	}

	@Override
	public Integer getUserInvestCount(int userId) {
		return sqlSession.selectOne(MAPPER + "getUserInvestCount", userId);
	}
	
	@Override
	public BidBorrowerInfo getBidBorrowerInfo(int bidId) {
		return sqlSession.selectOne(MAPPER + "getBidBorrowerInfo", bidId);
	}

	@Override
	public DirectionalBid getDirectionalBid(int bidId) {
		Map<String, Integer> params = new HashMap<>(1);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getDirectionalBid", params);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoAndPlanAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds,String productType,int cgNum,int isNeedHis,int isNovice) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(bidType)){
			if(bidType.equals("SXJH")) {
				map.put("planType", "2");
				map.put("bidType", "0");
			}else if(bidType.equals("YSJH")) {
					map.put("planType", "1");
					map.put("bidType", "0");
			}else if(bidType.equals("XSJH")) {
				map.put("novicePlan", "1");
				map.put("bidType", "0");
			}else{
				map.put("bidType", bidType);
			}
		}
		map.put("bidStatus", bidStatuses);
		map.put("minDays", minDays);
		map.put("maxDays", maxDays);
		if(StringUtils.isNotEmpty(sortType)){
			map.put("sortType", sortType);
		}
		if(StringUtils.isNotEmpty(sortBy)){
			map.put("sortBy", sortBy);
		}
		map.put("productType", productType);
		map.put("cgNum", cgNum);
		map.put("isNeedHis", isNeedHis);
		map.put("isNovice", isNovice);
		return sqlSession.selectList(MAPPER + "getInvestPlanAndBidList", map, pageBounds);
	}

	@Override
	public String getenterpriseSealCode(Integer bidId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bidId",bidId);
		return sqlSession.selectOne(MAPPER + "getenterpriseSealCode", map);
	}

	@Override
	public BidAgreement getBidAgreement(Integer bidId) {
		Map<String, Integer> params = new HashMap<>(1);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidAgreement", params);
	}
}
