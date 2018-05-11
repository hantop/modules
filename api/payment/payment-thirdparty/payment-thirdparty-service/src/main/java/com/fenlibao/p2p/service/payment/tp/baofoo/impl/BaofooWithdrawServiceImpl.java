package com.fenlibao.p2p.service.payment.tp.baofoo.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.trade.vo.WithdrawDelayRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.trade.pay.WithdrawManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.InterfaceCode;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.WithdrawState;
import com.fenlibao.p2p.model.trade.consts.TradeConsts;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6118_F02;
import com.fenlibao.p2p.model.user.enums.T6118_F03;
import com.fenlibao.p2p.model.user.enums.T6118_F05;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.enums.UserStatus;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooWithdrawService;
import com.fenlibao.p2p.service.trade.order.OrderManageService;
import com.fenlibao.p2p.service.trade.pay.TPWithdrawManageService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.encrypt.Base64Decoder;
import com.fenlibao.p2p.util.api.encrypt.Base64Encoder;
import com.fenlibao.p2p.util.api.http.HttpClientUtil;
import com.fenlibao.p2p.util.api.http.defines.HttpResult;
import com.fenlibao.p2p.util.payment.tp.baofoo.BaoFooUtil;
import com.fenlibao.p2p.util.payment.tp.baofoo.loader.CerLoader;
import com.fenlibao.p2p.util.payment.tp.baofoo.loader.PfxLoader;
import com.fenlibao.p2p.util.payment.tp.baofoo.rsa.RsaCodingUtil;
import com.fenlibao.p2p.util.payment.tp.baofoo.rsa.RsaReadUtil;
import com.fenlibao.p2p.util.trade.order.OrderUtil;
import com.fenlibao.p2p.util.trade.pay.PayUtil;

@Service
public class BaofooWithdrawServiceImpl implements BaofooWithdrawService{

	private static final Logger LOG = LogManager.getLogger(BaofooWithdrawServiceImpl.class);

	@Resource
	TPWithdrawManageService withdrawManageService;

	@Resource
	UserDao userDao;

	@Resource
	WithdrawManageDao withdrawManageDao;

	@Resource
	OrderManageService orderManageService;

