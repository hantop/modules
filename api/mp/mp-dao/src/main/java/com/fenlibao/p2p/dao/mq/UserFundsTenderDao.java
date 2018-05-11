package com.fenlibao.p2p.dao.mq;

import java.math.BigDecimal;
import java.util.Date;

import com.fenlibao.p2p.model.mp.entity.UserAuthStatus;
import com.fenlibao.p2p.model.mp.entity.UserFundsAccountInfo;

/** 
 * @ClassName: UserFundsTenderDao 
 * @Description: 积分用户资金操作dao
 * @author: laubrence
 * @date: 2016-2-12 1:15:23  
 */
public interface UserFundsTenderDao {
	
	/** 
	 * @Title: getPTUserAccountId 
	 * @Description: 获取平台用户id
	 * @return
	 * @return: int
	 */
	public int getPTUserAccountId();
	
	/** 
	 * @Title: getUserFundsAccountInfo 
	 * @Description: 获取用户资金账户
	 * @param userId
	 * @param accountType
	 * @return
	 * @return: UserFundsAccountInfo
	 */
	public UserFundsAccountInfo getUserFundsAccountInfo(int userId, String accountType);
	
	/** 
	 * @Title: updateUserFundsAccountAmount 
	 * @Description: 更新用户资金账户信息
	 * @param fundsAccountId
	 * @param accountAmount
	 * @param nowDatetime
	 * @return
	 * @return: int
	 */
	public int updateUserFundsAccountAmount(int fundsAccountId, BigDecimal accountAmount, Date nowDatetime);
	
	/** 
	 * @Title: addT6102Record 
	 * @Description: 添加资金流水记录
	 * @param zcwlzhId
	 * @param FeeCode
	 * @param zrwlzhId
	 * @param nowDatetime
	 * @param zrcashAmount
	 * @param zccashAmount
	 * @param balanceAmount
	 * @param remark
	 * @return
	 * @return: int
	 */
	public int addT6102Record(int zcwlzhId,int FeeCode,int zrwlzhId,Date nowDatetime,BigDecimal zrcashAmount,BigDecimal zccashAmount,BigDecimal balanceAmount, String remark);
	
	/** 
	 * @Title: getUserAuthInfo 
	 * @Description: 获取当前用户身份认证信息
	 * @return
	 * @return: int
	 */
	public UserAuthStatus getUserAuthInfo(int userId);
	
}
