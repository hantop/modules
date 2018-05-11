package com.fenlibao.p2p.controller.v_3.v_3_2_0.mp;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.mp.entity.MyPointInfo;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.enums.topup.ParvalueType;
import com.fenlibao.p2p.model.mp.enums.topup.TopUpStatus;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrder;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrderRecord;
import com.fenlibao.p2p.model.mp.vo.topup.ParvalueVO;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.consumption.ConsumptionService;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;
import com.fenlibao.p2p.service.pay.ILlpQuickPayService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.Validator;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yangzengcai
 * @date 2016年2月18日
 */
@RestController("v_3_2_0/MobileTopUpController")
@RequestMapping(value = "trade/topup", headers = APIVersion.v_3_2_0)
public class MobileTopUpController {

	private static final Logger logger = LogManager.getLogger(MobileTopUpController.class);
	
	@Resource
	private ITopUpService topUpService;
	
	@Resource
	private ILlpQuickPayService iLlpQuickPayService;
	
	@Resource
	private ITradeService iTradeService;
	
	@Resource
	private ConsumptionService consumptionService;
	@Resource
	private MemberPointsService memberPointsService;
	
	@Resource
	private UserInfoService userInfoService;

	@Resource
	private RedisService redisService;

	@Resource
	private BaofooBindCardService baofooBindCardService;


	/**
	 * 手机充值接口
	 * @param params
	 * @param parvalueCode
	 * @param phoneNum
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "mobile", method = RequestMethod.POST)
	public HttpResponse mobile(BaseRequestFormExtend params, 
			@RequestParam(required = false, value = "parvalueCode") String parvalueCode, 
			@RequestParam(required = false, value = "phoneNum") String phoneNum,
			@RequestParam(required = false, value = "integralCode") String integralCode, 
			@RequestParam(required = false, value = "integralQty")  String integralQty,
			@RequestParam(required = false, value = "dealPassword") String dealPassword) {       
		HttpResponse response = new HttpResponse();
		try {
			logger.info("用户ID:"+params.getUserId()+"充值手机号:"+phoneNum);
			if (!params.validate() || !StringUtils.isNoneBlank(parvalueCode, phoneNum,dealPassword)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			
			if(!Validator.isMobile(phoneNum)){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
				return response;
			}
			
			//验证用户交易密码是否正确
//        	if(!iTradeService.isValidUserPwd(Integer.valueOf(params.getUserId()), dealPassword)){
//        		response.setCodeMessage(ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getCode(), ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getMessage());//交易密码错误
//        		return response;
//        	}
			MobileTopUpOrder order = topUpService.addOrder(params.getUserId(), parvalueCode, phoneNum, integralCode, integralQty);
			
			this.topUpService.updateOrderInfo(order,integralCode,integralQty,params.getUserId(),order.getUserPayAmount(),order.getOrderId(), TopUpStatus.ING.getCode());
		} catch(BusinessException e){
			throw e;
		} catch (Exception e) {
			logger.error("[MobileTopUpController.mobile]异常:"+e.getMessage(),e);
			response.setCodeMessage(ResponseCode.MP_RECHARGE_ERROR); 
		}
		return response;
	}
	
	/**
	 * 获取手机充值(话费、流量)面额
	 * @param params
	 * @param rechargeType 充值类别：话费、流量
	 * @return
	 */
	@RequestMapping(value = "parvalue", method = RequestMethod.GET)
	public HttpResponse getParvalue(BaseRequestForm params,String rechargeType) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>(2);
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		
		List<ParvalueVO> bill = topUpService.getParvalue(ParvalueType.BILL); //话费面额
		List<ParvalueVO> traffic = topUpService.getParvalue(ParvalueType.TRAFFIC); //流量面额
		if(StringUtils.isNotEmpty(rechargeType)){
			if(rechargeType.equals(ParvalueType.BILL.getCode().toString())){
				data.put("billList", bill);
			}else if(rechargeType.equals(ParvalueType.TRAFFIC.getCode().toString())){
				data.put("trafficList", traffic);
			}
		}else{
			data.put("billList", bill);
			data.put("trafficList", traffic);
		}

