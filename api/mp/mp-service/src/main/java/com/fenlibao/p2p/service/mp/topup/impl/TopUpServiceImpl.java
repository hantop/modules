package com.fenlibao.p2p.service.mp.topup.impl;

import com.alibaba.fastjson.JSON;
import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.consumption.IConsumptionDao;
import com.fenlibao.p2p.dao.mq.MemberPointsDao;
import com.fenlibao.p2p.dao.mq.topup.ITopUpDao;
import com.fenlibao.p2p.dao.trade.IRefundDao;
import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;
import com.fenlibao.p2p.model.entity.pay.RefundOrderEntity;
import com.fenlibao.p2p.model.enums.consumption.ConsumptionType;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.mp.entity.PointsExchangeCashInfo;
import com.fenlibao.p2p.model.mp.entity.PointsType;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpErrorRecord;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpOrderInchargeEntity;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.entity.topup.ParvalueEntity;
import com.fenlibao.p2p.model.mp.enums.topup.ParvalueType;
import com.fenlibao.p2p.model.mp.enums.topup.RechargeChannel;
import com.fenlibao.p2p.model.mp.enums.topup.TopUpStatus;
import com.fenlibao.p2p.model.mp.vo.topup.*;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.service.mp.MpBaseService;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.mp.PointExchangeStatus;
import com.fenlibao.p2p.util.mp.topup.EncryptionUtil;
import com.fenlibao.p2p.util.mp.topup.HttpClientUtil;
import com.fenlibao.p2p.util.mp.topup.OrderUtil;
import com.fenlibao.thirdparty.yishang.YishangConst;
import com.fenlibao.thirdparty.yishang.YishangLinksService;
import com.fenlibao.thirdparty.yishang.enums.YishangTopupTypeEnum;
import com.fenlibao.thirdparty.yishang.util.YishangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopUpServiceImpl extends MpBaseService implements ITopUpService {
	
	@Resource
	private ITopUpDao topUpDao;
	@Resource
	private IRefundDao iRefundDao;
	@Resource
	private IConsumptionDao consumptionDao;
	@Resource
	private MemberPointsService memberPointsService;
	@Resource
	private MemberPointsDao memberPointsDao;
	@Resource
	private UserInfoService userInfoService;
	
	private static final Logger logger = LogManager.getLogger(TopUpServiceImpl.class);
	private static final String SENDORDERURL = Config.get("jf.invoke.url");
	
	private static final  String ys_url = Config.get("ys.invoke.url");//易赏平台接口接入地址
	
	private static final  String ys_key = Config.get("ys.key");//用户秘钥，由易赏平台提供
	
	private static final  String ys_userId =Config.get("ys.userId");//用户id，由易赏平台提供
	
	//@Transactional
	@Override
	public String mobileTopUp(MobileTopUpOrder order) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", Integer.valueOf(order.getId()));
		
		MobileTopupOrderEntity entity = topUpDao.getOrder(map);
		String result="";
		if(entity == null){
			logger.info("订单不存在，ID："+order.getId());
			return result;
		}
		logger.info("订单号："+order.getOrderId()+"订单状态："+entity.getStatus());
		if(entity.getStatus() == TopUpStatus.ING.getCode()){//提交中状态
			if(entity.getPhoneNum().equals(order.getMobile())){
				InvokeBusinessVO vo = new InvokeBusinessVO();
				vo.setMobile(order.getMobile());
				vo.setOrderId(order.getOrderId());
				vo.setParvalue(order.getParvalue());
				vo.setAgentcode(Config.get("jf.agentcode"));
				vo.setSignKey(Config.get("jf.signkey"));
				vo.setCallbackUrl(Config.get("jf.callbak.url"));
				
				result = topUp(vo);
			}
		}
		return result;
	}
	/**
	 * 向第三方提交充值申请
	 * @param order
	 * @throws IOException
	 */
	private String topUp(InvokeBusinessVO order) throws IOException {
		String requestParamStr = getRequestParamStr(order);
		logger.info("第三方接口参数："+requestParamStr);
		String result = HttpClientUtil.doPost(requestParamStr, SENDORDERURL);
		logger.info("订单ID："+order.getOrderId()+",第三方接口返回结果："+result);
		return result;
	}
	
	
	/**
	 * 易赏手机充值
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@Override
	public HttpResponse yishangTopUp(ConsumptionOrderEntity consumptionOrderEntity) throws Exception {
		
		HttpResponse response = new HttpResponse();
		if (consumptionOrderEntity == null) {
			throw new BusinessException(ResponseCode.MP_YISHANG_CONSUMPTIONORDER_NOT_EXIST);
		}
		logger.warn("[易赏]，充值，PaymentOrderId>>>>>"+consumptionOrderEntity.getPaymentOrderId());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("consumptionOrderId", consumptionOrderEntity.getId());
		MobileTopupOrderEntity entity = topUpDao.getOrder(map);
		
		if(entity == null){
			logger.warn("[易赏]，消费订单关联手机充值订单不存在，consumptionOrderId："+consumptionOrderEntity.getId());
			throw new BusinessException(ResponseCode.MP_YISHANG_ORDER_NOT_EXIST);
		}
		//判断提交中状态
		if(entity.getStatus() != TopUpStatus.WAIT.getCode()){
			logger.warn("[易赏]，手机充值订单,非待提交状态，consumptionOrderEntity:"+consumptionOrderEntity.getId()+",entity："+entity.getId()+",status："+entity.getStatus());
			throw new BusinessException(ResponseCode.MP_YISHANG_ORDER_NOT_WEIT);
		}
		//判断面值
		ParvalueEntity parvalueEntity=topUpDao.getParvalueByCode(entity.getParvalueCode());
		if(parvalueEntity==null){
			logger.warn("[易赏]，手机充值面值code不存在，parvalueCode："+entity.getParvalueCode());
			throw new BusinessException(ResponseCode.MP_YISHANG_PARVALUE_CODE_NOT_EXIST);
		}
		//使用积分的情况要先扣积分
		if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
			int pId = 0;//用户积分使用情况ID
			PointsExchangeCashInfo pointsExchangeCashInfo = memberPointsDao.getPointsExchangeCashConfigInfo(
					entity.getIntegralCode(), DateUtil.nowDate());
			BigDecimal exchangeAmount = memberPointsService.getPointsExchangeCashAmount(entity.getIntegralCode(),
					entity.getIntegralQty());
			try{
				logger.info("[易赏]，尝试扣除用户积分，积分code："+entity.getIntegralCode()+",积分数量："+entity.getIntegralQty()+",UserId:"+consumptionOrderEntity.getUserId());
				this.memberPointsService.minusUserAccountPoints(entity.getUserId(), entity.getIntegralQty());//减少用户账户积分
			}catch (BusinessException e) {
				logger.warn("[易赏]，扣除用户积分失败，不给充值.UserId:"+consumptionOrderEntity.getUserId()+"***e.getMessage():"+e.getMessage());
				if(consumptionOrderEntity.getPaymentOrderId() != null && consumptionOrderEntity.getPaymentOrderId() > 0){
					logger.warn("[易赏]，扣除用户积分失败退款.UserId"+consumptionOrderEntity.getUserId());
					RefundOrderEntity refundOrder=new RefundOrderEntity();
					refundOrder.setAmount(consumptionOrderEntity.getAmount());
					refundOrder.setConsumeOrderId(consumptionOrderEntity.getId());
					iRefundDao.addOrder(refundOrder);
				}
				response.setCodeMessage(e.getCode(),e.getMessage());
				return response;
			}
			pId=this.memberPointsDao.addPointsUseRecord(pointsExchangeCashInfo.getTypeId(),entity.getUserId(),
					entity.getIntegralQty(),exchangeAmount,PointExchangeStatus.EX_APPLY,DateUtil.nowDate());//添加用户积分使用情况记录
			entity.setP_id(pId);//pId更新到手机订单!!
			topUpDao.updateOrder(entity);
			this.addIntegralRecord(entity.getIntegralCode(), entity.getUserId(), entity.getIntegralQty());//添加积分变动流水-支出
		}
		logger.info("[易赏]，手机充值订单号："+entity.getId()+"订单状态："+entity.getStatus());
		//易赏手机充值
		Map<String,String> paramMap=getTrafficParamMap(parvalueEntity, entity);
		return yishang(paramMap,entity,consumptionOrderEntity);
	}


	/**
	 * 向第三方提交充值申请--易赏
	 * @throws IOException
	 */
	private HttpResponse yishang(Map<String,String> map,MobileTopupOrderEntity entity,ConsumptionOrderEntity consumptionOrderEntity) {
		logger.info("[易赏]第三方接口参数："+JSON.toJSONString(map));
		HttpResponse response = new HttpResponse();
		try{
			String result = YishangUtil.send(Config.get("ys.invoke.url"), map);
			logger.info("[易赏]订单ID："+map.get("orderId")+",第三方接口返回结果："+result);
			Map<String,Object> resultMap=(Map<String, Object>) JSON.parse(result);
//			if(CommonTool.randomNumber()!=""){
//				throw new LogicalException("异常测试");
//			}

			if(resultMap!=null && resultMap.get("result")!=null
					&& YishangConst.getAward_result_success.equals(String.valueOf(resultMap.get("result")))){
				logger.info("[易赏]返回结果为成功："+result);//充值成功 更新订单
				entity.setThirdpartyOrdernum(String.valueOf(resultMap.get("code")));//易赏消耗的串码
				updateYishangOrderInfo(entity,TopUpStatus.ING.getCode());//更新参数在方法里面操作
				return response;
			}else{
				//充值失败 更新订单
				logger.warn("[易赏]返回结果为失败：result:"+result+",PaymentOrderId="+consumptionOrderEntity.getPaymentOrderId());
				updateYishangOrderInfo(entity,TopUpStatus.FAILURE.getCode());
				if(consumptionOrderEntity.getPaymentOrderId() != null
						&& consumptionOrderEntity.getPaymentOrderId() > 0){
					logger.warn("[易赏]返回结果为失败，退款.consumptionOrderId:"+consumptionOrderEntity.getUserId()+",Amount:"+consumptionOrderEntity.getAmount());
					RefundOrderEntity refundOrder=new RefundOrderEntity();
					refundOrder.setAmount(consumptionOrderEntity.getAmount());
					refundOrder.setConsumeOrderId(consumptionOrderEntity.getId());
					iRefundDao.addOrder(refundOrder);
				}
				if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
					logger.warn("[易赏]返回结果为失败，退积分.UserId："+consumptionOrderEntity.getUserId());
					this.memberPointsDao.updatePointsUseRecordStatus(entity.getP_id(), PointExchangeStatus.EX_FAILURE, DateUtil.nowDate());//更新用户积分兑换状态为失败
					this.memberPointsService.minusUserAccountPoints(entity.getUserId(), 0-entity.getIntegralQty());//还原用户账户积分
					this.addIntegralIncomeRecord(entity.getIntegralCode(), entity.getUserId(), entity.getIntegralQty());//添加积分变动流水-收入
				}
				response.setCodeMessage(ResponseCode.MP_YISHANG_RECHARGE_FAILURE);
				return response;
			}
		}catch (Exception e){
			//记录异常信息
			topUpDao.addTopupErrorRecord(entity);
			logger.error("[易赏]接口调用异常:"+e.getMessage(),e);
			e.printStackTrace();
			response.setCodeMessage(ResponseCode.MP_RECHARGE_ERROR);
			return response;
		}
	}

	/**
	 * 三网接口，自动识别供应商，选择对应供应商奖品发放
	 */
	private Map<String,String> getTrafficParamMap(ParvalueEntity parvalueEntity,
				 MobileTopupOrderEntity entity) throws Exception {
		Map<String,String> paramMap=null;
		if(String.valueOf(parvalueEntity.getParvalue()).equals(YishangTopupTypeEnum.Topup_10.getCode())){//充值面额10
			paramMap=YishangLinksService.getTraffic(entity.getOrderNum(),entity.getPhoneNum(),
					Config.get("ys.userId"),Config.get("ys.key"),Config.get("ys.orderId"),
					Config.get("ys.yd.PrizeId_10"),Config.get("ys.yd.PrizePriceTypeId_10"),
					Config.get("ys.lt.PrizeId_10"),Config.get("ys.lt.PrizePriceTypeId_10"),
					Config.get("ys.dx.PrizeId_10"),Config.get("ys.dx.PrizePriceTypeId_10"));
		}else if(String.valueOf(parvalueEntity.getParvalue()).equals(YishangTopupTypeEnum.Topup_30.getCode())){//充值面额30
			paramMap=YishangLinksService.getTraffic(entity.getOrderNum(),entity.getPhoneNum(),
					Config.get("ys.userId"),Config.get("ys.key"),Config.get("ys.orderId"),
					Config.get("ys.yd.PrizeId_30"),Config.get("ys.yd.PrizePriceTypeId_30"),
					Config.get("ys.lt.PrizeId_30"),Config.get("ys.lt.PrizePriceTypeId_30"),
					Config.get("ys.dx.PrizeId_30"),Config.get("ys.dx.PrizePriceTypeId_30"));
		}else if(String.valueOf(parvalueEntity.getParvalue()).equals(YishangTopupTypeEnum.Topup_50.getCode())){//充值面额50
			paramMap=YishangLinksService.getTraffic(entity.getOrderNum(),entity.getPhoneNum(),
					Config.get("ys.userId"),Config.get("ys.key"),Config.get("ys.orderId"),
					Config.get("ys.yd.PrizeId_50"),Config.get("ys.yd.PrizePriceTypeId_50"),
					Config.get("ys.lt.PrizeId_50"),Config.get("ys.lt.PrizePriceTypeId_50"),
					Config.get("ys.dx.PrizeId_50"),Config.get("ys.dx.PrizePriceTypeId_50"));
		}else if(String.valueOf(parvalueEntity.getParvalue()).equals(YishangTopupTypeEnum.Topup_100.getCode())){//充值面额100
			paramMap=YishangLinksService.getTraffic(entity.getOrderNum(),entity.getPhoneNum(),
					Config.get("ys.userId"),Config.get("ys.key"),Config.get("ys.orderId"),
					Config.get("ys.yd.PrizeId_100"),Config.get("ys.yd.PrizePriceTypeId_100"),
					Config.get("ys.lt.PrizeId_100"),Config.get("ys.lt.PrizePriceTypeId_100"),
					Config.get("ys.dx.PrizeId_100"),Config.get("ys.dx.PrizePriceTypeId_100"));
		}

		//测试环境
		if("true".equals(Config.get("ys.develop.mode"))){
			logger.info("测试1元第三方接口参数");
			paramMap=YishangLinksService.getTraffic(entity.getOrderNum(),entity.getPhoneNum(),
					Config.get("ys.userId"),Config.get("ys.key"),Config.get("ys.orderId_test"),
					Config.get("ys.prizeId_test"),Config.get("ys.prizePriceTypeId_test"),
					Config.get("ys.prizeId_test"),Config.get("ys.prizePriceTypeId_test"),
					Config.get("ys.prizeId_test"),Config.get("ys.prizePriceTypeId_test"));
		}
		return paramMap;
	}
	/**
	 * 获取请求参数串
	 * @param order
	 * @return
	 */
	private static String getRequestParamStr(InvokeBusinessVO order) {
		StringBuffer params = new StringBuffer();
		String P1_agentcode = order.getAgentcode();//商户号
		String key = order.getSignKey();
		String P6_callbackurl = order.getCallbackUrl();//商户回调地址
		String P0_biztype = order.getBiztype();//"mobiletopup";//P0_biztype	业务类型	Max(20)	充值请求，手机充值为mobiletopup 固定值
		String P2_mobile = order.getMobile();//手机号
		String P3_parvalue = order.getParvalue();//"30";//充值金额
		String P4_productcode = order.getProductcode();//"SHKC";//充值产品 	1： SHKC 移动   2： SHKC_CU 联通  3： SHKC_CT  电信，其他产品详见接口文档
		String P5_requestid = order.getOrderId();//充值订单号
		String P7_extendinfo = order.getBiztype();//扩展信息
		String signSrc = P0_biztype+P1_agentcode+P2_mobile+P3_parvalue+P4_productcode+P5_requestid+P6_callbackurl+P7_extendinfo;
		String hmac = EncryptionUtil.hmacSign(signSrc, key);
		params.append("P0_biztype=").append(P0_biztype)
			  .append("&P1_agentcode=").append(P1_agentcode)
			  .append("&P2_mobile=").append(P2_mobile)
			  .append("&P3_parvalue=").append(P3_parvalue)
			  .append("&P4_productcode=").append(P4_productcode)
			  .append("&P5_requestid=").append(P5_requestid)
			  .append("&P6_callbackurl=").append(P6_callbackurl)
			  .append("&P7_extendinfo=").append(P7_extendinfo)
			  .append("&hmac=").append(hmac);
		return params.toString();
	}
	


	@Override
	public List<ParvalueVO> getParvalue(ParvalueType type) {
		return topUpDao.getParvalue(type);
	}

	@Override
	public ParvalueEntity getParvalueByCode(String code) {
		return topUpDao.getParvalueByCode(code);
	}

	/**
	 * 劲峰手机充值停止
	 * @param userId
	 * @param parvalueCode
	 * @param phoneNum
	 * @param integralCode
	 * @param integralQty
	 * @return
     * @throws Exception
     */
	@Deprecated
	@Transactional
	@Override
	public MobileTopUpOrder addOrder(Integer userId, String parvalueCode, String phoneNum,
			String integralCode, String integralQty) throws Exception {
		MobileTopUpOrder result = new MobileTopUpOrder();
		if ("1".equals("1")) {
			throw new BusinessException(ResponseCode.OTHER_CHANNEL_OUT_OF_SERVICE);
		}
		/*int frequency = topUpDao.getFrequencyByMonth(userId);
		if (frequency >= Integer.valueOf(Config.get("topup.mobile.frequency"))) {
			throw new BusinessException(ResponseCode.MP_TOPUP_FREQUENCY_OVER.getCode(),
					ResponseCode.MP_TOPUP_FREQUENCY_OVER.getMessage());
		}
		ParvalueEntity parvalueEntity = getParvalueByCode(parvalueCode);
		if (parvalueEntity == null) {
			throw new BusinessException(ResponseCode.MP_PARVALUE_NOT_EXIST.getCode(), 
					ResponseCode.MP_PARVALUE_NOT_EXIST.getMessage());
		}
		
		//用户积分操作
		BigDecimal exchangeAmount = BigDecimal.ZERO;//兑换金额
		Integer integralId = 0;//用户积分明细ID
		if(StringUtils.isNotEmpty(integralCode)&&StringUtils.isNotEmpty(integralQty)){
			//验证面额与能使用积分是否匹配
			Map<String,Object> parMap = new HashMap<String,Object>();
			parMap.put("parvalueCode", parvalueCode);
			parMap.put("pointsTypeCode", integralCode);
			List<ParvalueVO> parList = this.topUpDao.getParvalue(parMap);
			if(parList == null || parList.size()==0){
				throw new BusinessException(ResponseCode.MP_POINTS_CODE_ERROR.getCode(),
						ResponseCode.MP_POINTS_CODE_ERROR.getMessage());
			}
			
			//验证用户积分规则
			this.memberPointsService.pointsCanExchangeCash(userId, integralCode, Integer.parseInt(integralQty));
			//获取兑换数量、新增会员积分明细
			if (StringUtils.isNotBlank(integralCode)) {
				exchangeAmount = memberPointsService.getPointsExchangeCashAmount(integralCode, Integer.valueOf(integralQty));
				//integralId = addIntegralRecord(integralCode, userId, integralQty);
			}
		}
		
		//用户需支付金额
		BigDecimal userPayAmount = parvalueEntity.getSellingPrice().subtract(exchangeAmount);
		
		//添加订单
		MobileTopupOrderEntity order = new MobileTopupOrderEntity();
		order.setUserPayAmount(userPayAmount);
		order.setUserId(userId);
		order.setParvalueCode(parvalueCode);
		order.setPhoneNum(phoneNum);
		order.setP_id(integralId);
		order.setOrderNum(OrderUtil.getOrderNo_JF());
		order.setTopUpChannel(RechargeChannel.JFYP.getCode());
		order.setIntegralCode(integralCode);
		order.setIntegralQty(StringUtils.isNotEmpty(integralQty)?Integer.valueOf(integralQty):null);
		topUpDao.addOrder(order);
		if (order.getId() == null) {
			throw new BusinessException(ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getCode(),
					ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getMessage());
		}
		
		result.setParvalue(parvalueEntity.getParvalue().toString());
		result.setOrderId(order.getOrderNum());
		result.setMobile(phoneNum);
		result.setId(order.getId());
		result.setUserPayAmount(userPayAmount);*/
		
		return result;
	}
	
	/**
	 * 积分明细-支出
	 * @param integralCode
	 * @param userId
	 * @param integralQty
	 * @return
	 */
	private Integer addIntegralRecord(String integralCode, Integer userId, Integer integralQty) {
		PointsType type = memberPointsDao.getPointsTypeInfo(integralCode);
		if (type == null) {
			throw new BusinessException(ResponseCode.MP_INTEGRAL_NOT_EXIST.getCode(),
					ResponseCode.MP_INTEGRAL_NOT_EXIST.getMessage());
		}
		return memberPointsDao.addPointsSheetRecord(type.getId(), userId, 
				integralQty, 2, new Date());
	}
	
	/**
	 * 积分明细-收入
	 * @param integralCode
	 * @param userId
	 * @param integralQty
	 * @return
	 */
	private Integer addIntegralIncomeRecord(String integralCode, Integer userId, Integer integralQty) {
		PointsType type = memberPointsDao.getPointsTypeInfo(integralCode);
		if (type == null) {
			throw new BusinessException(ResponseCode.MP_INTEGRAL_NOT_EXIST.getCode(),
					ResponseCode.MP_INTEGRAL_NOT_EXIST.getMessage());
		}
		return memberPointsDao.addPointsSheetRecord(type.getId(), userId, 
				integralQty, 1, new Date());
	}

	@Transactional
	@Override
	public String updateOrderInfo(MobileTopUpCallbackVO vo) throws Exception {
		//获取订单
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", TopUpStatus.ING.getCode());
		if(StringUtils.isNotEmpty(vo.getR6_requestid())){
			map.put("orderNum", vo.getR6_requestid());
		}
		MobileTopupOrderEntity entity  = topUpDao.getOrder(map);
		if(null == entity){
			return "no order";
		}
		
		Integer status =0;//订单状态
		if(vo.getR8_returncode().equals("0")){
			return "0";
		}else if(vo.getR8_returncode().equals("1")){
			return "1";
		}else if(vo.getR8_returncode().equals("2")){
			status = TopUpStatus.SUCCESS.getCode();
		}else if(vo.getR8_returncode().equals("6")){
			status = TopUpStatus.FAILURE.getCode();
		}else{
			return "other";
		}
		vo.setR8_returncode(String.valueOf(status));
		Date endTime = OrderUtil.StringToDate(vo.getR10_trxDate(), "yyyyMMddHHmmss");
		
		MobileTopupOrderEntity order = new MobileTopupOrderEntity();
		order.setOrderNum(vo.getR6_requestid());
		order.setStatus(Integer.valueOf(vo.getR8_returncode()));
		order.setPlatformPayAmount(new BigDecimal(vo.getR4_trxamount()));
		order.setEndTime(endTime);
		order.setThirdpartyOrdernum(vo.getR7_trxid());
		this.topUpDao.updateOrder(order);
		
		if(order.getStatus() == TopUpStatus.SUCCESS.getCode()){//第三方充值成功
			if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
				//添加积分明细
				this.addIntegralRecord(entity.getIntegralCode(), entity.getUserId(), entity.getIntegralQty());
				//更新用户积分兑换状态
				this.memberPointsDao.updatePointsUseRecordStatus(entity.getP_id(), PointExchangeStatus.EX_SUCCESS, DateUtil.nowDate());
			}
			//确认用户资金账户交易
			transactionConfirm(entity.getUserId(),entity.getUserPayAmount(),FeeCode.REACHARGE,"话费充值交易确认");
		}else if(order.getStatus() == TopUpStatus.FAILURE.getCode()){//第三方充值失败
			logger.info("话费充值失败，用户ID:"+entity.getUserId()+"充值手机号："+entity.getPhoneNum());
			if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
				//更新用户积分兑换状态为失败
				this.memberPointsDao.updatePointsUseRecordStatus(entity.getP_id(), PointExchangeStatus.EX_FAILURE, DateUtil.nowDate());
				//还原用户账户积分
				this.memberPointsService.minusUserAccountPoints(entity.getUserId(), 0-entity.getIntegralQty());
			}
			//资金账户交易失败
			transactionFail(entity.getUserId(),entity.getUserPayAmount(),FeeCode.REACHARGE_SB,"话费充值交易失败");
		}
		return "success";
	}
	
	@Transactional
	@Override
	public void updateOrderInfo(MobileTopUpOrder order,String integralCode,String integralQty,Integer userId,BigDecimal userPayAmount,String orderNum, Integer status) throws Exception {
		try {
			int pId = 0;//用户积分使用情况ID
			//添加积分使用申请记录
			if(StringUtils.isNotEmpty(integralCode)&&StringUtils.isNotEmpty(integralQty)){
				PointsExchangeCashInfo pointsExchangeCashInfo = memberPointsDao.getPointsExchangeCashConfigInfo(integralCode, DateUtil.nowDate());
				BigDecimal exchangeAmount = memberPointsService.getPointsExchangeCashAmount(integralCode, Integer.valueOf(integralQty));
				pId = memberPointsDao.addPointsUseRecord(pointsExchangeCashInfo.getTypeId(),userId,Integer.valueOf(integralQty),exchangeAmount,PointExchangeStatus.EX_APPLY,DateUtil.nowDate());
			
				//减少用户账户积分
				this.memberPointsService.minusUserAccountPoints(userId, Integer.valueOf(integralQty));
			}
			
			MobileTopupOrderEntity updateOrder = new MobileTopupOrderEntity();
			updateOrder.setOrderNum(orderNum);
			updateOrder.setStatus(status);
			updateOrder.setP_id(pId);
			this.topUpDao.updateOrder(updateOrder);
			
			transactionApplyfor(userId,userPayAmount,FeeCode.REACHARGE,"话费充值交易申请");
			
			String result = this.mobileTopUp(order);
			
			if(!result.equals("000000")){
				throw new BusinessException(ResponseCode.MP_RECHARGE_ERROR.getCode(), ResponseCode.MP_RECHARGE_ERROR.getMessage());
			}
		}
		catch(BusinessException e){
			throw e;
		} 
		catch (Exception e) {
			throw new BusinessException(ResponseCode.MP_RECHARGE_ERROR.getCode(), ResponseCode.MP_RECHARGE_ERROR.getMessage());
		}
		
	}

	@Override
	public MobileTopupOrderEntity getOrderInfo(Integer id, String orderNum,Integer status) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(id == null && orderNum == null){
			return null;
		}
		if(id != null){
			map.put("id", id);
		}
		if(status != null){
			map.put("status", status);
		}
		if(StringUtils.isNotEmpty(orderNum)){
			map.put("orderNum", orderNum);
		}
		
		MobileTopupOrderEntity entity = topUpDao.getOrder(map);
		return entity;
	}

	@Override
	public int getFrequency(int userId, int days) {
		return topUpDao.getFrequency(userId, days);
	}

	/**
	 * 添加易赏充值订单
	 * @author junda.feng-2016-4-25
	 * @param parvalueCode 充值面额编码
	 * @param phoneNum 充值手机
	 * @param integralCode 使用积分类型编码
	 * @param integralQty 使用积分数量
	 * 
	 */
	@Transactional
	@Override
	public MobileTopupOrderEntity addYishangOrder(Integer userId,String parvalueCode,
			String phoneNum,String integralCode, String integralQty) throws Exception {
//		int frequency = topUpDao.getFrequencyByMonth(userId);
//		if (frequency >= Integer.valueOf(Config.get("topup.mobile.frequency"))) {
//			throw new BusinessException(ResponseCode.MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY.getCode(),
//					ResponseCode.MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY.getMessage());
//		}
		//只能给自己绑定的号码充值
		UserAccountInfoVO user=userInfoService.getUserAccountInfo(String.valueOf(userId));
		if(!user.getPhone().equals(phoneNum)){
			throw new BusinessException(ResponseCode.MP_YISHANG_PHONRN_ERROR.getCode(),
					ResponseCode.MP_YISHANG_PHONRN_ERROR.getMessage());
		}
		
		ParvalueEntity parvalueEntity = getParvalueByCode(parvalueCode);
		if (parvalueEntity == null ) {//充值面值不存在
			throw new BusinessException(ResponseCode.MP_PARVALUE_NOT_EXIST.getCode(), 
					ResponseCode.MP_PARVALUE_NOT_EXIST.getMessage());
		}
		//20161217 每月兑换不能超过300元话费 by kris
		Integer TopUpNum = topUpDao.getUserTopUpSameMonth(userId);
		Integer topupmax = Integer.valueOf(Config.get("topup.mobile.limit.max"));
		if(TopUpNum + parvalueEntity.getParvalue().intValue()>topupmax){
			throw new BusinessException(ResponseCode.MP_YISHANG_OVERTOP);
		}
		//通过枚举判断易赏充值面额是否存在
		if(!YishangTopupTypeEnum.isSignType(String.valueOf(parvalueEntity.getParvalue()))){
			throw new BusinessException(ResponseCode.MP_PARVALUE_NOT_EXIST.getCode(),
					ResponseCode.MP_PARVALUE_NOT_EXIST.getMessage());
		}
		
		//用户积分操作
		BigDecimal exchangeAmount = BigDecimal.ZERO;//兑换金额
		Integer integralId = 0;//用户积分明细ID
		if(StringUtils.isNotEmpty(integralCode)&&StringUtils.isNotEmpty(integralQty)){
			//验证面额与能使用积分是否匹配
			Map<String,Object> parMap = new HashMap<String,Object>();
			parMap.put("parvalueCode", parvalueCode);
			parMap.put("pointsTypeCode", integralCode);
			List<ParvalueVO> parList = this.topUpDao.getParvalue(parMap);
			if(parList == null || parList.size()==0){
				throw new BusinessException(ResponseCode.MP_POINTS_CODE_ERROR.getCode(),
						ResponseCode.MP_POINTS_CODE_ERROR.getMessage());
			}
			
			//验证用户积分规则
			this.memberPointsService.pointsCanExchangeCash(userId, integralCode, Integer.parseInt(integralQty));
			//获取兑换数量、新增会员积分明细
			if (StringUtils.isNotBlank(integralCode)) {
				exchangeAmount = memberPointsService.getPointsExchangeCashAmount(integralCode, Integer.valueOf(integralQty));
				//integralId = addIntegralRecord(integralCode, userId, integralQty);
			}
		}
		
		//用户需支付金额
		BigDecimal userPayAmount = parvalueEntity.getSellingPrice().subtract(exchangeAmount);

		if(userPayAmount.compareTo(new BigDecimal(0.00))<0){//支付金额不能为负数 junfe.feng 2016-5-12
			userPayAmount = new BigDecimal(0.00);
		}
		//添加消费订单
		ConsumptionOrderEntity consumptionOrder=new ConsumptionOrderEntity();
		consumptionOrder.setUserId(userId);
		consumptionOrder.setAmount(userPayAmount);
		consumptionOrder.setTypeCode(ConsumptionType.SJCZ.getCode());
		consumptionDao.addOrder(consumptionOrder);
		if (consumptionOrder.getId() == null) {
			throw new BusinessException(ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getCode(),
					ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getMessage());
		
		}
		
		//添加手机充值订单
		MobileTopupOrderEntity order = new MobileTopupOrderEntity();
		order.setConsumptionOrderId(consumptionOrder.getId());//消费订单ID
		order.setUserPayAmount(userPayAmount);
		order.setUserId(userId);
		order.setParvalueCode(parvalueCode);
		order.setPhoneNum(phoneNum);
		order.setP_id(integralId);
		order.setOrderNum(OrderUtil.getOrderNo_YS());
		order.setTopUpChannel(RechargeChannel.YS.getCode());
		order.setIntegralCode(integralCode);
		order.setIntegralQty(StringUtils.isNotEmpty(integralQty)?Integer.valueOf(integralQty):null);
		
		topUpDao.addYishangOrder(order);
		if (order.getId() == null) {
			throw new BusinessException(ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getCode(),
					ResponseCode.MP_ADD_MOBILE_TOPUP_ORDER_FAILURE.getMessage());
		}
		
//		yishangTopUp(consumptionOrder);
		return order;
	
	}
	/**
	 * 易赏--检查手机号码
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkYishang(String phone) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("service", "canRecharge");//服务接口
		map.put("phone", phone);//客户订单号
		map.put("userId", ys_userId);
		map.put("sign", YishangUtil.getSign(map, ys_key));
		String result = YishangUtil.send(ys_url, map);//{"message":"请求成功","result":"10000","canRecharge":"yes"}
		Map<String,Object> resultMap=(Map<String,Object>)JSON.parse(result);
		String canRecharge=(String) resultMap.get("canRecharge");
		if(YishangConst.canRecharge_success.equals(canRecharge)){//可以充值
			return true;
		}else{
			return false;
		}
	}
	@Override
	public List<MobileTopUpOrderRecord> getMobileTopupOrderList(int userId,
			int pageNum) {
		return topUpDao.getMobileTopupOrderList(userId, pageNum);
	}
	
	@Transactional
	@Override
	public void updateYishangOrderInfo(MobileTopupOrderEntity order,int state) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", Integer.valueOf(order.getId()));
		MobileTopupOrderEntity entity = topUpDao.getOrder(map);
		entity.setThirdpartyOrdernum(order.getThirdpartyOrdernum());
		entity.setP_id(order.getP_id());
		
		//更新消费订单提交时间和状态
		ConsumptionOrderEntity consumptionOrderEntity=consumptionDao.getConsumptionOrderById(entity.getConsumptionOrderId());
		consumptionOrderEntity.setSubmitTime(new Date());
		consumptionDao.updateOrder(consumptionOrderEntity);
		//更新手机订单
		if(TopUpStatus.ING.getCode()==state){//充值中
			entity.setStatus(TopUpStatus.ING.getCode());
		}else if(TopUpStatus.FAILURE.getCode()==state){//失败
			entity.setStatus(TopUpStatus.FAILURE.getCode());
		}
		topUpDao.updateOrder(entity);
	}
	@Override
	public MobileTopupOrderEntity getOrderInfoById(Integer id,Integer status) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(id == null ){
			return null;
		}
		if(id != null){
			map.put("id", id);
		}
		if(status != null){
			map.put("status", status);
		}
		
		MobileTopupOrderEntity entity = topUpDao.getOrder(map);
		return entity;
	}
	
	@Transactional
	@Override
	public String updateYishangOrderInfo(MobileTopupOrderEntity entity ) throws Exception {
		//更新手机订单状态
		this.topUpDao.updateOrder(entity);
		ConsumptionOrderEntity consumptionOrderEntity=consumptionDao.getConsumptionOrderById(entity.getConsumptionOrderId());
		consumptionOrderEntity.setFinishedTime(new Date());
		if(entity.getStatus() == TopUpStatus.SUCCESS.getCode()){//第三方充值成功
			if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
				//添加积分明细
				//this.addIntegralRecord(entity.getIntegralCode(), entity.getUserId(), entity.getIntegralQty());
				//更新用户积分兑换状态
				this.memberPointsDao.updatePointsUseRecordStatus(entity.getP_id(), PointExchangeStatus.EX_SUCCESS, DateUtil.nowDate());
			}
			
			consumptionDao.updateOrder(consumptionOrderEntity);
		}else if(entity.getStatus() == TopUpStatus.FAILURE.getCode()){//第三方充值失败
			logger.info("话费充值失败，用户ID:"+entity.getUserId()+",充值手机号:"+entity.getPhoneNum()+",订单号:"+entity.getOrderNum());
			if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
				//更新用户积分兑换状态为失败
				this.memberPointsDao.updatePointsUseRecordStatus(entity.getP_id(), PointExchangeStatus.EX_FAILURE, DateUtil.nowDate());
				//还原用户账户积分
				this.memberPointsService.minusUserAccountPoints(entity.getUserId(), 0-entity.getIntegralQty());
				//添加积分变动流水-收入
				this.addIntegralIncomeRecord(entity.getIntegralCode(), entity.getUserId(), entity.getIntegralQty());
			}
			
			//修改消费订单状态-待退款
			if(consumptionOrderEntity.getPaymentOrderId() != null 
					&& consumptionOrderEntity.getPaymentOrderId() > 0){
				RefundOrderEntity refundOrder=new RefundOrderEntity();
				refundOrder.setAmount(consumptionOrderEntity.getAmount());
				refundOrder.setConsumeOrderId(consumptionOrderEntity.getId());
				iRefundDao.addOrder(refundOrder);
			}
			
			consumptionDao.updateOrder(consumptionOrderEntity);
		}
		
		
		return "success";
	}
	
	
	@Override
	public MobileTopupOrderEntity getOrderInfoByOrderNum(String orderNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(orderNum == null ){
			return null;
		}
		map.put("orderNum", orderNum);
		
		return topUpDao.getOrder(map);
	}
	
	@Override
	public List<MobileTopUpOrderInchargeEntity> yishangQueryStatusList(Integer page,Integer limit) throws Exception {
		return topUpDao.getOrderListInCharge(page,limit);
	}
	
	@Override
	public Map<String,Object> yishangQueryStatus(MobileTopUpOrderInchargeEntity mobileTopUpOrderInchargeEntity) throws Exception {
		Map<String,Object> resultMap=null;
			Map<String,String> map = new HashMap<String,String>();
			map.put("service", "queryStatus");//服务接口
			map.put("customOrderCode", mobileTopUpOrderInchargeEntity.getCustomOrderCode());//客户订单号
			map.put("userId", Config.get("ys.userId"));//用户id，由易赏平台提供
			map.put("orderId", Config.get("ys.orderId"));//订单id，由易赏平台提供
			//测试环境
			if("true".equals(Config.get("ys.develop.mode"))){
				map.put("orderId", Config.get("ys.orderId_test"));//测试环境订单id
			}
			map.put("sign", YishangUtil.getSign(map, Config.get("ys.key")));
			String result = YishangUtil.send(Config.get("ys.invoke.url"), map);
			resultMap=(Map<String,Object>)JSON.parse(result);
			logger.info("主动查询结果:customOrderCode="+mobileTopUpOrderInchargeEntity.getCustomOrderCode()+":resultMap="+resultMap);
			return resultMap;
	}
	
	@Transactional
	@Override
	public void yishangQueryStatusResult(Map<String,Object> resultMap) throws Exception {
		if(resultMap!=null){
			if(resultMap.get("status")!=null){
				String customOrderCode=resultMap.get("customOrderCode").toString();
				//处理结果
				String status=resultMap.get("status").toString();
				//获取订单-锁记录
				MobileTopupOrderEntity entity= getOrderInfoByOrderNum(customOrderCode);
				if(entity.getStatus()==TopUpStatus.ING.getCode()){
					entity.setEndTime(new Date());
					if(YishangConst.result_state_charge_get.equals(status)||YishangConst.result_state_get.equals(status)){//成功
						logger.info("主动查询，充值成功:customOrderCode="+customOrderCode+":status="+status);
						entity.setStatus(TopUpStatus.SUCCESS.getCode());
						entity.setPlatformPayAmount(BigDecimal.ZERO);//平台支付金额为默认0
						updateYishangOrderInfo(entity);
					}else if(YishangConst.result_state_error.equals(status)){//失败
						logger.warn("主动查询，充值失败:customOrderCode="+customOrderCode+":status="+status);
						entity.setStatus(TopUpStatus.FAILURE.getCode());
						entity.setPlatformPayAmount(BigDecimal.ZERO);//平台支付金额为默认0
						updateYishangOrderInfo(entity);
					}
				}
			}
		}
	}

	@Override
	public List<MobileTopUpErrorRecord> yishangErrorRecords() throws Exception {
		return topUpDao.yishangErrorRecords();
	}

	@Override
	public MobileTopUpErrorRecord getYishangErrorRecord(Map map) throws Exception {
		return topUpDao.getYishangErrorRecord(map);
	}
	@Override
	public Map<String,Object> yishangQueryStatus(MobileTopUpErrorRecord mobileTopUpErrorRecord) throws Exception {
		Map<String,Object> resultMap=null;
		Map<String,String> map = new HashMap<String,String>();
		map.put("service", "queryStatus");//服务接口
		map.put("customOrderCode", mobileTopUpErrorRecord.getCustomOrderCode());//客户订单号
		map.put("userId", Config.get("ys.userId"));//用户id，由易赏平台提供
		map.put("orderId", Config.get("ys.orderId"));//订单id，由易赏平台提供
		//测试环境
		if("true".equals(Config.get("ys.develop.mode"))){
			map.put("orderId", Config.get("ys.orderId_test"));//测试环境订单id
		}
		map.put("sign", YishangUtil.getSign(map, Config.get("ys.key")));
		String result = YishangUtil.send(Config.get("ys.invoke.url"), map);
		resultMap=(Map<String,Object>)JSON.parse(result);
		logger.info("主动查询结果:customOrderCode="+mobileTopUpErrorRecord.getCustomOrderCode()+":resultMap="+resultMap);
		return resultMap;
	}
	@Transactional
	@Override
	public void yishangDealErrorRecord(Map<String, Object> resultMap) throws Exception {
		if(resultMap!=null){
			if(resultMap.get("status")!=null){
				String customOrderCode=resultMap.get("customOrderCode").toString();
				//处理结果
				String result=resultMap.get("result").toString();
				//获取异常记录-锁记录
				Map map=new HashMap();
				map.put("orderNum",customOrderCode);
				MobileTopUpErrorRecord errorRecord= getYishangErrorRecord(map);
				//获取订单-锁记录
				MobileTopupOrderEntity entity= getOrderInfoByOrderNum(customOrderCode);
				if(entity.getStatus()==TopUpStatus.WAIT.getCode()){//待提交状态
					entity.setEndTime(new Date());
					if(YishangConst.result_order_no_exist.equals(result)){//customOrderCode 不存在
						logger.warn("[易赏]异常数据查询，充值失败:customOrderCode="+customOrderCode+":result="+result);
						entity.setStatus(TopUpStatus.FAILURE.getCode());
						entity.setPlatformPayAmount(BigDecimal.ZERO);//平台支付金额为默认0
						updateYishangOrderInfo(entity);
					}
					//更新处理状态
					errorRecord.setState("YCL");
					topUpDao.updateErrorRecords(errorRecord);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		InvokeBusinessVO order = new InvokeBusinessVO();
//		String orderId = UUID.randomUUID().toString();
//		System.out.println(orderId);
//		order.setOrderId(orderId);//商户端生成的唯一订单编号
//		order.setMobile("13751763759");//充值手机号码
//		order.setParvalue("10");//充值面额，以元为单位
//		order.setProductcode("SHKC");//没有判断的话，可以填写（SHKC,SHKC_CU,SHKC_CT）其中的任意一个，
//		String requestParamStr = getRequestParamStr(order);
//		String result = HttpClientUtil.doPost(requestParamStr, SENDORDERURL);
//		System.out.println(result);
		System.out.println((Map<String,Object>)JSON.parse(null));

		Map<String,String> map = new HashMap<String,String>();
		map.put("service", "queryOperator");//服务接口
		map.put("phone", "15915926932");//客户订单号
		map.put("userId", "3655");
		map.put("sign", YishangUtil.getSign(map, "EBteDrk4AdN1GxQ+o-PB!10R#YUD0PmA"));
		String result = YishangUtil.send("http://api.1shang.com/service/apiService", map);//{"message":"请求成功","result":"10000","canRecharge":"yes"}
		Map<String,Object> resultMap=(Map<String,Object>)JSON.parse(result);
		String operator=(String) resultMap.get("operator");
		System.out.println(JSON.toJSONString(resultMap));
		System.out.println(operator);
	}
}
