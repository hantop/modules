package com.fenlibao.p2p.service.plan.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.plan.PlanInfoDao;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanTotalRepay;
import com.fenlibao.p2p.model.enums.bid.RepaymentModeEnum;
import com.fenlibao.p2p.model.enums.plan.InvestPlanInfo_CycleType;
import com.fenlibao.p2p.model.enums.plan.PlanTypeEnum;
import com.fenlibao.p2p.model.enums.plan.UserPlanRepay_State;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.service.plan.UserPlanRepayService;

@Service
public class UserPlanRepayServiceImpl implements UserPlanRepayService{

	private static final Logger LOG = LogManager.getLogger(UserPlanRepayServiceImpl.class);
	
	@Resource
	PlanInfoDao planInfoDao;
	
	@Transactional(propagation = Propagation.NESTED)
	@Override
	public void createExpectedRepayPlan(int planId) throws Exception{
		//计划信息
		InvestPlanInfo investPlanInfo=planInfoDao.getPlanInfo(planId);
		if(investPlanInfo==null){
			throw new RuntimeException("创建计划预期还款计划：计划不存在");
		}
		//用户出借计划记录
		List<UserPlan> userPlanList=planInfoDao.getUserPlansByPlanId(planId);
		if(userPlanList==null||userPlanList.isEmpty()){
			throw new RuntimeException("创建计划预期还款计划：没有计划出借记录");
		}
		List<UserPlanTotalRepay> listToInsert=new ArrayList<>();
		BigDecimal investCycle=new BigDecimal(investPlanInfo.getCycle());
		BigDecimal monthsOfYear=new BigDecimal(12);
		BigDecimal daysOfYear=new BigDecimal(365);
		BigDecimal rate= (investPlanInfo.getPlanType()==PlanTypeEnum.YYS.getCode())?investPlanInfo.getMaxYearlyRate():investPlanInfo.getInvestRate();
		
		if(RepaymentModeEnum.YCFQ.getCode().equals(investPlanInfo.getRepayMode())){
			for(UserPlan item:userPlanList){
				//期望本金记录
				UserPlanTotalRepay principal=new UserPlanTotalRepay();
				principal.setPlanId(planId);
				principal.setUserId(item.getUserId());
				principal.setUserPlanId(item.getUserPlanId());
				principal.setTransactionType(FeeCode.TZ_BJ);
				principal.setTerm(1);
				principal.setState(UserPlanRepay_State.WH.name());
				principal.setExpectAmount(item.getInvestAmount());
				principal.setExpectRepayDate(investPlanInfo.getExpireTime());
				listToInsert.add(principal);
				//期望利息记录
				UserPlanTotalRepay interest=new UserPlanTotalRepay();
				interest.setPlanId(planId);
				interest.setUserId(item.getUserId());
				interest.setUserPlanId(item.getUserPlanId());
				interest.setTransactionType(FeeCode.TZ_LX);
				interest.setTerm(1);
				interest.setState(UserPlanRepay_State.WH.name());
				BigDecimal interestAmount=BigDecimal.ZERO;
				if(InvestPlanInfo_CycleType.MONTH.getCode().equals(investPlanInfo.getCycleType())){
					interestAmount=item.getInvestAmount().multiply(rate).multiply(investCycle).divide(monthsOfYear, 2, RoundingMode.HALF_UP);
				}
				else{
					interestAmount=item.getInvestAmount().multiply(rate).multiply(investCycle).divide(daysOfYear, 2, RoundingMode.HALF_UP);
				}
				interest.setExpectAmount(interestAmount);
				interest.setExpectRepayDate(investPlanInfo.getExpireTime());
				listToInsert.add(interest);
				//期望加息券加息
				UserCouponInfo userCoupon=planInfoDao.getUserCoupon(item.getUserPlanId());
				if(userCoupon!=null){
					UserPlanTotalRepay couponRaiseInterest=new UserPlanTotalRepay();
					couponRaiseInterest.setPlanId(planId);
					couponRaiseInterest.setUserId(item.getUserId());
					couponRaiseInterest.setUserPlanId(item.getUserPlanId());
					couponRaiseInterest.setTransactionType(FeeCode.TZ_JXJL);
					couponRaiseInterest.setTerm(1);
					couponRaiseInterest.setState(UserPlanRepay_State.WH.name());
					
					BigDecimal couponRaiseInterestAmount=BigDecimal.ZERO;
					if(InvestPlanInfo_CycleType.MONTH.getCode().equals(investPlanInfo.getCycleType())){
						couponRaiseInterestAmount=item.getInvestAmount().multiply(userCoupon.getScope()).multiply(investCycle).divide(monthsOfYear, 2, RoundingMode.HALF_UP);
					}
					else{
						couponRaiseInterestAmount=item.getInvestAmount().multiply(userCoupon.getScope()).multiply(investCycle).divide(daysOfYear, 2, RoundingMode.HALF_UP);
					}
					couponRaiseInterest.setExpectAmount(couponRaiseInterestAmount);
					couponRaiseInterest.setExpectRepayDate(investPlanInfo.getExpireTime());
					listToInsert.add(couponRaiseInterest);
				}
				//期望计划加息
				if(investPlanInfo.getRaiseRate()!=null){
					if(investPlanInfo.getRaiseRate().compareTo(BigDecimal.ZERO)>0){
						UserPlanTotalRepay planRaiseInterest=new UserPlanTotalRepay();
						planRaiseInterest.setPlanId(planId);
						planRaiseInterest.setUserId(item.getUserId());
						planRaiseInterest.setUserPlanId(item.getUserPlanId());
						planRaiseInterest.setTransactionType(FeeCode.PLAN_JX);
						planRaiseInterest.setTerm(1);
						planRaiseInterest.setState(UserPlanRepay_State.WH.name());
						BigDecimal planRaiseInterestAmount=BigDecimal.ZERO;
						if(InvestPlanInfo_CycleType.MONTH.getCode().equals(investPlanInfo.getCycleType())){
							planRaiseInterestAmount=item.getInvestAmount().multiply(investPlanInfo.getRaiseRate()).multiply(investCycle).divide(monthsOfYear, 2, RoundingMode.HALF_UP);
						}
						else{
							planRaiseInterestAmount=item.getInvestAmount().multiply(investPlanInfo.getRaiseRate()).multiply(investCycle).divide(daysOfYear, 2, RoundingMode.HALF_UP);
						}
						planRaiseInterest.setExpectAmount(planRaiseInterestAmount);
						planRaiseInterest.setExpectRepayDate(investPlanInfo.getExpireTime());
						listToInsert.add(planRaiseInterest);
					}
				}
				
			}
		}
		planInfoDao.batchInsertUserPlanRepayPlan(listToInsert); 
	}

}
