package com.fenlibao.p2p.service.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dimeng.util.parser.DateParser;
import com.fenlibao.lianpay.v_1_0.enums.SignTypeEnum;
import com.fenlibao.lianpay.v_1_0.utils.YinTongUtil;
import com.fenlibao.lianpay.v_1_0.vo.BaseReturnParams;
import com.fenlibao.lianpay.v_1_0.vo.RefundCallBackVO;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.dao.trade.IRefundDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;
import com.fenlibao.p2p.model.entity.pay.RefundOrderEntity;
import com.fenlibao.p2p.model.enums.consumption.ConsumptionType;
import com.fenlibao.p2p.model.enums.pay.PaymentChannel;
import com.fenlibao.p2p.model.enums.pay.RefundStatus;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.Constant;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.pay.RefundVO;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.service.pay.ILlpQuickPayService;
import com.fenlibao.p2p.service.pay.IPaymentService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.HttpClientHandler;
import com.fenlibao.p2p.util.pay.OrderUtil;

@Service
public class LlpQuickPayServiceImpl implements ILlpQuickPayService {
	
	private static final Logger logger = LogManager.getLogger(LlpQuickPayServiceImpl.class);
	
	@Resource
	protected BankService bankService;
	@Resource
	private ILianLianPayService lianLianPayService;
	@Resource
	private IPaymentService paymentService;
	@Resource
	private IRefundDao refundDao;

