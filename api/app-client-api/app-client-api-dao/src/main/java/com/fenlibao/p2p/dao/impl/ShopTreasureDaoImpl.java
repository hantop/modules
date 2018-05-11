package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopInformation;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 开店宝相关
 */
@Repository
public class ShopTreasureDaoImpl implements ShopTreasureDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "ShopTreasureMapper.";

	@Override
	public BigDecimal getUserEarnStatistics(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getUserEarnStatistics", map);
	}

	@Override
	public BigDecimal getUserEarnByDate(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getUserEarnByDate",map);
	}

	@Override
	public ShopTreasureInfo getShopTreasure(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getShopTreasure", map);
	}

	@Override
	public List<ShopTreasureInfo> getCloseShopTreasureRecommend(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasureRecommend", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasureOrderby", map);
	}


	@Override
	public List<ShopTreasureInfo> getCloseShopTreasure(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasure", map);
	}

	@Override
	public ShopTreasureInfo getShopTreasureInfo(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getShopTreasureInfo", map);
	}

	@Override
	public BigDecimal getInvestStatistics(Map<String, Object> map) {
		BigDecimal totleMoney=this.sqlSession.selectOne(MAPPER+"getInvestStatistics", map);
		return totleMoney;
	}

	@Override
	public BigDecimal getEarnStatistics(Map<String, Object> map) {
		BigDecimal totleEarn=this.sqlSession.selectOne(MAPPER+"getEarnStatistics", map);
		return totleEarn;
	}

	@Override
	public ShopTreasureInfo getShopTreasureByZpid(int zqId) {
		return this.sqlSession.selectOne(MAPPER+"getShopTreasureByZpid",zqId);
	}

	@Override
	public ShopInformation getShopInfomationByBid(int bidId) {
		return this.sqlSession.selectOne(MAPPER+"getShopInfomationByBid", bidId);
	}

	@Override
	public int isUserInvest(int userId) {
		return this.sqlSession.selectOne(MAPPER+"isUserInvest", userId);
	}

	@Override
	public List<ShopTreasureInfo> getTimingBids(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "timingBid",map);
	}

	@Override
	public List<ShopTreasureInfo> getPreSaleBids(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getPreSaleBid",map);
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBids(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasureOrderby", map);
	}

	@Override
	public List<ShopTreasureInfo> getXfxdBidList(Map map) {
		return this.sqlSession.selectList(MAPPER + "getXfxdBidList", map);
	}

	@Override
	public List<ShopTreasureInfo> getAutoBidShopTreasure(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getAutoBidShopTreasure", map);
	}

	@Override
	public List<ShopTreasureInfo> getXfxdShortBidList(Map<String, Object> xfxdParam) {
		return this.sqlSession.selectList(MAPPER + "getXfxdShortBidList", xfxdParam);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoByTimeSort(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidInfoByTimeSort", map);
	}

	@Override
	public int isCGBid(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "isCGBid", map);
	}

	@Override
	public List<ShopTreasureInfo> getNoviceBidList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getNoviceBidList", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidPlansList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidPlansList", map);
	}

	@Override
	public ShopTreasureInfo getPlanInfo(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getPlanInfo", map);
	}

	@Override
	public List<PlanBidsStatus> getPlanBidsStatus(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getPlanBidsStatus", map);
	}

	@Override
	public int isPlanBid(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getPlanBid", map);
	}

	@Override
	public List<ShopTreasureInfo> getInvestmentBidsAndPlan(Map<String, Object> map) {
			return this.sqlSession.selectList(MAPPER + "getShopTreasureAndPlanOrderby", map);
	}

	@Override
	public List<ShopTreasureInfo> getStickShopTreasure(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getStickShopTreasure", map);
	}

	@Override
	public List<ShopTreasureInfo> getPlansList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getPlansList", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidListAndPlanList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidListAndPlanList", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidListAndPlanListOld(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidListAndPlanListOld", map);
	}

	@Override
	public List<ShopTreasureInfo> getOtherCloseShopTreasure(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getOtherShopTreasure", map);
	}

	@Override
	public ShopTreasureInfo getPlanDetail(Map<String, Object> map) {
			return this.sqlSession.selectOne(MAPPER+"getPlanDetail", map);

	}

	@Override
	public List<ShopTreasureInfo> getPlansListOderBy(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidPlanListOrderFull", map);
	}

	@Override
	public List<ShopTreasureInfo> getStickBidAndPlanTreasure(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getStickBidAndPlanShopTreasure", map);
	}

	@Override
	public List<ShopTreasureInfo> getStickBidAndPlanTreasureOld(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getStickBidAndPlanShopTreasureOld", map);
	}

	@Override
	public int isCGPlan(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "isCGPlan", map);
	}
}
