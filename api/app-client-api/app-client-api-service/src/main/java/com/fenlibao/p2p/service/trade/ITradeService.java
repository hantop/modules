package com.fenlibao.p2p.service.trade;

import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.pay.ThirdPartyAgreement;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITradeService {


	/**
	 * 交易记录列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @return
	 */
	public List<TradeRecordForm> getRecordList(int userId,Integer pageNo,Integer pagesize,VersionTypeEnum vte);
	
	/**
	 * 收益记录
	 * @param userId
	 * @return
	 */
	public List<EarningsRecordForm> getEarningsList(int userId);
	
	/**
	 * 重置交易密码
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public int resetPassword(int userId, String newPassword) throws Exception;
	
	/**
	 * 开通/关闭免交易密码接口 
	 * @param userId
	 * @param isOpen （0==没有开通，1==开通）
	 * @return isOpen
	 * @throws Exception
	 */
	public int switchNoPassword(int userId, int isOpen) throws Exception;
	
	/**
	 * 获取交易密码
	 * @param userId
	 * @return
	 */
	String getTradePassword(int userId);
	
	/**
	 * 保存连连支付充值成功后返回的协议后，若存在则不做操作
	 * @param userId
	 * @param noAgree (no_agree)协议号
	 * @throws Exception
	 */
	int saveNoAgree(int userId, String noAgree) throws Exception;
	
	/**
	 * 获取协议
	 * @param userId
	 * @return 协议号（no_agree）
	 */
	ThirdPartyAgreement getNoAgree(int userId);
	
	/**
	 * 待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getDueInAmount(int userId);
	
	/**
	 * 获取每个用户每个债权当前期的收益
	 * @throws Exception
	 */
	List<EarningsForm> getProspectiveEarnings() throws Exception;
	
	/**
	 * 统计每个用户每个标的每天收益
	 * @param yesterday
	 * @param entity
	 * @param days
	 * @param total
	 * @param BeforeZQEarnings
	 * @param type
	 * @param e
	 * @throws Exception
	 */
	void countDayEarnings(Date yesterday, DayEarningsEntity entity, BigDecimal days,
			BigDecimal total, BigDecimal BeforeZQEarnings, int type, EarningsForm e) throws Exception;
	
	/**
	 * 收益记录（预期收益、历史收益）
	 * @param userId
	 * @return
	 */
	Map<String, Object> getEarningsRecordList(int userId);
	
	/**
	 * 获取昨日收益
	 * @param userId
	 * @param type 收益类型
	 * @return
	 */
	BigDecimal getYesterdayEarnings(int userId, int type);
	
	/**
	 * 获取累计收益（历史收益+预期收益）
	 * <p>此预期收益从日收益表获取，动态变化
	 * @param userId
	 * @return
	 */
	BigDecimal getAccumulativeEarnings(int userId);

	
	/**
	 * 获取指定时间段内用户投标记录统计
	 * @param startDate  开始日期(包含)
	 * @param endDate    截至日期(包含)
	 * @param limit      条数
	 * @return
	 */
	List<TenderRecords> getUserTenderRecords(Date startDate,Date endDate,int limit);
	
	/**
	 * 用户在指定时间内的投标总额
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	double getUserTenderTotal(Date startDate,Date endDate,int userId);

	/**
	 * 是否已经设置交易密码
	 * @param userId
	 * @return
	 */
	boolean hasSetPwd(Integer userId);

	/**
	 * @param accountId 用户userId
	 * @param tradePassword 加密后的交易密码
	 * @return
	 * @throws Exception
	 */
	boolean isValidUserPwd(int accountId, String tradePassword)
			throws Exception;
	
	void validateAuth(int userId) throws Exception;
	
	/**
	 * 获取用户昨天收益总和
	 * @param userId
	 * @return
	 */
	BigDecimal getEarningsYesterdaySum(Integer userId);

	BigDecimal getArrivalEarningsByCreditId(Integer creditId);
	/**
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
	 */
	BigDecimal getYHGains(String userId);

	/**
	 * 计划投资记录列表
	 * @param planId
	 * @param pageNo
	 * @param pageSize
     * @return
     */
    List<PlanRecords> getPlanRecordsList(Integer planId,Integer pageNo,Integer pageSize);

	/**
	 * 我的计划投资对应标记录
	 * @param planRecordId
     * @return
     */
	List<BidRecords> getBidRecordsList(Integer planRecordId);

	/**
	 * 用户的投资列表（计划列表）
	 * @param userId
	 * @param isUp
	 * @param timestamp
     * @return
     */
	List<InvestInfo> getUserPlanList(int userId, int isUp, String timestamp);

    PlanFinacing getUserPlanDetail(int userId,Integer planRecordId);

	/**
	 * 判断是否已经购买计划
	 *
	 * @param userId
	 * @return
	 */
	int getpurchasedPlan(int userId,int planId);

	/**
	 * 获取用户最后投资计划的记录
	 * @param userId
	 * @param planId
	 * @return
	 */
    PlanFinacing getUserPlanDetailLast(int userId,int planId);

	/**
	 * 获取计划已获收益
	 * @param planRecordId
	 * @return
     */
	List<PlanBidProfit> getPlanProfit(int planRecordId);

	/**
	 * 获取计划加息券利率
	 * @param planRecordId
	 * @return
	 */
	List<PlanCreditInfo> getPlanInterestRise(int planRecordId);


	/**
	 * @return
	 */
	Map getInvestmentExt(int zqId);

	/**
	 * 用户投资计划详情 3.2版本
	 * @param userId
	 * @param planRecordId
     * @return
     */
	PlanFinacing getUserPlanRecord(int userId,Integer planRecordId);

	/**
	 *	获取 我的投资列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @param status 状态(1:持有中,2:退出中,3:已回款)
	 * @return
	 */
	List<InvestInfo> getUserProjectList(int userId, int isUp, String timestamp, String status);
	List<InvestInfo> getUserProjectList(int userId, int isUp, String timestamp, String status,int versionTypeEnum);

	/**
	 * 我的计划投资对应标记录
	 * @param recordId
	 * @return
	 */
    List<BidRecords> getNewBidRecordsList(int recordId);

	/**
	 * 计划加息券利率
	 * @param planRecordId
	 * @return
	 */
	double getNewPlanCouponRise(int planRecordId);

	/**
	 * 获取原有计划的信息
	 * @param creditId
	 * @return
	 */
    Map getOldPlanExt(int creditId);

	/**
	 * 获取用户投资的最后一条 新计划的记录
	 * @param userId
	 * @param planId
	 * @return
	 */
    PlanFinacing getNewPlanDetailLast(int userId, int planId);

	/**
	 * 计划投资记录列表
	 * @param planId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<PlanRecords> getNewRecordsList(int planId, Integer pageNo, Integer pageSize);

	/**
	 * 获取新计划的结清收益-自然结清
	 * @param creditId
	 * @return
	 */
	BigDecimal getNewPlanProfit(int creditId);

	/**
	 * 判断是否已经购买计划(3.2.0版本)
	 *
	 * @param userId
	 * @return
	 */
	int getpurchasedNewPlan(int userId,int planId);

	/**
	 *  不包括计划产品库
	 *  区分存管
	 * 待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getNewDueInAmount(int userId,int cgNum);

	/**
	 *
	 * 用户待收金额（本息）
	 * 区分存管
	 * @param userId
	 * @return
	 */
	DueInAmount getPlanDueInAmount(int userId,int cgNum);

	/** 不包含计划产品库
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
	 */
	BigDecimal getNewYHGains(String userId,int cgNum);

	/**
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
	 */
	BigDecimal getPlanYHGains(String userId,int cgNum);

	/**
	 * 老计划分享信息
	 * @param userId
	 * @param shareId
	 * @return
	 */
	InvestShareVO getTenderIdOldPlan(int userId, int shareId);

	/**
	 * 新计划分享信息
	 * @param userId
	 * @param shareId
	 * @return
	 */
	InvestShareVO getTenderIdNewPlan(int userId, int shareId);

	/**
	 * 更新往来账户，锁定账户余额，新增资金流水
	 * @param userId
	 * @param cashAmount
	 *  @param cashAmount 1:从往来账户转入到锁定账户 2:从锁定账户账户到往来账户
     */

	void updateUserAccountInfo(int userId, BigDecimal cashAmount,int type)throws Exception;

	/**
	 * 获取存管编号
	 * @param userId
	 * @param userRole
     * @return
     */
    String getPlatformNo(int userId,String userRole);

	/**
	 * 新增一键资金转移操作记录
	 * @param userId
	 * @param platformUserNo
	 * @param amount
	 * @return
	 */
    int addTransferApplication(int userId, String platformUserNo, BigDecimal amount, String status);

	/**
	 * 查询代充值结果
	 * @param requestId
	 * @return
	 */
	Boolean getResultOfXWRequest(int requestId);

	/**
	 * 更新资金迁移状态
	 * @param transferApplicationId
	 * @return
	 */
	int updateTransferApplication(int transferApplicationId, String status);

	void sendMessage();

}
