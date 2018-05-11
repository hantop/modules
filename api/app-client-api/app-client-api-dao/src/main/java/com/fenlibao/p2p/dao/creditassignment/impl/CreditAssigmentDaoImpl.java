package com.fenlibao.p2p.dao.creditassignment.impl;

import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CreditAssigmentDaoImpl implements CreditAssigmentDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "CreditassignmentMapper.";
	
	@Override
	public List<Zqzrlb> getCreditassignmentApplyforList(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER+"getCreditassignmentApplyforList", map);
	}

	@Override
	public int getCreditassignmentCount(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getCreditassignmentCount", map);
	}

	@Override
	public int getCreditassignmentApplyforCount(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getCreditassignmentApplyforCount", map);
	}

	@Override
	public int getRecord(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getRecord", map);
	}

	@Override
	public Integer getZqzrTenderRecord(String userId, String bidId) {
		Map<String, String> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getZqzrTenderRecord", params);
	}

	@Override
	public BigDecimal getPurchaseZqzrAmount(String userId, String bidId) {
		Map<String, String> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getPurchaseZqzrAmount", params);
	}

	@Override
	public InvestShareVO getTenderIdLatest(int userId, int applyforId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("applyforId", applyforId);
		return sqlSession.selectOne(MAPPER + "getTenderIdLatest", params);
	}

	@Override
	public List<InvestPlanInfo> getExpirePlans(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getExpirePlans", params);
	}

	@Override
	public List<UserPlan> getExpireUserPlans(int planId,int status) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		params.put("status", status);
		return sqlSession.selectList(MAPPER + "getExpireUserPlans", params);
	}

	@Override
	public List<UserPlanProduct> getUserPlanProducts(int userPlanId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectList(MAPPER + "getUserPlanProducts", params);
	}

	@Override
	public List<UserPlanProduct> getUserPlanProductsNeedTransfer(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getUserPlanProductsNeedTransfer", params);
	}

	@Override
	public UserPlanProduct getUserPlanProductByZqId(int zqId) {
		Map<String, Object> params = new HashMap<>();
		params.put("zqId", zqId);
		return sqlSession.selectOne(MAPPER + "getUserPlanProductByZqId", params);
	}

	@Override
	public int updateUserPlanProduct(UserPlanProduct userPlanProduct) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanProductId", userPlanProduct.getUserPlanProductId());
		params.put("returnedAmount", userPlanProduct.getReturnedAmount());
		return sqlSession.update(MAPPER + "updateUserPlanProduct", userPlanProduct);
	}

	@Override
	public Map<String,Object> getTotalReturnAmout(int userPlanId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectOne(MAPPER + "getTotalReturnAmout", params);
	}

	@Override
	public Map<String,Object> getTotalnAmoutParam(int userPlanId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectOne(MAPPER + "getTotalnAmoutParam", params);
	}

	@Override
	public UserPlanRepayment getUserPlanRepaymentByUserPlanProductId(int userPlanProductId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanProductId", userPlanProductId);
		return sqlSession.selectOne(MAPPER + "getUserPlanRepaymentByUserPlanProductId", params);
	}

	@Override
	public int addUserPlanRepayment(UserPlanRepayment repayment) {
		return sqlSession.insert(MAPPER + "addUserPlanRepayment", repayment);
	}

	@Override
	public UserPlan lockUserPlanById(int userPlanId,int userPlanStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("userPlanStatus", userPlanStatus);
		return sqlSession.selectOne(MAPPER + "lockUserPlanById", params);
	}

	@Override
	public UserPlan getUserPlanInfo(int userPlanId, int userPlanStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("userPlanStatus", userPlanStatus);
		return sqlSession.selectOne(MAPPER + "getUserPlanInfo", params);
	}

	@Override
	public int updateUserPlan(int userPlanId, int userPlanStatus, Date exiteTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("userPlanStatus", userPlanStatus);
		params.put("exiteTime", exiteTime);
		return sqlSession.update(MAPPER + "updateUserPlan", params);
	}

	@Override
	public List<UserPlan> getUserQuitPlans(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getUserQuitPlans", params);
	}

	@Override
	public List<UserPlanProduct> getUserPlanProductsNeedPfBuy(int limit, VersionTypeEnum versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("limit", limit);
		params.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		return sqlSession.selectList(MAPPER + "getUserPlanProductsNeedPfBuy", params);
	}

	@Override
	public List<UserPlan> getUserPlansInQuit(Integer limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getUserPlansInQuit", params);
	}

	@Override
	public int saveSettlementRecord(Integer userPlanId, Integer t6102_f01) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("t6102_f01", t6102_f01);
		return sqlSession.insert(MAPPER + "saveSettlementRecord", params);
	}

	@Override
	public Map<String, Object> getUserPlanSettlementAmout(int userPlanId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectOne(MAPPER + "getUserPlanSettlementAmout", params);
	}


	@Override
	public int updateInvestPlanInfo(int planId, int planStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		params.put("planStatus", planStatus);
		return sqlSession.update(MAPPER + "updateInvestPlanInfo", params);
	}

	@Override
	public int updateInvestPlan(int investPlanId, int investPlanStatus, Date settleTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("investPlanId", investPlanId);
		params.put("investPlanStatus", investPlanStatus);
		params.put("settleTime", settleTime);
		return sqlSession.update(MAPPER + "updateInvestPlan", params);
	}

	@Override
	public Map<String, Object> getYqAmout(int userPlanId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectOne(MAPPER + "getYqAmout", params);
	}

	@Override
	public int updateUserPlanRepay(int userPlanId, String repayState, Date repayTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("repayState", repayState);
		params.put("repayTime", repayTime);
		return sqlSession.update(MAPPER + "updateUserPlanRepay", params);
	}

	@Override
	public Map<String, Object> getExpireTimeBeforeReturnAmount(Integer userPlanId, Date expireTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("expireTime", expireTime);
		return sqlSession.selectOne(MAPPER + "getExpireTimeBeforeReturnAmount", params);
	}

	@Override
	public List<Map<String, Object>> getExpireTimeAfterReturnAmount(Integer userPlanId, Date expireTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		params.put("expireTime", expireTime);
		return sqlSession.selectList(MAPPER + "getExpireTimeAfterReturnAmount", params);
	}

	@Override
	public BigDecimal getRateManageRatioByPlanId(Integer planId) {
		Map<String, String> params = new HashMap<>(1);
		params.put("planId", "" + planId);
		return sqlSession.selectOne(MAPPER + "getRateManageRatioByPlanId", params);
	}

	@Override
	public List<Integer> getTargetCreditId(int userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return sqlSession.selectList(MAPPER + "getTargetCreditId", params);
	}

	@Override
	public List<Integer> getInCreditId(int userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return sqlSession.selectList(MAPPER + "getInCreditId", params);
	}
}
