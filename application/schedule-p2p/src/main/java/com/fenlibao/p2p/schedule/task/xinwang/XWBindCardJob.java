package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.service.xinwang.account.XWBindBankcardService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新网绑定银行卡异步通知-包括企业和个人
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWBindCardJob extends QuartzJobBean{
	private static final Logger logger = LogManager.getLogger(XWBindCardJob.class);
	@Resource
	private XWBindBankcardService bindService;
	@Resource
	private XWUserInfoService userInfoService;
	@Resource
	private XWRequestDao requestDao;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("====【..新网绑定银行卡开始.】实例ID:"+ context.getFireInstanceId());
		List<XinwangInterfaceName> interfaceNames = new ArrayList<>();
		Date requestTime = DateUtil.minuteAdd(new Date(), -10);
		interfaceNames.add(XinwangInterfaceName.ENTERPRISE_BIND_BANKCARD);
		interfaceNames.add(XinwangInterfaceName.PERSONAL_BIND_BANKCARD_EXPAND);
		interfaceNames.add(XinwangInterfaceName.ACTIVATE_STOCKED_USER);
		List<String> requestNoList = bindService.getOrderNeedComfired(interfaceNames,requestTime);
		XWRequest request = new XWRequest();
		for (String requestNo : requestNoList) {
			try {
				request = requestDao.getByRequestNo(requestNo);
				XinwangUserInfo userInfo = userInfoService.queryUserInfo(request.getPlatformUserNo());
				if (userInfo.validate() && StringUtils.isNotBlank(userInfo.getBankcardNo())) {
					bindService.comfiredOrder(userInfo.getPlatformUserNo(),userInfo.getBankcardNo(),userInfo.getBankcode(), requestNo);
					logger.info("银行卡绑定成功：requestNo:"+request.getRequestNo());
				} else {
					doFail(requestNo);
					logger.info("银行卡绑定失败 ：requestNo:"+request.getRequestNo());
				}
			} catch (Exception ex) {
				doFail(requestNo);
				logger.warn("新网绑定银行卡出现异常，requestNo:[{}],实例ID:[{}],异常信息:[{}]", request.getRequestNo(),context.getFireInstanceId(),ex.getMessage());
			}
		}
	}

	private void doFail(String requestNo) {
		XWRequest request = new XWRequest();
		request.setRequestNo(requestNo);
		request.setState(XWRequestState.SB);
		requestDao.updateRequest(request);
	}
}