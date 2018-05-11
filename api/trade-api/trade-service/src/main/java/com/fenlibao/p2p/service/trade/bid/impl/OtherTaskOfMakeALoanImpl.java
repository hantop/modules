package com.fenlibao.p2p.service.trade.bid.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.enums.BidSource;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfMakeALoan;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;

@Service
public class OtherTaskOfMakeALoanImpl implements OtherTaskOfMakeALoan{
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	BidManageDao bidManageDao;

	@Resource
	TradeCommonService tradeCommonService;

	@Resource
	TradeCommonDao tradeCommonDao;

	@Resource
	UserDao userDao;

	@Override
	@Transactional
	public void sendLetterAndMsg(int loanId, String flowNum, BigDecimal balance) throws Exception {
		try {
			logger.info("[" + loanId + "]放款后发信息开始。。。。。");
			T6230 t6230 = bidManageDao.getBidById(loanId);
			if (BidSource.QQM.getCode().equals(t6230.F36)) {
				return;
			}
			String letter = tradeCommonDao.getSystemVariable("LETTER.LOAN_SUCCESS");
			String msg = tradeCommonDao.getSystemVariable("MSG.LOAN_SUCCESS");
			letter=letter.replace("${title}", t6230.F03);
			msg=msg.replace("${title}", t6230.F03);
			BigDecimal amount = t6230.F05.subtract(t6230.F07).setScale(2, RoundingMode.HALF_UP);
			letter=letter.replace("${amount}", amount.toString());
			msg=msg.replace("${amount}", amount.toString());
			letter=letter.replace("${balance}", balance.setScale(2, RoundingMode.HALF_UP).toString());
			msg=msg.replace("${balance}", balance.setScale(2, RoundingMode.HALF_UP).toString());
			UserInfoEntity borrower = userDao.get(t6230.F02, null);
			// 站内信
			tradeCommonService.sendLetter(t6230.F02, "放款成功", letter);
			// 短信
			tradeCommonService.sendMsg(borrower.getPhone(), msg, 2);
			logger.info("[" + loanId + "]放款后发信息结束。。。。。");
		} catch (Exception e) {
			logger.error("放款发信息时出错", e);
		}
	}

}
