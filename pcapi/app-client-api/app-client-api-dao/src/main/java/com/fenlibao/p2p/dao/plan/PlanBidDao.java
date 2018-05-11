package com.fenlibao.p2p.dao.plan;

import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.plan.PlanBid;
import com.fenlibao.p2p.model.entity.plan.PlanType;
import com.fenlibao.p2p.model.entity.plan.TradeRecord;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;
import com.fenlibao.p2p.model.vo.plan.PlanProductVO;
import com.fenlibao.p2p.model.vo.plan.SysOrderVO;
import com.fenlibao.p2p.model.vo.plan.XwTenderVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/16 16:04.
 * @version 1.0
 */
public interface PlanBidDao {
    /**
     * 自动发布计划是否激活.
     * @param propertyKey
     * @return
     */
    Map<String,String> getEnableReleasePlanConfig(String propertyKey);

    /**
     * 获取启用的计划模板
     * @return
     */
    List<PlanType> getPlanTemplates();

    /**
     * 根据计划类型，天数，还款方式等获取状态为投标中的记录数
     * @param planType
     * @return
     */
    Integer getCountNewPlanBy(PlanType planType);

    /**
     * 根据计划的类型，天数，结清方式，获取标的总额
     * @param planType
     * @return
     */
    BigDecimal getBidSumAmountBy(Map<String, Object> planType);

    /**
     * 获取没有与计划相关联的标的
     * @param planType
     * @return
     */
    List<BidForPlanVO> getBidsForPlanBy(Map<String, Object> planType);

    /**
     * 添加计划
     * @param planType
     * @return
     */
    PlanType addInvestPlan(PlanType planType);

    /**
     * 更新绑定状态，binded=1:绑定、binded=0:不绑定
     * @param bindBids
     */
    void updateProductLib(Map<String, Object> bindBids);

    /**
     * 发布计划
     * @param planId
     */
    void releaseInvestPlan(Integer planId);

    /**
     * 获取该用户的逾期未还记录标数
     * @param accountId
     * @return
     */
    int getYqCount(int accountId);

    /**
     *
     * @param planId
     * @param isGetAllPlanBindBids true:获取计划绑定的所有处于投资投资中的标的、false:获取计划绑定且已被投资但未满的标的
     * @return
     */
    List<BidForPlanVO> getPlanBindBids(int planId, boolean isGetAllPlanBindBids);

    /**
     * 该计划中是否有绑定投资用户的借款标
     * @param planId
     * @param accountId
     * @return
     */
    BidForPlanVO thisUserBorrowBidInPlan(int planId, int accountId);

    /**
     * 锁标记录
     * @param bidId
     * @return
     */
    PlanBid lockBidById(Integer bidId);

    /**
     * 插入系统订单
     * @param bidOrderType
     * @param status
     * @param from
     * @param accountId
     * @return
     */
    Integer addSysOrder(int bidOrderType, String status, String from, int accountId);

    /**
     * 插入投机划订单
     * @param ordId
     * @param accountId
     * @param planId
     * @param investAmount
     */
    void addPlanOrder(int ordId, int accountId, Integer planId, BigDecimal investAmount);

    /**
     * 插入投标订单
     * @param ordId
     * @param accountId
     * @param bidId
     * @param investAmount
     */
    void addBidOrder(int ordId, int accountId, Integer bidId, BigDecimal investAmount);

    /**
     * 回填插入计划投资标的记录
     * @param map
     */
    void addUserPlanProduct(Map<String, Object> map);

    /**
     * 插入投标记录
     * @param investBidRecordMap
     * @return
     */
    Integer addInvestBidRecord(Map<String, Object> investBidRecordMap);

    /**
     * 更新标状态和可投金额
     * @param planBid
     */
    void updateBid(PlanBid planBid);

    /**
     * 设置标的满标时间为当前执行时间
     * @param bidId
     */
    void updateBidExt(Integer bidId);

    /**
     * 添加投标记录表记录
     * @param xwTenderRecordMap
     * @return
     */
    Integer addXwTenderRecord(Map<String, Object> xwTenderRecordMap);

    /**
     * 更新标状态和可投金额
     * @param updateBids
     */
    void updateBids(List<PlanBid> updateBids);

    /**
     * 设置标的满标时间为当前执行时间
     * @param updateBidFullTimes
     */
    void updateBidFullTimes(List<Integer> updateBidFullTimes);

    /**
     * 添加新网投标记录表记录
     * @param addXwTenders
     * @return
     */
    void addXwTenders(List<XwTenderVO> addXwTenders);

    /**
     * 回填计划配标记录
     * @param addPlanProducts
     */
    void addPlanProducts(List<PlanProductVO> addPlanProducts);

    /**
     * 添加购买计划异常订单日志
     * @param params
     */
    void addErrorLog(Map<String, String> params);

    void updatePlanOrder(Map<String, Object> params);

    /**
     * 根据订单Id锁订单
     * @param orderId
     * @return
     */
    SysOrderVO lockOrderById(int orderId);

    /**
     * 回填用户购买计划记录id到计划订单
     * @param params
     */
    void returnBackUserPlanId(Map<String, Object> params);

    /**
     * 根据code 获取标类型Id
     * @param code
     * @return
     */
    int getBidTypeByCode(String code);

    /**
     * 根据用户资金Id锁用户资金账号记录（单条）
     * @param accountId
     * @return
     */
    UserAccount lockUserAccountById(int accountId);

    void updateUserAmount(BigDecimal balance, int accountId);

    /**
     * 添加资金交易流水
     * @param tradeRecord
     */
    void addTradeRecord(TradeRecord tradeRecord);

    /**
     * 解绑计划绑定的标
     * @param planId
     */
    void unBindPlanBids(Integer planId);

    /**
     * 根据标id获取该标在新网的借款用户编号
     * @param bidId
     * @return
     */
    String getBidBorrowerUserNoBy(Integer bidId);
}
