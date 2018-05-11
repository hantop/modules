package com.fenlibao.service.pms.da.cs.investUser;


import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.investUser.*;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */
public interface InvestUserService {

    /**
     * 获取投标记录（不包括转出的债权）
     * @param investRecord
     * @return
     */
    public List<InvestRecord> getInvestList(InvestRecord investRecord, String userId, RowBounds rowBounds, String startTime, String endTime);


    /**
     * 获取债权记录
     * @param investRecord
     * @param userId
     * @param rowBounds
     * @param startTime
     * @param endTime
     * @return
     */
    public List<RightsRecord> getRightsList(RightsRecord investRecord, String userId, RowBounds rowBounds, String startTime, String endTime);

    /**
     * 补全投标记录InvestUserService#getInvestList未拿到的信息
     * @param investRecord
     * @return
     */
    public List<InvestRecord> getOtherInfoInvestList(InvestRecord investRecord);

    /**
     * 获取到期时间
     * @param list
     * @return
     */
    public List<InvestRecord> getExpireTime(List<InvestRecord> list);

    /**
     * 获取债权利息(标的利息,加息券利息,标加息利息,实际收益)
     * @param list
     * @return
     */
    public  List<InvestRecord> getRightsInterest(List<InvestRecord> list, String userId);

    /**
     * 获取债权手续费
     * @param rightsRecordList
     * @return
     */
    List<RightsRecord> getRightsRecordFeeList(List<RightsRecord> rightsRecordList);

    /**
     * 计划记录
     * @param userId
     * @param userInvestPlan
     * @param bounds
     * @return
     */
    List<UserInvestPlan> getPlanList(String userId, UserInvestPlan userInvestPlan, RowBounds bounds);

    /**
     * 计划详情
     * @param userId
     * @param userInvestPlan
     * @param bounds
     * @return
     */
    List<PlanDetail> getPlanDetail(String userId, UserInvestPlan userInvestPlan, RowBounds bounds);

    /**
     * 获取交易记录
     * @param id 资金记录表id t6101.F01
     * @param tradeForm
     * @param bounds
     * @return
     */
    List<TradeInfo> getTransactionList(String id, TradeInfo tradeForm, RowBounds bounds);

    List<TradeType> getTradeTypes();

    List<InvestRecord> getCouponList(List<InvestRecord> investRecordList, String userId);

    List<InvestRecord> getInvestBuyOutList(InvestRecord investRecord, String userId, RowBounds bounds, String startTime, String endTime);

    BigDecimal getExpectInterest(String recordId, String planId, String userId);

    BigDecimal getActualInterest(String recordId, String planId, String userId);

    BigDecimal getPlanScope(String recordId, String userId);

    BigDecimal getPlanRedPacketMoney(String recordId, String userId);
}
