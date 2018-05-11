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
import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6506;
import com.fenlibao.p2p.model.trade.enums.BidSource;
import com.fenlibao.p2p.model.trade.enums.T6230_F10;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfRepay;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;

@Service
public class OtherTaskOfRepayImpl implements OtherTaskOfRepay{
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	BidManageDao bidManageDao;

	@Resource
	RepayOrderDao repayOrderDao;

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
			logger.info("[" + loanId + "]还款后发信息开始。。。。。");
			List<T6501> t6501List = orderManageDao.getByFlowNo(flowNum);
			T6506 t6506Example = repayOrderDao.get(t6501List.get(0).F01);
			Timestamp currentTimestamp = tradeCommonDao.getCurrentTimestamp();
			Map<String, String> params = new HashMap<>();
			int term = t6506Example.F05;
			sumT6252(loanId, term, params);
			int totalTerms = bidManageDao.getTotalTerms(loanId);
			Map<String, Object> p6252 = new HashMap<>();
			p6252.put("F02", loanId);
			p6252.put("F06", term);
			p6252.put("F09", "YH");
			List<T6252> t6252s = bidManageDao.getRepayPlan(p6252);
			T6230 t6230 = bidManageDao.getBidById(loanId);
			String comment = "回款";
			// 查出自动封标用户
			List<Integer> sealBidAccounts = bidManageDao.getSealBidAccounts();
			boolean fromQQM = BidSource.QQM.getCode().equals(t6230.F36);

