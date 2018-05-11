package com.fenlibao.p2p.service.trade.bid;

import java.math.BigDecimal;

public interface OtherTaskOfMakeALoan {
	public void sendLetterAndMsg(int loanId, String flowNum, BigDecimal balance) throws Exception;
}
