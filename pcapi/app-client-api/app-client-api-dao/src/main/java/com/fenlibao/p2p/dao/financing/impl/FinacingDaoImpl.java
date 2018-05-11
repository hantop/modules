package com.fenlibao.p2p.dao.financing.impl;

import com.fenlibao.p2p.dao.financing.FinacingDao;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FinacingDaoImpl implements FinacingDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "FinacingMapper.";
	
	@Override
	public List<Finacing> getMaySettleFinacing(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getMaySettleFinacing", map);
	}

	/**
	 * @Title: getSellFinacing
	 * @Description: 获取用户投资债权列表
	 * @param map
	 * @return 
	 */
	@Override
	public List<Finacing> getFinacingList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getInvestRecord", map);
	}
	
	/**
	 * @Title: getFinacingDetail
	 * @Description: 获取用户投资债权详情
	 * @param map
	 * @return 
	 * @see FinacingDao#getFinacingDetail(java.util.Map)
	 */
	@Override
	public Finacing getFinacingDetail(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getFinacingDetail", map);
	}

	@Override
	public List<Finacing> getFinacingByBid(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getFinacingByBid", map);
	}

	@Override
	public List<Finacing> getInvestRecord(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getInvestRecord", map);
	}

	@Override
	public BigDecimal getZqzrAssets(String userId) {
		return this.sqlSession.selectOne(MAPPER+"getZqzrAssets", userId);
	}

	@Override
	public CreditInfo getUserCreditInfo(int creditId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("creditId",creditId);
		return this.sqlSession.selectOne(MAPPER+"getUserCreditInfo", map);
	}

	@Override
	public List<InvestInfo> getUserInvestList(int userId, String bidType, String[] bidStatus, PageBounds pageBounds,int cgNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("bidType", bidType);
		map.put("bidStatus", bidStatus);
		map.put("cgNum", cgNum);
		return this.sqlSession.selectList(MAPPER+"getUserInvestList", map, pageBounds);
	}

	@Override
	public InvestInfoDetail getUserInvestDetail(String userId, String creditId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		return this.sqlSession.selectOne(MAPPER+"getUserInvestDetail", map);
	}

	@Override
	public double getUserCollectInterest(int userId, int creditId, String[] repaymentStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("repaymentStatus",repaymentStatus);
		return sqlSession.selectOne(MAPPER+"getUserCollectInterest", map);
	}

	@Override
	public List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("tradeTypes",tradeTypes);
		return this.sqlSession.selectList(MAPPER+"getUserRepaymentItem", map);
	}

	@Override
	public RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("tradeTypes",tradeTypes);
		return this.sqlSession.selectOne(MAPPER+"getLastRepaymentItem", map);
	}


	@Override
	public Map<String, Map<String, String>> getInvestmentQty(Integer userId, VersionTypeEnum versionTypeEnum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		Map<String, Map<String, String>> qty = sqlSession.selectMap(MAPPER + "getInvestmentQty", map, "status");
		Integer profitQty = sqlSession.selectOne(MAPPER + "getProfitQty", map);
		if(qty.get(Status.HKZ.name())==null)qty.put(Status.HKZ.name(),new HashMap<String, String>());
		qty.get(Status.HKZ.name()).put("qty",profitQty==null?"0":profitQty.toString());
		return qty;
		
	}

	@Override
	public List<InvestInfo> getNearInvestList(int userId, PageBounds pageBounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		return this.sqlSession.selectList(MAPPER+"getNearInvestList", map, pageBounds);
	}

	@Override
	public List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("tradeTypes",tradeTypes);
		return this.sqlSession.selectList(MAPPER+"getNextRepaymentItem", map);
	}

	@Override
	public Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("tradeTypes",tradeTypes);
		return this.sqlSession.selectOne(MAPPER+"getNextRepaymentItemProfit", map);
	}

	@Override
	public PlanFinacing getUserPlanDetail(int userId, Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("planRecordId",planRecordId);
		return sqlSession.selectOne(MAPPER + "getUserPlanDetail", params);
	}

	@Override
	public List<Double> getPlanCollectInterest(Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",planRecordId);
		return sqlSession.selectList(MAPPER + "getPlanCollectInterest", params);
	}

	@Override
	public PlanFinacing getUserNewPlanDetail(int userId, Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("planRecordId",planRecordId);
		return sqlSession.selectOne(MAPPER + "getUserNewPlanDetail", params);
	}

	@Override
	public List<InvestInfo> getNearCredit(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("num", num);
		map.put("versionType", vte == null ? VersionTypeEnum.PT.getIndex() : vte.getIndex());
		return this.sqlSession.selectList(MAPPER+"getNearCredit", map, pageBounds);
	}
}
