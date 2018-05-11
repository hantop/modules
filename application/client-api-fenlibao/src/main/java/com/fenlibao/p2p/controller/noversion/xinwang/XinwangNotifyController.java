package com.fenlibao.p2p.controller.noversion.xinwang;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.common.LockService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.xinwang.XWBatchNotifyService;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;
import com.fenlibao.p2p.service.xinwang.account.*;
import com.fenlibao.p2p.service.xinwang.common.SpringContextUtil;
import com.fenlibao.p2p.service.xinwang.common.XWRequestService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnpBindcardService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnterpriseService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.XWExceptionRepay;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayService;
import com.fenlibao.p2p.util.xinwang.SignatureAlgorithm;
import com.fenlibao.p2p.util.xinwang.SignatureUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步通知接口
 * 
 * @author Iren08
 * @date 2017年3月8日 下午9:28:41
 * @version 1.0
 */
@Controller
@RequestMapping("/notify")
public class XinwangNotifyController {
	
	private final static Logger LOG = LoggerFactory.getLogger(XinwangNotifyController.class);

	private static final Map<String, Class<?>> CLAZZ = new HashMap<>(); //业务接口class

	static {
		CLAZZ.put("PERSONAL_REGISTER_EXPAND", XWPersonalRegisterService.class);
		CLAZZ.put("RESET_PASSWORD", XWResetPasswordService.class);
		CLAZZ.put("MODIFY_MOBILE_EXPAND", XWModifyMobileService.class);
		CLAZZ.put("UNBIND_BANKCARD", XWUnbindBankCardService.class);
		CLAZZ.put("RECHARGE", XWRechargeService.class);
		CLAZZ.put("PERSONAL_BIND_BANKCARD_EXPAND", XWBindBankcardService.class);
		CLAZZ.put("WITHDRAW", XWWithdrawService.class);
		CLAZZ.put("ENTERPRISE_REGISTER", XWEnterpriseRegisterService.class);
		CLAZZ.put("ENTERPRISE_INFORMATION_UPDATE", XWEnterpriseService.class);
		CLAZZ.put("ENTERPRISE_BIND_BANKCARD", XWEnpBindcardService.class);
		CLAZZ.put("ACTIVATE_STOCKED_USER", XWActivateStockedUserService.class);
		CLAZZ.put("USER_AUTHORIZATION", XWUserAuthService.class);
	}

	@Resource
    XWRequestService requestService;

	@Resource
	SysRepayDao repayDao;

	@Resource
	XWWithdrawTransactionService withdrawTransactionService;

	@Resource
	RedisService redisService;

	@Resource
	LockService lockService;

