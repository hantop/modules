package com.fenlibao.p2p.dao.financing.impl;

import com.fenlibao.p2p.dao.financing.FinacingDao;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.InterfaceConst;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
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
	public List<InvestInfo> getUserInvestList(int userId, int isUp, Date time, String[] bidStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("isUp",isUp);
		map.put("time",time);
		map.put("bidStatus",bidStatus);
		return this.sqlSession.selectList(MAPPER+"getUserInvestList", map);
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
	public RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("creditId",creditId);
		map.put("tradeTypes",tradeTypes);
		return this.sqlSession.selectOne(MAPPER+"getLastRepaymentItem", map);
	}

	@Override
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, Integer pageNo, Integer pagesize) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		map.put("pageNo",(pageNo-1)*pagesize);
		map.put("pagesize",pagesize);
		map.put("userId",userId);
		map.put("type",type);
		return this.sqlSession.selectList(MAPPER+"getAllUserRepaymentItem", map);
	}

	@Override
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, VersionTypeEnum versionTypeEnum, Date timestamp, int mode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timestamp",timestamp);
		map.put("userId",userId);
		map.put("type",type);
		map.put("versionType",versionTypeEnum.getIndex());
		map.put("mode",mode);
		return this.sqlSession.selectList(MAPPER+"getNewAllUserRepaymentItem", map);
	}

	@Override
	public List<UserRepaymentAmout> userRepaymentAmout(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		return this.sqlSession.selectList(MAPPER+"userRepaymentAmout", map);
	}

	@Override
	public List<UserRepaymentAmout> userRepaymentAmout(int userId, int versionTypeEnum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("isCg",versionTypeEnum);
		return this.sqlSession.selectList(MAPPER+"userRepaymentAmout", map);
	}

    @Override
    public List<PlanRepaymentDetail> getPlanRepaymentDetail(Integer planRecordId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planRecordId",planRecordId);
		return this.sqlSession.selectList(MAPPER+"getPlanRepaymentDetail", map);
    }
}
