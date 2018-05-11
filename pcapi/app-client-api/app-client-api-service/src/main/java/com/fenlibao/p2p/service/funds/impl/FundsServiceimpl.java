package com.fenlibao.p2p.service.funds.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.funds.IFundsDao;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.service.funds.IFundsService;

@Service
public class FundsServiceimpl implements IFundsService{
	
	@Resource
	private IFundsDao fundsDao;

	@Transactional(propagation = Propagation.MANDATORY) //调用该方法的service必须要有事务，否侧抛异常！！！
	@Override
	public void transfer(int userId, BigDecimal amount, FeeType feeType, String accountType, String platformAccount)
			throws Exception {
		// 锁定并获取用户相对应类型的账户
        UserAccount userAccount = this.lockAccount(userId, accountType);
        if(userAccount == null) {
            throw new LogicalException("用户资金账户不存在");
        }
        // 用户账户ID
        int userAccountId = userAccount.getAccountId();
        // 获取平台资金账户ID
        Integer platformAccountId = this.getAccountId(platformAccount, accountType);
        if(platformAccountId == null) {
            throw new LogicalException("平台资金账户不存在");
        }
        // 锁定资金账户
        UserAccount platform = this.lockAccountById(platformAccountId);
        int feeTypeCode = feeType.getCode();
        String feeTypeName = feeType.getName();
        // 平台账户余额
        BigDecimal platformBalance = platform.getBalance();
        // 插入个人资金交易记录(转入)
        this.insertFundsflowRecord(userAccountId, platformAccountId, 
        		feeTypeCode, amount, BigDecimal.ZERO, userAccount.getBalance().add(amount), feeTypeName);
        // 插入平台资金交易记录(转出)
        this.insertFundsflowRecord(platformAccountId, userAccountId, 
        		feeTypeCode, BigDecimal.ZERO, amount, platformBalance.subtract(amount), feeTypeName);
        // 增加用户资金余额
        this.increaseOrSubtractAccountBalance(userAccountId, amount);
        // 减少平台资金账户余额
        this.increaseOrSubtractAccountBalance(platformAccountId, BigDecimal.ZERO.subtract(amount));
	}

	@Override
	public UserAccount lockAccount(int userId, String accountType) {
		return this.fundsDao.lockAccount(userId, accountType);
	}

	@Override
	public Integer getAccountId(String account, String accountType) {
		return this.fundsDao.getAccountId(account, accountType);
	}

	@Override
	public UserAccount lockAccountById(int accountId) {
		return this.fundsDao.lockAccountById(accountId);
	}

	@Override
	public void insertFundsflowRecord(int fundsAccountId, int relativelyAccountId, int tradeTypeId, BigDecimal income, 
			BigDecimal expenditure,BigDecimal balance, String remark) throws Exception {
		this.fundsDao.insertFundsflowRecord(fundsAccountId, relativelyAccountId, tradeTypeId, income, expenditure, balance, remark);
	}

	@Override
	public void increaseOrSubtractAccountBalance(int accountId, BigDecimal amount) throws Exception {
		this.fundsDao.increaseOrSubtractAccountBalance(accountId, amount);
	}


}
