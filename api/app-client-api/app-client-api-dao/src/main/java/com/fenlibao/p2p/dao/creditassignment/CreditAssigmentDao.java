package com.fenlibao.p2p.dao.creditassignment;

import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CreditAssigmentDao {

	/**
	 * 债权转让信息
	 * @param map
	 * @return
	 */
    public List<Zqzrlb> getCreditassignmentApplyforList(Map<String,Object> map);
    
    /**
     * 债权转让数量
     * @param map
     * @return
     */
    public int getCreditassignmentApplyforCount(Map<String,Object> map);
    
    /**
     * 债权转让申请次数
     * @param map
     * @return
     */
    public int getCreditassignmentCount(Map<String,Object> map);
    
    /**
     * 获取债权转让成功记录
     * @param map
     * @return
     */
    public int getRecord(Map<String,Object> map);

    /**
     * 获取指定用户指定标最新债权记录对应的投标记录
     * @param userId
     * @param bidId
     * @return
     */
    Integer getZqzrTenderRecord(String userId, String bidId);

    /**
     * 获取用户购买债权转让的金额
     * @param userId
     * @param bidId
     * @return
     */
    BigDecimal getPurchaseZqzrAmount(String userId, String bidId);

    /**
     * 查询用户债权转让需要的参数
     * @param userId
     * @param applyforId
     * @return
     */
    InvestShareVO getTenderIdLatest(int userId, int applyforId);

    /**
     * 获取到期计划-还款中投资计划
     */
    List<InvestPlanInfo> getExpirePlans(int limit);

    /**
     * 到期未处理的用户计划-持有中
     */
    List<UserPlan> getExpireUserPlans(int planId,int status);

    /**
     * 更新投资计划
     * @throws Throwable
     */
    int updateInvestPlanInfo(int planId,int planStatus);

    /**
     * 到期用户计划，关联的债权(存在未还)(准备加入债权库)
     */
    List<UserPlanProduct> getUserPlanProducts(int userPlanId);

    /**
     * 到期用户计划，生成过还款计划仍没有上架的债权
     */
    List<UserPlanProduct> getUserPlanProductsNeedTransfer(int limit);


    /**
     * 根据债权id取，用户计划投资记录（锁定）
     */
    UserPlanProduct getUserPlanProductByZqId(int zqId);

    /**
     * 更新用户计划投资记录
     */
    int updateUserPlanProduct(UserPlanProduct userPlanProduct);

    /**
     * 到期用户计划，关联的债权(总待还金额)
     */
    Map<String,Object> getTotalReturnAmout(int userPlanId);

    /**
     * 到期用户计划，关联的债权(总待还金额)
     */
    Map<String,Object> getTotalnAmoutParam(int userPlanId);

    /**
     * 到期用户计划，关联的债权(总待还金额)
     */
    UserPlanRepayment getUserPlanRepaymentByUserPlanProductId(int userPlanProductId);

    /**
     * 添加计划还款计划
     */
    int addUserPlanRepayment(UserPlanRepayment repayment);


    /**
     * 锁住用户计划
     */
    UserPlan lockUserPlanById(int userPlanId,int userPlanStatus);
    /**
     * 锁住用户计划
     */
    UserPlan getUserPlanInfo(int userPlanId,int userPlanStatus);

    /**
     * 更新用户计划
     */
    int updateUserPlan(int userPlanId,int userPlanStatus,Date exiteTime);

    /**
     * 用户申请提前退出计划列表
     * @throws Throwable
     */
    List<UserPlan> getUserQuitPlans(int limit);

    /**
     * 用户计划退出 平台回购债权
     * @throws Throwable
     */
    List<UserPlanProduct> getUserPlanProductsNeedPfBuy(int limit, VersionTypeEnum versionTypeEnum);

    /**
     * 用户计划退出中列表
     * @throws Throwable
     */
    List<UserPlan> getUserPlansInQuit(Integer limit);

    int saveSettlementRecord(Integer userPlanId,Integer t6102_f01);

    /**
     * 用户计划以结算金额
     */
    Map<String,Object> getUserPlanSettlementAmout(int userPlanId);

    /**
     * 更新计划结清
     */
    int updateInvestPlan(int investPlanId,int investPlanStatus,Date settleTime);

    /**
     * 逾期罚息
     */
    Map<String,Object> getYqAmout(int userPlanId);

    /**
     * 更新计划投资人回款明细 t_user_plan_repay
     */
    int updateUserPlanRepay(int userPlanId,String repayState,Date repayTime);

    /**
     * 获取计划到期时已经回款的本金
     * @param userPlanId
     * @param expireTime
     * @return
     */
    Map<String, Object> getExpireTimeBeforeReturnAmount(Integer userPlanId, Date expireTime);

    /**
     * 获取计划到期时，没有回款的本金记录（根据日期分组）
     * @param userPlanId
     * @param expireTime
     * @return
     */
    List<Map<String, Object>> getExpireTimeAfterReturnAmount(Integer userPlanId, Date expireTime);

    BigDecimal getRateManageRatioByPlanId(Integer planId);

    List<Integer> getTargetCreditId(int userId);

    List<Integer> getInCreditId(int userId);
}
