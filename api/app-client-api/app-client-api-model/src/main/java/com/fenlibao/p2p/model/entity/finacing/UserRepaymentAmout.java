package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;

/**
 * Created by junda.feng on 2016/6/17.
 */
public class UserRepaymentAmout {
    
	private BigDecimal sum;//总数
	
	private String state;//'状态,WH:未还;YH:已还;HKZ:还款中;TQH:提前还;DF:垫付'

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
	
    
}
