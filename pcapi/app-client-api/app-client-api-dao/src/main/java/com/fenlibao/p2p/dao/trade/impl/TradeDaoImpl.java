package com.fenlibao.p2p.dao.trade.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.entity.bid.BidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.bid.PlanRecord;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
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
	public List<TradeRecordForm> getRecordList(Map<String, Object> params, PageBounds pageBounds) {
		 return sqlSession.selectList(MAPPER + "getRecordList", params,pageBounds);
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
	public String getNoAgree(int userId) {
		return sqlSession.selectOne(MAPPER + "getNoAgree", userId);
	}

	@Override
	public DueInAmount getDueInAmount(int userId) {
		return sqlSession.selectOne(MAPPER + "getDueInAmount", userId);
	}

	@Override
	public DueInAmount getDueInAmountByDepository(String userId,String depository) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("depository", depository);
		return sqlSession.selectOne(MAPPER + "getDueInAmountByDepository", map);
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
		return this.sqlSession.selectList(MAPPER+"getTenderRecords", map);
	}

	@Override
	public double getUserTenderTotal(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getUserTenderTotal", map);
	}

	@Override
	public boolean hasSetPwd(Integer userId) {
		String status = this.sqlSession.selectOne(MAPPER+"getPwdStatus", userId);
		if (InterfaceConst.AUTH_TRADPASSWORD_SETED.equals(status)) {
			return true;
		}
		return false;
	}

	@Override
	public BigDecimal getEarningsYesterdaySum(Integer userId) {
		return this.sqlSession.selectOne(MAPPER+"getEarningsYesterdaySum", userId);
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
		return this.sqlSession.selectOne(MAPPER+"getArrivalEarningsByCreditId", creditId);
	}

	@Override
	public Date getJxStartTime(Date jx_endTime, Integer ZQ_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("jx_endTime", jx_endTime);
		params.put("ZQ_id", ZQ_id);
		return this.sqlSession.selectOne(MAPPER+"getJxStartTime", params);
	}
	@Override
	public BigDecimal getYHGains(String userId) {
		return sqlSession.selectOne(MAPPER + "getYHGains", userId);
	}

	/**
	 * 获取计划可投标
	 * @param planId
	 * @return
	 */
	@Override
	public List<BidInfoForPlan> getPlanSurplusBidList(int planId){
		Map<String, Object> params = new HashMap<>();
		params.put("planId",planId);
		return sqlSession.selectList(MAPPER + "getPlanSurplusBidList", params);
	}

	/**
	 * 添加计划投资记录
	 * @param planId
	 * @param accountId
	 * @return
	 */
	@Override
	public int addPlanRecord(int planId, int accountId){
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
	 * 添加计划具体标投资记录
	 * @param planRecordId
	 * @param bidRecordId
	 * @return
	 */
	@Override
	public int addPlanBidRecord(int planRecordId, int bidRecordId){
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",planRecordId);
		params.put("bidRecordId",bidRecordId);
		return sqlSession.insert(MAPPER + "addPlanBidRecord", params);
	}


	/**
	 * 更新计划投资的金额和已投金额
	 * @param planRecordId
	 * @return
	 */
	@Override
	public void updatePlanRecordAmount(int planRecordId){
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",planRecordId);
		sqlSession.update(MAPPER + "updatePlanRecordAmount", params);
	}

	/**
	 * 获取获取计划投资金额
	 * @param planRecordId
	 * @return
	 */
	@Override
	public BigDecimal getPlanRecordAmount(int planRecordId){
		Map<String, Object> params = new HashMap<>();
		params.put("planRecordId",planRecordId);
		return sqlSession.selectOne(MAPPER + "getPlanRecordAmount", params);
	}

	/**
	 * 获取计划标题
	 * @param planId
	 * @return
	 */
	@Override
	public String getPlanTitle(int planId){
		Map<String, Object> params = new HashMap<>();
		params.put("planId",planId);
		return sqlSession.selectOne(MAPPER + "getPlanTitle", params);
	}

	/**
	 * 获取计划
	 * @param planId
	 * @return
	 */
	@Override
	public Plan getPlan(int planId){
		Map<String, Object> params = new HashMap<>();
		params.put("planId",planId);
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
	 * 更新计划状态
	 * @param planId
	 * @return
	 */
	@Override
	public void updatePlanStatus(int planId){
		Map<String, Object> params = new HashMap<>();
		params.put("planId",planId);
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
	public List<PlanRecords> getPlanRecordsList(Integer planId, PageBounds pageBounds) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.selectList(MAPPER + "getPlanRecordsList", params, pageBounds);
	}

	@Override
	public List<PlanRecords> getNewPanRecordsList(int planId, PageBounds pageBounds) {
		Map<String, Object> params = new HashMap<>();
		params.put("planId", planId);
		return sqlSession.selectList(MAPPER + "getNewPanRecordsList", params, pageBounds);
	}

	@Override
	public List<BidRecords> getPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("planRecordId", planRecordId);
		return sqlSession.selectList(MAPPER + "getBidRecordsList", params, pageBounds);
	}

	@Override
	public List<BidRecords> getNewPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("planRecordId", planRecordId);
		return sqlSession.selectList(MAPPER + "getNewPlanBidRecordsList", params, pageBounds);
	}

	/**
	 * 获取数据库当前时间
	 */
	@Override
	public Timestamp getDBCurrentTime() {
		return this.sqlSession.selectOne(MAPPER + "getDBCurrentTime");
	}

	@Override
	public List<PlanBidProfit> getOldPlanProfit(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER + "getOldPlanProfit", map);
	}

	@Override
	public DueInAmount getNewDueInAmount(int userId, VersionTypeEnum versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		return sqlSession.selectOne(MAPPER + "getNewDueInAmount", params);
	}

	@Override
	public DueInAmount getPlanDueInAmount(int userId, VersionTypeEnum versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		return sqlSession.selectOne(MAPPER + "getPlanDueInAmount", params);
	}

	@Override
	public BigDecimal getNewYHGains(String userId,VersionTypeEnum versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		return sqlSession.selectOne(MAPPER + "getNewYHGains", params);
	}

	@Override
	public BigDecimal getPlanYHGains(String userId,VersionTypeEnum versionTypeEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
		return sqlSession.selectOne(MAPPER + "getPlanYHGains", params);
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
}
