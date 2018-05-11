package com.fenlibao.p2p.controller.noversion.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.lianpay.v_1_0.utils.YinTongUtil;
import com.fenlibao.lianpay.v_1_0.vo.CallbackResponse;
import com.fenlibao.lianpay.v_1_0.vo.PayDataBean;
import com.fenlibao.lianpay.v_1_0.vo.RefundCallBackVO;
import com.fenlibao.p2p.dao.consumption.IConsumptionDao;
import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.service.consumption.ConsumptionService;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.service.pay.ILlpQuickPayService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.withdraw.IAipgBusManageService;
import com.fenlibao.p2p.service.withdraw.IAipgWithdrawService;
import com.fenlibao.p2p.util.pay.DigestUtil;
import com.fenlibao.p2p.util.pay.OrderUtil;

/**
 * 连连支付结果回调
 * 
 * @author yangzengcai
 * @date 2015年8月26日
 */
@RestController
@RequestMapping("/notify")
public class LianPayNotifyController extends AbstractLianLianPayController {

	@Resource
	private IRechargeService rechargeService;
	@Resource
	private IAipgWithdrawService aipgWithdrawService;
    @Resource
    private IAipgBusManageService aipgBusManageService;
    @Resource
    private ILianLianPayService lianlianService;
    @Resource
    private ILlpQuickPayService quickPayService;
    @Resource
    private ITopUpService topupService;
	@Resource
	private ConsumptionService consumptionService;

