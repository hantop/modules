package com.fenlibao.p2p.service.trade.bid.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.util.trade.pay.PayUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.PrepayOrderDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6521;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfPrepay;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;

@Service
public class OtherTaskOfPrepayImpl implements OtherTaskOfPrepay {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	BidManageDao bidManageDao;

	@Resource
	PrepayOrderDao prepayOrderDao;

	@Resource
	OrderManageDao orderManageDao;

	@Resource
	TradeCommonService tradeCommonService;

	@Resource
	UserDao userDao;

	@Resource
	TradeCommonDao tradeCommonDao;

	@Override
	@Transactional
	public void sendLetterAndMsg(int loanId, String flowNum) throws Exception {
		try {
			logger.info("[" + loanId + "]提前还款后发信息开始。。。。。");
			List<T6501> t6501List = orderManageDao.getByFlowNo(flowNum);
			T6521 t6521Example = prepayOrderDao.get(t6501List.get(0).F01);
			Timestamp currentTimestamp = tradeCommonDao.getCurrentTimestamp();
			int term = t6521Example.F05;
			Map<String, Object> p6252 = new HashMap<>();
			p6252.put("F02", loanId);
			p6252.put("F06", term);
			p6252.put("F09", "YH");
			List<T6252> t6252s = bidManageDao.getRepayPlan(p6252);
			Map<String, Object> retMap = getT6252Sum(t6252s);
			T6230 t6230 = bidManageDao.getBidById(loanId);
			String comment = "提前回款";

			for (T6252 t6252 : t6252s) {
				// 给投资人 发信息
				if (t6252.F05 == TradeFeeCode.TZ_LX) {
					String let = tradeCommonDao.getSystemVariable("LETTER.TZR_TQSK_LETTER");
					String msg = tradeCommonDao.getSystemVariable("MSG.TZR_TQSK_MSG");

					UserInfoEntity investor = userDao.get(t6252.F04, null);
					Map<String, BigDecimal> investorMap = (Map<String, BigDecimal>) retMap.get("investorMap");
					Map<String, BigDecimal> principalMap = (Map<String, BigDecimal>) retMap.get("principalMap");
					Map<String, BigDecimal> interestMap = (Map<String, BigDecimal>) retMap.get("interestMap");
					Map<String, BigDecimal> penaltyMap = (Map<String, BigDecimal>) retMap.get("penaltyMap");
					Map<String, BigDecimal> raiseInterestMap = (Map<String, BigDecimal>) retMap.get("raiseInterestMap");
					BigDecimal amount = investorMap.get("" + t6252.F11);
					BigDecimal principal = principalMap.get("" + t6252.F11);
					BigDecimal interest = interestMap.get("" + t6252.F11);
					// 违约金
					BigDecimal penalty = penaltyMap.get("" + t6252.F11);
					// 加息
					BigDecimal raiseInterest = BigDecimal.ZERO;
					// raiseInterestMap.get("" + t6252.F11);
					let = let.replace("${name}", t6230.F03);
					msg = msg.replace("${name}", t6230.F03);
					let = let.replace("${amount}", amount.toString());
					msg = msg.replace("${amount}", amount.toString());
					let = let.replace("${principal}", principal.toString());
					msg = msg.replace("${principal}", principal.toString());
					let = let.replace("${interest}", interest.toString());
					msg = msg.replace("${interest}", interest.toString());
					if (penalty != null) {
						let = let.replace("${penalty}", "，其他利息" + penalty + "元");
						msg = msg.replace("${penalty}", "，其他利息" + penalty + "元");
					}
					if (raiseInterest != null) {
						if (raiseInterest.compareTo(BigDecimal.ZERO) != 0) {
							let = let.replace("${raiseInterest}", "，加息奖励" + raiseInterest + "元");
							msg = msg.replace("${raiseInterest}", "，加息奖励" + raiseInterest + "元");
						} else {
							let = let.replace("${raiseInterest}", "");
							msg = msg.replace("${raiseInterest}", "");
						}
					} else {
						let = let.replace("${raiseInterest}", "");
						msg = msg.replace("${raiseInterest}", "");
					}
					// 站内信
					tradeCommonService.sendLetter(t6252.F04, comment, let);
					// 短信
					tradeCommonService.sendMsg(investor.getPhone(), msg, 2);
				}
			}
			//提前还款成功发送短信和站内信给借款人开关
			boolean flag = PayUtil.PAY_CONFIG.SEND_BORROWER_PREPAY_SMS();
			if(flag) {
				// 给借款人发信息
				UserInfoEntity borrower = userDao.get(t6252s.get(0).F03, null);
				String letter = tradeCommonDao.getSystemVariable("LETTER.JKR_TQHK_LETTER");
				String msg = tradeCommonDao.getSystemVariable("MSG.JKR_TQHK_MSG");
				BigDecimal amountSum = (BigDecimal) retMap.get("repaySum");
				BigDecimal principalSum = (BigDecimal) retMap.get("principalSum");
				BigDecimal interestSum = (BigDecimal) retMap.get("interestSum");
				BigDecimal penaltySum = (BigDecimal) retMap.get("penaltySum");
				letter = letter.replace("${name}", t6230.F03);
				msg = msg.replace("${name}", t6230.F03);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowTime = sdf.format(currentTimestamp);
				letter = letter.replace("${time}", nowTime);
				msg = msg.replace("${time}", nowTime);
				letter = letter.replace("${amount}", amountSum.toString());
				msg = msg.replace("${amount}", amountSum.toString());
				letter = letter.replace("${principal}", principalSum.toString());
				msg = msg.replace("${principal}", principalSum.toString());
				letter = letter.replace("${interest}", interestSum.toString());
				msg = msg.replace("${interest}", interestSum.toString());
				if (!"0.00".equals(penaltySum.toString())) {
					letter = letter.replace("${penalty}", "，违约金" + penaltySum.toString() + "元");
					msg = msg.replace("${penalty}", "，违约金" + penaltySum.toString() + "元");
				}
				// 站内信
				tradeCommonService.sendLetter(t6252s.get(0).F03, comment, letter);
				// 短信
				tradeCommonService.sendMsg(borrower.getPhone(), msg, 2);
			}
			logger.info("[" + loanId + "]提前还款后发信息结束。。。。。");
		} catch (Exception e) {
			logger.error("提前还款发信息时出错", e);
		}
	}

