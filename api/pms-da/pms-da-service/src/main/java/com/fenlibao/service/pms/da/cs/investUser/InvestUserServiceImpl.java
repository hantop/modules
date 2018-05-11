package com.fenlibao.service.pms.da.cs.investUser;

import com.fenlibao.dao.pms.da.cs.investUser.InvestUserMapper;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.investUser.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */
@Service
public class InvestUserServiceImpl implements InvestUserService {
    @Resource
    private InvestUserMapper investUserMapper;
    @Override
    public List<InvestRecord> getInvestList(InvestRecord investRecord,String userId,RowBounds bounds,String startTime,String endTime) {
        return investUserMapper.getInvestRecordList(investRecord,userId,bounds, startTime, endTime);
    }

    @Override
    public List<InvestRecord> getInvestBuyOutList(InvestRecord investRecord, String userId, RowBounds bounds, String startTime, String endTime) {
        return investUserMapper.getInvestBuyOutList(investRecord,userId,bounds, startTime, endTime);
    }

    @Override
    public List<RightsRecord> getRightsList(RightsRecord investRecord, String userId, RowBounds rowBounds, String startTime, String endTime) {
        return investUserMapper.getRightsList(investRecord,userId,rowBounds, startTime, endTime);
    }

    @Override
    public List<InvestRecord> getOtherInfoInvestList(InvestRecord investRecord) {
        return investUserMapper.getOtherInfoInvestList(investRecord);
    }

    @Override
    public List<InvestRecord> getExpireTime(List<InvestRecord> list) {
        return investUserMapper.getExpireTime(list);
    }

    @Override
    public List<InvestRecord> getRightsInterest(List<InvestRecord> list,String userId) {
        return investUserMapper.getRightsInterest(list,userId);
    }

    @Override
    public List<RightsRecord> getRightsRecordFeeList(List<RightsRecord> rightsRecordList) {
        return  investUserMapper.getRightsRecordFeeList(rightsRecordList);
    }

    @Override
    public List<UserInvestPlan> getPlanList(String userId, UserInvestPlan userInvestPlan, RowBounds bounds) {
        return  investUserMapper.getPlanList(userId,  userInvestPlan,  bounds);
    }

    @Override
    public List<PlanDetail> getPlanDetail(String userId, UserInvestPlan userInvestPlan, RowBounds bounds) {
        return investUserMapper.getPlanDetail(userId,  userInvestPlan,  bounds);
    }

    @Override
    public List<TradeInfo> getTransactionList(String id, TradeInfo tradeForm, RowBounds bounds) {
        return investUserMapper.getTransactionList(id, tradeForm, bounds);
    }

    @Override
    public List<TradeType> getTradeTypes() {
        return investUserMapper.getTradeTypes();
    }

    @Override
    public List<InvestRecord> getCouponList(List<InvestRecord> investRecordList, String userId) {
        return investUserMapper.getCouponList(investRecordList,userId);
    }

    @Override
    public BigDecimal getExpectInterest(String recordId, String planId, String userId) {
        return investUserMapper.getExpectInterest(recordId, planId, userId);
    }

    @Override
    public BigDecimal getActualInterest(String recordId, String planId, String userId) {
        return investUserMapper.getActualInterest(recordId, planId, userId);
    }

    @Override
    public BigDecimal getPlanScope(String recordId, String userId) {
        return investUserMapper.getPlanScope(recordId, userId);
    }

    @Override
    public BigDecimal getPlanRedPacketMoney(String recordId, String userId) {
        return investUserMapper.getPlanRedPacketMoney(recordId, userId);
    }
}
