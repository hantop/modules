package com.fenlibao.p2p.controller.noversion.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.util.loader.Config;

@RestController
@RequestMapping("/lianlianPay")
public class PaymentTestController {

	@RequestMapping(value = "test")
	public ModelAndView test(HttpServletResponse response) {
		response.addHeader(Config.get("header.authorization"), Config.get("header.authorization.value"));
//		String cardNo = AES.getInstace().encrypt("".getBytes());
//		System.out.println(cardNo);
		return new ModelAndView("/pay/lianlian/auth");
	}
		
	@RequestMapping(value = "auth/web")
	public ModelAndView auth_web(HttpServletResponse response) {
		response.addHeader(Config.get("header.authorization"), Config.get("header.authorization.value"));
//		String cardNo = AES.getInstace().encrypt("".getBytes());
//		System.out.println(cardNo);
		return new ModelAndView("/pay/lianlian/auth_web");
	}
	
	@RequestMapping(value = "quick")
	public ModelAndView quick(HttpServletResponse response) {
		response.addHeader(Config.get("header.authorization"), Config.get("header.authorization.value"));
//		String cardNo = AES.getInstace().encrypt("".getBytes());
//		System.out.println(cardNo);
		return new ModelAndView("/pay/lianlian/quick");
	}
	@RequestMapping(value = "decrypt")
	public Map<String, String> decrypt(String req_data) {
		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isNotBlank(req_data))
			data.put("req_data", AES.getInstace().decrypt(req_data));
		return data;
	}
	
}
