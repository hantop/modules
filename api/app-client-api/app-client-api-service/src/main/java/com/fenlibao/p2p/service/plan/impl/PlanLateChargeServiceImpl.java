package com.fenlibao.p2p.service.plan.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.plan.PlanLateChargeDao;
import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;
import com.fenlibao.p2p.model.enums.plan.UserPlanRepayment_TradeType;
import com.fenlibao.p2p.service.plan.PlanLateChargeService;

@Service
public class PlanLateChargeServiceImpl implements PlanLateChargeService{

	@Resource
	PlanLateChargeDao planLateChargeDao;
	
	private final long DAY_IN_MILLISECONDS = 86400000L;
	
	@Transactional
	@Override
	public void planLateCharge() throws Exception {
		List<UserPlanRepayment> principalAndInterestList= planLateChargeDao.getOverdueDebts();
        if (principalAndInterestList == null)
        {
            return;
        }
        List<UserPlanRepayment> overdueFeeList=new ArrayList<>();
        for(UserPlanRepayment item:principalAndInterestList){
        	int days = (int)Math.floor((new Date().getTime() - item.getRepaymentDate().getTime()) / DAY_IN_MILLISECONDS);
        	BigDecimal overdueFee=item.getAmount().multiply(item.getOverdueRate()).multiply(new BigDecimal(days)).setScale(2, RoundingMode.HALF_UP);
        	if(overdueFee.compareTo(BigDecimal.ZERO)==0){
        		overdueFee=new BigDecimal(0.01);
        	}
        	UserPlanRepayment overdueItem=planLateChargeDao.getUserPlanRepayment(item.getUserPlanProductId(), UserPlanRepayment_TradeType.YQFX.getCode());
        	if(overdueItem==null){
        		overdueItem=new UserPlanRepayment();
        		overdueItem.setUserPlanId(item.getUserPlanId());
        		overdueItem.setUserPlanProductId(item.getUserPlanProductId());
        		overdueItem.setPayeeId(item.getPayeeId());
        		overdueItem.setAmount(overdueFee);
        		overdueItem.setRepaymentDate(item.getRepaymentDate());
        		overdueItem.setTradeType(UserPlanRepayment_TradeType.YQFX.getCode());
        		overdueFeeList.add(overdueItem);
        	}
        	else{
        		Map<String, Object> param=new HashMap<>();
        		param.put("id", overdueItem.getUserPlanRepaymentId());
        		param.put("amount", overdueFee);
        		planLateChargeDao.updateUserPlanRepayment(param);
        	}
        }
        if(overdueFeeList.size()>0){
        	planLateChargeDao.batchInsertUserPlanRepayment(overdueFeeList);
        }
	}

}
