/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: ITenderExchangeOrderServiceImpl.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.creditassignment.impl 
 * @author: laubrence
 * @date: 2015-10-23 下午3:53:01 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.service.creditassignment.impl;

import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S62.entities.*;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S65.entities.T6507;
import com.fenlibao.p2p.model.consts.earnings.EarningsTypeConst;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.bid.BidBaseInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.RepaymentModeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.base.impl.OrderExecutorImpl;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.creditassignment.TenderExchangeOrderService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.util.DateUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: TenderExchangeOrderServiceImpl
 * @author: laubrence
 * @date: 2015-10-23 下午3:53:01  
 */
@Service
public class TenderExchangeOrderServiceImpl extends OrderExecutorImpl implements TenderExchangeOrderService {

	@Resource
	BidInfoService bidInfoService;

	@Resource
	FinacingService finacingService;

	@Resource
	private XWCreditService xwCreditService;

	/**
	 * @Title: doConfirm
	 * @Description: 债权转让订单确认
	 * @param connection
	 * @param orderId
	 * @param params
	 * @throws Exception 
	 */
	@Override
	public void doConfirm(Connection connection, int orderId,
			Map<String, String> params) throws Throwable {
		try {
			// 查询订单详情
			T6507 t6507 = selectT6507(connection, orderId);
			if (t6507 == null) {//订单详细信息不存在
				logger.info("订单详细信息不存在");
                throw new BusinessException(ResponseCode.ORDER_INFO_NOT_FOUND.getCode(),ResponseCode.ORDER_INFO_NOT_FOUND.getMessage());
			}
			// 锁定债权申请记录
			T6260 t6260 = selectT6260(connection, t6507.F02);
			if (t6260 == null) {//债权转让信息不存在
				logger.info("债权转让信息不存在");
                throw new BusinessException(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(),ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
			}
			if (t6260.F07 != T6260_F07.ZRZ) {//债权不是转让中状态,不能转让
				logger.info("债权不是转让中状态,不能转让");
                throw new BusinessException(ResponseCode.ZQZR_APPLY_NOT_ZRZ.getCode(),ResponseCode.ZQZR_APPLY_NOT_ZRZ.getMessage());
			}
			// 锁定债权信息
			T6251 t6251 = selectT6251(connection, t6260.F02);
			if (t6251 == null) {//债权信息不存在
				logger.info("债权信息不存在");
                throw new BusinessException(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(),ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
			}
			
			if(t6251.F07.compareTo(BigDecimal.ZERO) <= 0){//债权价值不能为0
				logger.info("债权价值不能为0");
                throw new BusinessException(ResponseCode.ZQZR_ZQ_VALUE_NOT_ZERO.getCode(),ResponseCode.ZQZR_ZQ_VALUE_NOT_ZERO.getMessage());
			}
			//String zjkt = String.valueOf(Config.get("ZQZR_SFZJKT"));
			if(t6507.F03 == t6251.F04){//不可购买自己的债权
            	logger.info("不能购买自己的债权");
            	throw new BusinessException(ResponseCode.ZQZR_CANNOT_BUY_MYSELF.getCode(),ResponseCode.ZQZR_CANNOT_BUY_MYSELF.getMessage());
			}
			
			// 锁定还款计划
			T6252[] t6252s = selectAllT6252(connection, t6260.F02);
			if (t6252s == null) {//债权还款计划不存在
				logger.info("债权还款计划不存在");
	            throw new BusinessException(ResponseCode.ZQZR_ZQ_REPAY_PLAN_NOT_FOUND.getCode(),ResponseCode.ZQZR_ZQ_REPAY_PLAN_NOT_FOUND.getMessage());
			}


			BidBaseInfo baseInfo = bidInfoService.getBidBaseInfo(t6251.F03);
			boolean isCGBid = baseInfo.getCgMode() == VersionTypeEnum.CG.getIndex();

			// 锁定购买人往来账户
			T6101 srwlzh = selectT6101(connection, t6507.F03, isCGBid? SysFundAccountType.XW_INVESTOR_WLZH:SysFundAccountType.WLZH);
			if (srwlzh == null) {//受让人往来资金账户不存在
				logger.info("受让人往来资金账户不存在");
                throw new BusinessException(ResponseCode.ZQZR_SRR_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.ZQZR_SRR_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}
			if (srwlzh.F06.compareTo(t6507.F05) < 0) {//受让人往来资金账户余额不足
				logger.info("受让人往来资金账户余额不足");
                throw new BusinessException(ResponseCode.ZQZR_SRR_USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.ZQZR_SRR_USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage());
			}
			T6101 zrwlzh = null;
			// 如果购买人和转让人是同一个人
			if(srwlzh.F02 == t6251.F04){
				zrwlzh = srwlzh;
			}else{
				zrwlzh = selectT6101(connection, t6251.F04, isCGBid? SysFundAccountType.XW_INVESTOR_WLZH:SysFundAccountType.WLZH);
			}
			if (zrwlzh == null) {//转让人往来资金账户不存在
				logger.info("转让人往来资金账户不存在");
                throw new BusinessException(ResponseCode.ZQZR_CRR_USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.ZQZR_CRR_USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
			}



			{
				// 扣减受让人资金账户
				srwlzh.F06 = srwlzh.F06.subtract(t6507.F05);
				updateT6101(connection, srwlzh.F06, srwlzh.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = srwlzh.F01;
				t6102.F03 = FeeCode.ZQZR_MR;
				t6102.F04 = zrwlzh.F01;
				t6102.F07 = t6507.F05;
				t6102.F08 = srwlzh.F06;
				t6102.F09 = String.format("购买债权:%s", t6251.F02);
				t6102.orderId = orderId;
				insertT6102(connection, t6102);
			}
			{
				// 增加转让人资金账户
				zrwlzh.F06 = zrwlzh.F06.add(t6507.F05);
				updateT6101(connection, zrwlzh.F06, zrwlzh.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = zrwlzh.F01;
				t6102.F03 = FeeCode.ZQZR_MC;
				t6102.F04 = srwlzh.F01;
				t6102.F06 = t6507.F05;
				t6102.F08 = zrwlzh.F06;
				t6102.F09 = String.format("转让债权:%s", t6251.F02);
				t6102.orderId = orderId;
				insertT6102(connection, t6102);
			}
			BigDecimal fee = t6507.F05.multiply(t6260.F08);
			fee = fee.setScale(2, RoundingMode.HALF_UP);
			if (fee.compareTo(BigDecimal.ZERO) > 0) {
				// 扣转让手续费
				if (zrwlzh.F06.compareTo(fee) < 0) {//转让人往来资金账户余额不足,扣转让手续费失败
					logger.info("转让人往来资金账户余额不足,扣转让手续费失败");
	                throw new BusinessException(ResponseCode.ZQZR_CRR_USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.ZQZR_CRR_USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage());
				}
				int pid = getPTID(connection);
				// 锁定平台往来账户
				T6101 ptwlzh = selectT6101(connection, pid, isCGBid? SysFundAccountType.XW_PLATFORM_INCOME_WLZH:SysFundAccountType.WLZH);
				if (ptwlzh == null) {//平台往来资金账户不存在
					logger.info("平台往来资金账户不存在");
	                throw new BusinessException(ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getMessage());
				}
				{
					// 扣减转让人资金账户
					zrwlzh.F06 = zrwlzh.F06.subtract(fee);
					updateT6101(connection, zrwlzh.F06, zrwlzh.F01);
					T6102 t6102 = new T6102();
					t6102.F02 = zrwlzh.F01;
					t6102.F03 = FeeCode.ZQZR_SXF;
					t6102.F04 = ptwlzh.F01;
					t6102.F07 = fee;
					t6102.F08 = zrwlzh.F06;
					t6102.F09 = String.format("转让债权手续费:%s", t6251.F02);
					t6102.orderId = orderId;
					insertT6102(connection, t6102);
				}
				{
					// 增加平台资金账户
					ptwlzh.F06 = ptwlzh.F06.add(fee);
					updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
					T6102 t6102 = new T6102();
					t6102.F02 = ptwlzh.F01;
					t6102.F03 = FeeCode.ZQZR_SXF;
					t6102.F04 = zrwlzh.F01;
					t6102.F06 = fee;
					t6102.F08 = ptwlzh.F06;
					t6102.F09 = String.format("转让债权手续费:%s", t6251.F02);
					t6102.orderId = orderId;
					insertT6102(connection, t6102);
				}
			}
			
			//插入转出者用户收益情况  [add by laubrence 2015-11-10 14:43:26]
			doEarnings(connection,t6251);
			
			// 债权剩余期数
			int remainTerm = selectRemainTerm(connection, t6251);
			{
				// 扣减转出人持有债权
				try (PreparedStatement pstmt = connection
						.prepareStatement("UPDATE S62.T6251 SET F07 = ? WHERE F01 = ?")) {
					pstmt.setBigDecimal(1, BigDecimal.ZERO);
					pstmt.setInt(2, t6251.F01);
					pstmt.execute();
				}
				// 生成新的债权编码
				int index = 0;
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT COUNT(*) FROM S62.T6251 WHERE F11 = ?")) {
					pstmt.setInt(1, t6251.F11);
					try (ResultSet resultSet = pstmt.executeQuery()) {
						if (resultSet.next()) {
							index = resultSet.getInt(1) + 1;
						} else {
							index = 1;
						}
					}
				}
				// 插入受让人债权记录
				T6251 newT6251 = new T6251();
				if(t6251.F02.contains("-")){
					newT6251.F02 = t6251.F02.substring(0, t6251.F02.length() - 1) + index;
				}else{
					newT6251.F02 = String.format("%s-%s", t6251.F02,
							Integer.toString(index));
				}
				newT6251.F03 = t6251.F03;
				newT6251.F04 = t6507.F03;
				newT6251.F05 = t6507.F05;
				newT6251.F06 = t6507.F04;
				newT6251.F07 = t6507.F04;
				newT6251.F08 = T6251_F08.F;
				newT6251.F09 = getCurrentDate(connection);
				newT6251.F10 = t6251.F10;
				newT6251.F11 = t6251.F11;
				newT6251.F12 = orderId;
				int rightId = insertT6251(connection, newT6251);
				// 修改还款计划
				for (T6252 t6252 : t6252s) {
					if(t6252.F05== SysTradeFeeCode.CJFWF||t6252.F05==SysTradeFeeCode.LX_GLF){
						//平台管理费1201和利息管理费7035收款用户为平台 不用改F04
					}else{
						t6252.F04 = t6507.F03;
					}
					t6252.F11 = rightId;
					updateT6252(connection, t6252);
				}
				
				// 插入购买记录
				T6262 t6262 = new T6262();
				t6262.F02 = t6260.F01;
				t6262.F03 = t6507.F03;
				t6262.F04 = t6507.F04;
				t6262.F05 = t6507.F05;
				t6262.F06 = fee;
				t6262.F07 = getCurrentTimestamp(connection);
				t6262.F08 = t6262.F04.subtract(t6262.F05);
				t6262.F09 = t6262.F05.subtract(t6262.F04).subtract(fee);
				t6262.F10 = remainTerm;
				insertT6262(connection, t6262,t6251.F01,rightId);
 
			}
			// BigDecimal dsbx = getDsbx(t6260.F02);
			{
				// 插入购买人统计数据
				T6263 t6263 = new T6263();
				t6263.F01 = t6507.F03;
				t6263.F02 = t6507.F04.subtract(t6507.F05);
				t6263.F03 = t6507.F04;
				t6263.F04 = t6507.F04.subtract(t6507.F05);
				t6263.F05 = 1;
				t6263.F06 = BigDecimal.ZERO;
				t6263.F07 = BigDecimal.ZERO;
				t6263.F08 = 0;

				insertT6263(connection, t6263);
			}

			{
				// 插入卖出者统计数据
				T6263 t6263 = new T6263();
				t6263.F01 = t6251.F04;
				t6263.F02 = t6507.F05.subtract(t6507.F04).subtract(fee);
				t6263.F03 = BigDecimal.ZERO;
				t6263.F04 = BigDecimal.ZERO;
				t6263.F05 = 0;
				t6263.F06 = t6507.F04;
				t6263.F07 = t6507.F05.subtract(t6507.F04).subtract(fee);
				t6263.F08 = 1;

				insertT6263(connection, t6263);
			}
			{
				// 修改债权信息为非转让状态
				updateT6251(connection, T6251_F08.F, t6251.F01);
				// 修改转让申请为已转出状态
				updateT6260(connection, T6260_F07.YJS, t6260.F01);
			}
			/**
			 * 新网提交请求，新网的请求无法用事务控制
			 */
			xwCreditService.doCreditAssignment(orderId,fee,BigDecimal.ZERO);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}
	
	private void doEarnings(Connection connection, T6251 entity) throws Throwable{
		//转让当天的收益是B的，所以不需要计算A的日收益 update by laubrence 2016-4-5 11:44:33
		/*
		//查询日收益是否有记录，并锁表
		int flagId = selectTDayEarnings(connection,entity);
		if(flagId==0){//如果已存在记录就不处理
			//插入转出者日收益
			insertTDayEarnings(connection,entity);
		}
		*/
		//更新日收益表为已到账
		updateTDayEarnings(connection,entity.F01);
		//插入已到账表总收益
		insertTArrivalEarnings(connection,entity);
	}
	
	//t_arrival_earnings
	private int insertTArrivalEarnings(Connection connection, T6251 entity) throws Throwable{
        T6231 t6231 = selectT6231(connection,entity.F03);
        if(t6231==null){
            return 0;
        }
//        int arrivalDays=0;
//        if(entity.F12 > 0){//债权转让的债权
//            arrivalDays = DateUtil.getDayBetweenDates(entity.F09,getCurrentDate(connection));
//        }else{//原始的债权
//            arrivalDays = DateUtil.getDayBetweenDates(t6231.F17,getCurrentDate(connection));
//        }
//		BigDecimal dayFee = getDayEarnings(connection,entity.F01);
		int bidType = selectBidType(connection, entity.F03);
//		if(arrivalDays<1){
//			return 0;
//		}
//        BigDecimal passdeProfit = dayFee.multiply(new BigDecimal(arrivalDays));

		//已获收益需要重新计算 by kris
		BigDecimal passdeProfit =getPastProfit(entity);

		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"insert into flb.t_arrival_earnings set user_id = ? ,  amount = ? , type= ? , create_time= ? , bid_type_id = ?, zq_id = ? ",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F04);
			pstmt.setBigDecimal(2, passdeProfit);
			if(entity.F12 > 0){
				pstmt.setInt(3, EarningsTypeConst.ZQZR);
			}else{
                pstmt.setInt(3, EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX+bidType);
			}
			pstmt.setTimestamp(4, getCurrentTimestamp(connection));
			pstmt.setInt(5, bidType);
			pstmt.setInt(6, entity.F01);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}


	//算持有天数利息 T6251 entity
	public BigDecimal getPastProfit(T6251 entity){
		String zqId =String.valueOf(entity.F01);
		String userId = String.valueOf(entity.F04);
		List<Finacing> list = this.finacingService.getFinacing(userId, null, zqId);
		if (null == list || list.size() == 0) {
			return BigDecimal.ZERO;
		}
		Finacing finacing = list.get(0);
		BigDecimal expectedProfit =BigDecimal.ZERO;
		//借款周期参数
		int loanPeriod=finacing.getMonth()==0?finacing.getLoanDays():finacing.getMonth();
		int period=finacing.getMonth()==0?365:12;

		//一次还清的利息计算
		if (RepaymentModeEnum.YCFQ.getCode().equals(finacing.getRepaymentMethod())) {
			//已过天数的收益（未到账）
			int passedDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),DateUtil.nowDate());//已算收益天数
			int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数

			if(totalDays>0){
				BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
						.multiply(new BigDecimal(finacing.getRate()))
						.multiply(new BigDecimal(loanPeriod))
						.divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
				expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
			}
		}

		//等额本息的收益计算
		if (RepaymentModeEnum.DEBX.getCode().equals(finacing.getRepaymentMethod())) {
			//查出下一期回款计划里面的利息
			int[] tradeTypes = new int[]{ com.fenlibao.p2p.model.global.FeeCode.TZ_LX, com.fenlibao.p2p.model.global.FeeCode.TZ_JXJL};
			Map nextRepaymentItemProfit = finacingService.getNextRepaymentItemProfit(Integer.parseInt(userId)
					,Integer.parseInt(zqId),tradeTypes);

			BigDecimal totalEarning=BigDecimal.ZERO;
			if(nextRepaymentItemProfit!=null)totalEarning= (BigDecimal) nextRepaymentItemProfit.get("profitRepaymentAmount");

			tradeTypes = new int[]{ com.fenlibao.p2p.model.global.FeeCode.TZ_LX};
			RepaymentInfo lastRepaymentInfo = finacingService.getLastRepaymentItem(Integer.parseInt(userId)
					,Integer.parseInt(zqId),tradeTypes);  //上一期的回款计划
			Date lastRepaymentDate = lastRepaymentInfo==null?finacing.getCreateTime():lastRepaymentInfo.getExpectedRepaymentDate();

			List<RepaymentInfo> nextRepayment = finacingService.getNextRepaymentItem(Integer.parseInt(userId)
					,Integer.parseInt(zqId),tradeTypes);  //下一期的回款计划
			Date nextRepaymentDate = nextRepayment.get(0).getExpectedRepaymentDate();

			//当月已过天数的收益（未到账）
			int passedDays= DateUtil.daysToLastRepaymentDay(lastRepaymentDate,new Date());//当月收益天数,当天收益不计
			int totalDays = DateUtil.getDayBetweenDates(lastRepaymentDate,nextRepaymentDate);//当月债权一共计息的天数
			if(totalDays>0){
				expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
			}
		}

		//每月付息到期还本的收益计算
		if (RepaymentModeEnum.MYFX.getCode().equals(finacing.getRepaymentMethod())) {
			//当月已过天数的收益（未到账）
			int[] tradeTypes = new int[]{ com.fenlibao.p2p.model.global.FeeCode.TZ_LX};

			RepaymentInfo  repaymentInfo = finacingService.getLastRepaymentItem(Integer.parseInt(userId)
					,Integer.parseInt(zqId),tradeTypes);  // 上一期的回款计划
			Date effectDate=repaymentInfo==null?finacing.getCreateTime():repaymentInfo.getExpectedRepaymentDate();

			int passedDays= DateUtil.daysToLastRepaymentDay(effectDate,new Date());//当月收益天数,当天收益不计
			int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数
			if(totalDays>0){
				BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
						.multiply(new BigDecimal(finacing.getRate()))
						.multiply(new BigDecimal(loanPeriod))
						.divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
				expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
			}
		}


		return expectedProfit;
	}


	private void updateTDayEarnings(Connection connection, int zqId) throws SQLException{
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE flb.t_day_earnings SET is_arrival = ? WHERE zq_id = ?")) {
			pstmt.setString(1, Status.S.name());
			pstmt.setInt(2, zqId);
			pstmt.execute();
		}
	}
	
	private int insertTDayEarnings(Connection connection, T6251 entity)
			throws Throwable {
		//查询标的类型
		int bidType = selectBidType(connection, entity.F03);

		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"insert into flb.t_day_earnings set user_id = ? , zq_id=? , bid_id=? ,bid_type_id = ?, earnings_date = ? , amount = ?,is_arrival= ?, create_time=? , type= ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F04);
			pstmt.setInt(2, entity.F01);
			pstmt.setInt(3, entity.F03);
			pstmt.setInt(4, bidType);
			pstmt.setDate(5, getCurrentDate(connection));
			pstmt.setBigDecimal(6, getDayEarnings(connection,entity.F01));
			pstmt.setString(7, Status.S.name());
			pstmt.setTimestamp(8,getCurrentTimestamp(connection));
			if(entity.F12 > 0){
				pstmt.setInt(9, EarningsTypeConst.ZQZR);
			}else{
				pstmt.setInt(9, EarningsTypeConst.BID_EARNINGS_TYPE_PREFIX+bidType);
			}
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}
	
	private int selectBidType(Connection connection, int bidId)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F04 FROM s62.t6230 t WHERE F01 = ? ")) {
			pstmt.setInt(1, bidId);

			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}
	
	private int selectTDayEarnings(Connection connection, T6251 entity)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT id FROM flb.t_day_earnings WHERE zq_id = ? and earnings_date = ? for update ")) {
			pstmt.setInt(1, entity.F01);
			pstmt.setDate(2, getCurrentDate(connection));

			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}
	