	@Override
    public void withdrawApply(int userId,BigDecimal withdrawAmount, String tradePassword) throws Exception{
	    //校验
	    validate(userId,withdrawAmount,tradePassword);
	    //用户信息
	    UserInfoEntity userInfo=userDao.get(userId, null);
        if (UserStatus.HMD.name() .equals(userInfo.getUserStatus()))
        {
        	LOG.error("提现失败：userId=[{}]被拉入黑名单",userId);
        	throw new TradeException(TradeResponseCode.PAYMENT_BLACKLIST);
        }
        //银行卡
        UserBankCardVO bankCard= userDao.getBankCard(userId);
        if (bankCard==null)
        {
        	LOG.error("提现失败：userId=[{}]银行卡不存在",userId);
			throw new TradeException(TradeResponseCode.PAYMENT_BANK_CARD_NOT_EXIST);
		}
        // 手续费
		BigDecimal poundage = getPoundage(userId);
		// 提现手续费扣除方式
		boolean feeIncluded = Boolean.parseBoolean(PayUtil.PAY_CONFIG.TXSXF_KCFS());
		BigDecimal totalAmount = BigDecimal.ZERO;// 提现应付金额
		if (feeIncluded) {
			if (totalAmount.doubleValue() < poundage.doubleValue()) {
				throw new TradeException(TradeResponseCode.PAYMENT_AMOUNT_LESSTHAN_POUNDAGE);
			}
			totalAmount = withdrawAmount;
			withdrawAmount = withdrawAmount.subtract(poundage);
		} else {
			totalAmount = withdrawAmount.add(poundage);
		}
		//生成一个流水号
		String flowNum=OrderUtil.getOrderNo(PaymentInstitution.BF.name());
	    //资金冻结，创建订单
		int orderId=withdrawManageService.withdrawApply(userId,totalAmount,withdrawAmount,poundage,userInfo,bankCard,flowNum);
		Map<String, String> httpResultMap = null;

		if(getTransactionState("withdrawDelayRequest") == 1){
			try {
				withdrawManageDao.insertWithdrawDelayRequest(userId, withdrawAmount, JSON.toJSONString(userInfo), JSON.toJSONString(bankCard), flowNum, orderId);
			}catch (Exception e){
				LOG.error("插入提现延迟请求第三方支付记录表异常，资金回退，userId：{}",userId,e);
				withdrawManageService.withdrawFail(orderId, null);
			}
		}else {
			//发送请求
			try {
				httpResultMap = buildRequestAndSend(userId, withdrawAmount, userInfo, bankCard, flowNum);
			}catch (TradeException te){
				if(TradeResponseCode.PAYMENT_REQUEST_FAIL.getCode().equals(te.getCode())){
					withdrawManageService.withdrawFail(orderId, null);
				}
				LOG.error("宝付发送请求失败:userId=[{}]",userId);
				LOG.error(te);
				return;
			} catch (Exception e){
				LOG.error("宝付发送请求失败:userId=[{}]",userId);
				LOG.error(e);
			}
			if(httpResultMap.isEmpty()){
				LOG.error("提现申请接口BF0040001返回空userId=[{}]",userId);
				throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_FAIL);
			}
			String returnCode=httpResultMap.get("return_code");
			boolean requestFail=!"0000".equals(returnCode)
					&&!"0300".equals(returnCode)
					&&!"0401".equals(returnCode)
					&&!"0999".equals(returnCode);
			if(requestFail){
				String thirdpartyOrderId=httpResultMap.get("trans_orderid");
				withdrawManageService.withdrawFail(orderId, thirdpartyOrderId);
				LOG.error("提现失败：userId=[{}]，return_msg=[{}]",userId,httpResultMap.get("return_msg"));
				throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_FAIL);
			}
			else{
				if(!"0000".equals(returnCode)){
					LOG.error("提现申请状态未知：userId=[{}]，flowNum=[{}]，return_code=[{}]，return_msg=[{}]",userId,flowNum,httpResultMap.get("return_code"),httpResultMap.get("return_msg"));
				}
			}
		}
    }

    @Override
	public void withdrawApplyAfter(Integer limit) throws Exception{

		List<WithdrawDelayRequest> withdrawDelayRequests = withdrawManageDao.findWithdrawDelayRequests(limit);

		for(WithdrawDelayRequest withdrawDelayRequest :withdrawDelayRequests){
			Integer userId = withdrawDelayRequest.getUserId();
			BigDecimal withdrawAmount = withdrawDelayRequest.getWithdrawAmount();
			UserInfoEntity userInfo = JSON.parseObject(withdrawDelayRequest.getUserInfo(), UserInfoEntity.class);
			UserBankCardVO bankCard = JSON.parseObject(withdrawDelayRequest.getBankCard(), UserBankCardVO.class);
			String flowNum = withdrawDelayRequest.getFlowNum();
			Integer orderId = withdrawDelayRequest.getOrderId();
			Map<String, String> httpResultMap = null;

			//发送请求
			try {
				httpResultMap = buildRequestAndSend(userId, withdrawAmount, userInfo, bankCard, flowNum);
				//更新提现延迟请求第三方支付记录表该记录为已处理
				withdrawManageDao.updateWithdrawDelayRequestsWithdrawState(withdrawDelayRequest.getId());
			}catch (TradeException te){
				if(TradeResponseCode.PAYMENT_REQUEST_FAIL.getCode().equals(te.getCode())){
					withdrawManageService.withdrawFail(orderId, null);
				}
				LOG.error("宝付发送请求失败:userId=[{}]",userId);
				LOG.error(te);
				continue;
			} catch (Exception e){
				LOG.error("宝付发送请求失败:userId=[{}]",userId);
				LOG.error(e);
			}
			if(httpResultMap.isEmpty()){
				LOG.error("提现申请接口BF0040001返回空userId=[{}]",userId);
				continue;
				//throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_FAIL);
			}
			String returnCode=httpResultMap.get("return_code");
			boolean requestFail=!"0000".equals(returnCode)
					&&!"0300".equals(returnCode)
					&&!"0401".equals(returnCode)
					&&!"0999".equals(returnCode)
					&&!"0003".equals(returnCode);//0003为重复提交订单
			if(requestFail){
				String thirdpartyOrderId=httpResultMap.get("trans_orderid");
				withdrawManageService.withdrawFail(orderId, thirdpartyOrderId);
				LOG.error("提现失败：userId=[{}]，return_msg=[{}]",userId,httpResultMap.get("return_msg"));
				//throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_FAIL);
			}
			else{
				if(!"0000".equals(returnCode)){
					LOG.error("提现申请状态未知：userId=[{}]，flowNum=[{}]，return_code=[{}]，return_msg=[{}]",userId,flowNum,httpResultMap.get("return_code"),httpResultMap.get("return_msg"));
				}
			}
		}
	}

	@Override
	public Integer getTransactionState(String withdrawDelayRequestJob) {
		return withdrawManageDao.getTransactionState(withdrawDelayRequestJob);
	}

	private void validate(int userId,BigDecimal withdrawAmount,String tradePassword) throws Exception {
		//是否有逾期
	    int overdueNum = userDao.countOverdue(userId);
	    if (overdueNum > 0) {
		    throw new TradeException(TradeResponseCode.PAYMENT_LOAN_OVERDUE);
	    }
	    //金额限制
        BigDecimal min = new BigDecimal(PayUtil.PAY_CONFIG.WITHDRAW_MIN_FUNDS());
        BigDecimal max = new BigDecimal(PayUtil.PAY_CONFIG.WITHDRAW_MAX_FUNDS());
        BigDecimal zero = new BigDecimal(0);
        if (withdrawAmount.compareTo(min) < 0 || withdrawAmount.compareTo(zero) <= 0)
        {
        	throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_LIMIT_LOW);
        }
        if (withdrawAmount.compareTo(max) > 0) {
        	throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_LIMIT.getCode(),String.format(TradeResponseCode.PAYMENT_WITHDRAW_LIMIT.getMessage(), min, max));
        }
		//认证状态校验
        T6118 authInfo = userDao.getAuthInfo(userId);
		if (authInfo == null) {
			LOG.error("提现失败：userId=[{}]用户不存在",userId);
			throw new UserException(UserResponseCode.USER_NOT_EXIST);
		}
		if (authInfo.F02==T6118_F02.BTG) {
			throw new UserException(UserResponseCode.USER_IDENTITY_UNAUTH);
		}
		if (authInfo.F03==T6118_F03.BTG) {
			throw new UserException(UserResponseCode.USER_PHONE_UNAUTH);
		}
		if (authInfo.F05==T6118_F05.WSZ) {
			throw new UserException(UserResponseCode.USER_NOT_SET_TRADE_PASSWORD);
		}
		//交易密码是否正确
		String encryptedTradePassword= UnixCrypt.crypt(tradePassword, DigestUtils.sha256Hex(tradePassword));
		int count =userDao.getTradePwdWrongCount(userId);
		if (TradeConsts.TRADE_PWD_WRONG_COUNT_MAX <= count) {
			throw new UserException(UserResponseCode.USER_TRADE_PASSWORD_ERROR);
		}
	    if(!authInfo.F08.equals(encryptedTradePassword)){
			int surplus = TradeConsts.TRADE_PWD_WRONG_COUNT_MAX - (count+1);
			userDao.updateTradePwdWrongCount(userId, false);
			if (0 == surplus) {
				throw new UserException(UserResponseCode.USER_TRADE_PASSWORD_ERROR);
			}
	    	 throw new UserException(UserResponseCode.USER_TRADE_PWD_WRONG_COUNT.getCode(),
					 String.format(UserResponseCode.USER_TRADE_PWD_WRONG_COUNT.getMessage(), surplus));
	    }
		if (0 < count) {
			userDao.updateTradePwdWrongCount(userId, true);
		}
	}

	/**
	 * 首次提现免手续费
	 * @param userId
	 * @return
	 */
	private BigDecimal getPoundage(Integer userId) throws Exception{
		String amount = PayUtil.PAY_CONFIG.WITHDRAW_POUNDAGE_1_RMB();
		BigDecimal poundage = new BigDecimal(amount);
		Integer successApplyId = withdrawManageDao.getSuccessApplyId(userId);
		if (successApplyId == null || successApplyId < 1) {
			poundage = BigDecimal.ZERO;
		}
		return poundage;
	}

	private Map<String,String> buildRequestAndSend(int userId,BigDecimal withdrawAmount,UserInfoEntity userInfo,UserBankCardVO bankCard,String flowNum) throws TradeException, Exception{
        Map<String,Object> requestParam = new HashMap<String,Object>();
        requestParam.put("terminal_id", BaoFooUtil.CONFIG.terminalIdDF());
        requestParam.put("version", BaoFooUtil.CONFIG.versionDF());
        requestParam.put("member_id", BaoFooUtil.CONFIG.memberIdDF());
        requestParam.put("data_type", "json");
        requestParam.put("data_content", createDataContent(userId,withdrawAmount,userInfo,bankCard,flowNum));
        return sendRequest(requestParam,InterfaceCode.BF0040001,flowNum);
	}

	private String createDataContent(int userId,BigDecimal withdrawAmount,UserInfoEntity userInfo,UserBankCardVO bankCard,String flowNum) throws Exception{
        Map<String,Object> dataContent = new HashMap<String,Object>();
        dataContent.put("trans_no", flowNum);
        dataContent.put("trans_money", withdrawAmount);
        dataContent.put("to_acc_name", userInfo.getFullName());
        dataContent.put("to_acc_no", StringHelper.decode(bankCard.getCardNoEncrypt()));
		T5020 bankParam=new T5020();
		bankParam.F01=bankCard.getBankId();
        T5020 bank=userDao.getBank(bankParam);
        dataContent.put("to_bank_name",bank.F02);
        dataContent.put("to_pro_name","");
        dataContent.put("to_city_name","");
        dataContent.put("to_acc_dept","");
        dataContent.put("trans_card_id",StringHelper.decode(userInfo.getIdCardEncrypt()));
        dataContent.put("trans_mobile",userInfo.getPhone());
        dataContent.put("trans_summary","");
        Map<String,Object> originMap=new HashMap<>();
        Map<String,Object> transContent=new HashMap<>();
        List<Object> transReqDatas=new ArrayList<>();
        Map<String,Object> transReqDatasItem1=new HashMap<>();
        List<Object> transReqData=new ArrayList<>();
        transReqData.add(dataContent);
        transReqDatasItem1.put("trans_reqData", transReqData);
        transReqDatas.add(transReqDatasItem1);
        transContent.put("trans_reqDatas", transReqDatas);
        originMap.put("trans_content", transContent);
        String dataContentJson= JSON.toJSONString(originMap);
        String dataContentBase64=Base64Encoder.encode(dataContentJson);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxStream(dataContentBase64,PfxLoader.getPrivateKeyDF(),BaoFooUtil.CONFIG.pfxPwdDF());
	    return dataContentEncrypted;
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> sendRequest(Map<String,Object> requestParam,InterfaceCode interfaceCode, String flowNo) throws TradeException, Exception{
        JSONObject requestParamJsonObject=new JSONObject(requestParam);
        String requestUrl= BaoFooUtil.CONFIG.requestUrlDf();
        requestUrl=requestUrl.replace("interfaceCode", interfaceCode.name());
        HttpResult httpResult=HttpClientUtil.httpsMapPost(requestUrl, requestParamJsonObject);
		if(httpResult.getStatusCode()!= HttpStatus.SC_OK){
			LOG.error("提现http请求失败，状态码=[{}],请求参数 >> {}", httpResult.getStatusCode(), requestParam.toString());
			throw new TradeException(TradeResponseCode.PAYMENT_REQUEST_FAIL);
		}
		if (httpResult.getBytes() == null) {
			LOG.error("提现http请求响应为空，请求参数 >> {}", requestParam.toString());
			throw new TradeException(TradeResponseCode.PAYMENT_RESPONSE_FAIL);
		}
        PublicKey publicKey= RsaReadUtil.getPublicKeyByText(CerLoader.getWithdrawPublicKey());
		String resultStr = "";
		try {
			resultStr = new String(httpResult.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if (StringUtils.isBlank(resultStr)) {
			LOG.error("提现http请求响应为空，请求参数 >> {}", requestParam.toString());
			throw new TradeException(TradeResponseCode.PAYMENT_RESPONSE_FAIL);
		}
		String httpResultJson="";
		if(resultStr.contains("{")){
			httpResultJson=resultStr;
		}
		else{
	        String rsaDecryptedHttpResult=RsaCodingUtil.decryptByPublicKey(resultStr, publicKey);
			if(rsaDecryptedHttpResult.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
				throw new TradeException(TradeResponseCode.PAYMENT_PUBLICKEY_WRONG);
			}
			httpResultJson = Base64Decoder.decode(rsaDecryptedHttpResult);
		}
		Map<String,String> httpResultMap=new HashMap<>();
		Map<String,Object> originMap=JSON.parseObject(httpResultJson, HashMap.class);
		LOG.info("提现：宝付返回结果" + originMap + "流水号FlowNo：" + flowNo);
		Map<String,Object> transContentMap=(Map<String,Object>)originMap.get("trans_content");
		Map<String,Object> transHeadMap=(Map<String,Object>)transContentMap.get("trans_head");
		for(String key:transHeadMap.keySet()){
			httpResultMap.put(key,(String)transHeadMap.get(key));
		}
		List<Object> transReqDatas=(List<Object>)transContentMap.get("trans_reqDatas");
		if(transReqDatas!=null){
			Map<String,Object> transReqData=(Map<String,Object>)transReqDatas.get(0);
//			List<Object> transReqDataList=(List<Object>)transReqData.get("trans_reqData");
//			if(transReqDataList!=null){
//				Map<String,Object> itemMap=(Map<String,Object>)transReqDataList.get(0);
//				for(String key:itemMap.keySet()){
//					httpResultMap.put(key,String.valueOf(itemMap.get(key)));
//				}
//			}
			Map<String,Object> itemMap=(Map<String,Object>)transReqData.get("trans_reqData");
			for(String key:itemMap.keySet()){
			    httpResultMap.put(key,String.valueOf(itemMap.get(key)));
		    }
		}
		return httpResultMap;
	}

	@Override
	public void withdrawResultQuery(int orderId) throws Exception {
		T6501 order= orderManageService.getOrder(orderId);
		//校验
		queryValidate(order);
		//发送查询请求
		Map<String,String> httpResultMap=queryBuildRequestAndSend(order);
		if(httpResultMap.isEmpty()){
			LOG.error("查询提现结果接口BF0040002返回空");
			throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_RESULT_QUERY_FAIL);
		}
		String returnCode=httpResultMap.get("return_code");
		boolean requestFail=!"0000".equals(returnCode)
				&&!"0300".equals(returnCode)
				&&!"0401".equals(returnCode)
				&&!"0999".equals(returnCode);
		
		if("0000".equals(returnCode)){
			//提交订单
			if(WithdrawState.SUCCESS.getValue()==Integer.parseInt(httpResultMap.get("state"))){
				withdrawManageService.withdrawSuccess(orderId,httpResultMap.get("trans_orderid"));
			}
			else if(WithdrawState.FAIL.getValue()==Integer.parseInt(httpResultMap.get("state"))
					||WithdrawState.REFUND.getValue()==Integer.parseInt(httpResultMap.get("state"))){
				withdrawManageService.withdrawFail(orderId,httpResultMap.get("trans_orderid"));
			}
		}
		else if(requestFail){
			String thirdpartyOrderId=httpResultMap.get("trans_orderid");  
			withdrawManageService.withdrawFail(orderId, thirdpartyOrderId);
			LOG.error("提现失败：userId=[{}]，return_msg=[{}]",order.F08,httpResultMap.get("return_msg"));
			throw new TradeException(TradeResponseCode.PAYMENT_WITHDRAW_FAIL);
		}
		else{
			LOG.error("提现状态未知：userId=[{}]，flowNum=[{}]，return_code=[{}]，return_msg=[{}]",order.F08,order.F10,httpResultMap.get("return_code"),httpResultMap.get("return_msg"));
		}
	}

	private void queryValidate(T6501 order)throws Exception{
		if(order==null){
			throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
		}
		if(T6501_F03.DQR!=order.F03){
			throw new TradeException(TradeResponseCode.ORDER_STATUS_WRONG);
		}
		if(TradeOrderType.WITHDRAW.orderType()!= order.F02){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
	}

	private Map<String,String> queryBuildRequestAndSend(T6501 order) throws Exception{
        Map<String,Object> requestParam = new HashMap<String,Object>();
        requestParam.put("terminal_id", BaoFooUtil.CONFIG.terminalIdDF());
        requestParam.put("version", BaoFooUtil.CONFIG.versionDF());
        requestParam.put("member_id", BaoFooUtil.CONFIG.memberIdDF());
        requestParam.put("data_type", "json");
        requestParam.put("data_content", queryCreateDataContent(order));
        return sendRequest(requestParam,InterfaceCode.BF0040002, order.F10);
	}

	private String queryCreateDataContent(T6501 order) throws Exception{
        Map<String,Object> dataContent = new HashMap<String,Object>();
        dataContent.put("trans_batchid", ""); //宝付批次号
        dataContent.put("trans_no", order.F10); //商户订单号
        Map<String,Object> originMap=new HashMap<>();
        Map<String,Object> transContent=new HashMap<>();
        List<Object> transReqDatas=new ArrayList<>();
        Map<String,Object> transReqDatasItem1=new HashMap<>();
        List<Object> transReqData=new ArrayList<>();
        transReqData.add(dataContent);
        transReqDatasItem1.put("trans_reqData", transReqData);
        transReqDatas.add(transReqDatasItem1);
        transContent.put("trans_reqDatas", transReqDatas);
        originMap.put("trans_content", transContent);
        String dataContentJson= JSON.toJSONString(originMap);
        String dataContentBase64=Base64Encoder.encode(dataContentJson);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxStream(dataContentBase64,PfxLoader.getPrivateKeyDF(),BaoFooUtil.CONFIG.pfxPwdDF());
	    return dataContentEncrypted;
	}
}
