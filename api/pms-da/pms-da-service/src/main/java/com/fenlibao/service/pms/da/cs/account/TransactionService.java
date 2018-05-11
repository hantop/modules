package com.fenlibao.service.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlanBid;
import com.fenlibao.model.pms.da.cs.form.InvestPlanForm;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户流水记录service
 * Created by Bogle on 2015/12/22.
 */
public interface TransactionService {

    /**
     * 根据用户传入的手机号分页获取充值流水列表
     *
     * @param transtaction
     * @param bounds
     * @return
     */
    List<Transaction> findRechargeHistoryByPhone(Transaction transtaction, RowBounds bounds);

    /**
     * 交易流水
     * @param transaction
     * @param bounds
     * @return
     */
    List<Transaction> findTractionHistoryByPhone(Transaction transaction, RowBounds bounds);

    /**
     * 提现记录
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
	 * 债权本金总计(买入)
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
	 * 债权本金总计(卖出)
	 * @param transaction
	 * @return
	 */
	BigDecimal getTotalOrigrienMoneyOut(TransactionForm transaction);

	/**
	 * 电话号码查id
	 * @param transaction
	 * @return
	 */
	Integer findUserIdByPhone(TransactionForm transaction);

	/**
	 * 用户计划记录
	 * @param planForm
	 * @param bounds
	 * @return
	 */
	List<UserInvestPlan> findUserPlan(InvestPlanForm planForm, RowBounds bounds);

	List<UserInvestPlanBid> findUserPlanBid(InvestPlanForm planForm, RowBounds bounds);

}