	private Map<String, Object> getT6252Sum(List<T6252> t6252s) throws Exception {
		// 还款总金额
		BigDecimal repaySum = BigDecimal.ZERO;
		BigDecimal principalAll = BigDecimal.ZERO;
		BigDecimal interestAll = BigDecimal.ZERO;
		BigDecimal penaltyAll = BigDecimal.ZERO;
		Map<String, Object> retMap = new HashMap<>();
		Map<String, BigDecimal> investorMap = new HashMap<>();
		Map<String, BigDecimal> principalMap = new HashMap<>();
		Map<String, BigDecimal> interestMap = new HashMap<>();
		Map<String, BigDecimal> penaltyMap = new HashMap<>();
		Map<String, BigDecimal> raiseInterestMap = new HashMap<>();
		for (int i = 0; i < t6252s.size(); i++) {
			T6252 t6252 = t6252s.get(i);
			// 加息由平台付，所以不算在借款人要还总数里面
			if (t6252.F05 != TradeFeeCode.TZ_JX&&t6252.F05 != TradeFeeCode.TZ_LX_BF) {
				repaySum = repaySum.add(t6252.F07);
			}
			String debtId = String.valueOf(t6252.F11);
			BigDecimal investmentAmount = investorMap.get(debtId);
			BigDecimal principalSum = principalMap.get(debtId);
			BigDecimal interestSum = interestMap.get(debtId);
			BigDecimal penaltySum = penaltyMap.get(debtId);
			BigDecimal raiseInterestSum = raiseInterestMap.get(debtId);
			if (t6252.F05 != TradeFeeCode.TZ_LX_BF) {
				if (investmentAmount != null) {
					investorMap.put(debtId, investmentAmount.add(t6252.F07));
				} else {
					investorMap.put(debtId, t6252.F07);
				}
			}
			if (t6252.F05 == TradeFeeCode.TZ_BJ) {
				principalAll = principalAll.add(t6252.F07);
				if (principalSum != null) {
					principalMap.put(debtId, principalSum.add(t6252.F07));
				} else {
					principalMap.put(debtId, t6252.F07);
				}
			}
			if (t6252.F05 == TradeFeeCode.TZ_LX) {
				interestAll = interestAll.add(t6252.F07);
				if (interestSum != null) {
					interestMap.put(debtId, interestSum.add(t6252.F07));
				} else {
					interestMap.put(debtId, t6252.F07);
				}
			}
			if (t6252.F05 == TradeFeeCode.TZ_WYJ) {
				penaltyAll = penaltyAll.add(t6252.F07);
				if (penaltySum != null) {
					penaltyMap.put(debtId, penaltySum.add(t6252.F07));
				} else {
					penaltyMap.put(debtId, t6252.F07);
				}
			}
			if (t6252.F05 == TradeFeeCode.TZ_JX) {
				if (raiseInterestSum != null) {
					raiseInterestMap.put(debtId, raiseInterestSum.add(t6252.F07));
				} else {
					raiseInterestMap.put(debtId, t6252.F07);
				}
			}
		}
		retMap.put("repaySum", repaySum);
		retMap.put("principalSum", principalAll);
		retMap.put("interestSum", interestAll);
		retMap.put("penaltySum", penaltyAll);
		retMap.put("investorMap", investorMap);
		retMap.put("principalMap", principalMap);
		retMap.put("interestMap", interestMap);
		retMap.put("penaltyMap", penaltyMap);
		retMap.put("raiseInterestMap", raiseInterestMap);
		return retMap;
	}
}
