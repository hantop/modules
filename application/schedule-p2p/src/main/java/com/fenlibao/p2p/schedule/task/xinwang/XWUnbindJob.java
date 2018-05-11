package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.service.xinwang.account.XWUnbindBankCardService;
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
import java.util.Date;
import java.util.List;

/**
 * 新网解绑银行卡异步通知
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWUnbindJob extends QuartzJobBean{
	private static final Logger logger = LogManager.getLogger(XWUnbindJob.class);
	@Resource
	private XWUnbindBankCardService unbindService;
	@Resource
	private XWAccountDao accountDao;
	@Resource
	private XWUserInfoService userInfoService;
	@Resource
	private XWRequestDao requestDao;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("====【..新网解绑对账开始.】实例ID:"+ context.getFireInstanceId());
		try {
			Date requestTime = DateUtil.minuteAdd(new Date(), -10);
			List<XWRequest> requestNoList = unbindService.getOrderNeedComfired(XinwangInterfaceName.UNBIND_BANKCARD, XWOrderStatus.DQR,requestTime);
			XWRequest requestParam = new XWRequest();
			for (XWRequest request : requestNoList) {
				try {
					requestParam.setRequestNo(request.getRequestNo());
					XinwangUserInfo userInfo = userInfoService.queryUserInfo(request.getPlatformUserNo());
					if (userInfo.validate() && StringUtils.isBlank(userInfo.getBankcardNo())) {
						requestParam.setState(XWRequestState.CG);
						unbindService.comfiredOrder(userInfo.getPlatformUserNo(), requestParam);
						logger.info("解绑成功：requestNo:" + request.getRequestNo());
					} else {
						requestParam.setState(XWRequestState.SB);
						requestDao.updateRequest(requestParam);
						logger.info("解绑失败：requestNo:" + request.getRequestNo());
					}
				} catch (Exception ex) {
					logger.warn("新网解绑对账出现异常，requestNo:[{}],实例ID:[{}],异常信息:[{}]", request.getRequestNo(), context.getFireInstanceId(), ex.getMessage());
				}
			}
		} catch (Throwable throwable) {
			logger.warn("====【..新网解绑对账出现异常.】实例ID:"+ context.getFireInstanceId());
		}
	}
}