	private BigDecimal getDayEarnings(Connection connection, int zqId)
			throws Exception {
        BigDecimal totalExpectedProfit = new BigDecimal(0);
        int bidId =0;
		try (PreparedStatement pstmt = connection
				.prepareStatement("select IFNULL(sum(t.F07),0),t.F02 from s62.t6252 t where t.F11=? and t.F05=?  limit 1")) {
			pstmt.setInt(1, zqId);
			pstmt.setInt(2, FeeCode.TZ_LX);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
                    totalExpectedProfit = resultSet.getBigDecimal(1);
                    bidId = resultSet.getInt(2);
				}
			}
		}
        T6231 t6231 = selectT6231(connection,bidId);
        if(t6231==null || t6231.F17==null || t6231.F18 ==null){
            return new BigDecimal(0);
        }
        int days = DateUtil.getDayBetweenDates(t6231.F17,t6231.F18);
        if(days <= 0){
            return new BigDecimal(0);
        }
        BigDecimal dayFee = totalExpectedProfit.divide(new BigDecimal(days), 2, BigDecimal.ROUND_DOWN);
        return dayFee;
	}

    /**
     * 获取标的计息日期和到期日期
     * @param connection
     * @param bidId
     * @return
     * @throws Exception
     */
    private T6231 selectT6231(Connection connection, int bidId)
            throws Exception {
        try (PreparedStatement pstmt = connection
                .prepareStatement("select t6231.F01, t6231.F17, t6231.F18 from s62.t6231 where t6231.F01 = ? ")) {
            pstmt.setInt(1, bidId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    T6231 t6231 = new T6231();
                    t6231.F01 = resultSet.getInt(1);
                    t6231.F17 = resultSet.getDate(2);
                    t6231.F18 = resultSet.getDate(3);
                    return t6231;
                }
            }
        }
        return null;
    }
	
	/*
	 * private SQLConnection getConnection() throws SQLException {
	 * SQLConnectionProvider connectionProvider = resourceProvider
	 * .getDataConnectionProvider(SQLConnectionProvider.class,
	 * P2PConst.DB_MASTER_PROVIDER); return connectionProvider.getConnection();
	 * }
	 * 
	 * // 待收本息 private BigDecimal getDsbx(int zqId) throws Exception { try
	 * (Connection connection = getConnection()) { try (PreparedStatement pstmt
	 * = connection .prepareStatement(
	 * "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F11 = ? AND (T6252.F05 = ? OR T6252.F05 = ?) AND T6252.F09 = ?"
	 * )) { pstmt.setInt(1, zqId); pstmt.setInt(2, FeeCode.TZ_LX);
	 * pstmt.setInt(3, FeeCode.TZ_BJ); pstmt.setString(4, T6252_F09.WH.name());
	 * try (ResultSet resultSet = pstmt.executeQuery()) { if (resultSet.next())
	 * { return resultSet.getBigDecimal(1); } } } } return BigDecimal.ZERO; }
	 */

	protected void updateT6252(Connection connection, T6252 t6252)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6252 SET F04 = ?, F11 = ? WHERE F01 = ?")) {
			pstmt.setInt(1, t6252.F04);
			pstmt.setInt(2, t6252.F11);
			pstmt.setInt(3, t6252.F01);
			pstmt.execute();
		}
	}

	protected void insertT6263(Connection connection, T6263 entity)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S62.T6263"
						+ " SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = F05 + ?, F06 = ?, F07 = ?, F08 = F08 + ? ON DUPLICATE KEY UPDATE"
						+ " F02 = F02 + ?, F03 = F03 + ?, F04 = F04 + ?, F05 = F05 + ?, F06 = F06 + ?, F07 = F07 + ?, F08 = F08 + ?")) {
			pstmt.setInt(1, entity.F01);
			pstmt.setBigDecimal(2, entity.F02);
			pstmt.setBigDecimal(3, entity.F03);
			pstmt.setBigDecimal(4, entity.F04);
			pstmt.setInt(5, entity.F05);
			pstmt.setBigDecimal(6, entity.F06);
			pstmt.setBigDecimal(7, entity.F07);
			pstmt.setInt(8, entity.F08);
			pstmt.setBigDecimal(9, entity.F02);
			pstmt.setBigDecimal(10, entity.F03);
			pstmt.setBigDecimal(11, entity.F04);
			pstmt.setInt(12, entity.F05);
			pstmt.setBigDecimal(13, entity.F06);
			pstmt.setBigDecimal(14, entity.F07);
			pstmt.setInt(15, entity.F08);
			pstmt.execute();
		}
	}

	private int insertT6251(Connection connection, T6251 entity)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S62.T6251 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setBigDecimal(4, entity.F05);
			pstmt.setBigDecimal(5, entity.F06);
			pstmt.setBigDecimal(6, entity.F07);
			pstmt.setString(7, entity.F08.name());
			pstmt.setDate(8, entity.F09);
			pstmt.setDate(9, entity.F10);
			pstmt.setInt(10, entity.F11);
			pstmt.setInt(11, entity.F12);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private int insertT6262(Connection connection, T6262 entity, int zczZqId, int zrzZqId)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S62.T6262 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ? , F11 = ? , F12 = ? ",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setBigDecimal(3, entity.F04);
			pstmt.setBigDecimal(4, entity.F05);
			pstmt.setBigDecimal(5, entity.F06);
			pstmt.setTimestamp(6, entity.F07);
			pstmt.setBigDecimal(7, entity.F08);
			pstmt.setBigDecimal(8, entity.F09);
			pstmt.setInt(9, entity.F10);
			pstmt.setInt(10, zczZqId);
			pstmt.setInt(11, zrzZqId);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private void updateT6251(Connection connection, T6251_F08 F01, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	private void updateT6260(Connection connection, T6260_F07 F01, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6260 SET F07 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	private int insertT6102(Connection connection, T6102 entity)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, order_id = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setTimestamp(4,getCurrentTimestamp(connection));
			pstmt.setBigDecimal(5, entity.F06);
			pstmt.setBigDecimal(6, entity.F07);
			pstmt.setBigDecimal(7, entity.F08);
			pstmt.setString(8, entity.F09);
			pstmt.setString(9, T6102_F10.WDZ.name());
			pstmt.setInt(10,entity.orderId);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private void updateT6101(Connection connection, BigDecimal F01, int F02)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private T6252[] selectAllT6252(Connection connection, int F11)
			throws SQLException {
		ArrayList<T6252> list = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F09 = ? AND T6252.F11 = ? FOR UPDATE")) {
			pstmt.setString(1, T6252_F09.WH.name());
			pstmt.setInt(2, F11);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				while (resultSet.next()) {
					T6252 record = new T6252();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6252_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
					record.F11 = resultSet.getInt(11);
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(record);
				}
			}
		}
		return ((list == null || list.size() == 0) ? null : list
				.toArray(new T6252[list.size()]));
	}

	private T6251 selectT6251(Connection connection, int F01)
			throws SQLException {
		T6251 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 ,F12 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6251();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getString(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = T6251_F08.parse(resultSet.getString(8));
					record.F09 = resultSet.getDate(9);
					record.F10 = resultSet.getDate(10);
					record.F11 = resultSet.getInt(11);
					record.F12 = resultSet.getInt(12);
				}
			}
		}
		return record;
	}

	private T6101 selectT6101(Connection connection, int F02, SysFundAccountType F03)
			throws SQLException {
		T6101 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? FOR UPDATE")) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F03.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6101();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = T6101_F03.parse(resultSet.getString(3));
					record.F04 = resultSet.getString(4);
					record.F05 = resultSet.getString(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getTimestamp(7);
				}
			}
		}
		return record;
	}

	private T6507 selectT6507(Connection connection, int F01)
			throws SQLException {
		T6507 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6507 WHERE T6507.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6507();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getBigDecimal(6);
				}
			}
		}
		return record;
	}

	private T6260 selectT6260(Connection connection, int F01)
			throws SQLException {
		T6260 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6260 WHERE T6260.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6260();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getBigDecimal(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getTimestamp(5);
					record.F06 = resultSet.getDate(6);
					record.F07 = T6260_F07.parse(resultSet.getString(7));
					record.F08 = resultSet.getBigDecimal(8);
				}
			}
		}
		return record;
	}

	private int selectRemainTerm(Connection connection,T6251 t6251) throws SQLException
	{
		int remainTerm = 0;
		try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(1) AS F01 FROM S62.T6252 WHERE T6252.F11 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
		{
			pstmt.setInt(1, t6251.F01);
			/**
			 * pstmt.setInt(2, FeeCode.TZ_BJ);
			 * Bug #19497 【后台-统计管理-债权转让统计表】已转让成功的债权，借款人还款后该列表的“剩余期限”会变动
			 */
			pstmt.setInt(2, FeeCode.TZ_LX);
			pstmt.setString(3, T6252_F09.WH.name());
			try (ResultSet resultSet = pstmt.executeQuery())
			{
				if (resultSet.next())
				{
					remainTerm = resultSet.getInt(1);
				}
			}
		}
		return remainTerm;
	}
}

