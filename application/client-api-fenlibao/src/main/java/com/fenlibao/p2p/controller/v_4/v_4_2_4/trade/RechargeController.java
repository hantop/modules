package com.fenlibao.p2p.controller.v_4.v_4_2_4.trade;

import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.controller.noversion.pay.AbstractLianLianPayController;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import com.fenlibao.p2p.service.bank.BankCardDmService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.pay.IUserPayInfoService;
import com.fenlibao.p2p.service.recharge.IRechargeMangeService;
import com.fenlibao.p2p.service.recharge.IRechargeOrderService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.OrderUtil;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 充值控制器
 * @author yangzengcai
 * @date 2015年8月17日
 */
@RestController("v_4_2_4/RechargeController")
@RequestMapping(value = "lianlianPay", headers = APIVersion.v_4_2_4)
public class RechargeController extends AbstractLianLianPayController {

	@Resource
	private IRechargeMangeService rechargeMangeService;
	@Resource
	private IUserPayInfoService userPayInfoService;
	@Resource
	private IRechargeOrderService rechargeOrderService;
	@Resource
	private BankCardDmService bankCardDmService;
	@Resource
	private IRechargeService rechargeService;
	@Resource
	private IOrderService orderService;
	@Resource
	private RedisService redisService;
	
	private static final String BANK_CARD_NO_REG = "^[0-9]{16,19}$";
	
	/**
	 * 充值
	 * <p>首次充值银行卡不能为空
	 * @return
	 */
	@RequestMapping(value = "recharge", method = RequestMethod.POST)
	public HttpResponse recharge(@ModelAttribute BaseRequestFormExtend params, HttpServletResponse response,
			String amount, String bankCardNo, Integer isBind, String returnUri) {
		HttpResponse custResponse = new HttpResponse();
		//userId是唯一的可以用来防重复提交，一个用户不可能同时进行多个操作
		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(params.getUserId().toString());
		if (redisService.existsKey(requestCacheKey)) {
			custResponse.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
			return custResponse;
		}
		try {
			if (!params.validate() || StringUtils.isBlank(amount) || isBind == null) {
				custResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return custResponse;
			}
			Map<String, String> param = new HashMap<String, String>();
			int userId = params.getUserId();
			//客户端微信端
			boolean isWap = (Constant.CLIENTTYPE_WAP + "").equals(params.getClientType())
					|| (Constant.CLIENTTYPE_WEIXIN + "").equals(params.getClientType());
			
			/*
			 * 校验银行卡是否绑定 
			 */
			if (StringUtils.isNotBlank(bankCardNo)) {
				bankCardNo = AES.getInstace().decrypt(bankCardNo);
				if (!bankCardNo.matches(BANK_CARD_NO_REG)) {
					throw new BusinessException(ResponseCode.PAYMENT_BANK_CARD_FORMAT_WRONG);
				}
				//支持银行
				if (!bankService.isSupportBank(bankCardNo)) {
					throw new BusinessException(ResponseCode.PAYMENT_NOT_SUPPORT_BANK);
				}
				//是否绑定银行卡
				if (InterfaceConst.BANK_CARD_STATUS_WRZ.equals(bankService.getBankCardBindStatus(userId))) { //是否绑定银行卡
					int isSuccess = bankCardDmService.bindBankcard(userId, bankCardNo); //首次充值进行绑定银行卡
					if (isSuccess < 1) {
						logger.info(String.format("用户ID为[%s]绑定银行卡失败", userId));
						throw new BusinessException(ResponseCode.FAILURE);
					}
					param.put("card_no", bankCardNo);
				} else {
					this.setPayFormPropertiesByUserId(param, userId);
				}
			} else {
				this.setPayFormPropertiesByUserId(param, userId);
			}
			//银行卡号不能为空
			if (StringUtils.isBlank(param.get("card_no"))) {
				custResponse.setCodeMessage(ResponseCode.PAYMENT_BANK_CARD_CANNOT_EMPTY);
				return custResponse;
			}
			//添加充值订单
			RechargeOrder order = rechargeMangeService.addOrder(new BigDecimal(amount),
					PaymentInstitution.LIANLIANGATE.getInstitutionCode(), userId, isBind);
			//获取用户账号
			T6110 t6110 = userPayInfoService.selectT6110(userId);
			//自然人
			if(t6110.F06 == T6110_F06.ZRR){
				//个人基础信息
				T6141 t6141 = userPayInfoService.selectT6141(userId);
				//通过
				if(t6141 == null || t6141.F04 != T6141_F04.TG){
					//这里需要客户端跳转到认证页面
					custResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH);
					return custResponse;
				}
				param.put("id_no",t6141.F07);
				param.put("acct_name", t6141.F02);
			}else{
				//企业基础信息
				T6161 t6161 = userPayInfoService.selectT6161(userId);
				if(t6161==null||StringHelper.isEmpty(t6161.F11)||StringHelper.isEmpty(t6161.F13)){
					custResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH
							);
					return custResponse;
				}
				param.put("id_no",t6161.F13);
				param.put("acct_name", t6161.F11);
			}
			
