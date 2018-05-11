package com.fenlibao.p2p.service.funds.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.funds.IFundsDao;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.funds.FundsFlowEntity;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.funds.IFundsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class FundsServiceimpl implements IFundsService{
	
	@Resource
	private IFundsDao fundsDao;

	@Transactional(propagation = Propagation.MANDATORY) //调用该方法的service必须要有事务，否侧抛异常！！！
	@Override
	public int transfer(int userId, BigDecimal amount, FeeType feeType,
						 String accountType, String platformAccount)
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
        int insertFundsflowRecord = this.insertFundsflowRecord(userAccountId, platformAccountId,
        		feeTypeCode, amount, BigDecimal.ZERO, userAccount.getBalance().add(amount), feeTypeName);
        // 插入平台资金交易记录(转出)
        this.insertFundsflowRecord(platformAccountId, userAccountId, 
        		feeTypeCode, BigDecimal.ZERO, amount, platformBalance.subtract(amount), feeTypeName);
        // 增加用户资金余额
        this.increaseOrSubtractAccountBalance(userAccountId, amount);
        // 减少平台资金账户余额
        this.increaseOrSubtractAccountBalance(platformAccountId, BigDecimal.ZERO.subtract(amount));
		return insertFundsflowRecord;
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
	public int insertFundsflowRecord(int fundsAccountId, int relativelyAccountId, int tradeTypeId, BigDecimal income,
			BigDecimal expenditure,BigDecimal balance, String remark) throws Exception {
		return this.fundsDao.insertFundsflowRecord(fundsAccountId, relativelyAccountId, tradeTypeId, income, expenditure, balance, remark);
	}

	@Override
	public void increaseOrSubtractAccountBalance(int accountId, BigDecimal amount) throws Exception {
		this.fundsDao.increaseOrSubtractAccountBalance(accountId, amount);
	}

	@Transactional(propagation = Propagation.MANDATORY) //调用该方法的service必须要有事务，否侧抛异常！！！
	@Override
	public int unlockUserSDZHBalance(int userId, BigDecimal amount, FeeType feeType, VersionTypeEnum versionType) throws Exception {
		// 锁定并获取用户相对应类型的账户
		UserAccount wlzhAccount = this.lockAccount(userId, VersionTypeEnum.CG.equals(versionType)? SysFundAccountType.XW_INVESTOR_WLZH.name():SysFundAccountType.WLZH.name());
		if(wlzhAccount == null) {
			throw new LogicalException("用户资金账户不存在");
		}
		// 用户 冻结账户账户ID
		int wlzhAccountId = wlzhAccount.getAccountId();
		// 锁定并获取用户相对应类型的账户
		UserAccount sdzjAccount = this.lockAccount(userId, VersionTypeEnum.CG.equals(versionType)? SysFundAccountType.XW_INVESTOR_SDZH.name():SysFundAccountType.SDZH.name());
		if(sdzjAccount == null) {
			throw new LogicalException("用户锁定账户不存在");
		}
		// 用户 往来账户ID
		int sdzhAccountId = sdzjAccount.getAccountId();
		int feeTypeCode = feeType.getCode();
		String feeTypeName = feeType.getName();
		// 增加用户WLZH余额
		this.increaseOrSubtractAccountBalance(wlzhAccountId, amount);
		// 插入用户WLZH交易记录(转入)
		FundsFlowEntity fundsFlowEntity = new FundsFlowEntity();
		fundsFlowEntity.setFundsAccountId(wlzhAccountId);
		fundsFlowEntity.setUserId(sdzhAccountId);
		fundsFlowEntity.setIncome(amount);
		fundsFlowEntity.setTradeTypeId(feeTypeCode);
		fundsFlowEntity.setBalance(wlzhAccount.getBalance().add(amount));
		fundsFlowEntity.setRemark(feeTypeName);
		fundsDao.insertFundsflowRecord(fundsFlowEntity);
		// 减少用户SDZH余额
		this.increaseOrSubtractAccountBalance(sdzhAccountId, BigDecimal.ZERO.subtract(amount));
		// 插入用户SDZH交易记录(转出)
		this.insertFundsflowRecord(sdzhAccountId, wlzhAccountId, feeTypeCode, BigDecimal.ZERO, amount,
			sdzjAccount.getBalance().subtract(amount), feeTypeName);
		return fundsFlowEntity.getId();
	}

	@Transactional(propagation = Propagation.MANDATORY) //调用该方法的service必须要有事务，否侧抛异常！！！
	@Override
	public int platformTransfer(int userId, BigDecimal amount, FeeType feeType, String accountType) throws Exception {
		// 锁定并获取用户相对应类型的账户
		UserAccount userAccount = this.lockAccount(userId, accountType);
		if(userAccount == null) {
			throw new LogicalException("用户资金账户不存在");
		}
		// 用户账户ID
		int userAccountId = userAccount.getAccountId();
		// 获取平台资金账户ID
		Map<String,Object> platformInfo = fundsDao.getPlatformAccountId(accountType);
		Integer platformAccountId = Integer.parseInt(String.valueOf(platformInfo.get("accountId")));
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
		FundsFlowEntity fundsFlowEntity = new FundsFlowEntity();
		fundsFlowEntity.setFundsAccountId(userAccountId);
		fundsFlowEntity.setUserId(platformAccountId);
		fundsFlowEntity.setIncome(amount);
		fundsFlowEntity.setTradeTypeId(feeTypeCode);
		fundsFlowEntity.setBalance(userAccount.getBalance().add(amount));
		fundsFlowEntity.setRemark(feeTypeName);
		fundsDao.insertFundsflowRecord(fundsFlowEntity);
		// 插入个人资金交易记录(转入)
//		this.insertFundsflowRecord(userAccountId, platformAccountId,
//				feeTypeCode, amount, BigDecimal.ZERO, userAccount.getBalance().add(amount), feeTypeName);
		// 插入平台资金交易记录(转出)
		this.insertFundsflowRecord(platformAccountId, userAccountId,
				feeTypeCode, BigDecimal.ZERO, amount, platformBalance.subtract(amount), feeTypeName);
		// 增加用户资金余额
		this.increaseOrSubtractAccountBalance(userAccountId, amount);
		// 减少平台资金账户余额
		this.increaseOrSubtractAccountBalance(platformAccountId, BigDecimal.ZERO.subtract(amount));

		return fundsFlowEntity.getId();
	}
}