	/**
	 * 充值回调
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "lianlian/recharge")
	public void recharge(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("+++++++++++++ recharge result call back success +++++++++++++++++++++++++");
		Map<String, String> params = new HashMap<String, String>();
		PrintWriter writer = null;
		InputStream in = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			in = request.getInputStream();
			params.putAll(this.readReqStr(in));
			if (DigestUtil.checkReturnSign(params)) {
				String Succeed = params.get("result_pay");
				String TransID = params.get("no_order");
				logger.info(String.format("no_order:[%s] result is %s", TransID, Succeed));
				if ("SUCCESS".equals(Succeed) && TransID.indexOf("_") != -1) {
					int userId = this.sucessSave(TransID, params);// 保存数据
					writer = response.getWriter();
					this.stopNotify(response, writer);
					logger.debug(String.format("+++++ no_order:[%s] recharge success +++++++", TransID));
					// 在回调成功后再来保存相关信息
					if (userId > 0) {
						this.rechargeService.perfectPayInfo(params, userId);
					}
				} else {
					logger.info(String.format("no_order:[%s] charge fail ", TransID));
				}
			} else {
				logger.warn("sign error, callback params >>> {}", params.toString());
			}
		} catch (Throwable e) {
			logger.error(e.toString(), e);
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 提现回调
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "lianlian/withdraw")
	public void withdraw(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("+++++++++++++ withdraw result call back success +++++++++++++++++++++++++");
		BufferedReader br = null;
		InputStreamReader inR = null;
		PrintWriter writer = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			// 接收信息
			inR = new InputStreamReader(request.getInputStream());
			br = new BufferedReader(inR);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// 将资料解码
			String reqBody = sb.toString();
			if (reqBody == null || "".equals(reqBody)) {
				String ip = request.getRemoteAddr();
				logger.info(String.format("代付请求null，IP：%s", ip));
				response.getWriter().write("连连异步请求·····");
				response.getWriter().close();
				return;
			}
			logger.debug(String.format("连连代付异步返回信息：%s", reqBody));
			String jsonDataString = URLDecoder.decode(reqBody, "UTF-8");
			// 转成map
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> retParams = mapper.readValue(jsonDataString,
					Map.class);

			// 成功       
            if (retParams.size() > 3) {
                // 订单号
                if (DigestUtil.checkReturnSign(retParams)) {
                    logger.debug("验签通过");
                    String result_pay = retParams.get("result_pay");
                    String no_order = retParams.get("no_order");
                    logger.info(String.format("no_order:[%s] result is %s", no_order, result_pay));
                    if ("SUCCESS".equals(result_pay)) {
                        // 交易流水号
                        Map<String, String> params = new HashMap<>();
                        params.put("oid_paybill", retParams.get("oid_paybill"));
                        params.put("money_order", retParams.get("money_order"));
                        int orderId = OrderUtil.getOrderIdByNoOrder(no_order);
                        // 结果处理
                        aipgWithdrawService.confirmForPay(orderId, params);
                        writer = response.getWriter();
                        logger.info(String.format("no_order:[%s]交易成功（最终结果）", no_order));
                        this.stopNotify(response, writer);
                        //更新银行卡绑定状态
                        rechargeService.updateBindStatus(orderId);
                    } else {
                    	logger.info(String.format("no_order:[%s]交易失败（最终结果），原因：%s", no_order, retParams.get("ret_msg")));
                        aipgBusManageService.withdrawFail(OrderUtil.getOrderIdByNoOrder(no_order), "代付失败");
                        this.stopNotify(response, writer);
                    }
                } else {
                	logger.warn("sign error, callback params >>> {}", retParams.toString());
                }
            } else {
                logger.info("交易失败（最终结果）");
                logger.info(String.format("原因：%s", retParams.get("ret_msg")));
            }
		} catch (Throwable e) {
			logger.error(e.toString(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inR != null) {
				try {
					inR.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 向连连支付发送成功状态，停止通知
	 * 
	 * @param response
	 * @param writer
	 * @throws JsonProcessingException
	 */
	private void stopNotify(HttpServletResponse response, PrintWriter writer)
			throws JsonProcessingException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		Map<String, String> param = new HashMap<String, String>();
		param.put("ret_code", "0000");
		param.put("ret_msg", "交易成功");
		ObjectMapper objMap = new ObjectMapper();
		writer.write(objMap.writeValueAsString(param));
	}
	
	/**
	 * 快捷支付异步通知处理
	 * @param req
	 * @param resp
	 */
	@RequestMapping("lianlian/quick")
	public CallbackResponse quickPay(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("+++++++++++++ lianlian quickPay call back success +++++++++++++++++++++++++");
		resp.setCharacterEncoding("UTF-8");
		CallbackResponse response = new CallbackResponse();
		try {
			String reqStr = validate(req);
	        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
	        if (YinTongUtil.isTradeSuccess(payDataBean)) {
	        	PaymentOrderEntity order = lianlianService.callbackConfirm(payDataBean);
	        	doConfirm(order);
	        }
		} catch (BusinessException busi) {
			response.failure(busi.getMessage());
		} catch (Exception e) {
			response.failure("处理失败");
			logger.error("快捷支付回调处理失败,报文[{}]", YinTongUtil.readReqStr(req));
			logger.error("快捷支付回调处理失败", e);
		}
		return response;
	}
	
	/**
	 * 快捷支付退款异步通知处理
	 * @param req
	 * @param resp
	 */
	@RequestMapping("lianlian/quick/refund")
	public CallbackResponse quickPayRefund(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("+++++++++++++ lianlian quickPay refund call back success +++++++++++++++++++++++++");
		resp.setCharacterEncoding("UTF-8");
		CallbackResponse response = new CallbackResponse();
		try {
			String reqStr = validate(req);
			RefundCallBackVO vo = JSON.parseObject(reqStr, RefundCallBackVO.class);
			quickPayService.confirmRefund(vo);
		} catch (BusinessException busi) {
			response.failure(busi.getMessage());
		} catch (Exception e) {
			response.failure("处理失败");
			logger.error("快捷支付退款回调处理失败,报文[{}]", YinTongUtil.readReqStr(req));
			logger.error("快捷支付退款回调处理失败", e);
		}
		return response;
	}
	
	/**
	 * 回调处理
	 * @param req
	 * @return 请求报文
	 * @throws Exception
	 */
	private String validate(HttpServletRequest req) throws Exception {
		String reqStr = YinTongUtil.readReqStr(req);
		if (YinTongUtil.isnull(reqStr)) {
			String ip = YinTongUtil.getIpAddr(req); //异常时记录ip
			logger.warn("快捷支付回调参数为空,IP[{}]", ip);
			throw new BusinessException("接收的数据为空");
		}
		String sign_type = YinTongUtil.getSignType(reqStr);
		String key = quickPayService.getSignKey(sign_type, false);
		if (!YinTongUtil.checkSign(reqStr, key)) {
			String ip = YinTongUtil.getIpAddr(req); //异常时记录ip
			logger.warn("快捷支付回调签名验证不通过,报文[{}],IP[{}]", reqStr, ip);
			throw new BusinessException("签名验证不通过");
		}
		return reqStr;
	}
	
	/**
	 * 处理具体的业务
	 * TODO 当一个支付订单对应多个消费订单时，需要根据消费类型进行相应的业务处理。
	 * <p>定义统一的处理接口，不同业务实现
	 * @param order
	 */
	private void doConfirm(PaymentOrderEntity order) {
		if (order != null) {
			try {
				ConsumptionOrderEntity consumptionOrder = consumptionService.getOrderByPaymentId(order.getId());
				topupService.yishangTopUp(consumptionOrder);
			} catch (Exception e) {
				logger.error("手机充值失败 , paymentOrderId["+order.getId()+"]", e);
			}
		}
	}
}
