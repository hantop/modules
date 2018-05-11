package com.fenlibao.p2p.service.payment.tp.baofoo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.payment.tp.common.ThirdpartyCommonDao;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.TxnSubType;
import com.fenlibao.p2p.model.payment.tp.common.entity.PayExtend;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6118_F02;
import com.fenlibao.p2p.model.user.enums.T6118_F03;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.trade.pay.BindCardService;
import com.fenlibao.p2p.service.user.UserService;
import com.fenlibao.p2p.util.api.DateUtil;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
@SuppressWarnings("unchecked")
public class BaofooBindCardServiceImpl implements BaofooBindCardService{
	
	private static final Logger LOG = LogManager.getLogger(BaofooBindCardServiceImpl.class);
	
	@Resource
	UserService userService;
	
	@Resource
	BindCardService bindCardService;
	
	@Resource
	ThirdpartyCommonDao thirdpartyCommonDao;

	@Transactional
	@Override
    public void bindCard(int userId,String bankCardNum,String phoneNum,String bankCode) throws Exception{
		if (validateBind(userId)) {
			throw new TradeException(TradeResponseCode.PAYMENT_IS_BINDING_BAOFOO);
		}
		//校验
		validate(userId);
		//用户信息
		UserInfoEntity userInfo = userService.get(userId, null);
		if(userInfo==null){
			LOG.error("绑卡失败：userId=[{}]用户不存在",userId);
			throw new UserException(UserResponseCode.USER_NOT_EXIST);
		}
		//银行信息
		T5020 bankParam=new T5020();
		bankParam.F04=bankCode;
		T5020 bank=userService.getBank(bankParam);
		if(null==bank||StringUtils.isBlank(bank.F06)){
			LOG.error("绑卡失败：userId=[{}]，bankCode=[{}]暂不支持该银行",userId,bankCode);
			throw new TradeException(TradeResponseCode.PAYMENT_BIND_CARD_FAIL.getCode(),"暂不支持该银行");
		}
		Map<String,String> httpResultMap=buildRequestAndSend(userId,bankCardNum,phoneNum,bank.F06,userInfo);
		if("0000".equals(httpResultMap.get("resp_code"))){
			//记录绑定信息
			boolean isChanged = bindCardService.bindCard(userId, bankCardNum, userInfo.getFullName(), bank.F01,phoneNum);
			//保存绑定id，用于支付时代表绑定关系
			saveBindId(userId,httpResultMap.get("bind_id"), isChanged);
		}
		else{
			LOG.error("绑卡失败：userId=[{}]，bankCardNum=[{}]，phoneNum=[{}]，bankCode=[{}]，resp_msg=[{}]",userId,bankCardNum,phoneNum,bankCode,httpResultMap.get("resp_msg"));
			String resp_msg=httpResultMap.get("resp_msg");
			if(resp_msg.contains("持卡人信息有误")){
				resp_msg="银行预留手机号码或银行卡号不正确，请与银行核实再重新输入";
			}
			throw new TradeException(TradeResponseCode.PAYMENT_BIND_CARD_FAIL.getCode(),resp_msg);
		}
    }

	private void validate(int userId) throws Exception{
		//认证状态校验
        T6118 authInfo = userService.getAuthInfo(userId);
		if (authInfo == null) {
			LOG.error("绑卡失败：userId=[{}]用户不存在",userId);
			throw new UserException(UserResponseCode.USER_NOT_EXIST);
		}
		if (authInfo.F02==T6118_F02.BTG) {
			LOG.error("绑卡失败：userId=[{}]未通过实名认证",userId);
			throw new UserException(UserResponseCode.USER_IDENTITY_UNAUTH);
		}
		if (authInfo.F03==T6118_F03.BTG) {
			LOG.error("绑卡失败：userId=[{}]手机未认证",userId);
			throw new UserException(UserResponseCode.USER_PHONE_UNAUTH);
		}
	}
	
	private Map<String,String> buildRequestAndSend(int userId,String bankCardNum,String phoneNum,String bankCode,UserInfoEntity userInfo) throws Exception{
		Map<String,Object> requestParam = new HashMap<String,Object>();
        requestParam.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        requestParam.put("version", BaoFooUtil.CONFIG.versionRZZF());
        requestParam.put("txn_type", "0431");
        requestParam.put("txn_sub_type", TxnSubType.DIRECT_BIND.getCode());
        requestParam.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        requestParam.put("data_type", "json");
        requestParam.put("data_content", createDataContent(bankCardNum,phoneNum,bankCode,userInfo));
        return BaoFooUtil.sendRequest(requestParam);
	}
	
