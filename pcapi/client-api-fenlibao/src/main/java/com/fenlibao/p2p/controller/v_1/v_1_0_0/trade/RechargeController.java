package com.fenlibao.p2p.controller.v_1.v_1_0_0.trade;

import com.dimeng.p2p.PaymentInstitution;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.controller.noversion.pay.AbstractLianLianPayController;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import com.fenlibao.p2p.service.pay.LlpAuthPayService;
import com.fenlibao.p2p.service.recharge.IRechargeMangeService;
import com.fenlibao.p2p.service.recharge.IRechargeOrderService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 充值控制器
 * @author yangzengcai
 * @date 2015年8月17日
 */
@RestController("v_1_0_0/RechargeController")
@RequestMapping("lianlianPay")
public class RechargeController extends AbstractLianLianPayController {

	@Resource
	private IRechargeMangeService rechargeMangeService;
	@Resource
	private IRechargeOrderService rechargeOrderService;
	@Resource
	private IOrderService orderService;	
	@Resource
	private LlpAuthPayService authPayService;
	@Resource
	private IRechargeService rechargeService;
	
	
	/**
	 * web认证支付
	 * @param params
	 * @param amount
	 * @param bankCardNo
	 * @return
	 */
	@RequestMapping(value = "recharge/web", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
	public HttpResponse web(BaseRequestFormExtend params, String returnUrl,
			String amount, String bankCardNo) {
		HttpResponse response = new HttpResponse();
		Map<String, String> req_data = new HashMap<>();
		if (!params.validate() || !StringUtils.isNoneBlank(amount, returnUrl)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			Map<String, Object> data = new HashMap<>(2);
			Integer userId = params.getUserId();
			if (StringUtils.isNotBlank(bankCardNo)) {
				bankCardNo = AES.getInstace().decrypt2(bankCardNo);
			}
			boolean isBind = authPayService.putBankCardNo(req_data, userId, bankCardNo);
			
			RechargeOrder order = rechargeMangeService.addOrder(new BigDecimal(amount),
					PaymentInstitution.LIANLIANGATE.getInstitutionCode(), userId, isBind);
			
			authPayService.setRequestData(req_data, returnUrl, order);
			rechargeOrderService.submit(order.getId(), null);
			orderService.insertOrderIdAndClientType(order.getId(), params.getClientType());
			
			data.put("req_data", req_data);
			data.put("req_url", Config.get(Payment.WEB_RECHARGE_FORMURL)); //web URL
			response.setData(data);
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
			logger.warn("userId[{}],bankCardNo[{}],amount[{}] top up failure.msg[{}]", params.getUserId(), bankCardNo, amount, busi.getMessage());
		} catch (Throwable e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("userId[{}],bankCardNo[{}],amount[{}] top up failure", params.getUserId(), bankCardNo, amount);
			logger.error(e.getMessage(), e);
		}
		return response;
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
		List<PaymentLimitVO> limitVOList = rechargeService.getLimitList(bankCode,channelCode);
		data.put("items", limitVOList);
		response.setData(data);
		return response;
	}
}
