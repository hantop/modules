package com.fenlibao.p2p.service.trade.bid.impl;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.dao.trade.order.FlowBidOrderDao;
import com.fenlibao.p2p.dao.trade.order.MakeALoanOrderDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.PrepayOrderDao;
import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.consts.TradeConsts;
import com.fenlibao.p2p.model.trade.entity.*;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6505;
import com.fenlibao.p2p.model.trade.entity.order.T6506;
import com.fenlibao.p2p.model.trade.entity.order.T6508;
import com.fenlibao.p2p.model.trade.entity.order.T6521;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.T6250_F07;
import com.fenlibao.p2p.model.trade.enums.T6250_F08;
import com.fenlibao.p2p.model.trade.enums.T6251_F08;
import com.fenlibao.p2p.model.trade.enums.T6252_F09;
import com.fenlibao.p2p.model.trade.enums.T6260_F07;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.util.api.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BidManageServiceImpl implements BidManageService{
	
	@Resource
	BidManageDao bidManageDao;
	
	@Resource
	TradeCommonDao tradeCommonDao;
	
	@Resource
	UserDao userDao;
	
	@Resource 
	OrderManageDao orderManageDao;
	
	@Resource
	FlowBidOrderDao flowBidOrderDao;
	
	@Resource
	CouponManageDao couponManageDao;
	
	@Resource
	MakeALoanOrderDao makeALoanOrderDao;
	
	@Resource
	RepayOrderDao repayOrderDao;
	
	@Resource
	PrepayOrderDao prepayOrderDao;
	
	protected static final int DECIMAL_SCALE = 9;
	
	/**
	 * 标发布
	 */
	@Override
	public void releaseBid(int loanId) throws Exception {
		T6230 t6230=bidManageDao.getBidById(loanId);
        Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
        Calendar raiseDeadline = Calendar.getInstance();
        raiseDeadline.setTime(currentTimestamp);
        raiseDeadline.add(Calendar.DAY_OF_YEAR,t6230.F08);
        raiseDeadline.set(Calendar.HOUR_OF_DAY, 24);
        raiseDeadline.set(Calendar.MINUTE, 0);
        raiseDeadline.set(Calendar.SECOND, 0);
		
		Map<String,Object> params=new HashMap<>();
		params.put("F20", T6230_F20.TBZ.name());
		if(t6230.F22==null){
			params.put("F22", currentTimestamp);
		}
		params.put("F31", new Timestamp(raiseDeadline.getTimeInMillis()));
		params.put("F35", null);
		params.put("F01", t6230.F01);
		bidManageDao.releaseBid(params);
	}

	@Transactional
	@Override
	public void flowBid(int loanId, String flowNum) throws Exception {
		// 标的详情,锁定标
		T6230 t6230 = bidManageDao.getBidById(loanId);
		if (t6230 == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if (T6230_F20.TBZ != t6230.F20 && T6230_F20.DFK != t6230.F20) {
			throw new TradeException(TradeResponseCode.TRADE_FLOW_CONDITIONS_NOT_SATISFIED);
		}
		
		Map<Integer,AssetAccount> tzrSdzhMap=new HashMap<>();
		Map<Integer,AssetAccount> tzrWlzhMap=new HashMap<>();
		List<CapitalFlow> t6102sToInsert = new ArrayList<>();
		List<T6250> t6250sToUpdate = new ArrayList<>();
		BigDecimal purchaseAmount=BigDecimal.ZERO;
		List<Integer> userCouponsToUpdate=new ArrayList<>();
		Timestamp currentTime = tradeCommonDao.getCurrentTimestamp();
		List<T6501> t6501List= orderManageDao.getByFlowNo(flowNum);
		for(T6501 t6501:t6501List){
			// 订单详情
			T6508 t6508 = flowBidOrderDao.get(t6501.F01);
			if (t6508 == null) {
				throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
			}
			// 锁定投标记录
			T6250 t6250 =bidManageDao.getTenderRecordById(t6508.F03);
			if (t6250 == null) {
				throw new TradeException(TradeResponseCode.BID_INVEST_RECORD_NOT_EXIST);
			}
			if (t6250.F07 == T6250_F07.S) {
				throw new TradeException(TradeResponseCode.TRADE_OPERATION_REPEAT);
			}
			// 锁定出借人锁定账户
			AssetAccount tzr_sdzh = tzrSdzhMap.get(t6508.F02);
			if (tzr_sdzh==null){
			    tzr_sdzh = userDao.getFundAccount(t6508.F02, T6101_F03.HXSDZH);
			}
			if (tzr_sdzh == null) {
				throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
			}
			// 锁定出借人往来账户
			AssetAccount tzr_wlzh = tzrWlzhMap.get(t6508.F02);
			if(tzr_wlzh == null){
			    tzr_wlzh = userDao.getFundAccount(t6508.F02, T6101_F03.HXWLZH);
			}
			if (tzr_wlzh == null) {
				throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
			}
			{
				// 扣减出借人锁定账户
				CapitalFlow t6102 = new CapitalFlow();
				t6102.F02 = tzr_sdzh.F01;
				t6102.F03 = TradeFeeCode.TZ_CX;
				t6102.F04 = tzr_wlzh.F01;
				t6102.F05 = currentTime;
				t6102.F07 = t6250.F04;
				t6102.F08 = tzr_sdzh.F06;
				t6102.F09 = String.format("撤销散标出借:%s", t6508.F03);
				t6102sToInsert.add(t6102);
			}
			{
				// 增加出借人往来账户
				CapitalFlow t6102 = new CapitalFlow();
				t6102.F02 = tzr_wlzh.F01;
				t6102.F03 = TradeFeeCode.TZ_CX;
				t6102.F04 = tzr_sdzh.F01;
				t6102.F05 = currentTime;
				t6102.F06 = t6250.F04;
				t6102.F08 = tzr_wlzh.F06;
				t6102.F09 = String.format("撤销散标出借:%s", t6508.F03);
				t6102sToInsert.add(t6102);
			}
			// 更新投标记录为取消
			t6250.F07=T6250_F07.S;
			t6250sToUpdate.add(t6250);
			//累加购买金额
			purchaseAmount=purchaseAmount.add(t6250.F04);
			//退还用户使用的加息券
			UserCoupon 	userCoupon=couponManageDao.selectUserCouponByTenderId(t6508.F03);
			if(userCoupon!=null){
				if(2==userCoupon.couponStatus){
					userCouponsToUpdate.add(t6508.F03);
				}
			}
		}
		//批量插入和更新
		for(T6250 item:t6250sToUpdate){
	        Map<String, Object> params = new HashMap<>(2);
	        params.put("F01", item.F01);
	        params.put("F07", item.F07.name());
			bidManageDao.updateTenderRecord(params);
		}
		tradeCommonDao.insertT6102s(t6102sToInsert);
		for(int tenderId:userCouponsToUpdate){
			couponManageDao.returnUserCoupon(tenderId);
		}
		
		if (T6230_F20.TBZ == t6230.F20) {
			// 更新可投金额
			t6230.F07 = t6230.F07.add(purchaseAmount);
			Map<String,Object> paramsT6230_1=new HashMap<>();
			paramsT6230_1.put("F01", t6230.F01);
			paramsT6230_1.put("F07", t6230.F07);
			bidManageDao.updateBid(paramsT6230_1);
		} else if (T6230_F20.DFK == t6230.F20) {
			// 判断是否全部出借均已取消,是则更新标状态为已流标
			Map<String,Object> paramsT6250=new HashMap<>();
			paramsT6250.put("F02", t6230.F01);
			paramsT6250.put("F07", "F");
			List<T6250> t6250List= bidManageDao.getTenderRecord(paramsT6250);

			if (t6250List.isEmpty()) {
				Map<String,Object> paramsT6230_2=new HashMap<>();
				paramsT6230_2.put("F01", t6230.F01);
				paramsT6230_2.put("F20", "YLB");
				bidManageDao.updateBid(paramsT6230_2);
				Map<String,Object> paramsT6231=new HashMap<>();
				paramsT6231.put("F01", t6230.F01);
				paramsT6231.put("F15", currentTime);
				bidManageDao.updateBidExInfo(paramsT6231);
			}
		}
	}

	@Transactional
	@Override
	public void makeALoan(int loanId, String flowNum,String ptwlzhCode) throws Exception {
		List<CapitalFlow> t6102sToInsert = new ArrayList<>();
		List<T6251> t6251sToInsert = new ArrayList<>();
		List<T6250> t6250sToUpdate = new ArrayList<>();
		Map<Integer, AssetAccount> tzrSdAccountMap = new HashMap<>();
		Map<Integer, AssetAccount> tzrWlAccountMap = new HashMap<>();

		// 查询标信息
		T6230 t6230 = bidManageDao.getBidById(loanId);
		if (t6230 == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if (t6230.F20 != T6230_F20.DFK) {
			throw new TradeException(TradeResponseCode.TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED);
		}
		// 锁定借款人往来账户
		AssetAccount jkr_wlzh = userDao.getFundAccount(t6230.F02, T6101_F03.HXWLZH);
		if (jkr_wlzh == null) {
			throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
		}
		// 平台往来账户信息
		AssetAccount ptwlzh = userDao.getFundAccountByF04(ptwlzhCode);
		if (ptwlzh == null) {
			throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
		}

		String comment = "放款";
		// 开始结束时间
		Date currentDate = tradeCommonDao.getCurrentDate();// 数据库当前日期
		Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
		Date interestDate = new Date(currentDate.getTime() + t6230.F19 * 86400000);
		Date endDate;
		if (t6230.F09 == 0) { // 按天借款
			endDate = new Date(DateUtil.dateAdd(interestDate.getTime(), t6230.F32));
		} else {
			endDate = new Date(DateUtil.rollNaturalMonth(interestDate.getTime(), t6230.F09));
		}
		List<T6501> t6501List= orderManageDao.getByFlowNo(flowNum);
		for (T6501 t6501: t6501List) {

			// 查询订单详情
			T6505 t6505 = makeALoanOrderDao.get(t6501.F01);
			if (t6505 == null) {
				throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
			}
			// 查询投标记录
			T6250 t6250 = bidManageDao.getTenderRecordById(t6505.F04);
			if (t6250 == null) {
				throw new TradeException(TradeResponseCode.BID_INVEST_RECORD_NOT_EXIST);
			}
			// 锁定出借人锁定账户
			AssetAccount tzr_sdzh = tzrSdAccountMap.get(t6505.F02);
			if (tzr_sdzh == null) {
				tzr_sdzh = userDao.getFundAccount(t6505.F02, T6101_F03.HXSDZH);
				tzrSdAccountMap.put(t6505.F02, tzr_sdzh);
			}
			if (tzr_sdzh == null) {
				throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
			}

			// 锁定出借人往来账户
			AssetAccount tzr_wlzh = tzrWlAccountMap.get(t6505.F02);
			if (tzr_wlzh == null) {
				tzr_wlzh = userDao.getFundAccount(t6505.F02, T6101_F03.HXWLZH);
				tzrWlAccountMap.put(t6505.F02, tzr_wlzh);
			}
			if (tzr_wlzh == null) {
				throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
			}

			// 扣减锁定账户
			CapitalFlow t6102tzr = new CapitalFlow();
			t6102tzr.F02 = tzr_sdzh.F01;
			t6102tzr.F03 = TradeFeeCode.TZ;
			t6102tzr.F04 = jkr_wlzh.F01;
			t6102tzr.F07 = t6505.F05;
			t6102tzr.F08 = tzr_sdzh.F06;
			t6102tzr.F09 = comment;
			t6102tzr.F12 = loanId;
			t6102sToInsert.add(t6102tzr);

			// 增加借款人往来账户资金
			CapitalFlow t6102jkr = new CapitalFlow();
			t6102jkr.F02 = jkr_wlzh.F01;
			t6102jkr.F03 = TradeFeeCode.JK;
			t6102jkr.F04 = tzr_sdzh.F01;
			t6102jkr.F06 = t6505.F05;
			t6102jkr.F08 = jkr_wlzh.F06;
			t6102jkr.F09 = comment;
			t6102jkr.F12 = loanId;
			t6102sToInsert.add(t6102jkr);

			// 插入债权
			T6251 t6251 = new T6251();
			t6251.F02 = zqCode(t6250.F01);
			t6251.F03 = t6230.F01;
			t6251.F04 = t6505.F02;
			t6251.F05 = t6250.F04;
			t6251.F06 = t6250.F04;
			t6251.F07 = t6250.F04;
			t6251.F08 = T6251_F08.F;
			t6251.F09 = currentDate;
			t6251.F10 = new Date(currentDate.getTime() + t6230.F19 * 86400000L);
			t6251.F11 = t6250.F01;
			t6251sToInsert.add(t6251);
			// 更新投标记录为已放款
			t6250sToUpdate.add(t6250);
		}

		// 批量插入6251，更新6250
		if(!t6251sToInsert.isEmpty()){
			bidManageDao.insertDebt(t6251sToInsert);
		}
		for(T6250 e:t6250sToUpdate){
			Map<String,Object> param=new HashMap<>();
			param.put("F08", T6250_F08.S.name());
			param.put("F01", e.F01);
			bidManageDao.updateTenderRecord(param);
		}

		// 判断是否全部出借均已成功放款
		Map<String,Object> p6250=new HashMap<>();
		p6250.put("F02", loanId);
		p6250.put("F07", "F");
		p6250.put("F08", "F");
		List<T6250> notYetList= bidManageDao.getTenderRecord(p6250);

		if (notYetList.isEmpty()) {
			// 记录起息日和结束日,放款时间
			Map<String,Object> p6231=new HashMap<>();
			p6231.put("F01", loanId);
			p6231.put("F12", currentTimestamp);
			p6231.put("F17", interestDate);
			p6231.put("F18", endDate);
			bidManageDao.updateBidExInfo(p6231);
			// 生成还款计划
			hkjh(t6230, interestDate, endDate, ptwlzh, t6102sToInsert);
			// 更新标状态为还款中
			Map<String,Object> p6230=new HashMap<>();
			p6230.put("F20", "HKZ");
			p6230.put("F01", loanId);
			bidManageDao.updateBid(p6230);
			// 收成交服务费
			T6238 t6238 = bidManageDao.getBidRateById(loanId);
			if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal fwf = getCJFee(t6238, t6230);
				{
					// 平台资金增加
					CapitalFlow t6102 = new CapitalFlow();
					t6102.F02 = ptwlzh.F01;
					t6102.F03 = TradeFeeCode.CJFWF;
					t6102.F04 = jkr_wlzh.F01;
					t6102.F06 = fwf;
					t6102.F08 = ptwlzh.F06;
					t6102.F09 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
					t6102.F12 = loanId;
					t6102sToInsert.add(t6102);
				}
				{
					// 借款人账户减少
					CapitalFlow t6102 = new CapitalFlow();
					t6102.F02 = jkr_wlzh.F01;
					t6102.F03 = TradeFeeCode.CJFWF;
					t6102.F04 = ptwlzh.F01;
					t6102.F07 = fwf;
					t6102.F08 = jkr_wlzh.F06;
					t6102.F09 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
					t6102.F12 = loanId;
					t6102sToInsert.add(t6102);
				}
				// 平台代收手续费
				if (t6238.F08.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal dssxf = t6238.F08;
					{
						// 平台资金增加
						CapitalFlow t6102 = new CapitalFlow();
						t6102.F02 = ptwlzh.F01;
						t6102.F03 = TradeFeeCode.DSFWF;
						t6102.F04 = jkr_wlzh.F01;
						t6102.F06 = dssxf;
						t6102.F08 = ptwlzh.F06;
						t6102.F09 = String.format("散标代收手续费:%s，标题：%s", t6230.F25, t6230.F03);
						t6102.F12 = loanId;
						t6102sToInsert.add(t6102);
					}
					{
						// 借款人账户减少
						CapitalFlow t6102 = new CapitalFlow();
						t6102.F02 = jkr_wlzh.F01;
						t6102.F03 = TradeFeeCode.DSFWF;
						t6102.F04 = ptwlzh.F01;
						t6102.F07 = dssxf;
						t6102.F08 = jkr_wlzh.F06;
						t6102.F09 = String.format("散标代收手续费:%s，标题：%s", t6230.F25, t6230.F03);
						t6102.F12 = loanId;
						t6102sToInsert.add(t6102);
					}
				}
			}
		}

		// 批量插入6102
		if(!t6102sToInsert.isEmpty()){
			tradeCommonDao.insertT6102s(t6102sToInsert);
		}
	}
	
	protected String zqCode(int recordId) {
		DecimalFormat decimalFormat = new DecimalFormat("0000000000");
		return ("Z" + decimalFormat.format(recordId));
	}
	
	protected BigDecimal getCJFee(T6238 t6238, T6230 t6230) throws Exception {
		BigDecimal wgCjFee = BigDecimal.ZERO;
		wgCjFee = t6238.F02.multiply(t6230.F05.subtract(t6230.F07)).setScale(2, RoundingMode.HALF_UP);
		return wgCjFee;
	}
	
	protected void hkjh(T6230 t6230, Date interestDate, Date endDate, AssetAccount ptwlzh,List<CapitalFlow> t6102sToInsert) throws Exception {
		Map<String,Object> p6251=new HashMap<>();
		p6251.put("F03", t6230.F01);
		p6251.put("F08", "F");
		List<T6251> t6251s = bidManageDao.getDebts(p6251);
		switch (t6230.F10) {
			case DEBX: {
				for (T6251 t6251 : t6251s) {
					bidManageDao.insertRepayPlan(calZRY_DEBX(t6230, t6251, interestDate, endDate,ptwlzh));
				}
				break;
			}
			case MYFX: {
				for (T6251 t6251 : t6251s) {
					bidManageDao.insertRepayPlan(calZRY_MYFX(t6230, t6251, interestDate, endDate, ptwlzh));
				}
				break;
			}
			case YCFQ: {
				for (T6251 t6251 : t6251s) {
					bidManageDao.insertRepayPlan(calYCFQ(t6230, t6251, interestDate, endDate, ptwlzh));
				}
				break;
			}
			default:
				throw new RuntimeException("不支持的还款方式");
		}
	}
	
	private ArrayList<T6252> calZRY_DEBX(T6230 t6230, T6251 t6251, Date interestDate,Date endDate,AssetAccount ptwlzh) throws Exception{
		ArrayList<T6252> t6252s = new ArrayList<>();
		//月利率
		BigDecimal monthRate = t6230.F06.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
				DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
		//剩余本金
		BigDecimal remainTotal = t6251.F07;
		//每月本息和
		BigDecimal monthPayTotal = debx(t6251.F07, monthRate, t6230.F09);
		
		//加息券
		UserCoupon userCoupon=couponManageDao.selectUserCouponByTenderId(t6251.F11);
		//每月加息
		BigDecimal monthlyRaiseLx=BigDecimal.ZERO;
		if(userCoupon!=null){
		   //加息后月利率
		   BigDecimal monthRate2 = t6230.F06.add(userCoupon.scope).setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
				DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);	
		   //加息后每月本息和
		   BigDecimal monthPayTotal2 = debx(t6251.F07, monthRate2, t6230.F09);
           monthlyRaiseLx=monthPayTotal2.subtract(monthPayTotal);
		}
		
		for (int term = 1; term <= t6230.F09; term++) {
			Date date = new Date(DateUtil.rollNaturalMonth(interestDate.getTime(), term));
			//当月利息
			BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (1 == term) {
				Map<String,Object> p6231=new HashMap<>();
				p6231.put("F01", t6230.F01);
				p6231.put("F06", date);
				bidManageDao.updateBidExInfo(p6231);
			}
			{
				// 利息
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = t6230.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = TradeFeeCode.TZ_LX;
				t6252.F06 = term;
				if (t6230.F09 == term) {
					t6252.F07 = monthPayTotal.subtract(remainTotal).compareTo(BigDecimal.ZERO) > 0
							? monthPayTotal.subtract(remainTotal) : BigDecimal.ZERO;
				} else {
					t6252.F07 = interest;
				}
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				t6252s.add(t6252);
			}
			if(userCoupon!=null){
				//加息
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = ptwlzh.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = TradeFeeCode.TZ_JX;
				t6252.F06 = term;
				t6252.F07 = monthlyRaiseLx;
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				t6252s.add(t6252);				
			}
			{
				// 本金
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = t6230.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = TradeFeeCode.TZ_BJ;
				t6252.F06 = term;
				if (t6230.F09 == term) {
					t6252.F07 = remainTotal;
				} else {
					t6252.F07 = monthPayTotal.subtract(interest);
				}
				remainTotal = remainTotal.subtract(t6252.F07);
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				t6252s.add(t6252);
			}
		}
		return t6252s;
	}
	
	private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms) {
		BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
		return total.multiply(monthRate).multiply(tmp).divide(tmp.subtract(new BigDecimal(1)), 2,
				BigDecimal.ROUND_HALF_UP);
	}

	private ArrayList<T6252> calZRY_MYFX(T6230 t6230, T6251 t6251, Date interestDate,Date endDate, AssetAccount ptwlzh) throws Exception {
		ArrayList<T6252> t6252s = new ArrayList<>();
		BigDecimal monthes = new BigDecimal(12);
		// 总利息（不包括加息）
		BigDecimal totalExlastLx = (t6251.F07.multiply(t6230.F06).divide(monthes, DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(t6230.F09))).setScale(2, BigDecimal.ROUND_HALF_UP);

		UserCoupon userCoupon = couponManageDao.selectUserCouponByTenderId(t6251.F11);
		// 总加息
		BigDecimal totalRaiseLx = BigDecimal.ZERO;
		if (userCoupon != null) {
			totalRaiseLx = (t6251.F07.multiply(userCoupon.scope).divide(monthes, DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
					.multiply(new BigDecimal(t6230.F09))).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		for (int term = 1; term <= t6230.F09; term++) {
			Date date = new Date(DateUtil.rollNaturalMonth(interestDate.getTime(), term));
			if (1 == term) {
				Map<String,Object> p6231=new HashMap<>();
				p6231.put("F01", t6230.F01);
				p6231.put("F02", t6230.F09);
				p6231.put("F03", t6230.F09);
				p6231.put("F06", date);
				bidManageDao.updateBidExInfo(p6231);
			}
			{
				// 利息
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = t6230.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = TradeFeeCode.TZ_LX;
				t6252.F06 = term;
				t6252.F07 = t6251.F07.multiply(t6230.F06).divide(monthes, 2, BigDecimal.ROUND_DOWN);
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				if (term < t6230.F09) {
					totalExlastLx = totalExlastLx.subtract(t6252.F07);
				}
				if (term == t6230.F09) {
					t6252.F07 = totalExlastLx;
				}
				t6252s.add(t6252);
			}
			if (userCoupon != null) {
				// 加息
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = ptwlzh.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = 7022;
				t6252.F06 = term;
				t6252.F07 = t6251.F07.multiply(userCoupon.scope).divide(monthes, 2, BigDecimal.ROUND_DOWN);
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				if (term < t6230.F09) {
					totalRaiseLx = totalRaiseLx.subtract(t6252.F07);
				}
				if (term == t6230.F09) {
					t6252.F07 = totalRaiseLx;
				}
				t6252s.add(t6252);
			}
			if (term == t6230.F09) {
				// 本金
				T6252 t6252 = new T6252();
				t6252.F02 = t6230.F01;
				t6252.F03 = t6230.F02;
				t6252.F04 = t6251.F04;
				t6252.F05 = TradeFeeCode.TZ_BJ;
				t6252.F06 = term;
				t6252.F07 = t6251.F07;
				t6252.F08 = date;
				t6252.F09 = T6252_F09.WH;
				t6252.F10 = null;
				t6252.F11 = t6251.F01;
				t6252s.add(t6252);
			}
		}
		return t6252s;
	}
	
	private ArrayList<T6252> calYCFQ(T6230 t6230, T6251 t6251, Date interestDate, Date endDate,
			AssetAccount ptwlzh) throws Exception {
		int days = t6230.F32; // 借款天数
		// 更新下个还款日
		Map<String,Object> p6231=new HashMap<>();
		p6231.put("F01", t6230.F01);
		p6231.put("F06", endDate);
		bidManageDao.updateBidExInfo(p6231);

		UserCoupon userCoupon = couponManageDao.selectUserCouponByTenderId(t6251.F11);

		ArrayList<T6252> t6252s = new ArrayList<>();
		{
			// 利息
			T6252 t6252 = new T6252();
			t6252.F02 = t6230.F01;
			t6252.F03 = t6230.F02;
			t6252.F04 = t6251.F04;
			t6252.F05 = TradeFeeCode.TZ_LX;
			t6252.F06 = 1;
			t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
			if (t6230.F09 == 0) {
				t6252.F07 = t6252.F07.multiply(t6230.F06).multiply(new BigDecimal(days)).divide(
						new BigDecimal(365), DECIMAL_SCALE,BigDecimal.ROUND_HALF_UP);
			} else {
				t6252.F07 = t6252.F07.multiply(t6230.F06).multiply(new BigDecimal(t6230.F09)).divide(new BigDecimal(12),
						DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
			}

			t6252.F08 = endDate;
			t6252.F09 = T6252_F09.WH;
			t6252.F10 = null;
			t6252.F11 = t6251.F01;
			t6252s.add(t6252);
		}
		if (userCoupon != null) {
			// 加息
			T6252 t6252 = new T6252();
			t6252.F02 = t6230.F01;
			t6252.F03 = ptwlzh.F02;
			t6252.F04 = t6251.F04;
			t6252.F05 = TradeFeeCode.TZ_JX;
			t6252.F06 = 1;
			t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
			if (t6230.F09 == 0) {
				t6252.F07 = t6252.F07.multiply(userCoupon.scope).multiply(new BigDecimal(days)).divide(
						new BigDecimal(365), DECIMAL_SCALE,
						BigDecimal.ROUND_HALF_UP);
			} else {
				t6252.F07 = t6252.F07.multiply(userCoupon.scope).multiply(new BigDecimal(t6230.F09))
						.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
			}

			t6252.F08 = endDate;
			t6252.F09 = T6252_F09.WH;
			t6252.F10 = null;
			t6252.F11 = t6251.F01;
			t6252s.add(t6252);
		}
		{
			// 本金
			T6252 t6252 = new T6252();
			t6252.F02 = t6230.F01;
			t6252.F03 = t6230.F02;
			t6252.F04 = t6251.F04;
			t6252.F05 = TradeFeeCode.TZ_BJ;
			t6252.F06 = 1;
			t6252.F07 = t6251.F07;
			t6252.F08 = endDate;
			t6252.F09 = T6252_F09.WH;
			t6252.F10 = null;
			t6252.F11 = t6251.F01;
			t6252s.add(t6252);
		}
		return t6252s;
	}

	/**
	 * 因为加息是由平台付，华兴还不支持，所以这里的还款并没有处理还款记录里面的加息，就视为已经还款。
	 * @param loanId
	 * @param flowNum
	 * @param ptwlzhCode
	 * @throws Exception
	 */
	@Transactional
	@Override
	public void repay(int loanId, String flowNum, String ptwlzhCode) throws Exception {
		List<CapitalFlow> t6102sToInsert = new ArrayList<>();
		List<T6252> t6252sToUpdate = new ArrayList<>();
		List<T6251> t6251sToUpdate = new ArrayList<>();
		Map<Integer, AssetAccount> skrAccountMap = new HashMap<>();
		Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
		// 锁定标
		T6230 t6230 = bidManageDao.getBidById(loanId);
		if (t6230 == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
	    // 锁定还款人账户
		AssetAccount hkrAcount = userDao.getFundAccount(t6230.F02, T6101_F03.HXWLZH);
		if (hkrAcount == null) {
			throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
		}
		// 当前期数
		int term = 0;
		// 构建备注
		String comment = "还款 ";
		List<T6501> t6501List= orderManageDao.getByFlowNo(flowNum);
		for (T6501 order : t6501List) {
			if (order != null) {
				//锁订单
				orderManageDao.get(order.F01);
				// 查询还款订单（t6506是t6501在还款方面的补充信息）
				T6506 t6506 = repayOrderDao.get(order.F01);
				if (t6506 == null) {
					throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
				}
				term = t6506.F05;
				// 查询还款记录
				Map<String,Object> p6252=new HashMap<>();
				p6252.put("F05", t6506.F07);
				p6252.put("F06", t6506.F05);
				p6252.put("F11", t6506.F04);
				T6252 t6252 = bidManageDao.getAndLockRepayPlan(p6252);
				if (t6252 == null) {
					throw new TradeException(TradeResponseCode.BID_REPAY_PLAN_NOT_EXIST);
				}

				// 已还的跳过
				if (t6252.F09 == T6252_F09.YH) {
					continue;
				}

				// 锁定收款人往来账户
				AssetAccount skrAcount = skrAccountMap.get(t6252.F04);
				if (skrAcount == null) {
					skrAcount = userDao.getFundAccount(t6252.F04, T6101_F03.HXWLZH);
					skrAccountMap.put(t6252.F04, skrAcount);
				}
				if (skrAcount == null) {
					throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
				}
				// 还款人金额减少
				CapitalFlow t6102hkr = new CapitalFlow();
				t6102hkr.F02 = hkrAcount.F01;
				t6102hkr.F03 = t6252.F05;
				t6102hkr.F04 = skrAcount.F01;
				t6102hkr.F07 = t6506.F06;
				t6102hkr.F08 = hkrAcount.F06;
				t6102hkr.F09 = comment;
				t6102hkr.F12 = t6230.F01;
				t6102sToInsert.add(t6102hkr);
				// 收款人金额增加
				CapitalFlow t6102skr = new CapitalFlow();
				t6102skr.F02 = skrAcount.F01;
				t6102skr.F03 = t6252.F05;
				t6102skr.F04 = hkrAcount.F01;
				t6102skr.F06 = t6506.F06;
				t6102skr.F08 = skrAcount.F06;
				t6102skr.F09 = comment;
				t6102skr.F12 = t6230.F01;
				t6102sToInsert.add(t6102skr);
				// 将还款计划状态改为已还
				t6252sToUpdate.add(t6252);

				if (t6506.F07 == TradeFeeCode.TZ_BJ) {
					// 更新债权持有金额
					T6251 t6251 = bidManageDao.getDebtById(t6506.F04);
					if (t6251 == null) {
						throw new RuntimeException("债权记录不存在");
					}
					t6251.F07 = t6251.F07.subtract(t6506.F06);
					if (t6251.F07.compareTo(BigDecimal.ZERO) <= 0) {
						t6251.F07 = BigDecimal.ZERO;
					}
					t6251sToUpdate.add(t6251);
				}

			}
		}
		// 执行批量更新和插入
		tradeCommonDao.insertT6102s(t6102sToInsert);
		for(T6252 e:t6252sToUpdate){
			Map<String,Object> p6252_1=new HashMap<>(3);
			p6252_1.put("F01", e.F01);
			p6252_1.put("F09", "YH");
			p6252_1.put("F10", currentTimestamp);
			bidManageDao.updateRepayPlan(p6252_1);
		}
		for(T6251 e:t6251sToUpdate){
			Map<String,Object> p6251=new HashMap<>(2);
			p6251.put("F01", loanId);
			p6251.put("F07", e.F07);
			bidManageDao.updateDebt(p6251);
		}

		// 查询本期是否还完,更新下次还款日期和剩余期数
		Map<String,Object> p6252_2=new HashMap<>(4);
		p6252_2.put("F02", loanId);
		List<Integer> F05List=new ArrayList<>();
		F05List.add(TradeFeeCode.TZ_BJ);
		F05List.add(TradeFeeCode.TZ_LX);
		F05List.add(TradeFeeCode.TZ_FX);
		//F05List.add(TradeFeeCode.TZ_YQ_SXF);
		p6252_2.put("F05List", F05List);
		p6252_2.put("F06", term);
		p6252_2.put("F09", "WH");
		List<T6252> currentPeriodRemainList = bidManageDao.getRepayPlan(p6252_2);
		if (currentPeriodRemainList .isEmpty()) {
			// 剩余期数减1,更新下次还款日期
			Map<String, Object> p6252_3 = new HashMap<>(2);
			p6252_3.put("F02", loanId);
			p6252_3.put("F06", term + 1);
			List<T6252> nextTermList = bidManageDao.getRepayPlan(p6252_3);
			int num = 0;
			T6231 t6231 = bidManageDao.getBidExInfoById(loanId);
			num = t6231.F03;
			if (num > 0) {
				Map<String, Object> p6231_1 = new HashMap<>(4);
				p6231_1.put("F01", loanId);
				p6231_1.put("F03", num - 1);
				if(!nextTermList.isEmpty()){
					p6231_1.put("F06", nextTermList.get(0).F08);
					p6231_1.put("F19", "F");
				}
				bidManageDao.updateBidExInfo(p6231_1);
			}

		}

		// 查询标是否还完
		Map<String, Object> p6252 = new HashMap<>(3);
		p6252.put("F02", loanId);
		List<Integer> F05list=new ArrayList<>();
		F05list.add(TradeFeeCode.TZ_BJ);
		F05list.add(TradeFeeCode.TZ_LX);
		F05list.add(TradeFeeCode.TZ_FX);
		//F05List.add(TradeFeeCode.TZ_YQ_SXF);
		p6252.put("F05List", F05list);
		p6252.put("F09", "WH");
		List<T6252> remainList = bidManageDao.getRepayPlan(p6252);
		if (remainList.isEmpty()) {
			Map<String, Object> p6230 = new HashMap<>(2);
			p6230.put("F01", loanId);
			p6230.put("F20", "YJQ"); // 已结请
			bidManageDao.updateBid(p6230);
			Map<String, Object> p6231_2 = new HashMap<>(2);
			p6231_2.put("F01", loanId);
			p6231_2.put("F13", currentTimestamp); // 结清时间
			bidManageDao.updateBidExInfo(p6231_2);
		}
		
	}
	
	@Transactional
	@Override
	public int tender(int bidId, BigDecimal amount, int userId) throws Exception {
		//对于其他（特殊账户、起投金额、出借金额整数、新手限制）等条件的校验放在外面，这里只做投标记录
		if (bidId <= 0) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
			throw new TradeException(TradeResponseCode.BID_INVESTMENT_AMOUNT_GT0);
		}
		//是否有逾期,逾期不能投标
		int count = userDao.countOverdue(userId);
		if (count > 0) {
			throw new TradeException(TradeResponseCode.BIT_LOAN_OVERDUE);
		}

		T6230 bidInfo = bidManageDao.getBidById(bidId);
		validateTender(bidInfo, amount);
		if (TradeConsts.BID_SFZJKT && userId == bidInfo.F02) {//借款人是否可投
			throw new TradeException(TradeResponseCode.BID_BORROWER_CANNOT_INVESTMENT);
		}
		T6501 order = new T6501();
		order.F02 = TradeOrderType.BID.orderType();
		order.F03 = T6501_F03.DTJ;
		order.F07 = T6501_F07.YH;
		order.F08 = userId;
		orderManageDao.add(order);
		//添加投标订单
		T6504 tenderOrder = new T6504();
		tenderOrder.F01 = order.F01;
		tenderOrder.F02 = userId;
		tenderOrder.F03 = bidId;
		tenderOrder.F04 = amount;
		orderManageDao.addTenderOrder(tenderOrder);
		return order.F01;
	}

	@Transactional
	@Override
	public void prepay(int loanId, String flowNum, String ptwlzhCode) throws Exception {
		List<CapitalFlow> t6102sToInsert = new ArrayList<>();
		List<T6521> t6252sToUpdate = new ArrayList<>();
		List<T6251> t6251sToUpdate = new ArrayList<>();
		List<T6260> t6260sToUpdate = new ArrayList<>();
		Map<Integer,AssetAccount> skrAccountMap=new HashMap<>();
		Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();

		// 锁定标
		T6230 t6230 = bidManageDao.getBidById(loanId);
		if (t6230 == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}

		// 锁定还款人往来账户
		AssetAccount hkrAcount = userDao.getFundAccount(t6230.F02, T6101_F03.HXWLZH);
		if (hkrAcount == null) {
			throw new RuntimeException("还款人往来账户不存在");
		}
		// 构建备注
		String comment = "回款";
		List<T6501> t6501List= orderManageDao.getByFlowNo(flowNum);
		for (T6501 order : t6501List) {
			if (order != null) {
				//锁订单
				orderManageDao.get(order.F01);
			    T6521 t6521 = prepayOrderDao.get(order.F01);
				// 查询还款记录
			    Map<String,Object> p6252=new HashMap<>();
			    p6252.put("F05", t6521.F07);
			    p6252.put("F06", t6521.F05);
			    p6252.put("F11", t6521.F04);
			    T6252 t6252=bidManageDao.getAndLockRepayPlan(p6252);
				if (t6252 == null) {
					throw new RuntimeException("还款记录不存在");
				}
				if (t6252.F09 == T6252_F09.YH) {
					continue;
				}

				// 锁定收款人往来账户
				AssetAccount skrAcount = null;
				if (t6521.F09 == t6521.F02) {
					skrAcount = hkrAcount;
				} else {
					skrAcount = skrAccountMap.get(t6521.F02);
					if (skrAcount == null) {
						skrAcount = userDao.getFundAccount(t6521.F02, T6101_F03.HXWLZH);
						skrAccountMap.put(t6521.F02, skrAcount);
					}
				}
				if (skrAcount == null) {
					throw new UserException(UserResponseCode.USER_ACCOUNT_NOT_EXIST);
				}
				// 还款
				CapitalFlow t6102hkr = new CapitalFlow();
				t6102hkr.F02 = hkrAcount.F01;
				t6102hkr.F03 = t6521.F07;
				t6102hkr.F04 = skrAcount.F01;
				t6102hkr.F07 = t6521.F06;
				t6102hkr.F08 = hkrAcount.F06;
				t6102hkr.F09 = comment;
				t6102hkr.F12 = t6230.F01;
				t6102sToInsert.add(t6102hkr);

				CapitalFlow t6102skr = new CapitalFlow();
				t6102skr.F02 = skrAcount.F01;
				t6102skr.F03 = t6521.F07;
				t6102skr.F04 = hkrAcount.F01;
				t6102skr.F06 = t6521.F06;
				t6102skr.F08 = skrAcount.F06;
				t6102skr.F09 = comment;
				t6102skr.F12 = t6230.F01;
				t6102sToInsert.add(t6102skr);
				t6252sToUpdate.add(t6521);

				// 更新债权持有金额
				if (t6521.F07 == TradeFeeCode.TZ_BJ) {
					// 锁定债权信息
					T6251 t6251 = bidManageDao.getDebtById(t6521.F04);
					if (t6251 == null) {
						throw new RuntimeException("债权记录不存在");
					}
					t6251.F07 = t6251.F07.subtract(t6521.F06);
					if (t6251.F07.compareTo(BigDecimal.ZERO) <= 0) {
						t6251.F07 = BigDecimal.ZERO;
					}
					t6251sToUpdate.add(t6251);
					if (T6251_F08.S == t6251.F08) {
						// 更新债权转让的状态
						Map<String,Object> p6260=new HashMap<>();
						p6260.put("F02", t6251.F01);
						p6260.put("F07NotEqual", T6260_F07.YJS.name());
						T6260 t6260 = bidManageDao.getDebtTransferApply(p6260);
						if (t6260 != null) {
							t6260sToUpdate.add(t6260);
						}
					}

				}

			}
			// 执行批量更新和插入
			tradeCommonDao.insertT6102s(t6102sToInsert);
			for(T6521 e:t6252sToUpdate){
				Map<String,Object> p6252_1=new HashMap<>(3);
				p6252_1.put("F05", e.F07);
				p6252_1.put("F06", e.F05);
				p6252_1.put("F11", e.F04);
				T6252 t6252= bidManageDao.getAndLockRepayPlan(p6252_1);
				Map<String,Object> p6252_2=new HashMap<>(3);
				p6252_2.put("F01", t6252.F01);
				p6252_2.put("F09", "YH");
				p6252_2.put("F10", currentTimestamp);
				bidManageDao.updateRepayPlan(p6252_2);
			}
			for(T6251 e:t6251sToUpdate){
				Map<String,Object> p6251=new HashMap<>(2);
				p6251.put("F01", loanId);
				p6251.put("F07", e.F07);
				bidManageDao.updateDebt(p6251);
			}
			for(T6260 e:t6260sToUpdate){
				Map<String,Object> p6260_2=new HashMap<>();
				p6260_2.put("F01", e.F01);
				p6260_2.put("F06", currentTimestamp);
				p6260_2.put("F07", T6260_F07.YJS.name());
				bidManageDao.updateDebtTransferApply(p6260_2);
			}

			// 如果整个标已经还完
			Map<String, Object> p6252_2 = new HashMap<>(3);
			p6252_2.put("F02", loanId);
			List<Integer> F05list=new ArrayList<>();
			F05list.add(TradeFeeCode.TZ_BJ);
			F05list.add(TradeFeeCode.TZ_LX);
			F05list.add(TradeFeeCode.TZ_WYJ);
			p6252_2.put("F05List", F05list);
			p6252_2.put("F09", "WH");
			List<T6252> remainList = bidManageDao.getRepayPlan(p6252_2);
			if (remainList.isEmpty()) {
				Map<String, Object> p6230 = new HashMap<>(2);
				p6230.put("F01", loanId);
				p6230.put("F20", "YJQ"); // 已结请
				bidManageDao.updateBid(p6230);
				Map<String, Object> p6231_2 = new HashMap<>(2);
				p6231_2.put("F01", loanId);
				p6231_2.put("F03", 0);
				p6231_2.put("F13", currentTimestamp); // 结清时间
				bidManageDao.updateBidExInfo(p6231_2);
			}
		}
	}

    @Override
	public T6504 getTenderOrder(int orderId) {
		return bidManageDao.getTenderOrder(orderId);
	}

	private void validateTender(T6230 bidInfo, BigDecimal amount) {
		if (bidInfo == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if (TradeConsts.T6230_F38_DM != bidInfo.F38) {
			throw new TradeException(TradeResponseCode.BID_TYPE_NOT_DM);
		}
		if (T6230_F20.TBZ != bidInfo.F20) {
			throw new TradeException(TradeResponseCode.BID_FULLED);
		}
		if (amount.compareTo(bidInfo.F07) > 0) {
			throw new TradeException(TradeResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);//投标金额大于可投金额
		}
		if (bidInfo.F07.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
			throw new TradeException(TradeResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //剩余可投金额不能低于最低起投金额
		}
	}

	@Override
	public T6230 process(int bidId, BigDecimal amount) throws Exception {
		T6230 bidInfo = bidManageDao.getBidById(bidId);
		validateTender(bidInfo, amount);
		Map<String, Object> bidParams = new HashMap<>(2);
		bidParams.put("F01", bidInfo.F01);
		//这里先扣减可投金额，如果用户去到华兴放弃出借或出借失败，确定最后状态后再加回来
		//有个很不好的体验，如果有多个用户去到华兴放弃的话，就会造成可投金额很长时间不能确认，华兴页面超时是20min
		bidParams.put("F07", bidInfo.F07.subtract(amount));
		bidManageDao.updateBid(bidParams);
		return bidInfo;
	}
	
	public List<T6252> getRepayList(int loanId,int term){
		Map<String,Object> p1=new HashMap<>(4);
		p1.put("F02", loanId);
		List<Integer> F05List=new ArrayList<>();
		F05List.add(TradeFeeCode.TZ_BJ);
		F05List.add(TradeFeeCode.TZ_LX);
		F05List.add(TradeFeeCode.TZ_FX);
		F05List.add(TradeFeeCode.TZ_YQ_SXF);
		F05List.add(TradeFeeCode.TZ_WYJ);
		p1.put("F05List", F05List);
		p1.put("F06", term);
		p1.put("F09", "WH");
		List<T6252> t6252List=bidManageDao.getRepayPlan(p1);
		return t6252List;
	}
	
	@Transactional
	public List<T6252> getPrepayList(int loanId, int term) throws Exception {
		List<T6252> list = null;
		T6230 t6230 = bidManageDao.getBidById(loanId);
		Date currentDate=tradeCommonDao.getCurrentDate();
		Timestamp currentTimestamp =tradeCommonDao.getCurrentTimestamp();
		
		// 本期的本金和利息订单
		Map<String,Object> p6252_1=new HashMap<>();
		p6252_1.put("F02", loanId);
		p6252_1.put("F06", term);
		p6252_1.put("F09", "WH");
		List<Integer> f05List=new ArrayList<>();
		f05List.add(TradeFeeCode.TZ_BJ);
		f05List.add(TradeFeeCode.TZ_LX);
		p6252_1.put("F05List", f05List);
		list=bidManageDao.getRepayPlan(p6252_1);
		T6231 t6231=bidManageDao.getBidExInfoById(loanId);
		Date starttime = t6231.F17;
		Date endtime = t6231.F18;
		int daysOfCycle = DateUtil.daysOfTwo(starttime, endtime);
		//从起息到还款日为止的借款总天数
		int loanDays = DateUtil.daysOfTwo(starttime, currentDate);
        //针对次日启息和当天按提前还款
        if(loanDays<0){
        	loanDays=0;
        }

        // 修改利息
		int preTermsDaySum= DateUtil.sumDaysOfNaturalMonth(starttime, term-1);
		int  currentTermLoanDays=loanDays-preTermsDaySum;
		modifyInterest(daysOfCycle,currentTermLoanDays,list,t6230,currentTimestamp);

		// 剩余本金还款订单
		Map<String,Object> p6252_2=new HashMap<>();
		p6252_2.put("F02", loanId);
		p6252_2.put("F05", TradeFeeCode.TZ_BJ);
		p6252_2.put("F06NotEqual", term);
		p6252_2.put("F09", "WH");
		List<T6252> list2=bidManageDao.getRepayPlan(p6252_2);
		list.addAll(list2);
		
		// 算出违约金
		List<T6252> t6252sToInsert=new ArrayList<>();
		int leftDays=daysOfCycle-loanDays;
		int lastNaturalMonthDays=DateUtil.daysOfLastNaturalMonth(starttime,daysOfCycle);
		
		Map<String,Object> p6252_3=new HashMap<>();
		p6252_3.put("F02", loanId);
		p6252_3.put("F05", TradeFeeCode.TZ_BJ);
		p6252_3.put("F06", term);
		List<T6252> sybjOfDebtList=bidManageDao.getSybjOfDebt(p6252_3);
		for(T6252 e:sybjOfDebtList){
			if (list == null) {
				list = new ArrayList<>();
			}
			BigDecimal wyjAmount=e.F07.multiply(t6230.F06).divide(new BigDecimal(365), 9, RoundingMode.HALF_UP);
			// 提前还款违约金还款计划
			if(leftDays>lastNaturalMonthDays){
				wyjAmount=wyjAmount.multiply(new BigDecimal(30)).setScale(2, RoundingMode.HALF_UP);
			}
			else if(leftDays>7){
				wyjAmount=wyjAmount.multiply(new BigDecimal(leftDays-7)).setScale(2, RoundingMode.HALF_UP);
			}
			else{
				wyjAmount=BigDecimal.ZERO;
			}
			if(wyjAmount.compareTo(BigDecimal.ZERO)!=0){
				T6252 wyj=penaltyRecord(term,e.F11,wyjAmount);
				list.add(wyj);
				t6252sToInsert.add(wyj);
			}
		}
		
		//插入违约金的还款记录
		bidManageDao.insertRepayPlan(t6252sToInsert);
		
		//将之后期数的利息改成零
		Map<String,Object> p6252_4=new HashMap<>();
		p6252_4.put("F09", T6252_F09.TQH.name());
		p6252_4.put("F10", currentTimestamp);
		p6252_4.put("F02", loanId);
		p6252_4.put("F06", term);
		List<Integer> F05List=new ArrayList<>();
		F05List.add(TradeFeeCode.TZ_LX);
		F05List.add(TradeFeeCode.TZ_JX);
		p6252_4.put("F05List", F05List);
		bidManageDao.updateT6252TQH(p6252_4);
		return list;
	}
	
	private void modifyInterest(int daysOfCycle,int currentTermLoanDays, List<T6252> list,T6230 t6230,Timestamp currentTimestamp) throws Exception {
		List<T6252> insert70022List=new ArrayList<>();
		for(T6252 e:list){
			if(e.F05==TradeFeeCode.TZ_LX){
				T6252 item=new T6252();
				item.F02=e.F02;
				item.F03=e.F03;
				item.F04=e.F04;
				item.F05=70022;
				item.F06=e.F06;
				item.F07=e.F07;
				item.F08=e.F08;
				item.F09=T6252_F09.YH;
				item.F10=currentTimestamp;
				item.F11=e.F11;
				insert70022List.add(item);
			}
		}
		bidManageDao.insertRepayPlan(insert70022List);
		//修改利息
		for(T6252 e:list){
			if(e.F05==TradeFeeCode.TZ_LX){
				if (daysOfCycle != 0) {
					Map<String,Object> pSybjOfDebt=new HashMap<>();
					pSybjOfDebt.put("F11", e.F11);
					pSybjOfDebt.put("F05", TradeFeeCode.TZ_BJ);
					pSybjOfDebt.put("F06", e.F06);
					T6252 sybjByDebt=bidManageDao.getSybjByDebt(pSybjOfDebt);
					e.F07=sybjByDebt.F07.multiply(t6230.F06).divide(new BigDecimal(365), 9, RoundingMode.HALF_UP).multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
				} else {
					e.F07 = BigDecimal.ZERO;
				}
				Map<String,Object> pModifyInterest=new HashMap<>();
				pModifyInterest.put("F01", e.F01);
				pModifyInterest.put("F07", e.F07);
				bidManageDao.updateRepayPlan(pModifyInterest);
			}
		}
		//修改加息
//		for(T6252 e:list){
//			if(e.F05==TradeFeeCode.TZ_JX){
//				if (daysOfCycle != 0) {
//					Map<String,Object> pSybjOfDebt=new HashMap<>();
//					pSybjOfDebt.put("F11", e.F11);
//					pSybjOfDebt.put("F05", TradeFeeCode.TZ_BJ);
//					pSybjOfDebt.put("F06", e.F06);
//					T6252 sybjByDebt=bidManageDao.getSybjByDebt(pSybjOfDebt);
//					T6251 t6251=bidManageDao.getDebtById(e.F11);
//					UserCoupon userCoupon=couponManageDao.selectUserCouponByTenderId(t6251.F11);
//					if(userCoupon!=null){
//						e.F07=sybjByDebt.F07.multiply(userCoupon.scope).divide(new BigDecimal(365), 9, RoundingMode.HALF_UP).multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
//					}
//					else{
//						e.F07 = BigDecimal.ZERO;
//					}
//				} else {
//					e.F07 = BigDecimal.ZERO;
//				}
//				Map<String,Object> pModifyInterest=new HashMap<>();
//				pModifyInterest.put("F01", e.F01);
//				pModifyInterest.put("F07", e.F07);
//				bidManageDao.updateRepayPlan(pModifyInterest);
//			}
//		}
		
	}
	
	private T6252 penaltyRecord(int f06,int f11,BigDecimal f07) throws Exception {
		T6252 tqRecord = new T6252();
		Map<String,Object> p6252=new HashMap<>();
		p6252.put("F05", TradeFeeCode.TZ_LX);
		p6252.put("F06", f06);
		p6252.put("F11", f11);
		T6252 e=bidManageDao.getAndLockRepayPlan(p6252);
		tqRecord.F02 = e.F02;
		tqRecord.F03 = e.F03;
		tqRecord.F04 = e.F04;
		tqRecord.F05 = TradeFeeCode.TZ_WYJ;
	    tqRecord.F06 = e.F06;
		tqRecord.F07 = f07;
		tqRecord.F08 = e.F08;
		tqRecord.F09 = e.F09;
		tqRecord.F10 = e.F10;
		tqRecord.F11 = e.F11;					
		return tqRecord;
	}

	@Transactional
	@Override
	public void preReleaseBid(int loanId, Timestamp displayTime, Timestamp releaseTime) throws Exception{
		Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
		if (releaseTime.getTime() < currentTimestamp.getTime()) {
			throw new TradeException(TradeResponseCode.TRADE_RELEASE_TIME_BEFORE_CURRENT_TIME);
		}
		T6230 t6230=bidManageDao.getBidById(loanId);
		// 标显示了就不能再修改了
		if (t6230.F35 != null) {
			if (currentTimestamp.getTime() - t6230.F35.getTime() >= 0) {
				throw new TradeException(TradeResponseCode.TRADE_DISPLAYED_BID_NOT_MODIFY);
			}
		}
        Calendar raiseDeadLine = Calendar.getInstance();
        raiseDeadLine.setTime(releaseTime);
        raiseDeadLine.add(Calendar.DAY_OF_YEAR,t6230.F08);
        raiseDeadLine.set(Calendar.HOUR_OF_DAY, 24);
        raiseDeadLine.set(Calendar.MINUTE, 0);
        raiseDeadLine.set(Calendar.SECOND, 0);
        Map<String,Object> params=new HashMap<>();
        params.put("F20", T6230_F20.YFB.name());
        params.put("F22", releaseTime);
        params.put("F31", new Timestamp(raiseDeadLine.getTimeInMillis()));
        params.put("F35", displayTime);
        params.put("F01", loanId);
        bidManageDao.updateBid(params);
	}

	@Override
	public void rollBackPreReleaseBid(int loanId) throws Exception{
		T6230 t6230=bidManageDao.getBidById(loanId);
		Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
		// 标显示了就不能再修改了
		if (t6230.F35 != null) {
			if (currentTimestamp.getTime() - t6230.F35.getTime() >= 0) {
				throw new TradeException(TradeResponseCode.TRADE_DISPLAYED_BID_NOT_MODIFY);
			}
		}
        Map<String,Object> params=new HashMap<>();
        params.put("F20", T6230_F20.DFB.name());
        params.put("F22", null);
        params.put("F31", null);
        params.put("F35", null);
        params.put("F01", loanId);
        bidManageDao.updateBid(params);
	}
}
