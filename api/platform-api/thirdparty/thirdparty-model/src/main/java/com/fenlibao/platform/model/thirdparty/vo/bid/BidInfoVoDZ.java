package com.fenlibao.platform.model.thirdparty.vo.bid;

import java.util.List;

import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;


/**
 * 借款标信息--多赚
 * @author junda.feng
 * @date 2016年5月30日
 * 
 */
public class BidInfoVoDZ extends BaseBidInfoVo<BidInfoVoDZ>{
	
	private String repaymentType;//还款方式
	private String warrantcom;//担保公司名称，如无默认--
    List<BidInvestrecordsVoDZ>  subscribes;

    public BidInfoVoDZ(BidInfoEntity bid) throws Throwable {
		super(bid);
		this.repaymentType=bid.getRepaymentType().getName();
		this.warrantcom="--";//担保公司名称，如无默认--，多个请用+分割
	}

    
	public String getRepaymentType() {
		return repaymentType;
	}


	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}


	public String getWarrantcom() {
		return warrantcom;
	}
	public void setWarrantcom(String warrantcom) {
		this.warrantcom = warrantcom;
	}
	public List<BidInvestrecordsVoDZ> getSubscribes() {
		return subscribes;
	}
	public void setSubscribes(List<BidInvestrecordsVoDZ> subscribes) {
		this.subscribes = subscribes;
	}

    
}