	@Override
	public String getSignKey(String sign_type, boolean isAdd) {
		String isTest = Payment.get(Payment.IS_QUICK_TEST);// 是否为测试
		String key = "";
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY_QUICK));
		} else if (SignTypeEnum.RSA.getCode().equals(sign_type)) {
			if (isAdd) {
				key = AES.getInstace().decrypt(Payment.get(Payment.RSA_P_KEY));
			} else {
				key = AES.getInstace().decrypt(Payment.get(Payment.RSA_PB_KEY));
			}
		}
		if ("true".equals(isTest)) {
			key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY_QUICK_TEST));
		}
		return key;
	}

	@Override
	public String getRequestData(PaymentOrderEntity order, String clientType) throws Throwable {
		Integer userId = order.getUserId();
		String sign_type = Payment.get(Payment.SIGN_TYPE);
		Map<String, String> param = new HashMap<>();
		if (order == null || order.getId() < 1) {
			throw new BusinessException(ResponseCode.PAYMENT_ORDER_NOT_EXIST);
		}
		if (Constant.isWap(clientType)) {
			lianLianPayService.setParamsForWap(param);
		}
		param.put("sign_type", sign_type);
		UserInfo userInfo = lianLianPayService.setUserInfo(param, userId);
		setRiskItem(param, userInfo);
		setRequestData(order, param);
		setCardNo(param, userId);
		lianLianPayService.setParamsForTest(param);
		order.setSn(param.get("no_order")); //获取最终的 no_order进行保存
		return YinTongUtil.getPayReqData(param, getSignKey(sign_type, true));
	}
	
	/**
	 * 封装请求参数
	 * @param order
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	private void setRequestData(PaymentOrderEntity order, Map<String, String> param)
			throws Throwable {
		param.put("user_id", OrderUtil.genLlpUserId(order.getUserId()));//快捷和认证使用同一个user_id
		param.put("notify_url", Config.get(Payment.NOTIFY_URL_QUICK_PAYMENT));
		param.put("name_goods", ConsumptionType.SJCZ.getName());
		param.put("oid_partner", Payment.get(Payment.OID_PARTNER_QUICK));
		param.put("busi_partner", Payment.get(Payment.BUSI_PARTNER));
		param.put("dt_order", DateParser.format(order.getCreateTime(), "yyyyMMddHHmmss"));
		param.put("no_order", OrderUtil.genLLQuickSN(order.getId(), order.getCreateTime()));
		param.put("money_order", order.getAmount().toString());
		param.put("flag_modify", "1"); //已经实名认证，不允许用户修改开户信息
	}
	
	/**
	 * 设置风控参数
	 * @param param
	 * @param userInfo
	 */
	private void setRiskItem(Map<String, String> param, UserInfo userInfo) {
		Map<String,String> riskItem = new HashMap<String, String>();
		riskItem.put("frms_ware_category","1010");
		riskItem.put("user_info_mercht_userno", userInfo.getUserId());//用户在商户系统中的标识
		riskItem.put("user_info_bind_phone", userInfo.getPhone());//绑定手机号
		riskItem.put("user_info_dt_register", DateParser.format(userInfo.getRegisterTime(),"yyyyMMddHHmmss"));//注册时间
		riskItem.put("frms_charge_phone", userInfo.getPhone());
		param.put("risk_item", JSON.toJSONString(riskItem));
	}
	
	private void setCardNo(Map<String, String> param, Integer userId) throws Throwable {
		String cardNo = bankService.getCardNo(userId);
		if (StringUtils.isNotBlank(cardNo)) {
			param.put("card_no", cardNo); //银行卡号
		} else {
			throw new BusinessException(ResponseCode.PAYMENT_UNBOUND_BANK_CARD);
		}
	}

	@Transactional
	@Override
	public Map<String, String> getRequestData(String amount, Integer userId, String clientType) throws Throwable {
		Map<String, String> data = new HashMap<>(3);
		PaymentOrderEntity order = paymentService.addOrder(amount, PaymentChannel.LLP_QUICK.getCode(), userId);
		order = paymentService.lockOrder(order.getId()); //主要是为了获取创建时间。同一个事务，没必要再判断订单的状态
		String req_data = getRequestData(order, clientType);
		logger.debug("req_data >>> " + req_data);
		data.put("req_data", AES.getInstace().encrypt(req_data));
		data.put("url", Config.get(Payment.WAP_QUICK_PAYMENT_FORMURL));
		data.put("paymentOrderId", order.getId().toString());
		paymentService.submit(order); //提交
		return data;
	}

	@Override
	public List<RefundVO> getWaitRefundOrder() {
		return refundDao.getWaitRefundOrder();
	}


	@Transactional
	@Override
	public void submitRefund(RefundVO vo, Map<String, String> params, String key, boolean isTest) throws Exception {
		RefundOrderEntity order = refundDao.lockOrder(vo.getId());
		if (order != null && order.getStatus() == RefundStatus.DTK) {
			String no_refund = OrderUtil.genSerialNumber(OrderUtil.REFUND_PREFIX, vo.getId(), vo.getCreateTime());
			if (isTest) {
				no_refund = OrderUtil.getLLP_no_order_test(no_refund);
			}
			params.put("no_refund", no_refund);
			params.put("dt_refund", OrderUtil.getDtOrder(vo.getCreateTime()));
			params.put("money_refund", vo.getAmount().toString());
			params.put("no_order", vo.getPaymentOrderSn());
			YinTongUtil.putSign(params, key);
			String result = HttpClientHandler.doPostJson(params, Config.get(Payment.LLP_REFUND_URL));
			if (YinTongUtil.checkSign(result, key)) {
				BaseReturnParams baseParams = YinTongUtil.getBaseReturnParams(result);
				if (BaseReturnParams.SUCCESS_CODE.equals(baseParams.getRet_code())) {
					order.setSn(no_refund);
					updateRefundStatus(order);
				} else {
					logger.warn("提交退款失败,RefundVO[{}],连连返回[{}]", vo.toString(), result);
					//TODO 对返回特定的状态码进行处理，订单状态改变
				}
			} else {
				logger.warn("退款申请结果返回签名校验失败,RefundVO[{}],result[{}]", vo.toString(), result);
			}
		} else {
			logger.warn("没有提交退款,RefundVO[{}],orderStatus[{}]", vo.toString(), order.getStatus());
		}
	}
	
	private void updateRefundStatus(RefundOrderEntity order) throws Exception {
		order.setSubmitTime(new Date());
		order.setStatus(RefundStatus.TKZ);
		refundDao.updateOrder(order);
	}

	@Transactional
	@Override
	public void confirmRefund(RefundCallBackVO vo) throws Exception {
		Integer orderId = OrderUtil.getOrderIdByNoOrder(vo.getNo_refund());
		RefundOrderEntity order = refundDao.lockOrder(orderId);
		RefundStatus status = RefundStatus.SB;
		if (order != null && RefundStatus.TKZ == order.getStatus()) {
			if (RefundCallBackVO.sta_refund_SUCCESS.equals(vo.getSta_refund())) {
				status = RefundStatus.CG;
			}
			order.setStatus(status);
			order.setFinishedTime(new Date());
			refundDao.updateOrder(order);
		} else {
			logger.warn("RefundCallBackVO[{}]退款回调不处理,order_status[{}]", 
					vo.toString(), order.getStatus());
		}
	}
}