		response.setData(data);
		return response;
	}
	
	
	
	/**
	 * 易赏-手机充值
	 * @author junda.feng 2016-4-20
	 * @param params
	 * @param phoneNum  手机号,必填
	 * @param parvalueCode  充值面额编码,必填
	 * @param integralCode  使用积分类型编码
	 * @param integralQty   使用积分数量
	 *
	 * 
	 * @return HttpResponse
	 */
	@RequestMapping(value = "mobile/yishang", method = RequestMethod.POST)
	public HttpResponse yishang(BaseRequestFormExtend params,
			String phoneNum, String parvalueCode,String integralCode,String integralQty) {       
		HttpResponse response = new HttpResponse();
		MobileTopupOrderEntity mobileTopUpOrder = null;
		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(params.getUserId().toString());
		if (redisService.existsKey(requestCacheKey)) {
			response.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
			return response;
		}
		//验证手机号码并创建订单
		try {
			logger.info("用户ID:"+params.getUserId()+"充值手机号:"+phoneNum);
			if (!params.validate() || !StringUtils.isNoneBlank(parvalueCode, phoneNum,integralCode,integralQty)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			if(!Validator.isMobile(phoneNum)){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
				return response;
			}


			//绑卡
			UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(params.getUserId()));
			List<BankCard> bankCards=userAccountInfoVO.getBankCards();
			boolean containBaofoo = baofooBindCardService.validateBind(params.getUserId());
			if((bankCards==null||bankCards.size()==0)&& !containBaofoo){
				response.setCodeMessage(ResponseCode.PAYMENT_UNBOUND_BANK_CARD);
				return response;
			}
			//判断可使用积分额度
			MyPointInfo myPoint = memberPointsService.getMyPoints(params.getUserId());
			int actualPoint=myPoint.getTotallPoint()<myPoint.getMyPoint()?myPoint.getTotallPoint():myPoint.getMyPoint();
			if(Integer.parseInt(integralQty)>actualPoint){
				response.setCodeMessage(ResponseCode.MP_MY_POINTS_QUOTA_LACK);
				return response;
			}

			Map<String, Object> data=new HashMap<String, Object>();

			//调用易赏查询接口
			boolean  check= topUpService.checkYishang(phoneNum);
			if(check){
				//创建手机充值订单，关联消费订单
				mobileTopUpOrder = topUpService.addYishangOrder(params.getUserId(), parvalueCode, phoneNum,integralCode, integralQty);
				//如果积分不够抵扣全额
				if(mobileTopUpOrder.getUserPayAmount().compareTo(BigDecimal.ZERO) > 0){
					//请求支付参数
					Map<String, String> iLlpQuickPayMap= iLlpQuickPayService.getRequestData(String.valueOf(mobileTopUpOrder.getUserPayAmount()),
							params.getUserId(), params.getClientType());
					data.put("req_data", iLlpQuickPayMap.get("req_data"));
					data.put("url", iLlpQuickPayMap.get("url"));
					String paymentOrderId =iLlpQuickPayMap.get("paymentOrderId");
					//更新消费订单
					ConsumptionOrderEntity consumptionOrder=consumptionService.getConsumptionOrderById(mobileTopUpOrder.getConsumptionOrderId());
					consumptionOrder.setPaymentOrderId(Integer.parseInt(paymentOrderId));
					consumptionService.updateOrder(consumptionOrder);
					response.setData(data);
				}else{
					ConsumptionOrderEntity consumptionOrder=consumptionService.getConsumptionOrderById(mobileTopUpOrder.getConsumptionOrderId());
					response=topUpService.yishangTopUp(consumptionOrder);
				}
				
			}else{
				response.setCodeMessage(ResponseCode.MP_YISHANG_CANRECHARGE_NOT_VALID); 
			}
		}
		catch(BusinessException e){
			throw e;
		}catch (Exception e) {
			logger.error("[MobileTopUpController.yishang]异常:"+e.getMessage(),e);
			response.setCodeMessage(ResponseCode.MP_RECHARGE_ERROR); 
		}catch (Throwable e) {
			logger.error("[MobileTopUpController.yishang]异常:"+e.getMessage(),e);
			response.setCodeMessage(ResponseCode.MP_RECHARGE_ERROR);
		}finally {
			redisService.removeKey(requestCacheKey);
		}
		
		return response;
	}
	
	
	
	/**
	 * 易赏-充值记录
	 * @author junda.feng 2016-4-25
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "mobile/yishang/record", method = RequestMethod.POST)
	public HttpResponse record(BaseRequestFormExtend params,Integer pageNum) {       
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		
		
		try {
			List<MobileTopUpOrderRecord>  list=topUpService.getMobileTopupOrderList(params.getUserId(), pageNum==null?1:pageNum);
			Map<String, Object> data=new HashMap<String, Object>();
			data.put("list",JSON.toJSON(list));
			response.setData(data);
			
		} catch (Throwable e) {
			logger.error("[MobileTopUpController.recharge]异常:"+e.getMessage(),e);
			response.setCodeMessage(ResponseCode.FAILURE); 
		}
		return response;
	}
	
	
	
}
