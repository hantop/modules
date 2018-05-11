package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopInformation;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
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
	public List<ShopTreasureInfo> getBidInfoList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasureOrderby", map);
	}

	@Override
	public List<ShopTreasureInfo> getBidInfoListByMap(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidInfoListByMap", map);
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
	public List<ShopTreasureInfo> getCloseShopTreasureOderByStatus(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getShopTreasureOderByStatus", map);
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
	public List<ShopTreasureInfo> getBidPlansList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getBidPlansList", map);
	}

	@Override
	public List<PlanBidsStatus> getPlanBidsStatus(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getPlanBidsStatus", map);
	}

	@Override
	public ShopTreasureInfo getPlanInfo(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getPlanInfo", map);
	}

	@Override
	public List<ShopTreasureInfo> getdirectionalBid(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getdirectionalBid",map);
	}

	@Override
	public int getpurchasedPlan(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getpurchasedPlan", map);
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
	public ShopTreasureInfo getPlanDetail(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getPlanDetail", map);
	}

	@Override
	public List<InvestInfo> getNearBid(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("num", num);
		param.put("versionType", vte == null ? VersionTypeEnum.PT.getIndex() : vte.getIndex());
		return this.sqlSession.selectList(MAPPER + "getNearBid", param,pageBounds);
	}

	@Override
	public int getNewPurchasedPlan(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getNewPurchasedPlan", map);
	}

	@Override
	public int isCGBid(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "isCGBid", map);
	}

	@Override
	public int countIvestProduct(String type,String versionType) {
		Map<String, Object> param = new HashMap<>();
		param.put("type", type);
		param.put("versionType", versionType);
		return this.sqlSession.selectOne(MAPPER + "countIvestProduct", param);
	}
}
