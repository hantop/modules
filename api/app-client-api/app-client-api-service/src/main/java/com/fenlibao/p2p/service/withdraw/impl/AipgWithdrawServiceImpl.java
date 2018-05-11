package com.fenlibao.p2p.service.withdraw.impl;

import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.BankCardInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.district.DistrictService;
import com.fenlibao.p2p.service.trade.IOrderService;
import com.fenlibao.p2p.service.withdraw.IAipgBusManageService;
import com.fenlibao.p2p.service.withdraw.IAipgManageService;
import com.fenlibao.p2p.service.withdraw.IAipgWithdrawService;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.OrderUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

//com.dimeng.p2p.pay.executor.AipgWithdrawExecutor
@Service
public class AipgWithdrawServiceImpl extends WithdrawExecutor implements IAipgWithdrawService {
	
    /**
     * 版本
     */
    protected static final String API_VERSION = "1.2";
     /**
     * 加密类型
     */
    protected static final String SIGN_TYPE = "RSA";
    
    @Resource
    private IAipgBusManageService aipgBusManageService;
    @Resource
    private IAipgManageService aipgManageService;
	@Resource
	protected BankService bankService;
	@Resource
	private IOrderService orderService;
	@Resource
	private DistrictService districtService;

	@Override
    protected void doSubmit(Connection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        // 调用代付接口
		String returnMsg = "";
        BusinessException busiEx = null;
		try {
			orderService.insertOrderIdAndClientType(orderId, params.get("clientType"));
			returnMsg = this.payment(connection, orderId, params);
		} catch (BusinessException busi) {
            busiEx = busi;
        } catch (Exception e) {
			logger.error(e.toString(), e);
            returnMsg = e.getMessage();
		}
		loanFailure(connection, orderId, returnMsg, busiEx);
		
        // 更新为放款中
        aipgBusManageService.updateT6130Status(connection, orderId, T6130_F09.FKZ);
    }
	
