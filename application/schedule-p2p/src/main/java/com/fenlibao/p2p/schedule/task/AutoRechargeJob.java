/**
 * Copyright © 2015 fenlibao.com. All rights reserved.
 *
 * @Title: ZqzrAutoCancelJob.java
 * @Prject: schedule-p2p
 * @Package: com.fenlibao.p2p.schedule.task
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-4 下午6:09:57 
 * @version: V1.1
 */
package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.model.entity.TransferApplication;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.autoRecharge.AutoRechargeService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 自动代充值
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutoRechargeJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(AutoRechargeJob.class);

	@Resource
	PTCommonDao ptCommonDao;

	@Resource
	XWUserInfoService xwuserInfoService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	AutoRechargeService autoRechargeService;

	@Resource
	private ITradeService tradeService;

	@Resource
	private XWRechargeService xwRechargeService;

	@Resource
	private XWUserInfoService xwUserInfoService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("执行自动代充值定時任务开始");

		int ptUserId = ptCommonDao.getSpecialUserId();
		//新网平台代充值往来账户
		XWFundAccount ptFundAccount = xwuserInfoService.getFundAccount(ptUserId, SysFundAccountType.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH);
		if (ptFundAccount == null) {
			logger.warn("平台资金账户不存在！");
			return;
		}

		boolean isEnougy =true;
		//待处理列表
		List<TransferApplication> list = autoRechargeService.findHardList();
		for (int i =0;i<list.size();i++){
			if(isEnougy){
				logger.info("执行"+list.get(i).getId());
				TransferApplication ta = list.get(i);

				if(ta.getAmount().compareTo(BigDecimal.ZERO) <= 0){//迁移资金必须大于零
					logger.warn(ResponseCode.MOVE_AMOUNT_FIND);
					continue;
				}

				if (StringUtils.isEmpty(userInfoService.isXWaccount(ta.getUserId()))) {//判断是否开通新网投资用户
					logger.warn(ResponseCode.USER_NOT_XW_ACCOUNT);
					continue;
				}

				//判断往来账户资金是否小于迁移资金
				if (ptFundAccount.getAmount().compareTo(ta.getAmount()) < 0) {
					logger.warn(ResponseCode.MOVE_RECHARGE_FIND);
					continue;
				}

				XinwangUserInfo userInfo = xwuserInfoService.queryUserInfo("SYS_GENERATE_006");
				if (userInfo.getAvailableAmount().compareTo(ta.getAmount()) < 0) {
					logger.warn(ResponseCode.MOVE_RECHARGE_FIND);
					continue;
				}


				//更新账户往来资金，新增资金流水
				/*try {
					tradeService.updateUserAccountInfo(ta.getUserId(),ta.getAmount(),1);
				}catch (Exception e){
					logger.warn(ResponseCode.MOVE_AMOUNT_USERINFO_FIND);
					continue;
				}*/
				//新网代客充值
				BusinessType businessType = new BusinessType();
				businessType.setCode(SysTradeFeeCode.HBJE);// 原线下充值交易类型
				businessType.setName("代充值");
				businessType.setStatus("QY");
				String platformNo= null;
				try {
					platformNo = tradeService.getPlatformNo(ta.getUserId(),"INVESTOR");
				}catch (Exception e){
					logger.warn(ResponseCode.USER_NOT_XW_ACCOUNT);
					continue;
				}
				// 新网平台代充值往来账户余额不足
				if(ptFundAccount != null && ptFundAccount.getAmount().compareTo(ta.getAmount()) < 0){
					logger.error(ResponseCode.XW_RECHARGEACCOUNT_BALANCE_FIND);
					continue;
				}

				int xwRequestId =0;
				//发起代客充值
				try {
					xwRequestId=xwRechargeService.doAlternativeRecharge(ta.getUserId(),platformNo, ta.getAmount(),businessType);
					//成功后更改状态
					ta.setStatus(1);
					autoRechargeService.update(ta);
				}catch (Exception e){
					logger.warn("执行自动代充值定時任务失败");
					//代客充值失败，将锁定账户资金回滚到往来账户资金
					try {
						tradeService.updateUserAccountInfo(ta.getUserId(),ta.getAmount(),2);
					}catch (Exception ex){
						logger.warn("将锁定账户资金回滚到往来账户资金失败");
						continue;
					}

				}
				// 查询是否代充值成功,将用户平台锁定账户里面的钱扣掉,生成一条由普通锁定账号到存管往来账号的流水;

				if(xwRequestId > 0){
					Boolean flag = tradeService.getResultOfXWRequest(xwRequestId);
					try{
						if(flag){
							tradeService.updateUserAccountInfo(ta.getUserId(), ta.getAmount(), 3);
						}else {
							// 失败,将锁定账号资金回滚到往来账户资金
							tradeService.updateUserAccountInfo(ta.getUserId(),ta.getAmount(),2);
							logger.error(ResponseCode.MOVE_RECHARGE_FIND);
						}
					}catch (Exception e){
						tradeService.updateTransferApplication(ta.getId(), "0");
						// 失败,将锁定账号资金回滚到往来账户资金
						try {
							tradeService.updateUserAccountInfo(ta.getUserId(),ta.getAmount(),2);
						}catch (Exception ex){
							logger.warn(ex.getMessage());
							continue;
						}

						logger.warn(ResponseCode.MOVE_RECHARGE_FIND);
					}
				}
			}
		}
	}
}