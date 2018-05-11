package com.fenlibao.p2p.model.vo.fiancing;

import java.math.BigDecimal;
import java.util.List;

import com.fenlibao.p2p.model.entity.finacing.UserRepaymentAmout;

/**
 * Created by junda.feng on 2016/6/20.
 * '状态,WH:未还;YH:已还;HKZ:还款中;TQH:提前还;DF:垫付'
 */
public class UserRepaymentAmoutVO {
    
	private BigDecimal expected=new BigDecimal(0);//待回款
	
	private BigDecimal history=new BigDecimal(0);//已回款

	
	public UserRepaymentAmoutVO(List<UserRepaymentAmout> list ) {
		if(list==null || list.size()==0){
			this.expected=BigDecimal.ZERO;
			this.history=BigDecimal.ZERO;
		}else{
			for (UserRepaymentAmout userRepaymentAmout : list) {
				if("WH".equals(userRepaymentAmout.getState())||"HKZ".equals(userRepaymentAmout.getState())) {
					this.expected=this.expected.add(userRepaymentAmout.getSum());
				}else{
					this.history=this.history.add(userRepaymentAmout.getSum());
				}
			}
		}
		
	}


	public BigDecimal getExpected() {
		return expected;
	}


	public void setExpected(BigDecimal expected) {
		this.expected = expected;
	}


	public BigDecimal getHistory() {
		return history;
	}


	public void setHistory(BigDecimal history) {
		this.history = history;
	}

}
