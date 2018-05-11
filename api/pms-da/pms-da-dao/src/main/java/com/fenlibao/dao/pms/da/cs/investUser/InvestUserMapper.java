package com.fenlibao.dao.pms.da.cs.investUser;

import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.investUser.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */

public interface InvestUserMapper {

    /**
     * 获取投标记录
     * @param investRecord
     * @return
     */
    public List<InvestRecord> getInvestRecordList(@Param("investRecord") InvestRecord investRecord, @Param("userId") String userId, RowBounds bounds, @Param("startTime") String startTime, @Param("endTime") String endTime);


    List<InvestRecord> getInvestBuyOutList(@Param("investRecord") InvestRecord investRecord, @Param("userId") String userId, RowBounds bounds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    public List<InvestRecord> getOtherInfoInvestList(InvestRecord investRecord);

    /**
     * 获取到期时间
     * @param list
     * @return
     */
    public List<InvestRecord> getExpireTime(List<InvestRecord> list);

    /**
     * 获取债权利息
     * @param list
     * @return
     */
    public List<InvestRecord> getRightsInterest(@Param("list") List<InvestRecord> list, @Param("userId") String userId);


    /**
     * 获取债权列表
     * @param rightsRecord
     * @param userId
     * @param bounds
     * @param startTime
     * @param endTime
     * @return
     */
    List<RightsRecord> getRightsList(@Param("rightsRecord") RightsRecord rightsRecord, @Param("userId") String userId, RowBounds bounds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取债权手续费
     * @param rightsRecordList
     * @return
     */
    List<RightsRecord> getRightsRecordFeeList(List<RightsRecord> rightsRecordList);

    /**
     * 计划记录
     * @param userId
     * @param
     * @param bounds
     * @return
     */
    List<UserInvestPlan> getPlanList(@Param("userId") String userId, @Param("investPlan") UserInvestPlan investPlan, RowBounds bounds);

    /**
     * 计划详情
     * @param userId
     * @param investPlan
     * @param bounds
     * @return
     */
    List<PlanDetail> getPlanDetail(@Param("userId") String userId, @Param("investPlan") UserInvestPlan investPlan, RowBounds bounds);

    /**
     * 获取交易记录流水
     * @param id
     * @param tradeForm
     * @param bounds
     * @return
     */
    List<TradeInfo> getTransactionList(@Param("id") String id, @Param("tradeForm") TradeInfo tradeForm, RowBounds bounds);

    List<TradeType> getTradeTypes();

    List<InvestRecord> getCouponList(@Param("list") List<InvestRecord> investRecordList, @Param("userId") String userId);

    BigDecimal getExpectInterest(@Param("recordId") String recordId, @Param("planId") String planId, @Param("userId") String userId);

    BigDecimal getActualInterest(@Param("recordId") String recordId, @Param("planId") String planId, @Param("userId") String userId);

    BigDecimal getPlanScope(@Param("recordId") String recordId, @Param("userId") String userId);

    BigDecimal getPlanRedPacketMoney(@Param("recordId") String recordId, @Param("userId") String userId);
}
