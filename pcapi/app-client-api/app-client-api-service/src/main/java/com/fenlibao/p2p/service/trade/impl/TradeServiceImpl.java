package com.fenlibao.p2p.service.trade.impl;

import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.consts.earnings.EarningsTypeConst;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TradeServiceImpl implements ITradeService {

	private static final Logger logger = LogManager.getLogger(TradeServiceImpl.class);
	
	@Resource
	private ITradeDao tradeDao;
	@Resource
	private UserInfoService userInfoService;
	
	@Override
	public List<TradeRecordForm> getRecordList(int userId, Integer dayType, String startTimestamp,
											   String endTimestamp, Integer type, VersionTypeEnum vte, PageBounds pageBounds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("type", type);

		if(VersionTypeEnum.CG.equals(vte)){
			params.put("accountType", InterfaceConst.XW_INVESTOR_WLZH);
		}else if(VersionTypeEnum.CGJK.equals(vte)){
			params.put("accountType", InterfaceConst.XW_BORROWERS_WLZH);
		}else{
			params.put("accountType", InterfaceConst.ACCOUNT_TYPE_WLZH);
		}

		if (dayType!=null) {
			params.put("dayType", dayType);
		}
		if (StringUtils.isNotBlank(startTimestamp)) {
			params.put("startTime", DateUtil.timestampToDate(Long.valueOf(startTimestamp)));
		}
		if (StringUtils.isNotBlank(endTimestamp)) {
			params.put("endTime", DateUtil.timestampToDate(Long.valueOf(endTimestamp)));
		}
		if(StringUtils.isNotBlank(startTimestamp)
				|| StringUtils.isNotBlank(endTimestamp)){
			params.put("dayType", null);//有开始和结束时间时不要最近N天
		}

		return tradeDao.getRecordList(params,pageBounds);
	}

	@Override
	public List<EarningsRecordForm> getEarningsList(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<EarningsRecordForm> list = new ArrayList<EarningsRecordForm>();
		params.put("userId", userId);
		params.put("feeTypes", new int[]{InterfaceConst.TZ_LX, InterfaceConst.TZ_WYJ, InterfaceConst.TZ_FX});
		params.put("status", Status.YH.name());
		/*
		 * TODO 这里要写成一次查询
		 */
		params.put("bidType", InterfaceConst.PRO_TYPE_KDB);
		BigDecimal KDB_amount = tradeDao.getEarningsList(params);
		params.put("bidType", InterfaceConst.PRO_TYPE_XJB);
		BigDecimal XJB_amount = tradeDao.getEarningsList(params);
		EarningsRecordForm form = new EarningsRecordForm();
		EarningsRecordForm form1 = new EarningsRecordForm();
		EarningsRecordForm form2 = new EarningsRecordForm();
		form.setAmount(KDB_amount);
		form.setEarningsTypeName("开店宝累计利息");
		form1.setAmount(XJB_amount);
		form1.setEarningsTypeName("薪金宝累计利息");
		form2.setAmount(XJB_amount.add(KDB_amount));
		form2.setEarningsTypeName("累计净利息");
		list.add(form);
		list.add(form1);
		list.add(form2);
		return list;
	}

	@Override
	public int resetPassword(int userId, String newPassword) throws Exception {
		return tradeDao.resetPassword(userId, newPassword);
	}

	@Override
	public int switchNoPassword(int userId, int isOpen) throws Exception {
		return tradeDao.switchNoPassword(userId, isOpen);
	}

	@Override
	public String getTradePassword(int userId) {
		return tradeDao.getTradePassword(userId);
	}

	@Override
	public int saveNoAgree(int userId, String noAgree) throws Exception {
		if (StringUtils.isNotBlank(this.getNoAgree(userId))) {
			return 1; //如果数据已经有了协议号，则不再需要更改。1代表成功
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("noAgree", noAgree);
		return tradeDao.saveNoAgree(params);
	}

	@Override
	public String getNoAgree(int userId) {
		return tradeDao.getNoAgree(userId);
	}

	@Override
	public DueInAmount getDueInAmount(int userId) {
		return tradeDao.getDueInAmount(userId);
	}

	@Override
	public DueInAmount getDueInAmountByDepository(String userId, String depository) {
		return tradeDao.getDueInAmountByDepository(userId,depository);
	}

	@Override
	public List<EarningsForm> getProspectiveEarnings() throws Exception {
		return tradeDao.getProspectiveEarnings();
	}

	@Transactional
	@Override
	public void countDayEarnings(Date yesterday, DayEarningsEntity entity, BigDecimal days,
			BigDecimal total, BigDecimal BeforeZQEarnings, int type, EarningsForm e) throws Exception {
		if (!tradeDao.existEarningsRecord(e.getUserId(), e.getZQ_id(), yesterday)) {
			entity = new DayEarningsEntity();
			if (e.getPeriod() > 1) { //多期
				Date jx_startTime = tradeDao.getJxStartTime(e.getJx_endTime(), e.getZQ_id());
				e.setJx_startTime(jx_startTime);
			}
			if (e.getZQZR_orderId() > 0) { //如果存在债权转让订单ID，则说明已经是债权转让
				type = EarningsTypeConst.ZQZR;
			} else {
				type = Integer.valueOf(EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX + e.getBidTypeId().toString());
			}
			if (0 == DateUtil.dateAdd(e.getJx_endTime(), -1).compareTo(yesterday)) { //等计息最后一天，将预期的收益减去历史的收益
				total = tradeDao.getHistoryTotalEarnings(e.getZQ_id());
				if (e.getZQZR_orderId() > 0) { //发生过债权转让,获取之前的债权收益
					BeforeZQEarnings = this.getBeforeZQEarnings(BeforeZQEarnings, e.getZQ_id());
					total = total.add(BeforeZQEarnings);
				}
				entity.setAmount(e.getAmount().subtract(total));
			} else {
				days = new BigDecimal(DateUtil.getDayBetweenDates(e.getJx_startTime(), e.getJx_endTime()));
				entity.setAmount(e.getAmount().divide(days, 2, BigDecimal.ROUND_DOWN));
			}
			entity.setBidId(e.getBidId());
			entity.setBidTypeId(e.getBidTypeId());
			entity.setEarningsDate(yesterday);
			entity.setUserId(e.getUserId());
			entity.setZQ_id(e.getZQ_id());
			entity.setType(type);
			tradeDao.insertDayEarnings(entity);
		}
	}
	
	/**
	 * 获取该债权之前的债权总收益
	 * @param ZQ_id
	 * @return
	 */
	private BigDecimal getBeforeZQEarnings(BigDecimal BeforeZQEarnings, Integer ZQ_id) {
		ZQ_id = tradeDao.getBeforeZQ_id(ZQ_id);
		if (ZQ_id != null && ZQ_id > 0) {
			BeforeZQEarnings = BeforeZQEarnings.add(tradeDao.getHistoryTotalEarnings(ZQ_id));
			this.getBeforeZQEarnings(BeforeZQEarnings, ZQ_id);
		}
		return BeforeZQEarnings;
	}
	
	@Override
	public Map<String, Object> getEarningsRecordList(int userId) {
		//其他收益（提前还款违约金、逾期罚息）
		List<EarningsForm> YH = tradeDao.getOtherInterest(userId, Status.YH);
		List<EarningsForm> WH = tradeDao.getOtherInterest(userId, Status.WH);
		//历史收益
		List<EarningsRecordForm> history = tradeDao.getTotalArrivalEarnings(userId);
		BigDecimal totalHistory = new BigDecimal(0);
		EarningsRecordForm form = new EarningsRecordForm();
		for (EarningsRecordForm e : history) {
			for (EarningsForm y : YH) {
				if (y.getZQZR_orderId() > 0 && EarningsTypeConst.ZQZR == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				} else if (EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX + y.getBidTypeId() == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				}
			}
			totalHistory = totalHistory.add(e.getAmount());
		}
		form.setAmount(totalHistory);
		form.setEarningsTypeName(EarningsRecordForm.TOTAL);
		history.add(form);
		
		//预期收益
		List<EarningsRecordForm> expect = tradeDao.getExpectDayEarnings(userId);
		BigDecimal totalExpect = new BigDecimal(0);
		for (EarningsRecordForm e : expect) {
			for (EarningsForm y : WH) {
				if (y.getZQZR_orderId() > 0 && EarningsTypeConst.ZQZR == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				} else if (EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX + y.getBidTypeId() == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				}
			}
			totalExpect = totalExpect.add(e.getAmount());
		}
		form = new EarningsRecordForm();
		form.setAmount(totalExpect);
		form.setEarningsTypeName(EarningsRecordForm.TOTAL);
		expect.add(form);
		
		//总收益
		List<EarningsRecordForm> total = new ArrayList<EarningsRecordForm>();
		form = new EarningsRecordForm();
		form.setAmount(totalExpect);
		form.setEarningsTypeName(EarningsRecordForm.EXPECT);
		total.add(form);
		form = new EarningsRecordForm();
		form.setAmount(totalHistory);
		form.setEarningsTypeName(EarningsRecordForm.HISTORY);
		total.add(form);
		form = new EarningsRecordForm();
		BigDecimal totalAmount = totalHistory.add(totalExpect);
		form.setAmount(totalAmount);
		form.setEarningsTypeName(EarningsRecordForm.TOTAL);
		total.add(form);
		
		Map<String, Object> all = new HashMap<String, Object>();
		all.put("expect", expect);
		all.put("history", history);
		all.put("totalEarnings", total);
		all.put("totalAmount", totalAmount.toString());
		return all;
	}

	@Override
	public BigDecimal getYesterdayEarnings(int userId, int type) {
		BigDecimal yesterdayEarnings = tradeDao.getYesterdayEarnings(userId, type);
		return yesterdayEarnings;
	}

	@Override
	public BigDecimal getAccumulativeEarnings(int userId) {
		//其他收益（提前还款违约金、逾期罚息）
		List<EarningsForm> YH = tradeDao.getOtherInterest(userId, Status.YH);
		List<EarningsForm> WH = tradeDao.getOtherInterest(userId, Status.WH);
		//历史收益
		List<EarningsRecordForm> history = tradeDao.getTotalArrivalEarnings(userId);
		BigDecimal totalHistory = new BigDecimal(0);
		for (EarningsRecordForm e : history) {
			for (EarningsForm y : YH) {
				if (y.getZQZR_orderId() > 0 && EarningsTypeConst.ZQZR == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				} else if (EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX + y.getBidTypeId() == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				}
			}
			totalHistory = totalHistory.add(e.getAmount());
		}
		//预期收益
		List<EarningsRecordForm> expect = tradeDao.getExpectDayEarnings(userId);
		BigDecimal totalExpect = new BigDecimal(0);
		for (EarningsRecordForm e : expect) {
			for (EarningsForm y : WH) {
				if (y.getZQZR_orderId() > 0 && EarningsTypeConst.ZQZR == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				} else if (EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX + y.getBidTypeId() == e.getCode()) {
					e.setAmount(e.getAmount().add(y.getAmount()));
				}
			}
			totalExpect = totalExpect.add(e.getAmount());
		}
		return totalHistory.add(totalExpect);
	}

	@Override
	public List<TenderRecords> getUserTenderRecords(Date startDate, Date endDate, int limit) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("limit", limit);
		
		return this.tradeDao.getUserTenderRecords(map);
	}

	@Override
	public double getUserTenderTotal(Date startDate, Date endDate, int userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("userId", userId);
		return this.tradeDao.getUserTenderTotal(map);
	}

	@Override
	public boolean hasSetPwd(Integer userId) {
		Auth auth = userInfoService.getAuthStatus(userId);
		if (auth == null || !auth.IDENTITY) { //如果没有实名认证则不需要校验
			return false;
		}
		return this.tradeDao.hasSetPwd(userId);
	}
	
	/**
	 * @Title: isValidUserPwd
	 * @Description: 验证用户交易密码是否正确
	 * @param accountId
	 * @param tradePassword (AES 加密后的数据)
	 * @return 成功返回true;错误返回false
	 * @throws Exception 
	 * @see com.fenlibao.p2p.service.trade.ITradeService#isValidUserPwd(int, java.lang.String) 
	 */
	@Override
	public boolean isValidUserPwd(int accountId, String tradePassword) throws Exception{
	
	    //先校验是否需开启免交易密码
		/*目前所有加入都需要密码验证  免密功能先屏蔽掉
		try {
			Map<String, Object> validData = bankService.getUserDealStatus(accountId);
			if(validData.isEmpty()){
	        	throw new LogicalException("30616");   //查询记录有误,没有查询到用户信息
	        }
		} catch (Exception e1) {
			throw new LogicalException("30616");   //查询记录有误,没有查询到用户信息
		}
		*/
	    String tradePwd=null;
	    try {
	        tradePwd = PasswordCryptUtil.cryptAESPassword(tradePassword);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new BusinessException(ResponseCode.TRADE_PASSWORD_DECRYPT_FAILURE.getCode(), ResponseCode.TRADE_PASSWORD_DECRYPT_FAILURE.getMessage()); //交易密码解密出现异常
	    }
	    //获取用户交易密码
	    String userTradePwd = getTradePassword(accountId);
	    if(StringUtils.isBlank(userTradePwd)){
	        throw new BusinessException(ResponseCode.TRADE_USER_NOT_SET_PASSWORD.getCode(), ResponseCode.TRADE_USER_NOT_SET_PASSWORD.getMessage()); //用户未设置交易密码
	    }
	    if(StringUtils.isNoneBlank(tradePwd) && userTradePwd.equals(tradePwd)){
	    	 return true; 
	    }
	    return false;//交易密码错误
	}

	@Override
	public void validateAuth(int userId) throws Exception {
		Auth auth = userInfoService.getAuthStatus(userId);
		if (auth == null) {
			throw new BusinessException(ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
		}
		if (!auth.IDENTITY) {
			throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH.getCode(), ResponseCode.USER_IDENTITY_UNAUTH.getMessage());
		}
		if (!auth.PHONE) {
			throw new BusinessException(ResponseCode.USER_PHONE_UNAUTH.getCode(), ResponseCode.USER_PHONE_UNAUTH.getMessage());
		}
		if (!auth.TRADE_PWD) {
			throw new BusinessException(ResponseCode.TRADE_USER_NOT_SET_PASSWORD.getCode(), ResponseCode.TRADE_USER_NOT_SET_PASSWORD.getMessage());
		}
	}

	@Override
	public BigDecimal getEarningsYesterdaySum(Integer userId) {
		return tradeDao.getEarningsYesterdaySum(userId);
	}

	@Override
	public BigDecimal getArrivalEarningsByCreditId(Integer creditId) {
		return tradeDao.getArrivalEarningsByCreditId(creditId);
	}

	@Override
	public BigDecimal getYHGains(String userId) {
		return tradeDao.getYHGains(userId);
	}

	@Override
	public List<BidRecords> getPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds) {
		return tradeDao.getPlanBidRecordsList(userId, planRecordId, pageBounds);
	}

	@Override
	public List<BidRecords> getNewPlanBidRecordsList(Integer userId, Integer planRecordId, PageBounds pageBounds) {
		return tradeDao.getNewPlanBidRecordsList(userId, planRecordId, pageBounds);
	}

	@Override
	public List<PlanRecords> getPlanRecordsList(Integer planId, PageBounds pageBounds) {
		return tradeDao.getPlanRecordsList(planId, pageBounds);
	}

	@Override
	public List<PlanRecords> getNewPanRecordsList(Integer planId, PageBounds pageBounds) {
		return tradeDao.getNewPanRecordsList(planId, pageBounds);
	}

	@Override
	public List<PlanBidProfit> getOldPlanProfit(int creditId) {
		Map<String, Object> map = new HashMap<>();
		map.put("planRecordId",creditId);
		return tradeDao.getOldPlanProfit(map);
	}

	@Override
	public DueInAmount getNewDueInAmount(int userId, VersionTypeEnum versionTypeEnum) {
		return tradeDao.getNewDueInAmount(userId, versionTypeEnum);
	}

	@Override
	public DueInAmount getPlanDueInAmount(int userId, VersionTypeEnum versionTypeEnum) {
		return tradeDao.getPlanDueInAmount(userId, versionTypeEnum);
	}


	@Override
	public BigDecimal getNewYHGains(String userId,VersionTypeEnum versionTypeEnum) {
		return tradeDao.getNewYHGains(userId,versionTypeEnum);
	}

	@Override
	public BigDecimal getPlanYHGains(String userId,VersionTypeEnum versionTypeEnum) {
		return tradeDao.getPlanYHGains(userId,versionTypeEnum);
	}
}
