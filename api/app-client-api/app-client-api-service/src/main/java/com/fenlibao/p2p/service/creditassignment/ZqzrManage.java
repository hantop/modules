package com.fenlibao.p2p.service.creditassignment;

import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO_131;
import com.fenlibao.p2p.util.Pager;

import java.sql.Timestamp;
import java.util.List;

public abstract interface ZqzrManage{
	

	/**
	 * 转让债权
	 * @throws Throwable
	 */
	public Timestamp transfer(AddTransfer addTransfer)throws Throwable;
	
	/**
	 * 债权转让申请列表
	 * @param status  申请状态
	 * @return
	 */
	public Pager<Zqzrlb> applyforList(String status,int curpage,int limit,String isTransfer,String timestamp);
	
	/**
	 * 债权转让申请详情
	 * @param status      申请状态
	 * @param applyforId  申请债权转让ID
	 * @return
	 */
	public ApplyforDetailVO applyforDetail(String status,int applyforId,String isTransfer);
	
	/**
	 * 债权转让申请次数
	 * @param creditId
	 * @return
	 */
	public int getCreditassignmentCount(int creditId);
	
	/**
	 * 债权转让成功记录
	 * @param applyforId  债权转让申请ID
	 * @return
	 */
	public int getRecordCount(int applyforId);

	/**
	 * 债权转让申请详情
	 * @param status      申请状态
	 * @param applyforId  申请债权转让ID
	 * @return
	 * @throws Exception 
	 */
	public ApplyforDetailVO_131 applyforDetail_131(String status,int applyforId,String isTransfer) throws Exception;


	/**
	 * 到期的投资计划(存在用户计划持有中未处理)
	 * @throws Throwable
	 */
	 List<InvestPlanInfo> getExpirePlans(int limit);
	/**
	 * 到期的用户投资计划
	 * @throws Throwable
	 */
	List<UserPlan> getExpireUserPlans(int planId,int status);

	/**
	 * 更新投资计划
	 * @throws Throwable
	 */
	int updateInvestPlanInfo(int planId,int planStatus);

	/**
	 * 转让用户投资计划关联的债权，并生成还款计划
	 * isAuto : 是否自动让出，用户申请退出是false
	 * @throws Throwable
	 */
	void doTansferAndRepayPlan(UserPlan userPlan,Boolean isAuto) throws Throwable;

	/**
	 * 计划债权申请转出
	 */
	void planProductTransfer(int userId,int ProductId) throws Throwable ;

	/**
	 * 用户申请提前退出计划列表
	 */
	List<UserPlan> getUserQuitPlans(int limit);

	/**
	 * 用户计划退出 平台回购债权
	 */
	List<UserPlanProduct> getUserPlanProductsNeedPfBuy(int limit, VersionTypeEnum versionTypeEnum);

	/**
	 * 用户计划退出中列表
	 */
	List<UserPlan> getUserPlansInQuit(Integer limit);


	/**
	 * 用户计划退出 结算
	 */
	void doSettlementPlan(UserPlan userPlan) throws Exception;

	/**
	 * 获取目标用户未还债权
	 * @param userId
	 * @return
	 */
	List<Integer> getTargetCreditId(int userId);

	/**
	 * 获取目标用户未还债权
	 * @param userId
	 * @return
	 */
	List<Integer> getInCreditId(int userId);
}