	@RequestMapping(value = "/xinwang")
	public String notify(HttpServletRequest request,HttpServletResponse response,Model model) {
		
		final String respData = StringEscapeUtils.unescapeHtml3(request.getParameter("respData"));
		final String interfaceName = request.getParameter("serviceName");
		LOG.info("receive notify content : " + respData);
		final Map<String, Object> respMap = JSON.parseObject(respData);
		String requestCacheKey = null;
		try {
			// 验签
			PublicKey publicKey = SignatureUtil.getRsaX509PublicKey(Base64
					.decodeBase64(XinWangUtil.CONFIG.lmPublicKey()));
			boolean verify = SignatureUtil.verify(
					SignatureAlgorithm.SHA1WithRSA, publicKey, respData,
					Base64.decodeBase64(request.getParameter("sign")));
			if (verify) {
				LOG.info("sign success !!");
				//批量交易回调
				if("ASYNC_TRANSACTION".equals(interfaceName)){
					List<Map<String,Object>> details = (List<Map<String,Object>>) respMap.get("details");
					//不同批次的的回调可能合在一个detail返回
					List<Map<String,Object>> tenderResponseList=new ArrayList<>();
					List<Map<String,Object>> repayResponseList=new ArrayList<>();
					List<Map<String,Object>> repayMarketingResponseList=new ArrayList<>();
					List<Map<String,Object>> exceptionRepayResponseList = new ArrayList<>();
					for(Map<String,Object> item:details){
						String bizTypeStr=(String)item.get("bizType");
						XWTradeType tradeType=XWTradeType.parse(bizTypeStr);
						String requestNo=(String)item.get("asyncRequestNo");
						requestCacheKey = (String)item.get("asyncRequestNo");
						//状态为成功或者失败才会插入数据
						String noticeStatus = "SUCCESS".equals(item.get("status"))?"CG":"SB";
						if (redisService.existsKey(requestCacheKey)) {//同一个流水只给一个线程
							LOG.warn(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode(),ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getMessage());
							continue;
						}
						//锁
						try {
							lockService.createLock(requestCacheKey, noticeStatus);
						} catch (Exception ex) {
							LOG.warn("异步通知同步锁插入异常，requestNo:" + requestCacheKey);
							throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
						}
						//保存返回报文
						XWResponseMessage responseParams=new XWResponseMessage();
						responseParams.setRequestNo(requestNo);
						responseParams.setResponseMsg(JSON.toJSONString(item));
						requestService.saveResponseMessage(responseParams);

						if(XWTradeType.TENDER== tradeType){
							tenderResponseList.add(item);
						}
						else if(XWTradeType.REPAYMENT==tradeType||XWTradeType.COMPENSATORY==tradeType){
							repayResponseList.add(item);
						}
						else if(XWTradeType.MARKETING==tradeType){
							SysCreditToRepay creditRepayDetail=repayDao.getCreditRepayDetailByMarketingRequestNo(requestNo);
							if(creditRepayDetail!=null){
								repayMarketingResponseList.add(item);
							}
						}else if(XWTradeType.PLATFORM_SERVICE_DEDUCT == tradeType){
							exceptionRepayResponseList.add(item);
						}
					}
					if(!tenderResponseList.isEmpty()){
						XWBatchNotifyService batchNotifyService = (XWBatchNotifyService) SpringContextUtil.getBean(XWMakeLoanService.class);
						try{
							batchNotifyService.handleNotify(tenderResponseList);
						}
						catch(Exception e){
							LOG.error("处理放款回调出错："+e.getMessage(), e);
							e.printStackTrace();
						}finally {
							for (Map<String, Object> item : tenderResponseList) {
								requestCacheKey = (String) item.get("asyncRequestNo");
								redisService.removeKey(requestCacheKey);
							}
						}
					}
					if(!repayResponseList.isEmpty()){
						XWBatchNotifyService batchNotifyService = (XWBatchNotifyService) SpringContextUtil.getBean(XWRepayService.class);
						try{
							batchNotifyService.handleNotify(repayResponseList);
						}
						catch(Exception e){
							LOG.error("处理还款回调出错："+e.getMessage(), e);
							e.printStackTrace();
						}finally {
							for (Map<String, Object> item : repayResponseList) {
								requestCacheKey = (String) item.get("asyncRequestNo");
								redisService.removeKey(requestCacheKey);
							}
						}
					}
					if(!repayMarketingResponseList.isEmpty()){
						XWBatchNotifyService batchNotifyService = (XWBatchNotifyService) SpringContextUtil.getBean(XWRepayService.class);
						try{
							batchNotifyService.handleNotify(repayMarketingResponseList);
						}
						catch(Exception e){
							LOG.error("处理还款加息回调出错："+e.getMessage(), e);
							e.printStackTrace();
						}finally {
							for (Map<String, Object> item : repayMarketingResponseList) {
								requestCacheKey = (String) item.get("asyncRequestNo");
								redisService.removeKey(requestCacheKey);
							}
						}
					}
					if(!exceptionRepayResponseList.isEmpty()){
						XWBatchNotifyService batchNotifyService = (XWBatchNotifyService) SpringContextUtil.getBean(XWExceptionRepay.class);
						try{
							batchNotifyService.handleNotify(exceptionRepayResponseList);
						}
						catch(Exception e){
							LOG.error("资金异常还款回调出错："+e.getMessage(), e);
							e.printStackTrace();
						}finally {
							for (Map<String, Object> item : exceptionRepayResponseList) {
								requestCacheKey = (String) item.get("asyncRequestNo");
								redisService.removeKey(requestCacheKey);
							}
						}
					}
					requestCacheKey = null;
				}
				//提现失败回充
				else if("BACKROLL_RECHARGE".equals(interfaceName)){
					String requestNo = (String) respMap.get("requestNo");
					String status = (String) respMap.get("status");
					requestCacheKey = requestNo;
					if (redisService.existsKey(requestCacheKey)) {//同一个流水只给一个线程
						LOG.warn(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode(),ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getMessage());
						return null;
					}
                    try {
                        lockService.createLock(requestCacheKey, "CG");
                    } catch (Exception ex) {
                        LOG.warn("异步通知同步锁插入异常，requestNo:" + requestCacheKey);
                        throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
                    }
					//保存回充请求报文
					XWResponseMessage message=new XWResponseMessage();
					message.setRequestNo(requestNo);
					message.setResponseMsg(respData);
                    requestService.saveRequestMessage(message);
					if("SUCCESS".equals(status)){
						try{
							withdrawTransactionService.backrollRecharge(respMap);
						}
						catch(Exception e){
							LOG.error("提现回充出错："+e.getMessage(),e);
							e.printStackTrace();
						}
					}
				}
				else{
					requestCacheKey = (String) respMap.get("requestNo");
					if (redisService.existsKey(requestCacheKey)) {//同一个流水只给一个线程
						LOG.warn(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode(),ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getMessage());
						return null;
					}
					String code = (String) respMap.get("code");
					//save response msg
					XWResponseMessage responseParams=new XWResponseMessage();
					responseParams.setRequestNo((String) respMap.get("requestNo"));
					responseParams.setResponseMsg(respData);
					requestService.saveResponseMessage(responseParams);
					if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
						// 请求受理且处理成功，根据不同接口处理
						// 备注：：：：：需判断金额是否相同 amount与请求是否相等
						//异步进行业务处理 线程处理 OR Rabbitmq
						String noticeStatus = null;
						//状态为成功或者失败才会插入数据
						if (interfaceName.equals(XinwangInterfaceName.RECHARGE.getCode()) ) {
							noticeStatus = "SUCCESS".equals(respMap.get("rechargeStatus"))?"CG":"FAIL".equals(respMap.get("rechargeStatus"))?"SB":null;
						}
						if (interfaceName.equals(XinwangInterfaceName.WITHDRAW.getCode())) {
							noticeStatus = "SUCCESS".equals(respMap.get("withdrawStatus"))?"CG":"FAIL".equals(respMap.get("withdrawStatus"))?"SB":null;
						}
						//锁
						if (StringUtils.isNotEmpty(noticeStatus)) {
							try {
								lockService.createLock(requestCacheKey, noticeStatus);
							} catch (Exception ex) {
								LOG.warn("异步通知同步锁插入异常，requestNo:" + requestCacheKey);
								throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
							}
						}
						XWNotifyService notifyService = (XWNotifyService) SpringContextUtil.getBean(CLAZZ.get(interfaceName));
						try{
						    notifyService.handleNotify(respMap);
						}
						catch(Exception e){
							LOG.error("接口"+interfaceName+"回调处理出错："+e.getMessage(),e);
							e.printStackTrace();
						}
					}
					else{
						//请求失败或处理失败
						XWRequest param=new XWRequest();
						param.setRequestNo((String)respMap.get("requestNo"));
						param.setState(XWRequestState.SB);
						requestService.updateRequest(param);
					}
				}

				if ("NOTIFY".equals(request.getParameter("responseType"))) {
					// 异步通知返回SUCCESS
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("SUCCESS");
				} else if ("CALLBACK".equals(request.getParameter("responseType"))) {
					// 同步通知 返回页面
					// 设置返回的参数
					model.addAttribute("message", "操作成功");
					return "xinwang/success";
				}
			} else {
				// 验签失败
				LOG.error("verify sign fail !!");
			}
		} catch (Exception e) {
			LOG.error("异步通知处理报错",e);
		} finally {
			if (StringUtils.isNotEmpty(requestCacheKey)) {
				redisService.removeKey(requestCacheKey);
			}
		}
		return null;
	}

}
