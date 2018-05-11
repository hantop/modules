package com.fenlibao.p2p.dao.trade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.bid.BidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;

import com.fenlibao.p2p.model.entity.trade.*;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

public interface ITradeDao {

	/**
	 * 交易记录列表
	 * @param params
	 * @return
	 */
	public List<TradeRecordForm> getRecordList(Map<String, Object> params, PageBounds pageBounds);
	
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
	
	/**
	 * 获取协议
	 * @param userId
	 * @return 协议号（no_agree）
	 */
	String getNoAgree(int userId);
	
	/**
	 * 待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getDueInAmount(int userId);

	/**
	 * 区分普通账户和存管账户
	 * 待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getDueInAmountByDepository(String userId,String depository);
	
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
	 * 获取计划可投标
	 * @param planId
	 * @return
	 */
	List<BidInfoForPlan> getPlanSurplusBidList (int planId);

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
	 * 计划投资记录列表
	 * @param planId
	 * @param pageBounds
	 * @return
	 */
	List<PlanRecords> getPlanRecordsList(Integer planId, PageBounds pageBounds);

	/**
	 * 计划投资记录列表(新)
	 * @param planId
	 * @param pageBounds
	 * @return
	 */
	List<PlanRecords> getNewPanRecordsList(int planId, PageBounds pageBounds);

	/**
	 * 我的计划投资对应标记录
	 * @param planRecordId
	 * @return
	 */
	List<BidRecords> getPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds);

	/**
	 * 我的计划投资对应标记录（新）
	 * @param planRecordId
	 * @return
	 */
	List<BidRecords> getNewPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds);

	/**
	 * 获取数据库当前时间
	 */
	Timestamp getDBCurrentTime();

	/**
	 * 获取老计划的收益
	 * @param map
	 * @return
	 */
    List<PlanBidProfit> getOldPlanProfit(Map<String, Object> map);

	/**
	 * 待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getNewDueInAmount(int userId, VersionTypeEnum versionTypeEnum);

	/**
	 * 计划待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getPlanDueInAmount(int userId, VersionTypeEnum versionTypeEnum);

	/**
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
	 */
	BigDecimal getNewYHGains(String userId,VersionTypeEnum versionTypeEnum);

	/**
	 * 获取用户“已还”收益（t6252）
	 * @param userId
	 * @return
	 */
	BigDecimal getPlanYHGains(String userId,VersionTypeEnum versionTypeEnum);

	/**
	 * 通过用户投资记录获取计划
	 * @param userPlanId
	 * @return
	 */
	InvestPlan getInvestPlanByUserPlanId(int userPlanId);
}
