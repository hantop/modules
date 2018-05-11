package com.fenlibao.p2p.service.mp;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dimeng.p2p.S61.enums.T6101_F03;
import com.fenlibao.p2p.dao.mq.UserFundsTenderDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.mp.entity.UserFundsAccountInfo;
import com.fenlibao.p2p.util.DateUtil;

@Service
public class MpBaseService {

	private static final Logger logger = LogManager.getLogger(MpBaseService.class);
	
	@Resource
    private UserFundsTenderDao userFundsTenderDao;
	
	/**
	 * 用户资金账户交易申请(用户往来账户-->用户锁定账户)
	 * @param userId  用户ID
	 * @param amount  交易金额
	 * @param transactionType  交易类型
	 * @param remark  备注
	 */
	public void transactionApplyfor(Integer userId,BigDecimal amount,int transactionType,String remark) {
		//锁定用户往来账户
		UserFundsAccountInfo userWlFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.WLZH.name());
		if(userWlFundsAccountInfo == null){
			logger.info(userId+"用户往来账户不存在");
			throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		
		//锁定用户锁定账户
		UserFundsAccountInfo userSdFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.SDZH.name());
		if(userSdFundsAccountInfo == null){
			logger.info(userId+"用户锁定账户不存在");
			throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		
		//用户往来账户余额
		BigDecimal wlBalance = userWlFundsAccountInfo.getFundsAmount().subtract(amount);
		if(wlBalance.compareTo(new BigDecimal(0)) < 0){
			logger.info(userId+"用户往来账户余额不足");
			throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage());
		}
		
		//用户锁定账户余额
		BigDecimal sdBalance = userSdFundsAccountInfo.getFundsAmount().add(amount);
		
		//更新用户往来账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(userWlFundsAccountInfo.getFundsAccountId(), wlBalance, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(userWlFundsAccountInfo.getFundsAccountId(), transactionType, userSdFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),new BigDecimal(0),amount,wlBalance, remark);
		
		//更新用户锁定账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(userSdFundsAccountInfo.getFundsAccountId(), sdBalance, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(userSdFundsAccountInfo.getFundsAccountId(), transactionType, userWlFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),amount,new BigDecimal(0),sdBalance, remark);
	}
	
	/**
	 * 确认用户资金账户交易成功(用户锁定账户-->平台往来账户)
	 * @param userId  用户ID
	 * @param amount  交易金额
	 * @param transactionType  交易类型
	 * @param remark  备注
	 */
	public void transactionConfirm(Integer userId,BigDecimal amount,int transactionType,String remark) {
		//获取平台用户id
		int ptUserId = userFundsTenderDao.getPTUserAccountId();
		if(ptUserId==0){
			throw new BusinessException(ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.COMMON_PLATFORM_ACCOUNT_NOT_EXIST.getMessage());
		}
				
		//锁定用户锁定账户
		UserFundsAccountInfo userSdFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.SDZH.name());
		if(userSdFundsAccountInfo == null){
			logger.info(userId+"用户锁定账户不存在");
			throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		BigDecimal userSdBalance = userSdFundsAccountInfo.getFundsAmount().subtract(amount);//用户锁定账户余额
		if(userSdBalance.compareTo(new BigDecimal(0))<0){
			logger.info(userId+"用户锁定账户余额不足");
			throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage());
		}
		
		//锁定平台往来账户
		UserFundsAccountInfo ptWlFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(ptUserId, T6101_F03.WLZH.name());
		BigDecimal ptWlAmount = ptWlFundsAccountInfo.getFundsAmount().add(amount);//平台往来账户余额
		
		//更新平台往来账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(ptWlFundsAccountInfo.getFundsAccountId(), ptWlAmount, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(ptWlFundsAccountInfo.getFundsAccountId(), transactionType, userSdFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),amount,new BigDecimal(0),ptWlAmount, remark);
		
		//更新用户锁定账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(userSdFundsAccountInfo.getFundsAccountId(), userSdBalance, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(userSdFundsAccountInfo.getFundsAccountId(), transactionType, ptWlFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),new BigDecimal(0),amount,userSdBalance, remark);
	}
	
	/**
	 * 资金账户交易失败(用户锁定账户-->用户往来账户)
	 * @param userId  用户ID
	 * @param amount  交易金额
	 * @param transactionType  交易类型
	 * @param remark  备注
	 */
	public void transactionFail(Integer userId,BigDecimal amount,int transactionType,String remark) {
		//锁定用户往来账户
		UserFundsAccountInfo userWlFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.WLZH.name());
		if(userWlFundsAccountInfo == null){
			throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		
		//锁定用户锁定账户
		UserFundsAccountInfo userSdFundsAccountInfo = userFundsTenderDao.getUserFundsAccountInfo(userId, T6101_F03.SDZH.name());
		if(userSdFundsAccountInfo == null){
			logger.info(userId+"用户锁定账户不存在");
			throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getCode(),ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST.getMessage());
		}
		
		//用户往来账户余额
		BigDecimal wlBalance = userWlFundsAccountInfo.getFundsAmount().add(amount);
		
		//用户锁定账户余额
		BigDecimal sdBalance = userSdFundsAccountInfo.getFundsAmount().subtract(amount);
		if(sdBalance.compareTo(new BigDecimal(0)) < 0){
			logger.info(userId+"用户锁定账户余额不足");
			throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),ResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage());
		}
		
		//更新用户往来账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(userWlFundsAccountInfo.getFundsAccountId(), wlBalance, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(userWlFundsAccountInfo.getFundsAccountId(), transactionType, userSdFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),amount,new BigDecimal(0),wlBalance, remark);
		
		//更新用户锁定账户余额、添加资金流水
		userFundsTenderDao.updateUserFundsAccountAmount(userSdFundsAccountInfo.getFundsAccountId(), sdBalance, DateUtil.nowDate());
		userFundsTenderDao.addT6102Record(userSdFundsAccountInfo.getFundsAccountId(), transactionType, userWlFundsAccountInfo.getFundsAccountId(), DateUtil.nowDate(),new BigDecimal(0),amount,sdBalance, remark);
	}
}
