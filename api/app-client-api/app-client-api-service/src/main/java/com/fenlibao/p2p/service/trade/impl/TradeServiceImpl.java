package com.fenlibao.p2p.service.trade.impl;

import com.dimeng.p2p.S61.enums.T6101_F03;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.common.util.pagination.PageHelper;
import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.consts.earnings.EarningsTypeConst;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import com.fenlibao.p2p.model.entity.SendSmsRecordExt;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.pay.ThirdPartyAgreement;
import com.fenlibao.p2p.model.entity.trade.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.EarningsForm;
import com.fenlibao.p2p.model.form.trade.EarningsRecordForm;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TradeServiceImpl implements ITradeService {

	private static final Logger logger = LogManager.getLogger(TradeServiceImpl.class);
	
	@Resource
	private ITradeDao tradeDao;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private SendSmsRecordDao sendSmsRecordDao;
	@Resource
	private SendSmsRecordExtDao sendSmsRecordExtDao;
	
	@Override
	public List<TradeRecordForm> getRecordList(int userId,Integer pageNo,Integer pagesize,VersionTypeEnum vte) {
		if(pageNo==null)pageNo=1;
		if(pagesize==null)pagesize=InterfaceConst.PAGESIZE;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("pageNo",(pageNo-1)*pagesize);
		params.put("pagesize",pagesize);
		if(VersionTypeEnum.CG.equals(vte)){
			params.put("accountType", InterfaceConst.XW_INVESTOR_WLZH);
		}else{
			params.put("accountType", InterfaceConst.ACCOUNT_TYPE_WLZH);
		}
		return tradeDao.getRecordList(params);
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
		tradeDao.updateTradePwdWrongCount(userId, true);
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
		ThirdPartyAgreement agreement=getNoAgree(userId);
		if(agreement!=null){
			if (StringUtils.isNotBlank(agreement.getLianlianAgreement())) {
				return 1; //如果数据已经有了协议号，则不再需要更改。1代表成功
			}
			else{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				params.put("noAgree", noAgree);
				return tradeDao.updateNoAgree(params);
			}
		}
		else{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("noAgree", noAgree);
			return tradeDao.saveNoAgree(params);
		}
	}

	@Override
	public ThirdPartyAgreement getNoAgree(int userId) {
		return tradeDao.getNoAgree(userId);
	}

	@Override
	public DueInAmount getDueInAmount(int userId) {
		return tradeDao.getDueInAmount(userId);
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
//		List<EarningsRecordForm> total = new ArrayList<EarningsRecordForm>();
//		form = new EarningsRecordForm();
//		form.setAmount(totalExpect);
//		form.setEarningsTypeName(EarningsRecordForm.EXPECT);
//		total.add(form);
//		form = new EarningsRecordForm();
//		form.setAmount(totalHistory);
//		form.setEarningsTypeName(EarningsRecordForm.HISTORY);
//		total.add(form);
//		form = new EarningsRecordForm();
//		BigDecimal totalAmount = totalHistory.add(totalExpect);
//		form.setAmount(totalAmount);
//		form.setEarningsTypeName(EarningsRecordForm.TOTAL);
//		total.add(form);
		
		Map<String, Object> all = new HashMap<String, Object>();
//		all.put("expect", expect);
//		all.put("history", history);
//		all.put("totalEarnings", total);
//		all.put("totalAmount", totalAmount.toString());
		all.put("expectGains", totalExpect.toString());
		all.put("historyGains", totalHistory.toString());
		all.put("totalGains", totalExpect.add(totalHistory).toString());
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
		int count = tradeDao.getTradePwdWrongCount(accountId);
		if (InterfaceConst.TRADE_PWD_WRONG_COUNT_MAX <= count) {
			throw new BusinessException(ResponseCode.USER_TRADE_PASSWORD_ERROR);
		}
	    String tradePwd=null;
		if (StringUtils.isBlank(tradePassword)) {
			return false;
		}
	    try {
	        tradePwd = PasswordCryptUtil.cryptAESPassword(tradePassword);
	    } catch (Exception e) {
	        logger.error("交易密码解密失败,userId=[{}]", accountId);
	        throw new BusinessException(ResponseCode.COMMON_PARAM_DECRYPT_FAILURE); //交易密码解密出现异常
	    }
	    //获取用户交易密码
	    String userTradePwd = getTradePassword(accountId);
	    if(StringUtils.isBlank(userTradePwd)){
	        throw new BusinessException(ResponseCode.USER_NOT_SET_TRADE_PASSWORD); //用户未设置交易密码
	    }
	    if(StringUtils.isBlank(tradePwd) || !userTradePwd.equals(tradePwd)){
			int surplus = InterfaceConst.TRADE_PWD_WRONG_COUNT_MAX - (count+1);
			tradeDao.updateTradePwdWrongCount(accountId, false);
			if (0 == surplus) {
				throw new BusinessException(ResponseCode.USER_TRADE_PASSWORD_ERROR);
			}
	    	 throw new BusinessException(ResponseCode.USER_TRADE_PWD_WRONG_COUNT.getCode(),
					 String.format(ResponseCode.USER_TRADE_PWD_WRONG_COUNT.getMessage(), surplus));
	    }
		if (0 < count) {
			tradeDao.updateTradePwdWrongCount(accountId, true);
		}
	    return true;
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
			throw new BusinessException(ResponseCode.USER_NOT_SET_TRADE_PASSWORD);
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
	public List<PlanRecords> getPlanRecordsList(Integer planId, Integer pageNo, Integer pageSize) {
		PageHelper.startPage(pageNo,pageSize,false);
		return tradeDao.getPlanRecordsList(planId,pageNo,pageSize);
	}

	@Override
	public List<BidRecords> getBidRecordsList(Integer planRecordId) {
		return tradeDao.getBidRecordsList(planRecordId);
	}

	@Override
	public List<InvestInfo> getUserPlanList(int userId, int isUp, String timestamp) {
		Date time = null;
		if (StringUtils.isNotEmpty(timestamp)) {
			time =  DateUtil.timestampToDate(Long.valueOf(timestamp));
		}
		return tradeDao.getUserPlanList(userId,isUp,time);
	}

	@Override
	public PlanFinacing getUserPlanDetail(int userId, Integer planRecordId) {
		return tradeDao.getUserPlanDetail(userId,planRecordId);
	}

	@Override
	public int getpurchasedPlan(int userId,int planId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("planId",planId);
		int i = tradeDao.getpurchasedPlan(map);
		return i;
	}

	@Override
	public PlanFinacing getUserPlanDetailLast(int userId,int planId) {
		return tradeDao.getUserPlanDetailLast(userId,planId);
	}

	@Override
	public List<PlanBidProfit> getPlanProfit(int planRecordId) {
		Map<String, Object> map = new HashMap<>();
		map.put("planRecordId",planRecordId);
		return tradeDao.getPlanProfit(map);
	}

	@Override
	public List<PlanCreditInfo> getPlanInterestRise(int planRecordId) {
		Map<String, Object> map = new HashMap<>();
		map.put("planRecordId",planRecordId);
		return tradeDao.getPlanInterestRise(map);
	}

	@Override
	public Map getInvestmentExt(int zqId) {
		return tradeDao.getInvestmentExt(zqId);
	}

	@Override
	public List<InvestInfo> getUserProjectList(int userId, int isUp, String timestamp, String status) {
		return getUserProjectList(userId,isUp,timestamp,status,1);
	}

	@Override
	public List<InvestInfo> getUserProjectList(int userId, int isUp, String timestamp,
											   String status, int versionTypeEnum) {
		List<InvestInfo> investInfos = new ArrayList<>();
		Date time = null;
		if (StringUtils.isNotEmpty(timestamp)) {
			time =  DateUtil.timestampToDate(Long.valueOf(timestamp));
		}
		if (status.equals("1")) {
			investInfos = tradeDao.getHoldPlanBid(userId, isUp, time,versionTypeEnum);
		} else if (status.equals("2")) {
			investInfos = tradeDao.getQuitPlanBid(userId, isUp, time,versionTypeEnum);
		} else if (status.equals("3")) {
			investInfos = tradeDao.getProfitPlanBid(userId, isUp, time,versionTypeEnum);
		}

		return investInfos;
	}

	@Override
	public PlanFinacing getUserPlanRecord(int userId, Integer planRecordId) {
		return tradeDao.getUserPlanRecord(userId,planRecordId);
	}

    @Override
    public List<BidRecords> getNewBidRecordsList(int recordId) {
        List<BidRecords> bidList = tradeDao.getNewBidRecordsList(recordId);
        List<BidRecords> creditList = tradeDao.getNewCreditList(recordId);

		bidList.addAll(creditList);

        return bidList;
    }

	@Override
	public double getNewPlanCouponRise(int planRecordId) {
		return tradeDao.getNewPlanCouponRise(planRecordId);
	}

	@Override
	public Map getOldPlanExt(int creditId) {
		return tradeDao.getOldPlanExt(creditId);
	}

	@Override
	public PlanFinacing getNewPlanDetailLast(int userId, int planId) {
		return tradeDao.getNewPlanDetailLast(userId, planId);
	}

	@Override
	public List<PlanRecords> getNewRecordsList(int planId, Integer pageNo, Integer pageSize) {
		return tradeDao.getNewRecordsList(planId,pageNo,pageSize);
	}

	@Override
	public BigDecimal getNewPlanProfit(int creditId) {
		return tradeDao.getNewPlanProfit(creditId);
	}

	@Override
	public int getpurchasedNewPlan(int userId, int planId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("planId",planId);
		int i = tradeDao.getpurchasedNewPlan(map);
		return i;
	}

	@Override
	public DueInAmount getNewDueInAmount(int userId,int cgNum) {
		return tradeDao.getNewDueInAmount(userId,cgNum);
	}

	@Override
	public DueInAmount getPlanDueInAmount(int userId,int cgNum) {
		return tradeDao.getPlanDueInAmount(userId,cgNum);
	}

	@Override
	public BigDecimal getNewYHGains(String userId,int cgNum) {
		return tradeDao.getNewYHGains(userId,cgNum);
	}

	@Override
	public BigDecimal getPlanYHGains(String userId,int cgNum) {
		return tradeDao.getPlanYHGains(userId,cgNum);
	}

	@Override
	public InvestShareVO getTenderIdOldPlan(int userId, int shareId) {
		return tradeDao.getTenderIdOldPlan(userId, shareId);
	}

	@Override
	public InvestShareVO getTenderIdNewPlan(int userId, int shareId) {
		return tradeDao.getTenderIdNewPlan(userId,shareId);
	}

	@Transactional
	@Override
	public void updateUserAccountInfo(int userId, BigDecimal cashAmount,int type) throws Exception{
		if(type!=0 && type ==1) {
			/**
			 * 获取往来账户余额
			 */
			UserAccount wlzhBalance = tradeDao.getUserBalance(userId, T6101_F03.WLZH.name());
			if (wlzhBalance == null) {
				throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(), ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			if (wlzhBalance.getBalance().compareTo(cashAmount) == -1) {
				throw new BusinessException(ResponseCode.MOVE_AMOUNT_WLZH_BLANCE_FIND.getCode(), ResponseCode.MOVE_AMOUNT_WLZH_BLANCE_FIND.getMessage());
			}
			/**
			 * 减少用户在平台往来账户金额
			 */
			tradeDao.updateUserFundsAccountAmount(userId, wlzhBalance.getBalance().subtract(cashAmount), T6101_F03.WLZH.name());

			/**
			 * 获取锁定账户余额
			 */
			UserAccount sdzhBalance = tradeDao.getUserBalance(userId, T6101_F03.SDZH.name());
			if (sdzhBalance == null) {
				throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(), ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			/**
			 * 增加锁定账户余额
			 */
			tradeDao.updateUserFundsAccountAmount(userId, sdzhBalance.getBalance().add(cashAmount), T6101_F03.SDZH.name());

			BigDecimal a = wlzhBalance.getBalance().subtract(cashAmount);
			//添加转出者资金流水记录
			tradeDao.addT6102Record(wlzhBalance.getAccountId(), FeeCode.HBJE, sdzhBalance.getAccountId(), new Date(), new BigDecimal(0), cashAmount, wlzhBalance.getBalance().subtract(cashAmount), "资金迁移");

			//添加转入者资金流水记录
			tradeDao.addT6102Record(sdzhBalance.getAccountId(), FeeCode.HBJE, wlzhBalance.getAccountId(), new Date(), cashAmount, new BigDecimal(0), sdzhBalance.getBalance().add(cashAmount), "资金迁移");



		}else if(type!=0 && type ==2){

			/**
			 * 获取锁定账户余额
			 */
			UserAccount sdzhBalance = tradeDao.getUserBalance(userId, T6101_F03.SDZH.name());
			if (sdzhBalance == null) {
				throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(), ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			if (sdzhBalance.getBalance().compareTo(cashAmount) == 0) {
				throw new BusinessException(ResponseCode.MOVE_AMOUNT_SDZH_BLANCE_FIND.getCode(), ResponseCode.MOVE_AMOUNT_SDZH_BLANCE_FIND.getMessage());
			}
			/**
			 * 减少锁定账户余额
			 */
			tradeDao.updateUserFundsAccountAmount(userId, sdzhBalance.getBalance().subtract(cashAmount), T6101_F03.SDZH.name());


			/**
			 * 获取往来账户余额
			 */
			UserAccount wlzhBalance = tradeDao.getUserBalance(userId, T6101_F03.WLZH.name());
			if (wlzhBalance == null) {
				throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(), ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}

			/**
			 * 增加平台往来账户金额
			 */
			tradeDao.updateUserFundsAccountAmount(userId, wlzhBalance.getBalance().add(cashAmount), T6101_F03.WLZH.name());



			//添加转出者资金流水记录
			tradeDao.addT6102Record(sdzhBalance.getAccountId(), FeeCode.HBJE, wlzhBalance.getAccountId(), new Date(), new BigDecimal(0), cashAmount, sdzhBalance.getBalance().subtract(cashAmount), "资金迁移");

			//添加转入者资金流水记录
			tradeDao.addT6102Record(wlzhBalance.getAccountId(), FeeCode.HBJE, sdzhBalance.getAccountId(), new Date(), cashAmount, new BigDecimal(0), wlzhBalance.getBalance().add(cashAmount), "资金迁移");
		}else if(type == 3){
			// 资金成功转移
			/**
			 * 获取平台锁定账户余额
			 */
			UserAccount sdzhBalance = tradeDao.getUserBalance(userId, T6101_F03.SDZH.name());

			/**
			 * 获取新网往来账户余额
			 */
			UserAccount xwwlzhBalance = tradeDao.getUserBalance(userId, "XW_INVESTOR_WLZH");

			/**
			 * 减少锁定账户余额(不再将锁定账户的钱增加到新网往来账户)
			 */
			tradeDao.updateUserFundsAccountAmount(userId, sdzhBalance.getBalance().subtract(cashAmount), T6101_F03.SDZH.name());

			//添加转出者资金流水记录
			tradeDao.addT6102Record(sdzhBalance.getAccountId(), FeeCode.HBJE, xwwlzhBalance.getAccountId(), new Date(), new BigDecimal(0), cashAmount, sdzhBalance.getBalance().subtract(cashAmount), "资金迁移");

			//添加转入者资金流水记录
			/*tradeDao.addT6102Record(xwwlzhBalance.getAccountId(), FeeCode.HBJE, sdzhBalance.getAccountId(), new Date(), cashAmount, new BigDecimal(0), xwwlzhBalance.getBalance().add(cashAmount), "资金迁移");*/

		}
	}

	@Override
	public String getPlatformNo(int userId, String userRole) {
		return tradeDao.getPlatformNo(userId,userRole);
	}

	@Override
	public int addTransferApplication(int userId, String platformUserNo, BigDecimal amount, String status) {
		return tradeDao.addTransferApplication(userId, platformUserNo, amount, status);
	}

	@Override
	public Boolean getResultOfXWRequest(int requestId) {
		String state = tradeDao.getRechargeState(requestId);
		if(state != null){
			if(state.equals("CG")){
				return true;
			}
		}
		return false;
	}

	@Override
	public int updateTransferApplication(int transferApplicationId, String status) {
		return tradeDao.updateTransferApplication(transferApplicationId, "0");
	}

	@Override
	public void sendMessage(){
		String transferContent = Sender.get("sms.transfer.money.content");
		String phonenumStrings = Config.get("transfer.money.phonenum");
		String[] phone = phonenumStrings.split(",");
		for (String s: phone) {
			//发短信
			try {
				sendMsg(s, transferContent);
				System.out.println(s);
			} catch (Throwable t) {
				logger.error("发送短信失败", t);
			}
		}
	}

	/**
	 * 发送短信
	 *
	 * @param phoneNum
	 * @param content
	 */
	private void sendMsg(String phoneNum, String content) {
		Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//过期时间30分钟
		SendSmsRecord record = new SendSmsRecord();
		record.setContent(content);
		record.setCreateTime(new Date());
		record.setStatus("W");
		record.setType(0);
		record.setOutTime(outTime);
		sendSmsRecordDao.insertSendSmsRecord(record);

		SendSmsRecordExt recordExt = new SendSmsRecordExt();
		recordExt.setId(record.getId());
		recordExt.setPhoneNum(phoneNum);
		sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
	}
}
