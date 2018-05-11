package com.fenlibao.p2p.dao.plan.impl;

import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.plan.PlanBid;
import com.fenlibao.p2p.model.entity.plan.PlanType;
import com.fenlibao.p2p.model.entity.plan.TradeRecord;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;
import com.fenlibao.p2p.model.vo.plan.PlanProductVO;
import com.fenlibao.p2p.model.vo.plan.SysOrderVO;
import com.fenlibao.p2p.model.vo.plan.XwTenderVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/16 16:04.
 * @version 1.0
 */
@Repository
public class PlanBidDaoImpl implements PlanBidDao {

    private static final String MAPPER = "PlanBidMapper.";

    @Resource
    private SqlSession sqlSession;

    @Override
    public Map<String, String> getEnableReleasePlanConfig(String propertyKey) {
        return sqlSession.selectOne(MAPPER + "getEnableReleasePlanConfig", propertyKey);
    }

    @Override
    public List<PlanType> getPlanTemplates() {
        return sqlSession.selectList(MAPPER + "getPlanTemplates");
    }

    @Override
    public Integer getCountNewPlanBy(PlanType planType) {
        return sqlSession.selectOne(MAPPER + "getCountNewPlanBy", planType);
    }

    @Override
    public BigDecimal getBidSumAmountBy(Map<String, Object> params) {
        return sqlSession.selectOne(MAPPER + "getBidSumAmountBy", params);
    }

    @Override
    public List<BidForPlanVO> getBidsForPlanBy(Map<String, Object> params) {
        return sqlSession.selectList(MAPPER + "getBidsForPlanBy", params);
    }

    @Override
    public PlanType addInvestPlan(PlanType planType) {
        sqlSession.insert(MAPPER + "addInvestPlan", planType);
        return planType;
    }

    @Override
    public void updateProductLib(Map<String, Object> bindBids) {
        sqlSession.update(MAPPER + "updateProductLib", bindBids);
    }

    @Override
    public void releaseInvestPlan(Integer planId) {
        sqlSession.update(MAPPER + "releaseInvestPlan", planId);
    }

    @Override
    public int getYqCount(int accountId) {
        return sqlSession.selectOne(MAPPER + "getYqCount", accountId);
    }

    @Override
    public List<BidForPlanVO> getPlanBindBids(int planId, boolean isGetAllPlanBindBids) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        params.put("isGetAllPlanBindBids", isGetAllPlanBindBids);
        return sqlSession.selectList(MAPPER + "getPlanBindBids", params);
    }

    @Override
    public BidForPlanVO thisUserBorrowBidInPlan(int planId, int accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        params.put("userId", accountId);
        return sqlSession.selectOne(MAPPER + "thisUserBorrowBidInPlan", params);
    }

    @Override
    public PlanBid lockBidById(Integer bidId) {
        return sqlSession.selectOne(MAPPER + "lockBidById", bidId);
    }

    @Override
    public Integer addSysOrder(int bidOrderType, String status, String from, int accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("planOrderType", bidOrderType);
        params.put("status", status);
        params.put("orderFrom", from);
        params.put("accountId", accountId);
        sqlSession.insert(MAPPER + "addSysOrder", params);
        return new Long((Long) params.get("id")).intValue();
    }

    @Override
    public void addPlanOrder(int orderId, int accountId, Integer planId, BigDecimal investAmount) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("accountId", accountId);
        params.put("planId", planId);
        params.put("investAmount", investAmount);
        sqlSession.insert(MAPPER + "addPlanOrder", params);
    }

    @Override
    public void addBidOrder(int orderId, int accountId, Integer bidId, BigDecimal investAmount) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("accountId", accountId);
        params.put("bidId", bidId);
        params.put("investAmount", investAmount);
        sqlSession.insert(MAPPER + "addBidOrder", params);
    }

    @Override
    public void addUserPlanProduct(Map<String, Object> params) {
        sqlSession.insert(MAPPER + "addUserPlanProduct", params);
    }

    @Override
    public Integer addInvestBidRecord(Map<String, Object> params) {
        sqlSession.insert(MAPPER + "addInvestBidRecord", params);
        return new Long((Long) params.get("id")).intValue();
    }

    @Override
    public void updateBid(PlanBid planBid) {
        sqlSession.update(MAPPER + "updateBid", planBid);
    }

    @Override
    public void updateBidExt(Integer bidId) {
        sqlSession.update(MAPPER + "updateBidExt", bidId);
    }

    @Override
    public Integer addXwTenderRecord(Map<String, Object> params) {
        sqlSession.insert(MAPPER + "addXwTenderRecord", params);
        return new Long((Long) params.get("id")).intValue();
    }

    @Override
    public void updateBids(List<PlanBid> updateBids) {
        sqlSession.update(MAPPER + "updateBids", updateBids);
    }

    @Override
    public void updateBidFullTimes(List<Integer> updateBidFullTimes) {
        sqlSession.update(MAPPER + "updateBidFullTimes", updateBidFullTimes);
    }

    @Override
    public void addXwTenders(List<XwTenderVO> addXwTenders) {
        Map<String, List<XwTenderVO>> params = new HashMap<>();
        params.put("addXwTenders", addXwTenders);
        sqlSession.insert(MAPPER + "addXwTenders", params);
    }

    @Override
    public void addPlanProducts(List<PlanProductVO> addPlanProducts) {
        Map<String, List<PlanProductVO>> params = new HashMap<>();
        params.put("addPlanProducts", addPlanProducts);
        sqlSession.insert(MAPPER + "addPlanProducts", params);
    }

    @Override
    public void addErrorLog(Map<String, String> params) {
        sqlSession.insert(MAPPER + "addErrorLog", params);
    }

    @Override
    public void updatePlanOrder(Map<String, Object> params) {
        sqlSession.update(MAPPER + "updatePlanOrder", params);
    }

    @Override
    public SysOrderVO lockOrderById(int orderId) {
        return sqlSession.selectOne(MAPPER + "lockOrderById", orderId);
    }

    @Override
    public void returnBackUserPlanId(Map<String, Object> params) {
        sqlSession.update(MAPPER + "returnBackUserPlanId", params);
    }

    @Override
    public int getBidTypeByCode(String code) {
        return sqlSession.selectOne(MAPPER + "getBidTypeByCode", code);
    }

    @Override
    public UserAccount lockUserAccountById(int accountId) {
        return sqlSession.selectOne(MAPPER + "lockUserAccountById", accountId);
    }

    @Override
    public void updateUserAmount(BigDecimal balance, int accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("balance", balance);
        params.put("accountId", accountId);
        sqlSession.update(MAPPER + "updateUserAmount", params);
    }

    @Override
    public void addTradeRecord(TradeRecord tradeRecord) {
        sqlSession.insert(MAPPER + "addTradeRecord", tradeRecord);
    }

    @Override
    public void unBindPlanBids(Integer planId) {
        sqlSession.update(MAPPER + "unBindPlanBids", planId);
    }

    @Override
    public String getBidBorrowerUserNoBy(Integer bidId) {
        return sqlSession.selectOne(MAPPER + "getBidBorrowerUserNoBy", bidId);
    }
}
