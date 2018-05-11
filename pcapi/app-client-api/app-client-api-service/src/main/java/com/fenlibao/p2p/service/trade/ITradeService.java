package com.fenlibao.p2p.service.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fenlibao.p2p.model.entity.trade.*;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

public interface ITradeService {


	/**
	 * 交易记录列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @return
	 */
	public List<TradeRecordForm> getRecordList(int userId, Integer dayType, String startTimestamp,
											   String endTimestamp, Integer type, VersionTypeEnum vte, PageBounds pageBounds);
	
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
	 * 我的计划投资对应标记录
	 * @param planRecordId
	 * @return
	 */
	List<BidRecords> getPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds);

	/**
	 * 我的计划投资对应标记录(新)
	 * @param planRecordId
	 * @return
	 */
	List<BidRecords> getNewPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds);

	/**
	 * 计划投资记录列表
	 * @param planId
	 * @param pageBounds
     * @return
     */
	List<PlanRecords> getPlanRecordsList(Integer planId, PageBounds pageBounds);

	/**
	 * 计划投资记录列表（新）
	 * @param planId
	 * @param pageBounds
     * @return
     */
	List<PlanRecords> getNewPanRecordsList(Integer planId, PageBounds pageBounds);

	/**
	 * 获取计划已获收益
	 * @param creditId
	 * @return
	 */
    List<PlanBidProfit> getOldPlanProfit(int creditId);

	/**
	 * 4.0.0版本适用
	 * 待收金额（本息）区分存管
	 * @param userId
	 * @return
	 */
	DueInAmount getNewDueInAmount(int userId, VersionTypeEnum versionTypeEnum);

	/**
	 * 3.2.0版本适用
	 * 计划待收金额（本息）
	 * @param userId
	 * @return
	 */
	DueInAmount getPlanDueInAmount(int userId, VersionTypeEnum versionTypeEnum);

	/** 不包含计划产品库
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
}
