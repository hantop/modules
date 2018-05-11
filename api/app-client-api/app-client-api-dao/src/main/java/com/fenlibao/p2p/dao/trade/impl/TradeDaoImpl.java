package com.fenlibao.p2p.dao.trade.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.bid.BidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.bid.PlanRecord;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.pay.ThirdPartyAgreement;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TradeDaoImpl extends BaseDao implements ITradeDao {

	public TradeDaoImpl() {
		super("TradeMapper");
	}

	@Override
	public List<TradeRecordForm> getRecordList(Map<String, Object> params) {
		return sqlSession.selectList(MAPPER + "getRecordList", params);
	}

	@Override
	public BigDecimal getEarningsList(Map<String, Object> params) {
		return sqlSession.selectOne(MAPPER + "getEarningsList", params);
	}

	@Override
	public int resetPassword(int userId, String newPassword)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("newPassword", newPassword);
		params.put("status", InterfaceConst.AUTH_TRADPASSWORD_SETED);
		return sqlSession.update(MAPPER + "resetPassword", params);
	}

	@Override
	public int switchNoPassword(int userId, int isOpen) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("isOpen", isOpen);
		return sqlSession.update(MAPPER + "changePasswordStatus", params);
	}

	@Override
	public String getTradePassword(int userId) {
		return sqlSession.selectOne(MAPPER + "getTradePassword", userId);
	}

	@Override
	public int saveNoAgree(Map<String, Object> params) throws Exception {
		return sqlSession.insert(MAPPER + "saveNoAgree", params);
	}
	
	@Override
	public int updateNoAgree(Map<String, Object> params) throws Exception {
		return sqlSession.update(MAPPER + "updateNoAgree", params);
	}

	@Override
	public ThirdPartyAgreement getNoAgree(int userId) {
		return sqlSession.selectOne(MAPPER + "getNoAgree", userId);
	}

	@Override
	public DueInAmount getDueInAmount(int userId) {
		return sqlSession.selectOne(MAPPER + "getDueInAmount", userId);
	}

	@Override
	public List<EarningsForm> getProspectiveEarnings() throws Exception {
		return sqlSession.selectList(MAPPER + "getProspectiveEarnings");
	}

	@Override
	public int insertDayEarnings(DayEarningsEntity entity) throws Exception {
		return sqlSession.insert(MAPPER + "insertDayEarnings", entity);
	}

	@Override
	public List<EarningsRecordForm> getEarningsList(Integer userId,
													String status, Integer[] feeTypes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("status", status);
		params.put("feeTypes", feeTypes);
		return sqlSession.selectList(MAPPER + "getEarningsRecordList", params);
	}

	@Override
	public boolean existEarningsRecord(Integer userId, Integer ZQ_id,
									   Date earningsDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("ZQ_id", ZQ_id);
		params.put("earningsDate", earningsDate);
		Integer result = sqlSession.selectOne(MAPPER + "existEarningsRecord", params);
		if (result != null) {
			return true;
		}
		return false;
	}

	@Override
	public BigDecimal getHistoryTotalEarnings(Integer ZQ_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ZQ_id", ZQ_id);
		return sqlSession.selectOne(MAPPER + "getHistoryTotalEarnings", params);
	}

	@Override
	public BigDecimal getYesterdayEarnings(int userId, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("type", type);
		return sqlSession.selectOne(MAPPER + "getYesterdayEarnings", params);
	}

	@Override
	public List<EarningsRecordForm> getExpectDayEarnings(int userId) {
		return sqlSession.selectList(MAPPER + "getExpectDayEarnings", userId);
	}

	@Override
	public boolean isExistArrivalEarnings() {
		Integer result = sqlSession.selectOne(MAPPER + "isExistArrivalEarnings");
		if (result != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<EarningsRecordForm> getTotalArrivalEarnings(Integer userId) {
		return sqlSession.selectList(MAPPER + "getArrivalEarnings", userId);
	}

	@Override
	public Integer getBeforeZQ_id(int nowZQ_id) {
		return sqlSession.selectOne(MAPPER + "getBeforeZQ_id", nowZQ_id);
	}

	@Override
	public List<TenderRecords> getUserTenderRecords(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER + "getTenderRecords", map);
	}

	@Override
	public double getUserTenderTotal(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getUserTenderTotal", map);
	}

	@Override
	public boolean hasSetPwd(Integer userId) {
		String status = this.sqlSession.selectOne(MAPPER + "getPwdStatus", userId);
		if (InterfaceConst.AUTH_TRADPASSWORD_SETED.equals(status)) {
			return true;
		}
		return false;
	}

	@Override
	public BigDecimal getEarningsYesterdaySum(Integer userId) {
		return this.sqlSession.selectOne(MAPPER + "getEarningsYesterdaySum", userId);
	}

	@Override
	public List<EarningsForm> getOtherInterest(int userId, Status status) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("status", status.name());
		return sqlSession.selectList(MAPPER + "getOtherInterest", params);
	}

	@Override
	public BigDecimal getArrivalEarningsByCreditId(Integer creditId) {
		return this.sqlSession.selectOne(MAPPER + "getArrivalEarningsByCreditId", creditId);
	}

	@Override
	public Date getJxStartTime(Date jx_endTime, Integer ZQ_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("jx_endTime", jx_endTime);
		params.put("ZQ_id", ZQ_id);
		return this.sqlSession.selectOne(MAPPER + "getJxStartTime", params);
	}

	@Override
	public BigDecimal getYHGains(String userId) {
		return sqlSession.selectOne(MAPPER + "getYHGains", userId);
	}

	@Override
	public int getTradePwdWrongCount(int userId) {
		return sqlSession.selectOne(MAPPER + "getTradePwdWrongCount", userId);
	}

	@Override
	public void updateTradePwdWrongCount(int userId, boolean isReset) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("isReset", isReset);
		sqlSession.update(MAPPER + "updateTradePwdWrongCount", params);
	}

	@Override
	public List<PlanRecords> getPlanRecordsList(Integer planId, Integer pageNo, Integer pageSize) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.selectList(MAPPER + "getPlanRecordsList", params);
	}

	@Override
	public List<BidRecords> getBidRecordsList(Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId", planRecordId);
		return sqlSession.selectList(MAPPER + "getBidRecordsList", params);
	}

	@Override
	public List<InvestInfo> getUserPlanList(int userId, int isUp, Date timestamp) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("isUp", isUp);
		params.put("time", timestamp);
		return sqlSession.selectList(MAPPER + "getUserPlanList", params);
	}

	@Override
	public PlanFinacing getUserPlanDetail(int userId, Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("planRecordId", planRecordId);
		return sqlSession.selectOne(MAPPER + "getUserPlanDetail", params);
	}

	/**
	 * 获取计划可投标
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public List<BidInfoForPlan> getPlanSurplusBidList(int planId, boolean isCheckMinAmount) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		params.put("isCheckMinAmount", isCheckMinAmount);
		return sqlSession.selectList(MAPPER + "getPlanSurplusBidList", params);
	}

	/**
	 * 添加计划投资记录
	 *
	 * @param planId
	 * @param accountId
	 * @return
	 */
	@Override
	public int addPlanRecord(int planId, int accountId) {
		PlanRecord planRecord = new PlanRecord();
		planRecord.setPlanId(planId);
		planRecord.setUserId(accountId);
		sqlSession.insert(MAPPER + "addPlanRecord", planRecord);
		return planRecord.getId();
	}

	/**
	 * 加锁
	 */
	@Override
	public void lockPlan(int planId){
		Map<String, Object> params = new HashMap<>();
		params.put("planId",planId);
		sqlSession.selectOne(MAPPER + "lockPlan", params);
	}

	/**
	 * 加锁
	 */
	@Override
	public void lockInvestPlan(int investPlanId){
		Map<String, Object> params = new HashMap<>();
		params.put("investPlanId",investPlanId);
		sqlSession.selectOne(MAPPER + "lockInvestPlan");
	}

	/**
	 * 添加计划具体标投资记录
	 *
	 * @param planRecordId
	 * @param bidRecordId
	 * @return
	 */
	@Override
	public int addPlanBidRecord(int planRecordId, int bidRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId", planRecordId);
		params.put("bidRecordId", bidRecordId);
		return sqlSession.insert(MAPPER + "addPlanBidRecord", params);
	}


	/**
	 * 更新计划投资的金额和已投金额
	 *
	 * @param planRecordId
	 * @return
	 */
	@Override
	public void updatePlanRecordAmount(int planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId", planRecordId);
		sqlSession.update(MAPPER + "updatePlanRecordAmount", params);
	}

	/**
	 * 获取获取计划投资金额
	 *
	 * @param planRecordId
	 * @return
	 */
	@Override
	public BigDecimal getPlanRecordAmount(int planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId", planRecordId);
		return sqlSession.selectOne(MAPPER + "getPlanRecordAmount", params);
	}

	/**
	 * 获取计划标题
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public String getPlanTitle(int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.selectOne(MAPPER + "getPlanTitle", params);
	}


	/**
	 * 获取计划
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public Plan getPlan(int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.selectOne(MAPPER + "getPlan", params);
	}

	/**
	 * 获取新版计划
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public InvestPlan getInvestPlan(int planId, int isLock){
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		params.put("isLock", isLock);
		return sqlSession.selectOne(MAPPER + "getInvestPlan", params);
	}

	/**
	 * 通过用户投资记录获取计划
	 * @param userPlanId
	 * @return
	 */
	@Override
	public InvestPlan getInvestPlanByUserPlanId(int userPlanId){
		Map<String, Object> params = new HashMap<>();
		params.put("userPlanId", userPlanId);
		return sqlSession.selectOne(MAPPER + "getInvestPlanByUserPlanId", params);
	}

	/**
	 * 更新计划状态
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public void updatePlanStatus(int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		sqlSession.update(MAPPER + "updatePlanStatus", params);
	}

	/**
	 * 更新计划状态
	 *
	 * @param planId
	 * @return
	 */
	@Override
	public int updateInvestPlanStatus(int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.update(MAPPER + "updateInvestPlanStatus", params);
	}

	@Override
	public int getpurchasedPlan(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getpurchasedPlan", map);
	}

	@Override
	public PlanFinacing getUserPlanDetailLast(int userId, int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("planId", planId);
		return sqlSession.selectOne(MAPPER + "getUserPlanDetailLast", params);
	}

	@Override
	public List<PlanBidProfit> getPlanProfit(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER + "getPlanProfit", map);
	}

	/**
	 * 获取数据库当前时间
	 */
	@Override
	public Timestamp getDBCurrentTime() {
		return this.sqlSession.selectOne(MAPPER + "getDBCurrentTime");
	}

	@Override
	public List<PlanCreditInfo> getPlanInterestRise(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER + "getPlanInterestRise", map);
	}

	@Override
	public Map getInvestmentExt(int zqId) {
		Map<String, Object> params = new HashMap<>();
		params.put("zqId", zqId);
		Map applyTime = sqlSession.selectOne(MAPPER + "getApplyTime", params);
		Map actualRepaymentDate = sqlSession.selectOne(MAPPER + "getActualRepaymentDate", params);
		Map<String, Object> ext = new HashMap();
		ext.put("applyTime", applyTime == null ? null : applyTime.get("applyTime"));
		ext.put("actualRepaymentDate", actualRepaymentDate == null ? null : actualRepaymentDate.get("actualRepaymentDate"));
		return ext;
	}

	@Override
	public PlanFinacing getUserPlanRecord(int userId, Integer planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("planRecordId", planRecordId);
		return sqlSession.selectOne(MAPPER + "getUserPlanRecord", params);
	}

	/**
	 * 我的月升计划投资对应标记录
	 *
	 * @param recordId
	 * @return
	 */
	@Override
	public List<BidRecords> getNewBidRecordsList(int recordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId", recordId);
		return sqlSession.selectList(MAPPER + "getNewBidRecords", params);
	}

	/**
	 * 我的月升计划投资对应债权记录
	 * @param recordId
	 * @return
	 */
	@Override
	public List<BidRecords> getNewCreditList(int recordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",recordId);
		return sqlSession.selectList(MAPPER + "getNewCreditRecords", params);
	}

	@Override
	public double getNewPlanCouponRise(int planRecordId) {
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",planRecordId);
		String couponRise = sqlSession.selectOne(MAPPER + "getNewPlanCouponRise", params);
		if(StringUtils.isEmpty(couponRise)){
			return 0;
		}else {
			return Double.valueOf(couponRise);
		}
		//return sqlSession.selectOne(MAPPER + "getNewPlanCouponRise", params);
	}

	@Override
	public List<InvestInfo> getHoldPlanBid(int userId, int isUp, Date time) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("isUp", isUp);
		params.put("time", time);
		return sqlSession.selectList(MAPPER + "getHoldPlanBid", params);
	}

	@Override
	public List<InvestInfo> getHoldPlanBid(int userId, int isUp, Date time, int versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("isUp", isUp);
		params.put("time", time);
		params.put("versionTypeEnum", versionTypeEnum);
		return sqlSession.selectList(MAPPER + "getHoldPlanBid", params);
	}

	@Override
	public List<InvestInfo> getQuitPlanBid(int userId, int isUp, Date time) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("isUp", isUp);
		params.put("time", time);
		return sqlSession.selectList(MAPPER + "getQuitPlanBid", params);
	}

	@Override
	public List<InvestInfo> getQuitPlanBid(int userId, int isUp, Date time, int versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("isUp", isUp);
		params.put("time", time);
		params.put("versionTypeEnum", versionTypeEnum);
		return sqlSession.selectList(MAPPER + "getQuitPlanBid", params);
	}

	@Override
	public List<InvestInfo> getProfitPlanBid(int userId, int isUp, Date time) {
		return getProfitPlanBid( userId,  isUp,  time,1);
	}

	@Override
	public List<InvestInfo> getProfitPlanBid(int userId, int isUp, Date time, int versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("isUp", isUp);
		params.put("time", time);
		params.put("versionTypeEnum", versionTypeEnum);
		return sqlSession.selectList(MAPPER + "getProfitPlanBid", params);
	}

	@Override
	public Map getOldPlanExt(int creditId) {
		return sqlSession.selectOne(MAPPER + "getOldPlanExt", creditId);
	}

	@Override
	public PlanFinacing getNewPlanDetailLast(int userId, int planId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",userId);
		params.put("planId", planId);
		return sqlSession.selectOne(MAPPER + "getNewPlanDetailLast", params);
	}

	@Override
	public List<PlanRecords> getNewRecordsList(int planId, Integer pageNo, Integer pageSize) {
		Map<String, Object> params = new HashMap<>();
		if(pageNo==null)pageNo=1;
		if(pageSize==null)pageSize=InterfaceConst.PAGESIZE;
		params.put("pageNo",(pageNo-1)*pageSize);
		params.put("pageSize",pageSize);
		params.put("planId", planId);
		return sqlSession.selectList(MAPPER + "getNewRecordsList", params);
	}

	@Override
	public BigDecimal getNewPlanProfit(int creditId) {
		Map<String, Object> params = new HashMap<>();
		params.put("creditId",creditId);
		return sqlSession.selectOne(MAPPER + "getNewPlanProfit", params);
	}

	@Override
	public int getpurchasedNewPlan(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "getpurchasedNewPlan", map);
	}

	@Override
	public DueInAmount getNewDueInAmount(int userId,int cgNum) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId",userId);
		params.put("cgNum",cgNum);
		return sqlSession.selectOne(MAPPER + "getNewDueInAmount", params);
	}

	@Override
	public DueInAmount getPlanDueInAmount(int userId,int cgNum) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId",userId);
		params.put("cgNum",cgNum);
		return sqlSession.selectOne(MAPPER + "getPlanDueInAmount", params);
	}

	@Override
	public BigDecimal getNewYHGains(String userId,int cgNum) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId",userId);
		params.put("cgNum",cgNum);
		return sqlSession.selectOne(MAPPER + "getNewYHGains", params);
	}

	@Override
	public BigDecimal getPlanYHGains(String userId,int cgNum) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId",userId);
		params.put("cgNum",cgNum);
		return sqlSession.selectOne(MAPPER + "getPlanYHGains", params);
	}

	@Override
	public InvestShareVO getTenderIdOldPlan(int userId, int shareId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("shareId", shareId);
		return sqlSession.selectOne(MAPPER+"getTenderIdOldPlan",params);
	}

	@Override
	public InvestShareVO getTenderIdNewPlan(int userId, int shareId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("shareId", shareId);
		return sqlSession.selectOne(MAPPER+"getTenderIdNewPlan",params);
	}

	@Override
	public int updateUserFundsAccountAmount(int userId, BigDecimal accountAmount,  String accountType) {
		Map<String, Object> params = new HashMap<>(3);
		params.put("userId", userId);
		params.put("accountAmount", accountAmount);
		params.put("accountType",accountType);
		return sqlSession.update(MAPPER + "updateUserFundsAccountAmount",params);
	}

	@Override
	public UserAccount getUserBalance(int userId, String accountType) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("accountType",accountType);
		return this.sqlSession.selectOne(MAPPER + "getUserBalance", params);
	}

	@Override
	public int addT6102Record(int zcwlzhId, int FeeCode, int zrwlzhId, Date nowDatetime, BigDecimal zrcashAmount, BigDecimal zccashAmount, BigDecimal balanceAmount, String remark) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zcwlzhId", zcwlzhId);
		map.put("FeeCode", FeeCode);
		map.put("zrwlzhId", zrwlzhId);
		map.put("nowDatetime", nowDatetime);
		map.put("zrcashAmount", zrcashAmount);
		map.put("zccashAmount", zccashAmount);
		map.put("balanceAmount", balanceAmount);
		map.put("remark", remark);
		return sqlSession.insert(MAPPER + "addT6102Record",map);
	}

	@Override
	public String getPlatformNo(int userId, String userRole) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("userRole", userRole);
		return this.sqlSession.selectOne(MAPPER + "getPlatformNo", map);
	}

	@Override
	public int addTransferApplication(int userId, String platformUserNo, BigDecimal amount, String status) {
		TransferApplicationEntity transferApplicationEntity = new TransferApplicationEntity();
		transferApplicationEntity.setUserId(userId);
		transferApplicationEntity.setPlatformUserNo(platformUserNo);
		transferApplicationEntity.setAmount(amount);
		transferApplicationEntity.setStatus(status);
		sqlSession.insert(MAPPER + "addTransferApplication", transferApplicationEntity);
		return transferApplicationEntity.getId();
	}

	@Override
	public String getRechargeState(int requestId) {
		Map<String, Object> params = new HashMap<>();
		params.put("requestId",requestId);
		return sqlSession.selectOne(MAPPER + "getRechargeState", params);
	}

	@Override
	public int updateTransferApplication(int transferApplicationId, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("transferApplicationId", transferApplicationId);
		params.put("status", status);
		return sqlSession.update(MAPPER + "updateTransferApplication", params);
	}
}
