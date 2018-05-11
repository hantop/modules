package com.fenlibao.p2p.dao.trade;

import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.bid.BidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.pay.ThirdPartyAgreement;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITradeDao {

	/**
	 * 交易记录列表
	 * @param params
	 * @return
	 */
	public List<TradeRecordForm> getRecordList(Map<String, Object> params);

	/**
	 * 根据表单类型获取相对的收益记录
	 * @param params
	 * @return
	 */
	public BigDecimal getEarningsList(Map<String, Object> params);


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
	 * @param noAgree (no_agree)
	 * @throws Exception
	 */
	int saveNoAgree(Map<String, Object> params) throws Exception;
	
	int updateNoAgree(Map<String, Object> params) throws Exception;

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
	 * 记录用户每天每个标的收益
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int insertDayEarnings(DayEarningsEntity entity) throws Exception;

	/**
	 * 获取用户的预期收益或历史收益
	 * @param userId
	 * @param types
	 * @return
	 */
	List<EarningsRecordForm> getEarningsList(Integer userId, String status, Integer[] feeTypes);

	/**
	 * 判断用户的某个标的每天收益是否存在
	 * @param userId
	 * @param bidId
	 * @param earningsDate
	 * @return
	 */
	boolean existEarningsRecord(Integer userId, Integer ZQ_id, Date earningsDate);

	/**
	 * 获取某个债权的历史收益(日收益表)
	 * @param ZQ_id
	 * @param userId
	 * @return
	 */
	BigDecimal getHistoryTotalEarnings(Integer ZQ_id);

	/**
	 * 获取昨日收益
	 * @param userId
	 * @param type 收益类型
	 * @return
	 */
	BigDecimal getYesterdayEarnings(int userId, int type);

	/**
	 * 获取用户在日收益表未到账的总收益
	 * @param userId
	 * @return
	 */
	List<EarningsRecordForm> getExpectDayEarnings(int userId);

	/**
	 * 判断是否已初始化已到账收益
	 * @return
	 */
	boolean isExistArrivalEarnings();

	/**
	 * 获取用户的总已到账收益
	 * @return
	 */
	List<EarningsRecordForm> getTotalArrivalEarnings(Integer userId);

	/**
	 * 获取转让前的债权ID
	 * @param nowZQ_id 现在债权ID
	 * @return
	 */
	Integer getBeforeZQ_id(int nowZQ_id);

	/**
	 * 用户的投标记录（t6250）
	 * @param map
	 * @return
	 */
	List<TenderRecords> getUserTenderRecords(Map<String,Object> map);

	/**
	 * 用户在指定时间内的投标总额 （t6250）
	 * @param map
	 * @return
	 */
	double getUserTenderTotal(Map<String,Object> map);

	/**
	 * 是否已经设置交易密码
	 * @param userId
	 * @return
	 */
	boolean hasSetPwd(Integer userId);

	/**
	 * 获取用户昨天收益总和
	 * @param userId
	 * @return
	 */
	BigDecimal getEarningsYesterdaySum(Integer userId);

	/**
	 * 获取其他收益（提前还款违约金、逾期罚息）
	 * @param userId
	 * @param type YH(已还)、WH(未还)
	 * @return
	 */
	List<EarningsForm> getOtherInterest(int userId, Status status);

	/**
	 * 获取债权的已到账收益
	 * @param creditId
	 * @return
     */
	BigDecimal getArrivalEarningsByCreditId(Integer creditId);

	/**
	 * 获取分期债权的计息开始时间
	 * @param jx_endTime
	 * @param ZQ_id
	 * @return
	 */
	Date getJxStartTime(Date jx_endTime, Integer ZQ_id);

	/**
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
     */
	BigDecimal getYHGains(String userId);

	/**
	 * 获取用户当日交易密码输入错误次数
	 * @param userId
	 * @return
     */
	int getTradePwdWrongCount(int userId);

	/**
	 * 更新用户交易密码输入错误次数
	 * @param userId
	 * @param isReset
     */
	void updateTradePwdWrongCount(int userId, boolean isReset);

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
	 * 用户的投资计划列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
     * @return
     */
	List<InvestInfo> getUserPlanList(int userId, int isUp, Date timestamp);

	PlanFinacing getUserPlanDetail(int userId,Integer planRecordId);

	/**
	 * 获取计划可投标
	 * @param planId
	 * @return
     */
	List<BidInfoForPlan> getPlanSurplusBidList (int planId, boolean isCheckMinAmount);

	/**
	 * 添加计划投资记录
	 * @param planId
	 * @param accountId
	 * @return
	 */
	int addPlanRecord(int planId, int accountId);

	/**
	 * 加锁
	 */
	void lockPlan(int planId);

	/**
	 * 加锁
	 */
	void lockInvestPlan(int investPlanId);

	/**
	 * 添加计划具体标投资记录
	 * @param planRecordId
	 * @param bidRecordId
     * @return
     */
	int addPlanBidRecord(int planRecordId, int bidRecordId);

	/**
	 * 更新计划投资的金额和已投金额
	 * @param planRecordId
	 * @return
     */
	void updatePlanRecordAmount(int planRecordId);

	/**
	 * 获取获取计划投资金额
	 * @param planRecordId
	 * @return
	 */
	BigDecimal getPlanRecordAmount(int planRecordId);

	/**
	 * 获取计划标题
	 * @param planId
	 * @return
     */
	String getPlanTitle(int planId);

	/**
	 * 获取计划
	 * @param planId
	 * @return
	 */
	Plan getPlan(int planId);

	/**
	 * 获取计划
	 * @param planId id
	 * @param isLock 是否锁记录 1加锁 2不锁
     * @return
     */
	InvestPlan getInvestPlan(int planId, int isLock);

	/**
	 * 通过用户投资记录获取计划
	 * @param userPlanId
	 * @return
     */
	InvestPlan getInvestPlanByUserPlanId(int userPlanId);

	/**
	 * 更新计划状态
	 * @param planId
     */
	void updatePlanStatus(int planId);

	/**
	 * 更新计划状态
	 * @param planId
     */
	int updateInvestPlanStatus(int planId);

	/**
	 * 判断是否已经购买计划
	 * @param map
	 * @return
	 */
	int getpurchasedPlan(Map<String, Object> map);

    PlanFinacing getUserPlanDetailLast(int userId, int planId);

	List<PlanBidProfit> getPlanProfit(Map<String, Object> map);

	/**
	 * 获取数据库当前时间
	 */
	Timestamp getDBCurrentTime();


	List<PlanCreditInfo> getPlanInterestRise(Map<String, Object> map);

	/**
	 * @return
	 */
	Map getInvestmentExt(int zqId);

	/**
	 * 用户投资计划 3.2版本
	 * @param userId
	 * @param planRecordId
     * @return
     */
	PlanFinacing getUserPlanRecord(int userId,Integer planRecordId);


	/**
	 * 我的月升计划投资对应标记录
	 * @param recordId
	 * @return
	 */
    List<BidRecords> getNewBidRecordsList(int recordId);

	/**
	 * 我的月升计划投资对应债权记录
	 * @param recordId
	 * @return
	 */
	List<BidRecords> getNewCreditList(int recordId);

	/**
	 * 计划加息券利率
	 * @param planRecordId
     * @return
     */
	double getNewPlanCouponRise(int planRecordId);

	/**
	 * 持有中的标 计划
	 * @param userId
	 * @param isUp
	 * @param time
	 * @return
	 */
    List<InvestInfo> getHoldPlanBid(int userId, int isUp, Date time);
	List<InvestInfo> getHoldPlanBid(int userId, int isUp, Date time,int versionTypeEnum);

	/**
	 * 退出中的标 计划
	 * @param userId
	 * @param isUp
	 * @param time
	 * @return
	 */
	List<InvestInfo> getQuitPlanBid(int userId, int isUp, Date time);
	List<InvestInfo> getQuitPlanBid(int userId, int isUp, Date time,int versionTypeEnum);

	/**
	 * 已回款的标 计划
	 * @param userId
	 * @param isUp
	 * @param time
	 * @return
	 */
	List<InvestInfo> getProfitPlanBid(int userId, int isUp, Date time);
	List<InvestInfo> getProfitPlanBid(int userId, int isUp, Date time,int versionTypeEnum);

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
	 * 计划的投资记录
	 * @param planId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    List<PlanRecords> getNewRecordsList(int planId, Integer pageNo, Integer pageSize);

	/**
	 * 获取自然结清的新计划收益
	 * @param creditId
	 * @return
	 */
	BigDecimal getNewPlanProfit(int creditId);

	/**
	 * 判断是否已经购买计划(3.2.0版本)
	 * @param map
	 * @return
	 */
	int getpurchasedNewPlan(Map<String, Object> map);

	/**
	 *
	 * 待收金额（本息）
	 * 区分存管
	 * @param userId
	 * @return
	 */
	DueInAmount getNewDueInAmount(int userId,int cgNum);

	/**
	 * 3.2.0版本适用
	 * 计划待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getPlanDueInAmount(int userId,int cgNum);

	/**
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


	InvestShareVO getTenderIdOldPlan(int userId, int shareId);

	InvestShareVO getTenderIdNewPlan(int userId, int shareId);

	/**
	 * @Title: updateUserFundsAccountAmount
	 * @Description: 更新用户资金账户信息
	 * @param userId
	 * @param accountAmount
	 * @return
	 * @return: int
	 */
	public int updateUserFundsAccountAmount(int userId, BigDecimal accountAmount,String accountType);

	/**
	 * 获取账户余额
	 * @param userId
	 * @param accountType
	 * @return
	 */
	UserAccount getUserBalance(int userId, String accountType);

	/**
	 * @Title: addT6102Record
	 * @Description: 添加资金流水记录
	 * @param zcwlzhId
	 * @param FeeCode
	 * @param zrwlzhId
	 * @param nowDatetime
	 * @param zrcashAmount
	 * @param zccashAmount
	 * @param balanceAmount
	 * @param remark
	 * @return
	 * @return: int
	 */
	int addT6102Record(int zcwlzhId,int FeeCode,int zrwlzhId,Date nowDatetime,BigDecimal zrcashAmount,BigDecimal zccashAmount,BigDecimal balanceAmount, String remark);

	String getPlatformNo(int userId, String userRole);

	/**
	 * 一键资金转移记录
	 * @param userId
	 * @param platformUserNo
	 * @param amount
	 * @param status
	 * @return
	 */
	int addTransferApplication(int userId, String platformUserNo, BigDecimal amount, String status);

	String getRechargeState(int requestId);

	/**
	 * 更新一键资金转移状态
	 * @param transferApplicationId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	int updateTransferApplication(int transferApplicationId, String state);
}