	private String createDataContent(String bankCardNum,String phoneNum,String bankCode,UserInfoEntity userInfo) throws Exception{
        Map<String,String> dataContent = new HashMap<String,String>();
        dataContent.put("txn_sub_type", TxnSubType.DIRECT_BIND.getCode());
        dataContent.put("biz_type", "0000");
        dataContent.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        dataContent.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        dataContent.put("trans_serial_no", OrderUtil.getFlowNo());
        dataContent.put("trans_id", OrderUtil.getOrderNo(PaymentInstitution.BF.name()));//TODO 生成了还要保存
        if(BaoFooUtil.CONFIG.isTest()){
        	dataContent.put("acc_no", "6228480444455553333");
        	dataContent.put("id_card", "320301198502169142");
        	dataContent.put("id_holder", "王宝");
        	dataContent.put("pay_code", "ABC");
        }
        else{
        	dataContent.put("acc_no", bankCardNum);
        	dataContent.put("id_card", StringHelper.decode(userInfo.getIdCardEncrypt()));
        	dataContent.put("id_holder", userInfo.getFullName());
        	dataContent.put("pay_code", bankCode);
        }
        dataContent.put("id_card_type", "01");
        dataContent.put("mobile", phoneNum);
        dataContent.put("valid_date", "");
        dataContent.put("valid_no", "");
        dataContent.put("trade_date", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        dataContent.put("additional_info", "");
        dataContent.put("req_reserved", "");
        String dataContentJson= JSON.toJSONString(dataContent);
        String dataContentBase64=Base64Encoder.encode(dataContentJson);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxStream(dataContentBase64,PfxLoader.getPrivateKeyRZZF(),BaoFooUtil.CONFIG.pfxPwdRZZF());
	    return dataContentEncrypted;
	}
		
	private void saveBindId(int userId,String bindId, boolean isChanged) throws Exception{
		PayExtend payExtend=thirdpartyCommonDao.getPayExtend(userId);
		PayExtend param=new PayExtend();
		param.setUserId(userId);
		param.setBaofooBindId(bindId);
		if (isChanged) {
			param.setLianlianAgreement(null);
		}
		else{
			if(payExtend!=null){
				param.setLianlianAgreement(payExtend.getLianlianAgreement());
			}
		}
		if(payExtend!=null){
			thirdpartyCommonDao.updatePayExtend(param);
		}
		else{
			thirdpartyCommonDao.insertPayExtend(param);
		}
	}

	@Override
	public String bindCardResultQuery(String bankCardNum) throws Exception {
		Map<String,String> httpResultMap=bindResultQueryBuildRequestAndSend(bankCardNum);
		if("0000".equals(httpResultMap.get("resp_code"))){
            LOG.info("查询绑定关系成功：bankCardNum=[{}]，bind_id=[{}]",bankCardNum,httpResultMap.get("bind_id"));
		    return "查询绑定关系成功：bankCardNum="+bankCardNum+"，bind_id="+httpResultMap.get("bind_id");
		}
		else{
			LOG.error("查询绑定关系失败：bankCardNum=[{}]，resp_msg=[{}]",bankCardNum,httpResultMap.get("resp_msg"));
			return "查询绑定关系失败：bankCardNum="+bankCardNum+"，resp_msg="+httpResultMap.get("resp_msg");
		}
	}

	@Override
	public boolean validateBind(int userId) {
		PayExtend bindInfo = thirdpartyCommonDao.getPayExtend(userId);
		if (bindInfo == null || StringUtils.isBlank(bindInfo.getBaofooBindId())) {
			return false;
		}
		return true;
	}

	private Map<String,String> bindResultQueryBuildRequestAndSend(String bankCardNum) throws Exception{
		Map<String,Object> requestParam = new HashMap<String,Object>();
        requestParam.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        requestParam.put("version", BaoFooUtil.CONFIG.versionRZZF());
        requestParam.put("txn_type", "0431");
        requestParam.put("txn_sub_type", TxnSubType.BIND_QUERY.getCode());
        requestParam.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        requestParam.put("data_type", "json");
        requestParam.put("data_content", bindResultQueryCreateDataContent(bankCardNum));
        return BaoFooUtil.sendRequest(requestParam);
	}
    
	private String bindResultQueryCreateDataContent(String bankCardNum) throws Exception{
        Map<String,String> dataContent = new HashMap<String,String>();
        dataContent.put("txn_sub_type", TxnSubType.BIND_QUERY.getCode());
        dataContent.put("biz_type", "0000");
        dataContent.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        dataContent.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        dataContent.put("trans_serial_no", OrderUtil.getFlowNo());
        dataContent.put("acc_no", bankCardNum);
        dataContent.put("trade_date", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        dataContent.put("additional_info", "");
        dataContent.put("req_reserved", "");
        String dataContentJson= JSON.toJSONString(dataContent);
        String dataContentBase64=Base64Encoder.encode(dataContentJson);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxStream(dataContentBase64,PfxLoader.getPrivateKeyRZZF(),BaoFooUtil.CONFIG.pfxPwdRZZF());
	    return dataContentEncrypted;
	}
}