			for (T6252 t6252 : t6252s) {
				// 给投资人 发信息
				if (t6252.F05 == TradeFeeCode.TZ_LX) {
					if (!(fromQQM && sealBidAccounts.contains(t6252.F04))) {
						String let = "";
						let = tradeCommonDao.getSystemVariable("LETTER.TZR_TBSK_LETTER");
						String msg = "";
						msg = tradeCommonDao.getSystemVariable("MSG.TZR_TBSK_MSG");
						UserInfoEntity investor = userDao.get(t6252.F04, null);
						BigDecimal principal = selectT6252Amount(t6252, TradeFeeCode.TZ_BJ);
						BigDecimal interest = t6252.F07;
						BigDecimal latecharge = selectT6252Amount(t6252, TradeFeeCode.TZ_FX);
						BigDecimal raiseInterest = BigDecimal.ZERO;
						// selectT6252Amount(t6252, TradeFeeCode.TZ_JX);
						String amount = principal.add(interest).add(latecharge).add(raiseInterest).toString();
						let=let.replace("${name}", t6230.F03);
						msg=msg.replace("${name}", t6230.F03);
						let=let.replace("${amount}", amount);
						msg=msg.replace("${amount}", amount);
						let=let.replace("${principal}", principal.toString());
						msg=msg.replace("${principal}", principal.toString());
						let=let.replace("${interest}", interest.toString());
						msg=msg.replace("${interest}", interest.toString());
						if (latecharge.compareTo(BigDecimal.ZERO) != 0) {
							let=let.replace("${latecharge}", "，其他利息" + latecharge + "元");
							msg=msg.replace("${latecharge}", "，其他利息" + latecharge + "元");
						}
						if (t6230.F10 != T6230_F10.YCFQ) {
							let=let.replace("${term}", "第" + term + "/" + totalTerms + "期");
							msg=msg.replace("${term}", "第" + term + "/" + totalTerms + "期");
						}
						if (raiseInterest.compareTo(BigDecimal.ZERO) != 0) {
							let=let.replace("${raiseInterest}", "，加息奖励" + raiseInterest + "元");
							msg=msg.replace("${raiseInterest}", "，加息奖励" + raiseInterest + "元");
						}
						// 站内信
						tradeCommonService.sendLetter(t6252.F04, comment, let);
						// 短信
						tradeCommonService.sendMsg(investor.getPhone(), msg, 2);
					}
				}
			}
			//#正常还款成功发送短信和站内信给借款人开关
			boolean flag = PayUtil.PAY_CONFIG.SEND_BORROWER_REPAY_SMS();
			if (!BidSource.QQM.getCode().equals(t6230.F36) || flag) {
				// 给借款人发信息
				UserInfoEntity borrower = userDao.get(t6252s.get(0).F03, null);
				String letter = "";
				String msg = "";
				msg = tradeCommonDao.getSystemVariable("MSG.JKR_JKHK_MSG");
				letter = tradeCommonDao.getSystemVariable("LETTER.JKR_JKHK_LETTER");
				letter=letter.replace("${name}", t6230.F03);
				msg=msg.replace("${name}", t6230.F03);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowTime = sdf.format(currentTimestamp);
				letter=letter.replace("${time}", nowTime);
				msg=msg.replace("${time}", nowTime);
				letter=letter.replace("${amount}", params.get("amountSum"));
				msg=msg.replace("${amount}", params.get("amountSum"));
				letter=letter.replace("${principal}", params.get("principalSum"));
				msg=msg.replace("${principal}", params.get("principalSum"));
				letter=letter.replace("${interest}", params.get("interestSum"));
				msg=msg.replace("${interest}", params.get("interestSum"));

				if ((new BigDecimal(params.get("latechargeSum"))).compareTo(BigDecimal.ZERO) != 0) {
					letter=letter.replace("${latecharge}", "，逾期罚息" + params.get("latechargeSum") + "元");
					msg=msg.replace("${latecharge}", "，逾期罚息" + params.get("latechargeSum") + "元");
					letter=letter.replace("${latefeeforpt}", "，逾期手续费" + params.get("latefeeforptSum") + "元");
					msg=msg.replace("${latefeeforpt}", "，逾期手续费" + params.get("latefeeforptSum") + "元");
				}
				if (t6230.F10 != T6230_F10.YCFQ) {
					letter=letter.replace("${term}", "第" + term + "/" + totalTerms + "期");
					msg=msg.replace("${term}", "第" + term + "/" + totalTerms + "期");
				}
				if (term == totalTerms) {
					letter=letter.replace("${clear}", "，该借款已结清");
					msg=msg.replace("${clear}", "，该借款已结清");
				}
				// 站内信
				tradeCommonService.sendLetter(t6252s.get(0).F03, comment, letter);
				// 短信
				tradeCommonService.sendMsg(borrower.getPhone(), msg, 2);
			}
			logger.info("[" + loanId + "]还款后发信息结束。。。。。");
		} catch (Exception e) {
			logger.error("还款发信息时出错", e);
		}
	}

	private void sumT6252(int loanId, int term, Map<String, String> params) throws Exception {
		BigDecimal amountSum = BigDecimal.ZERO;
		BigDecimal principalSum = BigDecimal.ZERO;
		BigDecimal interestSum = BigDecimal.ZERO;
		BigDecimal latechargeSum = BigDecimal.ZERO;
		BigDecimal latefeeforptSum = BigDecimal.ZERO;
		Map<String, Object> p6252 = new HashMap<>();
		p6252.put("F02", loanId);
		p6252.put("F06", term);
		List<T6252> list = bidManageDao.getRepayPlan(p6252);
		for (T6252 e : list) {
			// 加息不是由借款人来出
			if (e.F05 != TradeFeeCode.TZ_JX) {
				amountSum = amountSum.add(e.F07);
			}
			if (e.F05 == TradeFeeCode.TZ_BJ) {
				principalSum = principalSum.add(e.F07);
			}
			if (e.F05 == TradeFeeCode.TZ_LX) {
				interestSum = interestSum.add(e.F07);
			}
			if (e.F05 == TradeFeeCode.TZ_FX) {
				latechargeSum = latechargeSum.add(e.F07);
			}
			if (e.F05 == TradeFeeCode.TZ_YQ_SXF) {
				latefeeforptSum = latefeeforptSum.add(e.F07);
			}
		}
		params.put("amountSum", amountSum.toString());
		params.put("principalSum", principalSum.toString());
		params.put("interestSum", interestSum.toString());
		params.put("latechargeSum", latechargeSum.toString());
		params.put("latefeeforptSum", latefeeforptSum.toString());
	}

	private BigDecimal selectT6252Amount(T6252 t6252, int F05) {
		BigDecimal amount = BigDecimal.ZERO;
		Map<String, Object> p6252 = new HashMap<>();
		p6252.put("F05", F05);
		p6252.put("F06", t6252.F06);
		p6252.put("F11", t6252.F11);
		List<T6252> list = bidManageDao.getRepayPlan(p6252);
		for (T6252 e : list) {
			amount = amount.add(e.F07);
		}
		return amount;
	}
}
