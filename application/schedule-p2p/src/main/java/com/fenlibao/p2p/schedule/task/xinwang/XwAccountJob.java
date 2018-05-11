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
package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.entity.PlatformUser;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.XWUserType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.xinwang.account.XWEnterpriseRegisterService;
import com.fenlibao.p2p.service.xinwang.account.XWPersonalRegisterService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时检查新网用户开户
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XwAccountJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(XwAccountJob.class);

	@Resource
	UserInfoService userInfoService;

	@Resource
	XWUserInfoService xwUserInfoService;

	@Resource
	XWPersonalRegisterService xwPersonalRegisterService;

	@Resource
	XWEnterpriseRegisterService enterpriseRegisterService;

	@Resource
	XWAccountDao accountDao;

	@Resource
	XWRequestDao requestDao;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("====【..定时检查新网开户用户是否通过审核，并操作开户.】");
		List<PlatformUser> platformUserNoList = new ArrayList<PlatformUser>();
		try {
			platformUserNoList = userInfoService.getPlatformUserNo("DQR",100);
			if(null!=platformUserNoList&&platformUserNoList.size()>0){
				PlatformAccount platformAccount;
				XinwangUserInfo xinwangUserInfo;
				for(PlatformUser platformUserNo : platformUserNoList) {
					try {
						//platformAccount = accountDao.getPlatformAccountInfoByPlatformUserNo(platformUserNo.getPlatformUserNo());
						xinwangUserInfo = xwUserInfoService.queryUserInfo(platformUserNo.getPlatformUserNo());
						if (xinwangUserInfo != null && xinwangUserInfo.validate()) {
							Map<String, Object> map = new HashMap<>();
							map.put("requestNo", platformUserNo.getRequestNo());
							map.put("platformUserNo", platformUserNo.getPlatformUserNo());
							map.put("userRole", xinwangUserInfo.getUserRole());
							map.put("auditStatus", xinwangUserInfo.getAuditStatus());
							map.put("bankcardNo", xinwangUserInfo.getBankcardNo());
							map.put("bankcode", xinwangUserInfo.getBankcode());
							if (XWUserType.PERSONAL.getCode().equals(xinwangUserInfo.getUserType())) {
								map.put("mobile", xinwangUserInfo.getMobile());
								map.put("realName", xinwangUserInfo.getName());
								map.put("idCardNo", xinwangUserInfo.getIdCardNo());
								xwPersonalRegisterService.handleNotify(map);
							} else {
								enterpriseRegisterService.handleNotify(map);
							}

							logger.info("新网开户成功：requestNo:" + platformUserNo.getRequestNo());
						}else if("100009".equals(xinwangUserInfo.getErrorCode())){
							XWRequest request = new XWRequest(platformUserNo.getRequestNo());
							request.setState(XWRequestState.SB);
							requestDao.updateRequest(request);
						}
					} catch (Exception ex) {
						logger.error("开户定时器执行操作出现异常："+ex.toString(), ex);
						ex.printStackTrace();
					}
				}
			}
		} catch (Throwable e) {
			logger.error("==========================【定时检查新网开户用户是否通过审核，并操作开户...】："+e.toString(), e);
			e.printStackTrace();
		}
	}
}