	@SuppressWarnings("unchecked")
	private void loanFailure(Connection connection, int orderId, String returnMsg, BusinessException busiEx) throws Throwable {
        String ret_code = null;
        String ret_msg = null;
        if (returnMsg.indexOf("ret_code") >= 0) {
            Map<String, String>  retMap = new Gson().fromJson(returnMsg, Map.class);
            ret_code = retMap.get("ret_code");
            ret_msg = retMap.get("ret_msg");
        } else {
            ret_msg = returnMsg;
        }
        if (!"0000".equals(ret_code)) {
            logger.info(String.format("orderId=[%s],代付失败，返回码：[%s]备注：[%s]", orderId, ret_code, ret_msg));
            try {
                aipgManageService.fksb(ret_msg, orderId);
            } catch (Exception e) {
                logger.info(String.format("refund fail, OrderId[%s]", orderId));
                logger.error(e.toString(), e);
                this.insertOrderExceptionLog(orderId, String.format("refund fail, OrderId[%s]", orderId) + e);
            }
            // 更新为提现失败
            aipgBusManageService.updateT6130Status(connection, orderId, T6130_F09.TXSB);
            if (busiEx != null) {
                throw busiEx;
            } else {
                throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_FAIL); //这里不能把连连返回的信息抛给用户
            }
        }
    }
    
    @Override
    protected void doConfirm(Connection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        super.doConfirm(connection, orderId, params);
        // 更新为成功
        aipgBusManageService.updateT6130Status(connection, orderId, T6130_F09.YFK);
    }
    
    /**
     * 连连代付调用接口
     * <请求参数封装与接口调用>
     * @param connection
     * @param orderId
     * @param params
     * @return
     * @throws Throwable
     */
    private String payment(Connection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        logger.info("放款开始，订单号：" + orderId + ",参数：" + params);
        // 请求信息
        Map<String, String>  requsetParams =  getRequestParams(connection, orderId);
        // 调 用
        return aipgManageService.loan(requsetParams);
    }
    
    
    /**
     * 请求信息集合
     * @return 返回map集合
     * @throws Throwable 
     */
    private Map<String, String> getRequestParams(Connection connection, int orderId) throws Throwable
    {
        // 查询订单
        T6503 t6503 = null;
        // 获取银行卡信息
        BankCardInfo bankCardInfo = null;
        t6503 = selectT6503(connection, orderId);
        // 银行卡号ID
        int cardId = aipgBusManageService.getWithdrawBankCardByOrderId(connection, orderId);
        if (cardId == 0)
        {
            throw new BusinessException(ResponseCode.PAYMENT_BANK_CARD_NOT_EXIST);
        }
        // 获取银行卡信息
        bankCardInfo = aipgBusManageService.getUserBankCardInfo(connection, cardId);
        
        // 请求参数
        Map<String, String> params = new HashMap<String, String>();
        // 平台来源
        params.put("platform", "m.fenlibao.com");
        // 签名方式
        params.put("sign_type", SIGN_TYPE);
        //订单创建时间
        Timestamp orderTime = orderService.getCreateTimeByOrderId(orderId);
        // 商户流水号
        String no_order = OrderUtil.genLlpWithdrawOrderId(orderId, orderTime);
        //是否为测试
        boolean isTest=BooleanParser.parse(Payment.get(Payment.IS_PAY_TEST));
		if(isTest){
			no_order = OrderUtil.genLlpWithdrawOrderId_Test(orderId, orderTime);
		}
        // 商户流水号
        params.put("no_order", no_order); //String.valueOf(orderId)
        // 商户时间(格式: YYYYMMDDH24MISS)
        params.put("dt_order", new SimpleDateFormat("yyyyMMddHHmmss").format(orderTime));
        // 代付金额
        params.put("money_order", t6503.F03.toString());
        // 对公对私标志
        String flagCard = bankCardInfo.getFlag();
        String person = String.valueOf(InterfaceConst.ACCOUNT_NAME_TYPE_PERSON);
        if (person.equals(flagCard)) {
        	flagCard = "0"; //连连对私是 0
        } else {
        	flagCard = "1"; //连连对公是 1
        }
        params.put("flag_card", flagCard);
        // 银行账号
        params.put("card_no", StringHelper.decode(bankCardInfo.getBankCardNo()));
        // 银行账号名称
        params.put("acct_name", bankCardInfo.getRealName());
        // 银行编码
        String bankCode = bankCardInfo.getBankCode();
        params.put("bank_code", bankCode);
        //判断是否需要支行信息
        if (!this.validateBankCode(bankCode)) {
        	String cityCode = bankCardInfo.getCityCode();
            if (StringUtils.isNotBlank(cityCode)) {
            	String provinceCode = districtService.getProvinceCodeByCityCode(cityCode);
            	if (StringUtils.isBlank(provinceCode)) {
                    throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_CANNOT_FIND_SUBBRANCH_PROVINCE);
            	}
            	// 开户行所在省编号
            	params.put("province_code", provinceCode);
            	// 开户行所在市编号
            	params.put("city_code", cityCode);
            	// 开户支行名称
            	params.put("brabank_name", bankCardInfo.getBankName());
            } else {
            	throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_CANNOT_FIND_SUBBRANCH);
            }
        }
        // 订单描述
        params.put("info_order", "连连代付");
        // 版本号
        params.put("api_version", API_VERSION);
        // 大额行号
        params.put("prcptcd", "");
        
        return params;
    }
    

	/**
	 * 判断银行卡提现是否需要支行信息
	 * 
	 * @param bankCode
	 * @return
	 */
	private boolean validateBankCode(String bankCode) {
        String bankCodes = Payment.get("NOT_REQUIRED_BANK_BRANCH_INFO");//TODO 应用表维护t5020
        if (StringUtils.isNotBlank(bankCodes)) {
            if (bankCodes.indexOf(bankCode) >= 0) {
                return true;
            }
        } else {
            logger.warn("找不到提现免支行银行编码！！！");
        }
		return false;
	}

	@Override
	public void insertRefundFailLog(int orderId, String log) throws Exception {
		this.insertOrderExceptionLog(orderId, log);
	}
	
}
