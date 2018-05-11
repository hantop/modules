package com.fenlibao.p2p.service.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.lianpay.v_1_0.enums.AppRequestEnum;
import com.fenlibao.lianpay.v_1_0.vo.PayDataBean;
import com.fenlibao.p2p.dao.UserBaseInfoDao;
import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;
import com.fenlibao.p2p.model.enums.pay.PaymentOrderStatus;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.user.EnterpriseBaseInfoVO;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.service.pay.IPaymentService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.DigestUtil;
import com.fenlibao.p2p.util.pay.HttpClientHandler;
import com.fenlibao.p2p.util.pay.OrderUtil;

@Service
public class LianLianPayServiceImpl implements ILianLianPayService {

	private static final Logger logger = LogManager.getLogger(LianLianPayServiceImpl.class);
	
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private UserBaseInfoDao userBaseInfoDao;
	@Resource
	private IPaymentService paymentService;

	@Override
	public String queryBankCardByNo(String bankCardNo) throws Throwable {
		Map<String, String> params = new HashMap<>(4);
		String oid_partner = Payment.get(Payment.OID_PARTNER);
		boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST));
		if (isTest) {
			oid_partner = Payment.get(Payment.OID_PARTNER_TEST);
		}
		params.put("oid_partner", oid_partner);
		params.put("sign_type", "MD5");
		params.put("card_no", bankCardNo);
		String sign = DigestUtil.createSendSign(params);
		params.put("sign", sign);

		return HttpClientHandler.doPostJson(params, "https://traderapi.lianlianpay.com/bankcardquery.htm");
	}

	/**
	 * wap所需参数
	 * @param param
	 * @param clientType
	 * @return
	 */
	@Override
	public void setParamsForWap(Map<String, String> param) throws Exception {
		param.put("app_request", AppRequestEnum.WAP.getCode().toString());
		param.put("bg_color", Payment.get(Payment.BG_COLOR));
		param.put("font_color", Payment.get(Payment.FONT_COLOR));
		param.put("url_return", Config.get(Payment.URL_RETURN_QUICK_PAYMENT));
	}
	
	/**
	 * 测试模式（需放最后）
	 * @param param
	 * @return
	 */
	@Override
	public void setParamsForTest(Map<String, String> param) {
		boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_QUICK_TEST));// 是否为测试
		if (isTest) {
			param.put("no_order", OrderUtil.getLLP_no_order_test(param.get("no_order")));
			param.put("user_id", OrderUtil.getLLP_user_id_test(param.get("user_id")));
			param.put("money_order", Payment.get(Payment.RECHARGE_TEST_AMOUNT));
			param.put("oid_partner", Payment.get(Payment.OID_PARTNER_QUICK_TEST));
		}
	}

	/**
	 * 设置用户信息
	 * @param param
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	@Override
	public UserInfo setUserInfo(Map<String, String> param, Integer userId) throws Throwable {
		Map<String, Object> user = new HashMap<>();
		user.put("userId", userId);
		UserInfo userInfo = userInfoDao.getUserInfoByPhoneNumOrUsername(user);
		if (T6110_F06.ZRR.name().equals(userInfo.getUserType())) {
			if (!T6141_F04.TG.name().equals(userInfo.getAuthStatus())) {
				throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH);
			}
			param.put("id_no", StringHelper.decode(userInfo.getIdCardEncrypt()));
			param.put("acct_name", userInfo.getFullName());
		} else {
			EnterpriseBaseInfoVO info = userBaseInfoDao.getEnterpriseBaseInfo(userId);
			if (info == null || !StringUtils.isNoneBlank(info.getCorporate(), info.getIdCardNo())) {
				throw new BusinessException(ResponseCode.USER_IDENTITY_UNAUTH);
			}
			param.put("id_no", StringHelper.decode(info.getIdCardNo()));
			param.put("acct_name", info.getCorporate());
		}
		return userInfo;
	}

	@Transactional
	@Override
	public PaymentOrderEntity callbackConfirm(PayDataBean payDataBean) throws Exception {
		Integer orderId = OrderUtil.getOrderIdByNoOrder(payDataBean.getNo_order());
		PaymentOrderEntity order = paymentService.lockOrder(orderId);
		if (order != null && PaymentOrderStatus.DQR == order.getStatus()) {
			order.setFinishedTime(new Date());
			order.setStatus(PaymentOrderStatus.CG);
			paymentService.updateOrder(order);
			//只有操作成功才返回order进行充值。因为充值模块在mp，mp已经依赖了api，api不再依赖mp，所以返回order在Controller操作
			//防止多次通知多次充值
			return order; 
		} else {
			logger.info("连连快捷支付回调不处理,no_order[{}],order_status[{}]", 
					payDataBean.getNo_order(), order.getStatus());
		}
		return null;
	}
	
}
