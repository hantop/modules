package com.fenlibao.p2p.service.payment.tp.baofoo.impl;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.payment.tp.common.ThirdpartyCommonDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.RechargeManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.StateFailCodeRZZF;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.StateUnknownCodeRZZF;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.TxnSubType;
import com.fenlibao.p2p.model.payment.tp.common.entity.PayExtend;
import com.fenlibao.p2p.model.trade.entity.CapitalFlow;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6502;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooRechargeService;
import com.fenlibao.p2p.service.trade.order.OrderManageService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.payment.tp.baofoo.BaoFooUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class BaofooRechargeServiceImpl implements BaofooRechargeService {

    private static final Logger log = LogManager.getLogger(BaofooRechargeServiceImpl.class);

    @Resource
    private OrderManageDao orderManageDao;
    @Resource
    private RechargeManageDao rechargeManageDao;
    @Resource
    private ThirdpartyCommonDao thirdpartyCommonDao;
    @Resource
    private UserDao userDao;
    @Resource
    private OrderManageService orderManageService;
    @Resource
    private TradeCommonDao tradeCommonDao;

    @Transactional
    @Override
    public String pre(int orderId, String serialNum, String userIp) throws Exception {
        T6501 order;
        if (StringUtils.isNotBlank(serialNum)) {
            order = orderManageDao.getBySerialNum(serialNum);
            if (order == null) {
                throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
            }
            orderId = order.F01;
        } else {
            order = orderManageDao.get(orderId);
        }
        T6502 rechargeOrder = rechargeManageDao.getOrder(orderId);
        Map<String, Object> busiparams = getPreParams(order, rechargeOrder.F03, userIp);
        Map<String, Object> reqParams = BaoFooUtil.getReqParams(TxnSubType.PRE_TOPUP, busiparams);
        Map<String, String> httpResult = BaoFooUtil.sendRequest(reqParams);
        if (httpResult == null || !"0000".equals(httpResult.get("resp_code"))) {
            if (httpResult != null) {
                log.error("宝付预充值失败，serialNum = [{}]，httpResult=[{}]",serialNum, httpResult.toString());
                throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),httpResult.get("resp_msg"));
            }
            log.error("宝付预充值失败，宝付返回空，serialNum = [{}]",serialNum);
            throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),"宝付应答消息异常，请联系客服400-930-5559核查充值状态");
        }
        //一个business_no对应一个验证码
        String baoFooBusiNo = httpResult.get("business_no");
        if (StringUtils.isBlank(baoFooBusiNo)) {
            log.error("baofoo business_no is null，serialNum = [{}]",serialNum);
            throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),"宝付应答消息异常，请联系客服400-930-5559核查充值状态");
        }
        rechargeManageDao.updateOrder(orderId, baoFooBusiNo);
        return order.F10;
    }

    @Override
    public BigDecimal confirm(String serialNum, String captcha) throws Exception {
        T6501 order = orderManageDao.getBySerialNum(serialNum);
        if (order == null) {
            log.error("宝付确认充值失败，订单找不到 serialNum = [{}]", serialNum);
            throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
        }
        if (!T6501_F03.DTJ.equals(order.F03)) {
            throw new TradeException(TradeResponseCode.ORDER_STATUS_WRONG);
        }
        //将订单状态改为待确认
        T6501 updateOrder = new T6501();
        updateOrder.F01 = order.F01;
        updateOrder.F03 = T6501_F03.DQR;
        updateOrder.F05 = new Timestamp((new Date()).getTime());
        orderManageDao.update(updateOrder);
        
        T6502 rechargeOrder = rechargeManageDao.getOrder(order.F01);
        Map<String, Object> busiParams = getConfirmParams(rechargeOrder.F08, captcha, new Date(order.F04.getTime()));
        Map<String, Object> reqParams = BaoFooUtil.getReqParams(TxnSubType.CONFIRM_TOPOP, busiParams);
        Map<String, String> httpResult = BaoFooUtil.sendRequest(reqParams);
        if (httpResult == null) {
        	log.error("宝付确认充值失败，返回报文为空，serialNum=[{}]",serialNum);
            throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),"宝付应答消息异常，请联系客服400-930-5559核查充值状态");
        }
        if(StateUnknownCodeRZZF.contain(httpResult.get("resp_code"))){
            log.error("宝付确认充值状态未知，serialNum=[{}]，resp_code=[{}]，resp_msg=[{}]",serialNum,httpResult.get("resp_code"),httpResult.get("resp_msg"));
            throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),httpResult.get("resp_msg"));    
        }
        if(StateFailCodeRZZF.contain(httpResult.get("resp_code"))){
        	orderManageService.complete(order.F01, T6501_F03.SB);
        	log.error("宝付确认充值失败，serialNum=[{}]，httpResult=[{}]",serialNum, httpResult.get("resp_msg"));
        	throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),httpResult.get("resp_msg"));    
        }
        
        String successAmount = httpResult.get("succ_amt");
        if (StringUtils.isBlank(successAmount)
                || rechargeOrder.F03.multiply(new BigDecimal("100")).compareTo(new BigDecimal(successAmount)) != 0) {//单位为分
            log.error("宝付确认充值失败，金额不正确：serialNum=[{}]，successAmount=[{}]，orderAmount=[{}]", serialNum,successAmount, rechargeOrder.F03);
            throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL.getCode(),"交易状态异常，请联系客服400-930-5559");
        }
        operateFunds(order.F08, rechargeOrder.F03, order.F01);
        return rechargeOrder.F03;
    }

    /**
     * 获取预充值参数
     * @param order
     * @param amount
     * @param userIp
     * @return
     */
    private Map<String, Object> getPreParams(T6501 order, BigDecimal amount, String userIp) {
        Map<String, Object> params = BaoFooUtil.getBaseParams(TxnSubType.PRE_TOPUP);
        Map<String, Object> riskContent = new HashMap<>(1);
        PayExtend bindInfo = thirdpartyCommonDao.getPayExtend(order.F08);
        if (bindInfo == null || StringUtils.isBlank(bindInfo.getBaofooBindId())) {
        	log.error("用户尚未绑定银行卡，serialNum=[{}]",order.F10);
            throw new TradeException(TradeResponseCode.PAYMENT_UNBOUND_BANK_CARD);
        }
        riskContent.put("client_ip", userIp);
        params.put("trans_id", order.F10);//商户订单号
        if(BaoFooUtil.CONFIG.isTest()){
        	params.put("bind_id", "201604271949318660");
        }
        else{
        	params.put("bind_id", bindInfo.getBaofooBindId());//绑定标识号
        }
        params.put("txn_amt", amount.multiply(new BigDecimal("100")));//交易金额(单位分)
        params.put("trade_date", DateUtil.getYYYYMMDDHHMMSS(new Date(order.F04.getTime())));//订单日期
        params.put("risk_content", JSONObject.toJSON(riskContent));//风险控制参数
        return params;
    }

    /**
     * 获取确认充值参数
     * @param busiNo
     * @param captcha
     * @param createTime
     * @return
     */
    private Map<String, Object> getConfirmParams(String busiNo, String captcha, Date createTime) {
        Map<String, Object> params = BaoFooUtil.getBaseParams(TxnSubType.CONFIRM_TOPOP);
        params.put("business_no", busiNo);//由宝付返回，用于交易中唯一标识一笔交易
        params.put("sms_code", captcha);//支付时的短信验证码
        params.put("trade_date", DateUtil.getYYYYMMDDHHMMSS(createTime));//订单日期
        return params;
    }
    
    /**
     * 获取充值结果查询参数
     * @param busiNo
     * @param captcha
     * @param createTime
     * @return
     */
    private Map<String, Object> getRechargeResultParams(String origTransId, Timestamp createTime) {
    	Map<String, Object> params = BaoFooUtil.getBaseParams(TxnSubType.RECHARGE_QUERY);
    	params.put("orig_trans_id", origTransId);//原始商户订单号
    	params.put("orig_trade_date", DateUtil.getYYYYMMDDHHMMSS(createTime));//原交易订单时间
    	return params;
    }

    /**
     * 操作资金
     * @param userId
     * @param amount
     * @param orderId
     */
    @Transactional
    private void operateFunds(int userId, BigDecimal amount, int orderId) throws Exception {
        AssetAccount userWLZH = userDao.getFundAccount(userId, T6101_F03.WLZH);
        userWLZH.F06 = userWLZH.F06.add(amount);
        List<CapitalFlow> flows = new ArrayList<>(1);
        CapitalFlow flowWLZH = new CapitalFlow();
        flowWLZH.F02 = userWLZH.F01;
        flowWLZH.F03 = TradeFeeCode.CZ;
        flowWLZH.F04 = userWLZH.F01;
        flowWLZH.F06 = amount;
        flowWLZH.F08 = userWLZH.F06;
        flowWLZH.F09 = "账户充值";
        flows.add(flowWLZH);
        tradeCommonDao.insertT6102s(flows);
        userDao.updateAccount(userWLZH);
        orderManageService.complete(orderId, T6501_F03.CG);
    }

	@Override
	public void queryResult(int orderId) throws Exception {
        T6501 order = orderManageDao.get(orderId);
        if (order == null) {
            throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
        }
		if(T6501_F03.DQR!=order.F03){
			throw new TradeException(TradeResponseCode.ORDER_STATUS_WRONG);
		}
		if(TradeOrderType.CHARGE.orderType()!= order.F02){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
        T6502 rechargeOrder = rechargeManageDao.getOrder(order.F01);
        Map<String, Object> busiParams = getRechargeResultParams(order.F10, order.F04);
        Map<String, Object> reqParams = BaoFooUtil.getReqParams(TxnSubType.RECHARGE_QUERY, busiParams);
        Map<String, String> httpResult = BaoFooUtil.sendRequest(reqParams);
        if (httpResult == null) {
        	log.error("宝付确认充值失败，返回报文为空，serialNum=[{}]",order.F10);
            throw new TradeException(TradeResponseCode.PAYMENT_RECHARGE_RESULT_QUERY_FAIL);
        }
        String respCode=httpResult.get("resp_code");
        if("0000".equals(respCode)||"BF00114".equals(respCode)){
            String orderState = httpResult.get("order_stat");
            if("S".equals(orderState)){
            	operateFunds(order.F08, rechargeOrder.F03, order.F01);
            }
            else if("F".equals(orderState)||"FF".equals(orderState)){
            	orderManageService.complete(order.F01, T6501_F03.SB);
                log.error("充值失败，serialNum=[{}]，httpResult=[{}]", order.F10,httpResult.get("resp_msg"));
                throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL);
            }
        }
        else if(StateFailCodeRZZF.contain(httpResult.get("resp_code"))){
        	orderManageService.complete(order.F01, T6501_F03.SB);
        	log.error("充值失败，serialNum=[{}]，httpResult=[{}]", order.F10,httpResult.get("resp_msg"));
        	throw new TradeException(TradeResponseCode.PAYMENT_TOPUP_FAIL);    
        }
        else{
        	log.error("充值结果查询状态未知，serialNum=[{}]，resp_code=[{}]，resp_msg=[{}]", order.F10,httpResult.get("resp_code"),httpResult.get("resp_msg"));
        }
	}
	
}
