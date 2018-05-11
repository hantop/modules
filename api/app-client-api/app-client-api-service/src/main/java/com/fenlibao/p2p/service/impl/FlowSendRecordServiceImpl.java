package com.fenlibao.p2p.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.impl.TFlowSendRecordDaoImpl;
import com.fenlibao.p2p.model.entity.TFlowSendRecord;
import com.fenlibao.p2p.service.FlowSendRecordService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.pay.AESForFlowUtil;
import com.fenlibao.p2p.util.pay.HttpClientHandler;
import org.apache.logging.log4j.Logger;

@Service
public class FlowSendRecordServiceImpl implements FlowSendRecordService {
	
	private static final Logger logger= LogManager.getLogger(FlowSendRecordServiceImpl.class);

	
	@Resource
	private TFlowSendRecordDaoImpl flowSendRecordDaoImpl;
	
	@Override
	public List<TFlowSendRecord> getWaitFlowSendRecord(String channelCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] channelCodes = channelCode.split(",");
		map.put("channelCodes", channelCodes);
		
		List<TFlowSendRecord>  list = flowSendRecordDaoImpl.getWaitFlowSendRecord(map);
		return list;
	}

	@Override
	public void addFlowSendRecord(int userId, String activityType, int flow, int sendStatus,String requestNo, String remark) {
		TFlowSendRecord record = new TFlowSendRecord();
		record.setUserId(userId);
		record.setActivityType(activityType);
		record.setFlowSize(flow);
		record.setSendStatus(sendStatus);
		record.setRemark(remark);
		record.setRequestNo(requestNo);
		record.setCreateTime(new Date());
		
		this.flowSendRecordDaoImpl.addFlowSendRecord(record);
		
	}
	
	/**
	 * @Title: updateSendFlow
	 * @Description: TODO
	 * @param request_no 
	 * @see com.fenlibao.p2p.service.FlowSendRecordService#updateSendFlow(java.lang.String) 
	 */
	@Override
	public void updateSendFlow(String requestNo,int sendStatus) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("requestNo", requestNo);
		paramMap.put("sendStatus", sendStatus);
		if(sendStatus == 3){//发送成功 更新发送时间
			paramMap.put("sendTime", new Date());
		}
		
		this.flowSendRecordDaoImpl.updateSendFlow(paramMap);
	}
	
	
	/** 
	 * @Title: sendTeleFlow 
	 * @Description: TODO
	 * @param telephone
	 * @return
	 * @return: String
	 */
	@Override
	public String sendTeleFlow(String telephone, String requestNo) {
		logger.info("telephone:"+telephone+" requestNo:"+requestNo);
		String retString = null;
		try {
			Map<String, String> requestParam = buildRequestParams(telephone,requestNo);
			if(requestParam==null) {
				logger.info("********build params is empty**********");
				return retString;
			}
			String actionUrl = getFlowRequestActionUrl();
			retString = HttpClientHandler.doPostJson(requestParam, actionUrl);
			logger.info("-----调用结束-------------"+retString);
		}catch (Exception e) {
			logger.error(e);
		}
		return retString;
	}
	
	/**
	 * @ 构造请求参数
	 * @return 返回map集合
	 */
	private Map<String, String> buildRequestParams(String telephone, String requestNo) {		
		// 封装请求参数
		Map<String, String> params = new HashMap<String, String>();
		try {
			String secretKey = Config.get("secreKey");
			String vector = Config.get("vector");
			String code = AESForFlowUtil.Encrypt(getRequestParamCode(telephone,requestNo).toString(), secretKey, vector);
			
			// 合作方 ID
			params.put("partner_no", Config.get("partnerNo"));
			//请求code
			params.put("code", code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return params;
	}
	
	/** 
	 * @Title: getRequestParamCode 
	 * @Description: 获取接口请求参数
	 * @param telephone
	 * @return
	 * @return: JSONObject
	 */
	private JSONObject getRequestParamCode(String telephone, String requestNo) {
		JSONObject json = new JSONObject();
		json.put("request_no", requestNo);
		json.put("service_code", Config.get("serviceCode"));
		json.put("contract_id", Config.get("contractId"));
		json.put("activity_id", Config.get("activityId"));
		json.put("order_type", Config.get("orderType"));
		json.put("phone_id", telephone);
		json.put("plat_offer_id", Config.get("platOfferId"));
		json.put("effect_type", Config.get("effectType"));
		return json;
	}
	
	/** 
	 * @Title: getFlowRequestActionUrl 
	 * @Description: 获取电信流量接口请求地址接口
	 * @return
	 * @return: String
	 */
	public String getFlowRequestActionUrl() {	
		return Config.get("flowActionUrl");
	}
	
	/** 
	 * @Title: genRequestNo 
	 * @Description:  生成交易流水号(时间毫秒戳+手机号  共24位)
	 * @param telephone
	 * @return
	 * @return: String
	 */
	@Override
	public String genRequestNo(String telephone){
		return String.valueOf(Calendar.getInstance().getTimeInMillis())+telephone;  
	}
	
}