			String notifyUrl =  Config.get(Payment.NOTIFY_URL_RECHARGE); 
			param.put("notify_url", notifyUrl);
			param.put("dt_order",DateParser.format(order.orderTime, "yyyyMMddHHmmss"));
			param.put("name_goods", "充值"); 
			String money_order=order.amount.toString();
			Timestamp orderTime = orderService.getCreateTimeByOrderId(order.id);
			String no_order = OrderUtil.genLlpRechargeOrderId(order.id, orderTime); //这里的时间必须从数据再次获取！！！
			String user_id = OrderUtil.genLlpUserId(userId);
			//是否为第三方支付测试
			boolean isTest=BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST));
			if(isTest){
				money_order=Payment.get(Payment.RECHARGE_TEST_AMOUNT);
				no_order = OrderUtil.genLlpRechargeOrderId_Test(order.id, orderTime);
				user_id = OrderUtil.genLlpUserId_Test(userId);
			}
			param.put("no_order", no_order);
			param.put("money_order", money_order);
			param.put("risk_item", userPayInfoService.getRiskItem(userId));
			param.put("user_id", user_id);
			
			String oid_partner = Payment.get(Payment.OID_PARTNER);
			boolean isTestAccount = BooleanParser.parse(Payment.get(Payment.IS_ACCOUNT_TEST)); 
			if (isTestAccount) { //是否是测试账号
				oid_partner = Payment.get(Payment.OID_PARTNER_TEST);
			}
			param.put("oid_partner", oid_partner);
			param.put("sign_type", Payment.get(Payment.SIGN_TYPE));
			param.put("busi_partner", Payment.get(Payment.BUSI_PARTNER));
			//订单待提交
			rechargeOrderService.submit(order.id, null);
			//记录订单（充值、提现）与客户端类型的关系
			orderService.insertOrderIdAndClientType(order.id, params.getClientType());
			//对充值参数进行签名和封装转成json格式
			String req_data = null;
			if(isWap) {
				String url_return = Config.get(Payment.URL_RETURN);
				param.put("app_request", "3");
				param.put("bg_color", Payment.get(Payment.BG_COLOR));
				param.put("font_color", Payment.get(Payment.FONT_COLOR));
				if (StringUtils.isNotBlank(returnUri)) {
					url_return = Payment.get(Payment.RECHARGE_RETURN_BASE_URL).concat(returnUri);
				}
				param.put("url_return", url_return);
				req_data = this.getPayFormJsonForWAP(param);
			} else {
				req_data = this.getPayFormJsonForSDK(param);
			}
			String formUrl = Config.get(Payment.WAP_RECHARGE_FORMURL);
			
			req_data = AES.getInstace().encrypt(req_data);
			formUrl = AES.getInstace().encrypt(formUrl);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("req_data", req_data);
			data.put("url", formUrl);
			custResponse.setData(data);
		} catch(BusinessException busi) {
			logger.warn("recharge failrue,userId=[{}],bankCardNo=[{}],msg=[{}]", params.getUserId(),bankCardNo,busi.getMessage());
			custResponse.setCodeMessage(busi);
		} catch (Throwable e) {
			custResponse.setCodeMessage(ResponseCode.FAILURE);
			logger.error(String.format("recharge failrue,userId[%s],bankCardNo[%s]", params.getUserId(),bankCardNo), e);
		} finally {
			redisService.removeKey(requestCacheKey);
		}
		return custResponse;
	}

	/**
	 * 获取充值限额列表
	 * @param params
	 * @param bankCode
     * @return
     */
	@RequestMapping(value = "limit/list", method = RequestMethod.GET)
	public HttpResponse getLimitList(BaseRequestForm params, String bankCode, String channelCode) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>(1);
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		if (StringUtils.isBlank(bankCode)) {
			bankCode = null;
		}
		List<PaymentLimitVO> limitVOList = rechargeService.getLimitList(bankCode, channelCode);
		data.put("items", limitVOList);
		response.setData(data);
		return response;
	}
	
}
