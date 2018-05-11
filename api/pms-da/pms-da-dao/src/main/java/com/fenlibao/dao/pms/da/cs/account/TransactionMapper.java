package com.fenlibao.dao.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlanBid;
import com.fenlibao.model.pms.da.cs.form.InvestPlanForm;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 流水记录mapper
 * Created by Bogle on 2015/12/22.
 */
public interface TransactionMapper {

    /**
     * 分页获取流水信息
     *
     * @param transtaction
     * @param bounds
     * @return
     */
    List<Transaction> findRechargeHistory(Transaction transtaction, RowBounds bounds);

    /**
     * 交易流水
     * @param transaction
     * @param bounds
     * @return
     */
    List<Transaction> findTractionHistoryByPhone(Transaction transaction, RowBounds bounds);

    /**
     * 体现记录
     * @param transaction
     * @param bounds
     * @return
     */
    List<Transaction> findwithdrawalHistoryByPhone(Transaction transaction, RowBounds bounds);

    /**
     * 投资记录--买入
     * @param transaction
     * @param bounds
     * @return
     */
    List<Transaction> findInvestHistoryByPhone(Transaction transaction, RowBounds bounds);

    /**
     * 投资记录--卖出
     * @param transaction
     * @param bounds
     * @return
     */
	List<Transaction> findInvestSoldHistoryByPhone(TransactionForm transaction, RowBounds bounds);

	/**
	 * 债权本金总计 (买入)
	 * @param transaction
	 * @return
	 */
	BigDecimal getTotalOrigrienMoney(TransactionForm transaction);
	
	/**
	 * 投资金额总计(买入)
	 * @param transaction
	 * @return
	 */
	BigDecimal getTotalInvestMoney(TransactionForm transaction);

	/**
	 * 投资金额总计(卖出)
	 * @param transaction
	 * @return
	 */
	BigDecimal getTotalInvestMoneyOut(TransactionForm transaction);

	/**
	 * 债权本金总计 (卖出)
	 * @param transaction
	 * @return
	 */
	BigDecimal getTotalOrigrienMoneyOut(TransactionForm transaction);

    Integer findUserIdByPhone(TransactionForm transaction);

	/**
	 * 用户计划
	 * @param planForm
	 * @param bounds
	 * @return
	 */
	List<UserInvestPlan> getUserInvestPlan(InvestPlanForm planForm, RowBounds bounds);

	/**
	 * 用户计划加息券
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserPlanCoupon(
			@Param("userId")Integer userId
	);

	/**
	 * 用户计划返现券
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserPlanRedPackets(
			@Param("userId")Integer userId
	);

    /**
     * 用户全部计划收益
     * @param userId
     * @param oldPlanIds 旧计划
     * @param newPlanIds 新计划
     * @return
     */
    List<Map<String, Object>> getUserPlanIncome(
            @Param("userId")Integer userId,
            @Param("oldPlanIds") List<Integer> oldPlanIds,
            @Param("newPlanIds") List<Integer> newPlanIds
            );

    /**
     * 用户旧计划收益
     * @param userId
     * @param oldPlanIds
     * @return
     */
    List<Map<String, Object>> getUserOldPlanIncome(
            @Param("userId")Integer userId,
            @Param("oldPlanIds") List<Integer> oldPlanIds
    );

    /**
     * 用户新计划收益
     * @param userId
     * @param newPlanIds
     * @return
     */
    List<Map<String, Object>> getUserNewPlanIncome(
            @Param("userId")Integer userId,
            @Param("newPlanIds") List<Integer> newPlanIds
            );


	/**
	 * 用户旧计划记录标的信息
	 * @param planForm
	 * @param bounds
	 * @return
	 */
	List<UserInvestPlanBid> findUserOldPlanBid(InvestPlanForm planForm, RowBounds bounds);

	/**
	 * 用户新计划记录标的信息
	 * @param planForm
	 * @param bounds
	 * @return
	 */
	List<UserInvestPlanBid> findUserNewPlanBid(InvestPlanForm planForm, RowBounds bounds);

}
