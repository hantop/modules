package com.fenlibao.service.pms.da.cs.account.impl;

import com.fenlibao.dao.pms.da.cs.account.TransactionMapper;
import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlanBid;
import com.fenlibao.model.pms.da.cs.form.InvestPlanForm;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import com.fenlibao.service.pms.da.cs.account.TransactionService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Bogle on 2015/12/22.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<Transaction> findRechargeHistoryByPhone(Transaction transtaction, RowBounds bounds) {
        return transactionMapper.findRechargeHistory(transtaction,bounds);
    }

    @Override
    public List<Transaction> findTractionHistoryByPhone(Transaction transaction, RowBounds bounds) {
        return transactionMapper.findTractionHistoryByPhone(transaction,bounds);
    }

    @Override
    public List<Transaction> findwithdrawalHistoryByPhone(Transaction transaction, RowBounds bounds) {
        return transactionMapper.findwithdrawalHistoryByPhone(transaction,bounds);
    }

    @Override
    public List<Transaction> findInvestHistoryByPhone(Transaction transaction, RowBounds bounds) {
		List<Transaction> list = new ArrayList<>();
		list = transactionMapper.findInvestHistoryByPhone(transaction,bounds);
		for (Transaction temp : list) {
			if (temp.getScope() != null && temp.getScope().compareTo(BigDecimal.ZERO) > 0){
				temp.setScope(temp.getScope().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
			}
			if (temp.getBidScope() != null && temp.getBidScope().compareTo(BigDecimal.ZERO) > 0){
				temp.setBidScope(temp.getBidScope().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
			}
            if (temp.getInterestSum() != null ){
                temp.setInterestSum(new BigDecimal(temp.getInterestSum()).setScale(2,RoundingMode.HALF_UP).toString());
            }
            if (temp.getOrigenRate() != null ){
                temp.setOrigenRate(new BigDecimal(temp.getOrigenRate()).setScale(2,RoundingMode.HALF_UP).toString());
            }
            if (temp.getActuralEarn() != null ){
                temp.setActuralEarn(new BigDecimal(temp.getActuralEarn()).setScale(2,RoundingMode.HALF_UP).toString());
            }
            if (temp.getBidInterestSum() != null ){
                temp.setBidInterestSum(new BigDecimal(temp.getBidInterestSum()).setScale(2,RoundingMode.HALF_UP).toString());
            }
		}
		return list;
    }

	@Override
	public List<Transaction> findInvestSoldHistoryByPhone(TransactionForm transaction, RowBounds bounds) {
		return transactionMapper.findInvestSoldHistoryByPhone(transaction,bounds);
	}

	@Override
	public BigDecimal getTotalOrigrienMoney(TransactionForm transaction) {
		return transactionMapper.getTotalOrigrienMoney(transaction);
	}

	@Override
	public BigDecimal getTotalInvestMoney(TransactionForm transaction) {
		return transactionMapper.getTotalInvestMoney(transaction);
	}

	@Override
	public BigDecimal getTotalInvestMoneyOut(TransactionForm transaction) {
		return transactionMapper.getTotalInvestMoneyOut(transaction);
	}

	@Override
	public BigDecimal getTotalOrigrienMoneyOut(TransactionForm transaction) {
		return transactionMapper.getTotalOrigrienMoneyOut(transaction);
	}

    @Override
    public Integer findUserIdByPhone(TransactionForm transaction) {
        return transactionMapper.findUserIdByPhone(transaction);
    }

	@Override
	public List<UserInvestPlan> findUserPlan(InvestPlanForm planForm, RowBounds bounds) {
        List<Integer> oldPlanIds = new ArrayList<Integer>();// 旧计划
        List<Integer> newPlanIds = new ArrayList<Integer>();// 新计划
		List<UserInvestPlan> userInvestPlanBase = transactionMapper.getUserInvestPlan(planForm, bounds);
        if(userInvestPlanBase.size() > 0){
            for (UserInvestPlan userInvestPlan: userInvestPlanBase) {
                if(userInvestPlan.getBidScope() != null){//标加息
                    userInvestPlan.setBidScope(userInvestPlan.getBidScope().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }
                /*if(userInvestPlan.getScope() != null){//加息券加息
                    userInvestPlan.setScope(userInvestPlan.getScope().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }*/
                if(userInvestPlan.getType() == 1){//月升计划
                    userInvestPlan.setMinYearlyRate(
                            userInvestPlan.getMinYearlyRate() == null? null :
                                    userInvestPlan.getMinYearlyRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                    userInvestPlan.setMaxYearlyRate(
                            userInvestPlan.getMaxYearlyRate() == null? null :
                                    userInvestPlan.getMaxYearlyRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                }else{
                    userInvestPlan.setInvestRate(userInvestPlan.getInvestRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                }
                if(userInvestPlan.getPlanType().equals("0")) {// 旧计划标记
                    if (!oldPlanIds.contains(userInvestPlan.getPlanId())) {
                        oldPlanIds.add(Integer.valueOf(userInvestPlan.getPlanId()));
                    }
                }else if(userInvestPlan.getPlanType().equals("1")){//新计划标志
                    if (!newPlanIds.contains(userInvestPlan.getPlanId())) {
                        newPlanIds.add(Integer.valueOf(userInvestPlan.getPlanId()));
                    }
                }
            }
            // 用户计划加息券
            List<Map<String, Object>> userPlanCoupon = transactionMapper.getUserPlanCoupon(planForm.getUserId());
            // 用户计划返现券
            List<Map<String, Object>> userPlanRedPacket = transactionMapper.getUserPlanRedPackets(planForm.getUserId());
            // 用户收益
            List<Map<String, Object>> userPlanIncome = new ArrayList<Map<String, Object>>();
            if(oldPlanIds.size() > 0){
                if(newPlanIds.size() > 0){
                    userPlanIncome = transactionMapper.getUserPlanIncome(planForm.getUserId(), oldPlanIds, newPlanIds);
                }else{
                    userPlanIncome = transactionMapper.getUserOldPlanIncome(planForm.getUserId(), oldPlanIds);
                }
            }else{
                if(newPlanIds.size() > 0){
                    userPlanIncome = transactionMapper.getUserNewPlanIncome(planForm.getUserId(), newPlanIds);
                }
            }
            for (UserInvestPlan userInvestPlan: userInvestPlanBase){
                // 加息券
                Map<String, Object> ucoupon = getUserPlanRewardDetail(userPlanCoupon, userInvestPlan.getPlanType(), userInvestPlan.getRecordId());
                if(ucoupon != null){
                    String scope = ucoupon.get("scope").toString() == null ? null : ucoupon.get("scope").toString();
                    userInvestPlan.setScope(new BigDecimal(scope).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                }
                // 返现券
                Map<String, Object> uredPackets = getUserPlanRewardDetail(userPlanRedPacket, userInvestPlan.getPlanType(), userInvestPlan.getRecordId());
                if(uredPackets != null){
                    String redPacketMoney = uredPackets.get("redPacketMoney").toString() == null ? null : uredPackets.get("redPacketMoney").toString();
                    userInvestPlan.setRedPacketMoney(new BigDecimal(redPacketMoney).setScale(2, RoundingMode.HALF_UP));
                }
                Map<String, Object> detail = getUserPlanIncomeDetail(userPlanIncome, userInvestPlan.getPlanType(), userInvestPlan.getRecordId());
                if(detail != null){
                    String origenRate = detail.get("origenRate").toString() == null ? null : detail.get("origenRate").toString();
                    String interest = detail.get("interest").toString() == null ? null : detail.get("interest").toString();
                    String planInterest = detail.get("planInterest").toString() == null ? null : detail.get("planInterest").toString();
                    String paymentInterate = detail.get("paymentInterate").toString() == null ? null : detail.get("paymentInterate").toString();
                    String penalty = detail.get("penalty").toString() == null ? null : detail.get("penalty").toString();
                    userInvestPlan.setOrigenRate(origenRate);
                    userInvestPlan.setInterest(interest);
                    userInvestPlan.setPlanInterate(planInterest);
                    userInvestPlan.setPaymentInterate(paymentInterate);
                    userInvestPlan.setPenalty(penalty);
                }
            }
        }
		return userInvestPlanBase;
	}

    private Map<String, Object> getUserPlanIncomeDetail(List<Map<String, Object>> userPlanIncome, String planType, String recordId){
        for (Map<String, Object> userPlanIncomeDetail : userPlanIncome) {
            if (recordId.toString().equals(userPlanIncomeDetail.get("recordId").toString())
                    && planType.toString().equals(userPlanIncomeDetail.get("planType").toString())) {
                return userPlanIncomeDetail;
            }
        }
        return null;
    }

    private Map<String, Object> getUserPlanRewardDetail(List<Map<String, Object>> userPlanReward, String planType, String recordId){
        for (Map<String, Object> userPlanRewardDetail : userPlanReward) {
            if (recordId.toString().equals(userPlanRewardDetail.get("recordId").toString())
                    && planType.toString().equals(userPlanRewardDetail.get("planType").toString())) {
                return userPlanRewardDetail;
            }
        }
        return null;
    }

    @Override
    public List<UserInvestPlanBid> findUserPlanBid(InvestPlanForm planForm, RowBounds bounds) {
        List<UserInvestPlanBid> userInvestPlanBids = new ArrayList<UserInvestPlanBid>();
        if(planForm.getPlanType() == 0){
            userInvestPlanBids = transactionMapper.findUserOldPlanBid(planForm, bounds);// 旧计划记录
            for (UserInvestPlanBid userInvestBid: userInvestPlanBids) {
                userInvestBid.setRate(userInvestBid.getRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }
        }else{
            userInvestPlanBids = transactionMapper.findUserNewPlanBid(planForm, bounds);// 新计划记录
            for (UserInvestPlanBid userInvestBid: userInvestPlanBids) {
                userInvestBid.setRate(userInvestBid.getRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return userInvestPlanBids;
    }
}
