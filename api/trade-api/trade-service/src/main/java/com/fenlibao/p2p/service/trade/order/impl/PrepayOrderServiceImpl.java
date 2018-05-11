package com.fenlibao.p2p.service.trade.order.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.PrepayOrderDao;
import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6231;
import com.fenlibao.p2p.model.trade.entity.T6251;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.UserCoupon;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6506;
import com.fenlibao.p2p.model.trade.entity.order.T6521;
import com.fenlibao.p2p.model.trade.enums.T6252_F09;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.service.trade.order.PrepayOrderService;
import com.fenlibao.p2p.util.api.DateUtil;

@Service
public class PrepayOrderServiceImpl implements PrepayOrderService{

	@Resource
	BidManageDao bidManageDao;
	
	@Resource
	OrderManageDao orderManageDao;
	
	@Resource
	TradeCommonDao tradeCommonDao;
	
	@Resource
	PrepayOrderDao prepayOrderDao;
	
	@Resource
	CouponManageDao couponManageDao;
	
	@Transactional
	@Override
	public List<T6252> createOrder(int loanId,int term, String channelFlow, Integer pmsUserId) throws Exception {
		List<T6252> t6252List=getPrepayList(loanId,term);
		if(t6252List!=null){
			for(T6252 t6252:t6252List){
				T6501 t6501 = new T6501();
				t6501.F02 = TradeOrderType.PREPAYMENT_LOAN.orderType();
				t6501.F03 = T6501_F03.DTJ;
				t6501.F04 = tradeCommonDao.getCurrentTimestamp();
				t6501.F07 = T6501_F07.YH;
				t6501.F08 = t6252.F03;
				t6501.F09 = pmsUserId==null?0:pmsUserId;
				t6501.F10 = channelFlow;
				orderManageDao.add(t6501);
				T6521 t6521 = new T6521();
				t6521.F01 = t6501.F01;
				t6521.F02 = t6252.F04;
				t6521.F03 = t6252.F02;
				t6521.F04 = t6252.F11;
				t6521.F05 = t6252.F06;
				t6521.F06 = t6252.F07;
				t6521.F07 = t6252.F05;
				t6521.F08 = term;
				t6521.F09 = t6252.F03;
				prepayOrderDao.add(t6521);
			}
		}
		return t6252List;
	}

	@Transactional
	@Override
	public void updateOrder(T6501_F03 orderStatus, String channelFlow) throws Exception {
		T6501 t6501 = new T6501();
		t6501.F03 = orderStatus;
		t6501.F10 = channelFlow;
		orderManageDao.updateByFlowNo(t6501);
	}
	
	private List<T6252> getPrepayList(int loanId, int term) throws Exception {
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
			}
		}
		return list;
	}
	
	private void modifyInterest(int daysOfCycle,int currentTermLoanDays, List<T6252> list,T6230 t6230,Timestamp currentTimestamp) throws Exception {
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
			}
		}
		//修改加息
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

}
