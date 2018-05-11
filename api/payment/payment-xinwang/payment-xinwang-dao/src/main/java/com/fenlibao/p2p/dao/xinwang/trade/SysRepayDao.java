package com.fenlibao.p2p.dao.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.UnusualRepay;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 还款
 */
public interface SysRepayDao {
    /**
     * 获取当前还款期号
     * @return
     */
    Integer getCurrentTerm(Integer projectId);

    /**
     * 获取还款计划
     * @return
     */
    List<XWRepaymentPlan> getRepaymentPlan(Map<String,Object> params);

    /**
     * 获取还款计划
     * @param params
     * @return
     */
    XWRepaymentPlan getRepaymentPlanByUniqueKey(Map<String,Object> params);

    /**
     * 获取剩余待还本金
     * @param params
     * @return
     */
    BigDecimal getRemainPrincipalOfCredit(Map<String,Object> params);

    /**
     * 更新还款计划
     * @param params
     */
    void updateRepaymentPlanById(Map<String,Object> params);



    /**
     * 更新还款计划多个id
     * @param params
     */
    void updateRepaymentPlanByIds(Map<String,Object> params);



    /**
     * 根据债权更新还款计划
     * @param params
     */
    void updateRepaymentPlanByCredit(Map<String,Object> params);

    /**
     * 更新还款计划
     * @param params
     */
    void finishRepaymentPlanOfCredit(Map<String,Object> params);

    /**
     * 提前还款，更新剩余期利息
     * @param params
     */
    void updateInterestOfRemainTerms(Map<String,Object> params);

    /**
     * 批量插入债权还款明细
     * @param list
     */
    void batchInsertCreditRepayDetail(List<SysCreditToRepay> list);

    /**
     * 更新债权还款明细
     * @param params
     */
    void updateCreditRepayDetail(Map<String,Object> params);

    /**
     * 创建标还款补充信息表
     * @param projectRepayInfo
     */
    void createProjectRepayInfo(SysProjectRepayInfo projectRepayInfo);

    /**
     * 更新标还款补充信息表
     * @param params
     */
    void updateProjectRepayInfo(Map<String,Object> params);

    /**
     * 获取债权还款明细
     * @param repayRequestNo
     * @return
     */
    SysCreditToRepay getCreditRepayDetailByRepayRequestNo(String repayRequestNo);

    /**
     * 获取债权还款明细
     * @param marketingRequestNo
     * @return
     */
    SysCreditToRepay getCreditRepayDetailByMarketingRequestNo(String marketingRequestNo);

    /**
     * 用整标还款订单id获取债权还款详情
     * @param orderId
     * @return
     */
    List<SysCreditToRepay> getCreditRepayDetailByOrderId(Integer orderId);

    /**
     * 用还款流水号获取所属整标还款订单信息
     * @param requestNo
     * @return
     */
    SysProjectRepayInfo getProjectRepayInfoByRequestNo(String requestNo);

    /**
     * 用整标还款订单id获取整标还款订单信息
     * @param orderId
     * @return
     */
    SysProjectRepayInfo getProjectRepayInfoByOrderId(Integer orderId);

    /**
     * 新网还款成功，但平台还款异常的借款人债权还款
     * @param orderId
     * @return
     */
    List<String> getAbnormalCreditRepays(Integer orderId);

    /**
     * 新网还款成功，但平台还款异常的平台债权加息还款
     * @param orderId
     * @return
     */
    List<String> getAbnormalCreditMarketings(Integer orderId);

    /**
     * 获取受理失败的借款人还款请求
     * @param orderId
     * @return
     */
    List<SysCreditToRepay> getCreditRepayAcceptFailList(Integer orderId);

    /**
     * 获取受理失败的平台加息请求
     * @param orderId
     * @return
     */
    List<SysCreditToRepay> getCreditMarketingAcceptFailList(Integer orderId);

    /**
     * 获取批次受理成功但没有收到回调的请求
     * @return
     */
    List<XWRequest> getRepayResultConfirmList();

    /**
     * 获取新网还款成功后平台没有还款的请求
     * @return
     */
    List<XWRequest> getRepayRerunList();

    /**
     * 获取批次受理成功但没有收到回调的请求
     * @return
     */
    List<XWRequest> getRepayMarketingResultConfirmList();

    /**
     * 获取新网还款加息成功后平台没有还加息的请求
     * @return
     */
    List<XWRequest> getRepayMarketingRerunList();

    /**
     * 获取自动还款列表
     * @return
     */
    List<Integer> getAutoRepayProjectList();

    /**
     * 获取封标用户
     * @return
     */
    List<Integer> getSealBidUsers();

    /**
     * 如果标所属计划已经全部结清，就返回计划id，否则返回null。如果标不属于任一个计划，也是返回null
     * @param projectId
     * @return
     */
    Integer planHasProjectNotYetRepay(Integer projectId);

    /**
     * 没有结束订单的还款
     * @return
     */
    List<SysProjectRepayInfo> getOrderNotFinishRepayRecords();

    /**
     * 剩余期数是0但还没改状态的标
     * @return
     */
    List<Integer> getProjectNotFinishRepayRecords();

    /**
     * 平台债权已经还完款，但还款计划还是WH的还款计划
     * @return
     */
    List<Integer> getNotFinishRepayPlan();

    void insertRepayMsgFlag(Integer orderId);

    Integer getRepayMsgFlagByOrderId(Integer orderId);

    void updateRepayMsgFlagByOrderId(Map<String,Object> params);

    List<UnusualRepay> getUnusualRepays();

    int updateUnusualRepay(UnusualRepay unusualRepay,Boolean preStatus);

    int addUnulsualRepay(UnusualRepay unusualRepay);

    UnusualRepay getUnusualRepayByOrder(Integer orderId);
}
