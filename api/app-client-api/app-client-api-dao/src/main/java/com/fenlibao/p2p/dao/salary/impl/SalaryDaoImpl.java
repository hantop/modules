package com.fenlibao.p2p.dao.salary.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.salary.SalaryDao;
import com.fenlibao.p2p.model.entity.salary.BidInfo;
import com.fenlibao.p2p.model.entity.salary.SalaryInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbBidInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbJoinRecord;
import com.fenlibao.p2p.model.vo.SalaryDetailVO;

@Repository
public class SalaryDaoImpl implements SalaryDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "SalaryMapper.";

	@Override
	public BigDecimal getUserEarnByDate(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER + "getUserEarnByDate", map);
	}

	@Override
	public BigDecimal getXjbInvestSum(String userId) {
		return sqlSession.selectOne(MAPPER + "getXjbInvestSum", userId);
	}

	@Override
	public BigDecimal getXjbTenderFreezeSum(String userId) {
		return sqlSession.selectOne(MAPPER + "getXjbTenderFreezeSum", userId);
	}

	@Override
	public SalaryInfo getSalaryInfo(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getSalarInfo", map);
	}

	@Override
	public SalaryInfo getSalaryDetail(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getSalaryDetail", map);
	}

	@Override
	public List<BidInfo> getSalaryPlanHistoryList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getSalaryPlanHistoryList", map);
	}

	@Override
	public List<UserXjbBidInfo> getUserXjbBidList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getUserXjbBidList", map);
	}

	@Override
	public List<UserXjbBidInfo> getBuyBidList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getBuyBidList", map);
	}

	@Override
	public UserXjbJoinRecord getUserXjbJoinRecord(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getUserXjbJoinRecord", map);
	}

	@Override
	public BidInfo getContinueBuyBid(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getContinueBuyBid", map);
	}

	@Override
	public int getBidPeriodById(int loanId) {
		return this.sqlSession.selectOne(MAPPER+"getBidPeriodById", loanId);
	}

	@Override
	public String getUserName(int userId) {
		return this.sqlSession.selectOne(MAPPER+"getUserName", userId);
	}

	@Override
	public SalaryDetailVO getSalaryDetailInfo(int salaryId) {
		return this.sqlSession.selectOne(MAPPER+"getSalaryDetailInfo", salaryId);
	}

	@Override
	public BidInfo isUserBided(Map map) {
		return this.sqlSession.selectOne(MAPPER+"isUserBided", map);
	}

	@Override
	public BidInfo getBidNextInvestDay(Map map) {
		return this.sqlSession.selectOne(MAPPER+"getBidNextInvestDay", map);
	}
	
	@Override
	public BidInfo getUserNextInvestDay(Map map){
		return this.sqlSession.selectOne(MAPPER+"getUserNextInvestDay", map);
	}

	@Override
	public BidInfo isUserHaveBid(Map map) {
		return this.sqlSession.selectOne(MAPPER+"isUserHaveBid", map);
	}

	/* (non Javadoc) 
	 * @Title: getUserXjbInvestDayList
	 * @Description: 获取当前用户投资日的薪金宝计划列表
	 * @param map
	 * @return: List<UserXjbBidInfo> 
	 * @see com.fenlibao.p2p.dao.salary.SalaryDao#getUserXjbInvestDayList(java.util.Map) 
	 */
	@Override
	public List<UserXjbBidInfo> getUserXjbInvestDayList(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getUserXjbInvestDayList", map);
	}
}
