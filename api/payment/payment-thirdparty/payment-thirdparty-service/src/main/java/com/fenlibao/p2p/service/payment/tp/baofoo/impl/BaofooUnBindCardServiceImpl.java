package com.fenlibao.p2p.service.payment.tp.baofoo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.payment.tp.common.ThirdpartyCommonDao;
import com.fenlibao.p2p.model.payment.tp.baofoo.enums.TxnSubType;
import com.fenlibao.p2p.model.payment.tp.common.entity.PayExtend;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooUnBindCardService;
import com.fenlibao.p2p.service.trade.pay.BindCardService;
import com.fenlibao.p2p.service.user.UserService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.encrypt.Base64Encoder;
import com.fenlibao.p2p.util.payment.tp.baofoo.BaoFooUtil;
import com.fenlibao.p2p.util.payment.tp.baofoo.loader.PfxLoader;
import com.fenlibao.p2p.util.payment.tp.baofoo.rsa.RsaCodingUtil;
import com.fenlibao.p2p.util.trade.order.OrderUtil;

@Service
@SuppressWarnings("unchecked")
public class BaofooUnBindCardServiceImpl  implements BaofooUnBindCardService{

	private static final Logger LOG = LogManager.getLogger(BaofooUnBindCardServiceImpl.class);
	
	@Resource
	UserService userService;
	
	@Resource
	ThirdpartyCommonDao thirdpartyCommonDao;
	
	@Resource
	BindCardService bindCardService;
	
	@Transactional
	@Override
	public void unBindCard(Integer userId) throws Exception {
		//用户信息
		UserInfoEntity userInfo = userService.get(userId, null);
		if(userInfo==null){
			LOG.error("解绑失败：userId=[{}]用户不存在",userId);
			throw new UserException(UserResponseCode.USER_NOT_EXIST);
		}
		
		//绑卡协议
		boolean hasBind = true;
		PayExtend bindInfo = thirdpartyCommonDao.getPayExtend(userId);
		if (bindInfo == null || StringUtils.isBlank(bindInfo.getBaofooBindId())) {
			hasBind = false;
		}
		if (!hasBind) {
			LOG.error("解绑失败：userId=[{}]用户没有在绑的卡",userId);
			throw new TradeException(TradeResponseCode.PAYMENT_UNBIND_NO_CARD);
		}
		Map<String,String> httpResultMap=buildRequestAndSend(bindInfo.getBaofooBindId());
		if("0000".equals(httpResultMap.get("resp_code"))){
			//解绑
			bindCardService.unBindCard(userId);
			PayExtend param=new PayExtend();
			param.setUserId(userId);
			thirdpartyCommonDao.updatePayExtend(param);
		}
		else{
			LOG.error("解绑失败：userId=[{}]，resp_msg=[{}]",userId,httpResultMap.get("resp_msg"));
			throw new TradeException(TradeResponseCode.PAYMENT_UNBIND_FAIL.getCode(),httpResultMap.get("resp_msg"));
		}
	}

	private Map<String,String> buildRequestAndSend(String bindId) throws Exception{
		Map<String,Object> requestParam = new HashMap<String,Object>();
        requestParam.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        requestParam.put("version", BaoFooUtil.CONFIG.versionRZZF());
        requestParam.put("txn_type", "0431");
        requestParam.put("txn_sub_type", TxnSubType.UN_BIND.getCode());
        requestParam.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        requestParam.put("data_type", "json");
        requestParam.put("data_content", createDataContent(bindId));
        return BaoFooUtil.sendRequest(requestParam);
	}
	
	private String createDataContent(String bindId) throws Exception{
        Map<String,String> dataContent = new HashMap<String,String>();
        dataContent.put("txn_sub_type", TxnSubType.UN_BIND.getCode());
        dataContent.put("biz_type", "0000");
        dataContent.put("terminal_id", BaoFooUtil.CONFIG.terminalIdRZZF());
        dataContent.put("member_id", BaoFooUtil.CONFIG.memberIdRZZF());
        dataContent.put("trans_serial_no", OrderUtil.getFlowNo());
        if(BaoFooUtil.CONFIG.isTest()){
        	dataContent.put("bind_id", "201604271949318660");
        }
        else{
        	dataContent.put("bind_id", bindId);
        }
        LOG.info("解绑参数：bindId=[{}]",dataContent.get("bind_id"));
        dataContent.put("trade_date", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        dataContent.put("additional_info", "");
        dataContent.put("req_reserved", "");
        String dataContentJson= JSON.toJSONString(dataContent);
        String dataContentBase64=Base64Encoder.encode(dataContentJson);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxStream(dataContentBase64,PfxLoader.getPrivateKeyRZZF(),BaoFooUtil.CONFIG.pfxPwdRZZF());
	    return dataContentEncrypted;
	}
	
}
