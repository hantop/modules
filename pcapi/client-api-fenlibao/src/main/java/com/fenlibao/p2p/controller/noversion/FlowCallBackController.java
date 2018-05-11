/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: FlowRechargeController.java 
 * @Prject: client-api-fenlibao
 * @Package: com.fenlibao.p2p.controller.noversion 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-27 下午3:38:55 
 * @version: V1.1   
 */
package com.fenlibao.p2p.controller.noversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.service.FlowSendRecordService;
import com.fenlibao.p2p.util.loader.Config;


/** 
 * @ClassName: FlowRechargeController 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-27 下午3:38:55  
 */
@RestController
@RequestMapping("/notify")
public class FlowCallBackController {

	protected static final Logger logger = LogManager.getLogger(FlowCallBackController.class);

	@Resource
	FlowSendRecordService flowSendRecordService;
	
	/** 
	 * @Title: flowcallback 
	 * @Description: 电信流量充值回调处理
	 * @param request
	 * @return
	 * @return: int
	 */
	@RequestMapping(value = "flowcallback", method = RequestMethod.POST)
	public int flowcallback(HttpServletRequest request) {
		String returnString =null;
		try {
			//流方式接受请求参数
			BufferedReader br = new BufferedReader(new InputStreamReader(
	                    (ServletInputStream) request.getInputStream()));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            returnString = sb.toString();
            

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //获取订单流水号和请求编码
		JSONObject json = JSONObject.parseObject(returnString);  
		if(json!=null){
			String requestNo=json.get("request_no")!=null?String.valueOf(json.get("request_no")):null;
			String resultCode=json.get("result_code")!=null?String.valueOf(json.get("result_code")):null;
			int sendStatus = 4;
			//sendStatus 发送状态 3：发送成功；4：发送失败
			if("00000".equals(resultCode)){
				sendStatus = 3;
			}
			flowSendRecordService.updateSendFlow(requestNo,sendStatus);
		}
		
		return 1;
	}
	
	/** 
	 * @Title: flowTest 
	 * @Description: 电信流量充值测试
	 * @return
	 * @return: HttpResponse
	 */
	@RequestMapping(value = "flowTest", method = RequestMethod.GET)
	public HttpResponse flowTest(HttpServletRequest request,
    		@RequestParam(required = false, value = "iphone") String iphone,
    		@RequestParam(required = false, value = "token") String token,
    		@RequestParam(required = false, value = "userId") String userId) {
		HttpResponse response = new HttpResponse();
		//flowSendRecordService.sendTeleFlow(iphone,flowSendRecordService.genRequestNo(iphone));
		//flowSendRecordService.getWaitFlowSendRecord(Config.get("telecom800.channel.code"));
		return response;
	}
	
}
