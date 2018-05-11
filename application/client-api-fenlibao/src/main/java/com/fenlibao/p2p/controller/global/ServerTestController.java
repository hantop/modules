package com.fenlibao.p2p.controller.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dimeng.util.StringHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.thirdparty.yishang.util.YishangUtil;

/**
 * 模拟连连server，测试使用
 * @author yangzengcai
 * @date 2015年9月20日
 */
@RequestMapping("server")
@RestController
public class ServerTestController {

	@RequestMapping("lianlian/withdraw")
	public void withdraw(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("----------Imitation Lianlian Success-----------");
		Map<String, String> params = new HashMap<String, String>();
		PrintWriter writer = null;
		InputStream in = null;
			try {
				response.setContentType("text/html");
				response.setCharacterEncoding("UTF-8");
				in = request.getInputStream();
				writer = response.getWriter();
				params.putAll(this.readReqStr(in));
				System.out.println(params);
				returnMsg(response, writer);
			} catch (Throwable e) {
				e.printStackTrace();
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
	
	private void returnMsg(HttpServletResponse response, PrintWriter writer)
			throws JsonProcessingException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		String msg =  "{\"ret_code\":\"0000\",\"ret_msg\":交易成功\",\"sign_type\":\"RSA\",\"sign\":\"ZPZULntRpJwFmGNIVKwjLEF2Tze7bqs60rxQ22CqT5J1UlvGo575QK9z/+p+7E9cOoRoWzqR6xHZ6WVv3dloyGKDR0btvrdqPgUAoeaX/YOWzTh00vwcQ+HBtXE+vPTfAqjCTxiiSJEOY7ATCF1q7iP3sfQxhS0nDUug1LP3OLk=\"}";
		Random random = new Random();
		if (random.nextInt() % 2 == 0) {
			msg = "{\"ret_code\":\"1001\",\"ret_msg\":\"商户请求签名未通过\"}";
		}
		writer.write(msg);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> readReqStr(InputStream is) throws Throwable {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		ObjectMapper objm = new ObjectMapper();
		Map<String, String> params = objm.readValue(sb.toString(), Map.class);
		return params;
	}
	
	////////////////////////yishang////////////
	class YSCallbackVO {
		private String customOrderCode;
		private String orderId;
		private String code;
		private String phone;
		private String status;
		private String sign;
		public String getCustomOrderCode() {
			return customOrderCode;
		}
		public void setCustomOrderCode(String customOrderCode) {
			this.customOrderCode = customOrderCode;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
	}
	
	@RequestMapping("yishang/topup")
	public Map<String, String> yishang(String service, String phone, String operation, String customOrderCode,
			String userId, String prizeId, String prizePriceTypeId, String orderId, String sign) {
		System.out.println("-----------------------------------------");
		System.out.println("-------------yishang top up--------------");
		System.out.println("-----------------------------------------");
		Map<String, String> response = new HashMap<>();
		response.put("result", "10000");
		response.put("message", "请求成功");
		response.put("canRecharge", "yes");
		try {
			Map<String, String> params = new HashMap<>();
			params.put("service", "getAward");//服务接口
			params.put("phone", phone);//电话
			params.put("operation", "recharge");//operation直充标记，默认recharge
			params.put("customOrderCode", customOrderCode);//客户订单号
			params.put("userId", userId);//用户id，由易赏平台提供
			params.put("prizeId", prizeId);//奖品id，由易赏平台提供
			params.put("prizePriceTypeId", prizePriceTypeId);//面值id，由易赏平台提供
			params.put("orderId", orderId);//订单id，由易赏平台提供
			String mySign = YishangUtil.getSign(params, Config.get("ys.key"));
			if (mySign.equals(sign)) {
				
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return response;
	}
	
	
}
