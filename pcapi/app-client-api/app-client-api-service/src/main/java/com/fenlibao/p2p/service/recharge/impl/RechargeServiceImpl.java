package com.fenlibao.p2p.service.recharge.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysPaymentInstitution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.dao.trade.IRechargeDao;
import com.fenlibao.p2p.model.entity.pay.BranchInfo;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.recharge.IRechargeOrderService;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.DigestUtil;
import com.fenlibao.p2p.util.pay.HttpClientHandler;
import com.fenlibao.p2p.util.pay.OrderUtil;

@Service
public class RechargeServiceImpl extends BaseAbstractService implements
		IRechargeService {

	@Resource
	private IRechargeDao rechargeDao;
	@Resource
	private IRechargeOrderService rechargeOrderService;
	@Resource
	protected BankService bankService;
	@Resource
	protected ITradeService tradeService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IWithdrawService withdrawService;

	/**
	 * 在连连获取充值结果
	 * 
	 * @param order
	 * @return
	 * @throws Throwable
	 */
	private String queryLianlianResult(RechargeOrder order) throws Throwable {
		// 构建信息集合
		Map<String, String> requestParam = getRequestParams(order);
		// 签名
		String sign = DigestUtil.createSendSign(requestParam);
		// 签名(必)
		requestParam.put("sign", sign);
		// 请求地址

		String actionUrl = Config.get(Payment.ORDER_RESULT_QUERY_URL);
		// 发送请求拿到同步返回结果字符串
		String retString = HttpClientHandler
				.doPostJson(requestParam, actionUrl);

		return retString;
	}

	/**
	 * 代付查询（提现）请求信息集合
	 * 
	 * @param requestEntity
	 *            请求实体类
	 * @return 返回map集合
	 */
	private Map<String, String> getRequestParams(RechargeOrder order) {
		// 请求参数
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 商户编号(必)
		params.put("oid_partner", Payment.get(Payment.OID_PARTNER));
		// 签名方式
		params.put("sign_type", Payment.get(Payment.SIGN_TYPE));
		// 是否为第三方支付测试
		String no_order = OrderUtil.genLlpRechargeOrderId(order.getId(),
				order.getCreateTime());
		boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST));
		if (isTest) {
			no_order = OrderUtil.genLlpRechargeOrderId_Test(order.getId(),
					order.getCreateTime());
		}
		// 订单号
		params.put("no_order", no_order);
		// 商户时间(格式: YYYYMMDDH24MISS)
		params.put("dt_order",
				DateParser.format(order.getCreateTime(), "yyyyMMddHHmmss"));
		// 连连支付支付单号
		params.put("oid_paybill", "");
		// 收付标识
		params.put("type_dc", "");
		// 查询版本号
		params.put("query_version", "");

		return params;
	}

	@Override
	public List<RechargeOrder> getDQROrder() {
		return rechargeDao.getDQROrder();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void queryResult(List<RechargeOrder> list) throws Throwable {
		String resultMsg = "";
		ObjectMapper objm = null;
		Map<String, String> retParams = null; // 返回参数集合
		String result_pay = ""; // 支付结果
		Map<String, String> params = null;
		Timestamp orderTime = null;
		int orderId = 0;
		for (RechargeOrder order : list) {
			try { //这里try是防止中断循环
				orderTime = order.getCreateTime();
				orderId = order.getId();
				resultMsg = this.queryLianlianResult(order);
				logger.info(String.format("连连充值对账返回信息：%s", resultMsg));
				objm = new ObjectMapper();
				retParams = objm.readValue(resultMsg, Map.class);
				if (retParams.size() > 2) {
					if (DigestUtil.checkReturnSign(retParams)) {
						if ("0000".equals(retParams.get("ret_code"))) {
							result_pay = retParams.get("result_pay");

							if ("SUCCESS".equals(result_pay)) {
								// 连连主动通知30次，每两分钟一次，为了不和主动通知冲突，定时器的操作，需要判断时间，避开？
								// confirm()里有对DQR订单(T6501)进行上锁,这样应该不会有问题
								// 结果处理
								if (DateUtil.getDifferenceMin(orderTime
										.getTime()) > 10) { // 先让连连主动通知5次
									params = new HashMap<>();
									params.put("paymentOrderId",
											String.valueOf(orderId));
									params.put("amount",
											retParams.get("money_order"));

									rechargeOrderService.confirmForPay(orderId,
											params);
									logger.info(String.format(
											"ID为[%s]的订单交易成功（最终结果）", orderId));
									this.perfectPayInfo(retParams, order.getUserId());
								}
							} else if ("PROCESSING".equals(result_pay)) {
								// 银行代付处理中
								logger.info(String.format(
										"ID为[%s]的订单处于银行代付处理中,等待查询对账", orderId));
							} else {
								if (DateUtil.getDifferenceMin(orderTime.getTime()) > 60) {
									if ("WAITING".equals(result_pay)) {
										this.updateOrderStatus(orderId, InterfaceConst.ORDER_STATUS_WAITING);
										logger.info(String.format(
													"ID为[%s]的订单交易失败（最终结果），支付结果：%s",
													orderId, result_pay));
									} else if ("REFUND".equals(result_pay)) {
										this.updateOrderStatus(orderId, InterfaceConst.ORDER_STATUS_REFUND);
										logger.info(String.format(
													"ID为[%s]的订单交易失败（最终结果），支付结果：%s",
													orderId, result_pay));
									} else {
										this.updateOrderStatus(orderId, InterfaceConst.ORDER_STATUS_SB);
										logger.info(String.format(
													"ID为[%s]的订单交易失败（最终结果），支付结果：%s",
													orderId, result_pay));
									}
								}
							}
						}

					} else {
						if (DateUtil.getDifferenceMin(orderTime.getTime()) > 10080) { //7天
							this.updateOrderStatus(orderId, InterfaceConst.ORDER_STATUS_SB);
							logger.info(String.format("ID为[%s]的订单定时器验签失败",
									orderId));
						}
					}
				} else {
					// 这里要判断订单的时间
					// 否则用户在提交充值表单时，刚好定时器也去查询，结果是没有记录的，这样就大问题了
					if (DateUtil.getDifferenceMin(orderTime.getTime()) > 10080) { //7天
						if ("8901".equals(retParams.get("ret_code"))) { // 8901在连连没有记录
							this.updateOrderStatus(orderId, InterfaceConst.ORDER_STATUS_MJL);
							logger.info(String.format("ID为[%s]的订单在连连没有记录",
									orderId));
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
			Thread.sleep(500);
		}

	}
	
	@Transactional
	@Override
	public int updateOrderStatus(int orderId, String status)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("status", status);
		params.put("completeTime", new Date());
		return this.rechargeDao.updateOrderStatus(params);
	}

	@Override
	public void perfectPayInfo(Map<String, String> params, int userId)
			throws Exception {
		String accountName = params.get("acct_name"); // 开户名
		String bankCode = params.get("bank_code"); // 银行编码
		String noAgree = params.get("no_agree"); // 协议号

		String status = bankService.getBankCardBindStatus(userId);
		if (InterfaceConst.BANK_CARD_STATUS_WRZ.equals(status)) {
			String bindStatus = InterfaceConst.BANK_CARD_STATUS_YRZ;
			if (this.validateBankCode(bankCode)) {
				bindStatus = InterfaceConst.BANK_CARD_STATUS_KTX;
			}
			// 更新用户银行卡信息
			BankCardVO bankInfo = new BankCardVO();
			bankInfo.setAcount(userId);
			bankInfo.setAccountName(accountName);
			bankInfo.setBindStatus(bindStatus);
			bankInfo.setBankID(bankService.getIdByCode(bankCode));
			bankInfo.setStatus(T6114_F08.QY.name());
			bankService.updateBankCardInfo(bankInfo);
		} else if (InterfaceConst.BANK_CARD_STATUS_YRZ.equals(status)
				&& this.validateBankCode(bankCode)) {
			// 更新用户银行卡信息
			BankCardVO bankInfo = new BankCardVO();
			bankInfo.setAcount(userId);
			bankInfo.setBindStatus(InterfaceConst.BANK_CARD_STATUS_KTX);
			bankService.updateBankCardInfo(bankInfo);
		}

		// 保存协议号
		if (StringUtils.isNotBlank(noAgree)) {
			tradeService.saveNoAgree(userId, noAgree);
		}
	}

	/**
	 * 判断银行卡提现是否需要支行信息
	 * 
	 * @param bankCode
	 * @return
	 */
	private boolean validateBankCode(String bankCode) {
		String[] bankCodes = InterfaceConst.BANK_CODES;
		for (int i = 0; i < bankCodes.length; i++) {
			if (bankCodes[i].equals(bankCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateBindStatus(int orderId) throws Exception {
		// 更新银行卡为可提现（KTX）状态
		Integer userId = orderService.getUserIdByOrderId(orderId);
		if (userId != null) {
			String status = bankService.getBankCardBindStatus(userId);
			if (!InterfaceConst.BANK_CARD_STATUS_KTX.equals(status)) {
				BranchInfo info = withdrawService.getBranchInfoByOrderId(orderId); //获取相对应的提现成功的订单的支行信息进行保存
				BankCardVO bankInfo = new BankCardVO();
				bankInfo.setAcount(userId);
				bankInfo.setBindStatus(InterfaceConst.BANK_CARD_STATUS_KTX);
				bankInfo.setBankKhhName(info.getBranchName());
                bankInfo.setCity(info.getCityCode());
				bankService.updateBankCardInfo(bankInfo);
			}
		}
	}

	@Override
	public BigDecimal getOfflineRechargeAmount(int userId, int hours) {
		return rechargeDao.getOfflineRechargeAmount(userId, hours);
	}

	@Override
	public List<PaymentLimitVO> getLimitList(String bankCode, String channelCode) {
		if ((SysPaymentInstitution.XW.getChannelCode() + "").equals(channelCode)) {
			return rechargeDao.getCgLimit(bankCode);
		}
		return rechargeDao.getLimitList(bankCode, channelCode);
	}

}
