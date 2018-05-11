package com.fenlibao.p2p.dao.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.UnusualRepay;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/13.
 */
@Repository
public class SysRepayDaoImpl implements SysRepayDao{
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysRepayMapper.";

    @Override
    public Integer getCurrentTerm(Integer projectId) {
        return sqlSession.selectOne(MAPPER+"getCurrentTerm",projectId);
    }

    @Override
    public List<XWRepaymentPlan> getRepaymentPlan(Map<String, Object> params) {
        return sqlSession.selectList(MAPPER+"getRepaymentPlan",params);
    }

    @Override
    public XWRepaymentPlan getRepaymentPlanByUniqueKey(Map<String, Object> params) {
        return sqlSession.selectOne(MAPPER+"getRepaymentPlanByUniqueKey",params);
    }

    @Override
    public BigDecimal getRemainPrincipalOfCredit(Map<String, Object> params) {
        return sqlSession.selectOne(MAPPER+"getRemainPrincipalOfCredit",params);
    }

    @Override
    public void updateRepaymentPlanById(Map<String,Object> params) {
        sqlSession.update(MAPPER+"updateRepaymentPlanById",params);
    }

    @Override
    public void finishRepaymentPlanOfCredit(Map<String, Object> params) {
        sqlSession.update(MAPPER+"finishRepaymentPlanOfCredit",params);
    }

    @Override
    public void updateInterestOfRemainTerms(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateInterestOfRemainTerms",params);
    }

    @Override
    public void batchInsertCreditRepayDetail(List<SysCreditToRepay> list) {
        Map<String, Object> params = new HashMap<>();
        params.put("list", list);
        sqlSession.insert(MAPPER+"batchInsertCreditRepayDetail", params);
    }

    @Override
    public void updateCreditRepayDetail(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateCreditRepayDetail",params);
    }

    @Override
    public void createProjectRepayInfo(SysProjectRepayInfo projectRepayInfo) {
        sqlSession.insert(MAPPER+"createProjectRepayInfo",projectRepayInfo);
    }

    @Override
    public void updateProjectRepayInfo(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateProjectRepayInfo",params);
    }

    @Override
    public SysCreditToRepay getCreditRepayDetailByRepayRequestNo(String repayRequestNo) {
        return sqlSession.selectOne(MAPPER+"getCreditRepayDetailByRepayRequestNo",repayRequestNo);
    }

    @Override
    public SysCreditToRepay getCreditRepayDetailByMarketingRequestNo(String marketingRequestNo) {
        return sqlSession.selectOne(MAPPER+"getCreditRepayDetailByMarketingRequestNo",marketingRequestNo);
    }

    @Override
    public List<SysCreditToRepay> getCreditRepayDetailByOrderId(Integer orderId) {
        return sqlSession.selectList(MAPPER+"getCreditRepayDetailByOrderId",orderId);
    }

    @Override
    public SysProjectRepayInfo getProjectRepayInfoByRequestNo(String requestNo) {
        return sqlSession.selectOne(MAPPER+"getProjectRepayInfoByRequestNo",requestNo);
    }

    @Override
    public SysProjectRepayInfo getProjectRepayInfoByOrderId(Integer orderId) {
        return sqlSession.selectOne(MAPPER+"getProjectRepayInfoByOrderId",orderId);
    }

    @Override
    public List<String> getAbnormalCreditRepays(Integer orderId) {
        return sqlSession.selectList(MAPPER+"getAbnormalCreditRepays",orderId);
    }

    @Override
    public List<String> getAbnormalCreditMarketings(Integer orderId) {
        return sqlSession.selectList(MAPPER+"getAbnormalCreditMarketings",orderId);
    }

    @Override
    public List<SysCreditToRepay> getCreditRepayAcceptFailList(Integer orderId) {
        return sqlSession.selectList(MAPPER+"getCreditRepayAcceptFailList",orderId);
    }

    @Override
    public List<SysCreditToRepay> getCreditMarketingAcceptFailList(Integer orderId) {
        return sqlSession.selectList(MAPPER+"getCreditMarketingAcceptFailList",orderId);
    }

    @Override
    public List<XWRequest> getRepayResultConfirmList() {
        return sqlSession.selectList(MAPPER+"getRepayResultConfirmList");
    }

    @Override
    public List<XWRequest> getRepayRerunList() {
        return sqlSession.selectList(MAPPER+"getRepayRerunList");
    }

    @Override
    public List<XWRequest> getRepayMarketingResultConfirmList() {
        return sqlSession.selectList(MAPPER+"getRepayMarketingResultConfirmList");
    }

    @Override
    public List<XWRequest> getRepayMarketingRerunList() {
        return sqlSession.selectList(MAPPER+"getRepayMarketingRerunList");
    }

    @Override
    public List<Integer> getAutoRepayProjectList() {
        return sqlSession.selectList(MAPPER+"getAutoRepayProjectList");
    }

    @Override
    public List<Integer> getSealBidUsers() {
        return sqlSession.selectList(MAPPER+"getSealBidUsers");
    }

    @Override
    public Integer planHasProjectNotYetRepay(Integer projectId) {
        return sqlSession.selectOne(MAPPER+"planHasProjectNotYetRepay",projectId);
    }

    @Override
    public List<SysProjectRepayInfo> getOrderNotFinishRepayRecords() {
        return sqlSession.selectList(MAPPER+"getOrderNotFinishRepayRecords");
    }

    @Override
    public List<Integer> getProjectNotFinishRepayRecords() {
        return sqlSession.selectList(MAPPER+"getProjectNotFinishRepayRecords");
    }

    @Override
    public List<Integer> getNotFinishRepayPlan() {
        return sqlSession.selectList(MAPPER+"getNotFinishRepayPlan");
    }

    @Override
    public void insertRepayMsgFlag(Integer orderId) {
        sqlSession.insert(MAPPER+"insertRepayMsgFlag",orderId);
    }

    @Override
    public Integer getRepayMsgFlagByOrderId(Integer orderId) {
        return sqlSession.selectOne(MAPPER+"getRepayMsgFlagByOrderId",orderId);
    }

    @Override
    public void updateRepayMsgFlagByOrderId(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateRepayMsgFlagByOrderId",params);
    }

    @Override
    public List<UnusualRepay> getUnusualRepays() {
        return sqlSession.selectList(MAPPER + "getUnusualRepays");
    }

    @Override
    public int updateUnusualRepay(UnusualRepay unusualRepay,Boolean preStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", unusualRepay.getId());
        params.put("status", unusualRepay.getStatus());
        params.put("preStatus", preStatus);
        return sqlSession.update(MAPPER + "updateUnusualRepay", params);
    }

    @Override
    public int addUnulsualRepay(UnusualRepay unusualRepay) {
        return sqlSession.insert(MAPPER + "addUnulsualRepay", unusualRepay);
    }

    @Override
    public UnusualRepay getUnusualRepayByOrder(Integer orderId) {
        return sqlSession.selectOne(MAPPER + "getUnusualRepayByOrder",orderId);
    }

    @Override
    public void updateRepaymentPlanByCredit(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateRepaymentPlanByCredit",params);
    }

    @Override
    public void updateRepaymentPlanByIds(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateRepaymentPlanByIds",params);

    }
}
