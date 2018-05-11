package com.fenlibao.p2p.controller.noversion.pay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.entity.topup.ParvalueEntity;
import com.fenlibao.p2p.model.mp.enums.topup.TopUpStatus;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.service.mp.topup.IGatewayDataService;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.mp.topup.EncryptionUtil;
import com.fenlibao.thirdparty.yishang.YishangConst;
import com.fenlibao.thirdparty.yishang.util.YishangUtil;

/**
 * 手机充值异步回调
 * @author yangzengcai
 * @date 2016年2月17日
 */
@RestController
@RequestMapping("/notify")
public class MobileTopUpNotifyContrller {

	private static final Logger logger = LogManager.getLogger(MobileTopUpNotifyContrller.class);
	
	@Resource
	private ITopUpService topUpService;
	
	@Resource
	private IGatewayDataService gatewayDataService;
	
	@Resource
	private MemberPointsService memberPointsService;
	
	@Resource
	private PrivateMessageService privateMessageService;
	
	@RequestMapping(value = "mobile/topup")
	public String mobileTopUp(MobileTopUpCallbackVO vo) {
		
		try {
			this.gatewayDataService.addGatewayData(vo);
			
			StringBuffer param = new StringBuffer();
			param.append(vo.getR0_biztype()).append(vo.getR1_agentcode()).append(vo.getR2_mobile()).append(vo.getR3_parvalue())
			.append(vo.getR4_trxamount()).append(vo.getR5_productcode()).append(vo.getR6_requestid()).append(vo.getR7_trxid())
			.append(vo.getR8_returncode()).append(vo.getR9_extendinfo()).append(vo.getR10_trxDate());
			
			String hmac = EncryptionUtil.hmacSign(param.toString(), Config.get("jf.signkey"));
			if(!hmac.equals(vo.getHmac())){
				logger.info("hmac error");
				return "hmac error";
			}
			
			MobileTopupOrderEntity entity  = this.topUpService.getOrderInfo(null, vo.getR6_requestid(), TopUpStatus.ING.getCode());//获取订单
			
			String result = this.topUpService.updateOrderInfo(vo);
			
			//充值成功发送站内信
			if(result.equals("success")){
				ParvalueEntity parvalueEntity = this.topUpService.getParvalueByCode(entity.getParvalueCode());//面额
				
				String znxSuffixContent = Sender.get("znx.suffix.content");
				
				String content = Sender.get("sms.recharge.with.integral");
				if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
					BigDecimal exchangeAmount = memberPointsService.getPointsExchangeCashAmount(entity.getIntegralCode(), entity.getIntegralQty());
					content = content.replace("#{rechargeTime}", DateUtil.getDateTime(entity.getCreateTime())).replace("#{integralQty}", entity.getIntegralQty().toString())
							.replace("#{exchangeAmount}", exchangeAmount.toString()).replace("#{parvalue}", parvalueEntity.getParvalue().toString());
				}else{
					content = Sender.get("sms.recharge.without.integral");
					content = content.replace("#{rechargeTime}", DateUtil.getDateTime(entity.getCreateTime())).replace("#{parvalue}", parvalueEntity.getParvalue().toString());
				}
				
				privateMessageService.sendLetter(entity.getUserId().toString(), InterfaceConst.PRIVATEMESSAGE_TITLE, content+znxSuffixContent);
			}
			return result;
			
		} catch (Exception e) {
			logger.error("[MobileTopUpNotifyContrller.mobileTopUp]-callbakc error:"+e.getMessage(),e);
			return "callback error";
		}
	}
	
	
	

	/** 
	 * @Title: yishangCallback 
	 * @Description: 易赏充值结果回调接口
	 * @param customOrderCode 兑奖客户系统流水号（客户系统调用该接口的唯一标记）
	 * @param orderId  订单 id
	 * @param code  消耗的串码
	 * @param phone 电话号码
	 * @param status 状态， 充值成功或者失败。
	 * @param sign 校验码
	 * 
	 * @return: String
	 */
	@RequestMapping(value = "mobile/topup/yishang")
	public String yishangCallback(String customOrderCode,String orderId,String code,String phone,
			String status,String sign) {
		Map<String,Object> resultMap=new HashMap<>();
		try {
			resultMap.put("customOrderCode", customOrderCode);//orderId
			resultMap.put("orderId", orderId);
			resultMap.put("code", code);
			logger.info("[MobileTopUpNotifyContrller.yishangCallback]--易赏回调收到的参数:customOrderCode="+customOrderCode+":orderId="+orderId+":status="+status+":phone="+phone+":sign="+sign+":code="+code);
			//验证签名    校验码生成规则说明： MD5（ customOrderCode+orderId+status+key）， 32位小写字符串。
			String paramSign= YishangUtil.Md5(customOrderCode+orderId+status+Config.get("ys.key"),"UTF-8");
			
			if(!paramSign.equals(sign)){
				logger.warn("sign error");
				resultMap.put("resultCode", YishangConst.resultCode_fail);//接收失败
				return JSON.toJSONString(resultMap);
			}
			
			//获取订单
			MobileTopupOrderEntity entity= topUpService.getOrderInfoByOrderNum(customOrderCode);
			if(entity==null){
				logger.warn("充值订单不存在：customOrderCode="+customOrderCode);
				resultMap.put("resultCode", YishangConst.resultCode_code_no_exist);//customOrderCode 不存在
				return JSON.toJSONString(resultMap);
			}
			if(entity.getStatus()!=TopUpStatus.ING.getCode()){
				logger.warn("推送重复:customOrderCode="+customOrderCode+":TopUpStatus="+entity.getStatus());
				resultMap.put("resultCode", YishangConst.resultCode_push_repeat);//推送重复
				return JSON.toJSONString(resultMap);
			}
			if(!code.equals(entity.getThirdpartyOrdernum())){
				logger.warn("code 错误:customOrderCode="+customOrderCode+":code="+code);
				resultMap.put("resultCode", YishangConst.resultCode_code_error);//code 错误
				return JSON.toJSONString(resultMap);
			}
			if(!entity.getPhoneNum().equals(phone)){
				logger.warn("phone 错误:customOrderCode="+customOrderCode+":phone="+phone);
				resultMap.put("resultCode", YishangConst.resultCode_phone_error);//phone 错误
				return JSON.toJSONString(resultMap);
			}
			resultMap.put("resultCode", YishangConst.resultCode_success);//接收成功
			
			entity.setEndTime(new Date());
			if(YishangConst.result_state_charge_get.equals(status)||YishangConst.result_state_get.equals(status)){//成功
				logger.info("充值成功:customOrderCode="+customOrderCode+":status="+status);
				entity.setStatus(TopUpStatus.SUCCESS.getCode());
				topUpService.updateYishangOrderInfo(entity);
				//充值成功发送站内信
				ParvalueEntity parvalueEntity = this.topUpService.getParvalueByCode(entity.getParvalueCode());//面额
				
				String znxSuffixContent = Sender.get("znx.suffix.content");
				
				String content = Sender.get("sms.recharge.with.integral");
				if(StringUtils.isNotEmpty(entity.getIntegralCode())&&entity.getIntegralQty()!=null){
					BigDecimal exchangeAmount = memberPointsService.getPointsExchangeCashAmount(entity.getIntegralCode(), entity.getIntegralQty());
					content = content.replace("#{rechargeTime}", DateUtil.getDateTime(entity.getCreateTime())).replace("#{integralQty}", entity.getIntegralQty().toString())
							.replace("#{exchangeAmount}", exchangeAmount.toString()).replace("#{parvalue}", parvalueEntity.getParvalue().toString());
				}else{
					content = Sender.get("sms.recharge.without.integral");
					content = content.replace("#{rechargeTime}", DateUtil.getDateTime(entity.getCreateTime())).replace("#{parvalue}", parvalueEntity.getParvalue().toString());
				}
				
				privateMessageService.sendLetter(entity.getUserId().toString(), InterfaceConst.PRIVATEMESSAGE_TITLE, content+znxSuffixContent);
			
			}else if(YishangConst.result_state_error.equals(status)){//失败
				logger.warn("充值失败:customOrderCode="+customOrderCode+":status="+status);
				entity.setStatus(TopUpStatus.FAILURE.getCode());
				topUpService.updateYishangOrderInfo(entity);
			}else if(YishangConst.result_state_inchage.equals(status)){//处理中
				logger.info("处理中:customOrderCode="+customOrderCode+":status="+status);
			}
			
			
			return JSON.toJSONString(resultMap);
		} catch (Exception e) {
			logger.error("[MobileTopUpNotifyContrller.yishangCallback]-易赏回调处理出错:customOrderCode="+customOrderCode+":"+e.getMessage(),e);
			//resultMap.put("resultCode", "10001");//接收失败
			return JSON.toJSONString(resultMap);
		}
		
		
	}
	
//	public String yishangCallback(HttpServletRequest request) {
//		Map map=request.getParameterMap();
//		 StringBuffer sb = new StringBuffer();
//		Iterator<String> it=map.keySet().iterator();
//		
//		while(it.hasNext()){
//			String key=it.next();
//			String value=request.getParameter(key);
//			
//			sb.append(key+":"+value+"\n");
//		}
//		return sb.toString();
//	}
		
//	/** 
//	 * @Title: yishangCallback2
//	 * @Description: 易赏充值回调--流方式
//	 * @return: String
//	 */
//	@RequestMapping(value = "mobile/topup/yishang2")
//	public String yishangCallback2(HttpServletRequest request) {
//		
//		String returnString =null;
//		try {
//			//流方式接受请求参数
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//	                    (ServletInputStream) request.getInputStream()));
//            String line = null;
//            StringBuffer sb = new StringBuffer();
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            returnString = sb.toString();
//            
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return returnString;
//	}
		
}
