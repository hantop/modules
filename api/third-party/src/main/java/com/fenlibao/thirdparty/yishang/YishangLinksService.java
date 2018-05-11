package com.fenlibao.thirdparty.yishang;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fenlibao.thirdparty.yishang.util.YishangUtil;

/**
 * 易赏-充值接口参数封装
 * @author junda.feng 2016-4-20
 *
 */
public class YishangLinksService {
	
	/**
	 * 通用兑换接口
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public static  Map<String,String>  getAwardParm(String userId,String key,String url,
			String customOrderCode,String phone,String prizeId,String prizePriceTypeId,String orderId) throws UnsupportedEncodingException{
			Map<String,String> map = new HashMap<String,String>();
			map.put("service", "getAward");//服务接口
			map.put("phone", phone);//电话
			map.put("operation", "recharge");//operation直充标记，默认recharge
			map.put("customOrderCode", customOrderCode);//客户订单号
			map.put("userId", userId);//用户id，由易赏平台提供
			map.put("prizeId", prizeId);//奖品id，由易赏平台提供
			map.put("prizePriceTypeId", prizePriceTypeId);//面值id，由易赏平台提供
			map.put("orderId", orderId);//订单id，由易赏平台提供
			map.put("sign", YishangUtil.getSign(map, key));
		return map;
	}
	
	/**
	 *  三网接口，自动识别供应商，选择对应供应商奖品发放
	 * @return
	 */
	public static Map<String,String> getTraffic(String customOrderCode,String phone,
			String userId,String key,String orderId,
			String ydPrizeId,String ydPrizePriceTypeId,
			String ltPrizeId,String ltPrizePriceTypeId,
			String dxPrizeId,String dxPrizePriceTypeId){
		Map<String,String> map = new HashMap<String,String>();
		try{
			map.put("service", "getTraffic");//服务接口
			map.put("phone", phone);//电话
			map.put("operation", "recharge");//operation
			map.put("customOrderCode",customOrderCode);//客户订单号
			map.put("userId", userId);//用户id，由易赏平台提供
			map.put("ydPrizeId",ydPrizeId);//移动奖品id，由易赏平台提供
			map.put("ydPrizePriceTypeId", ydPrizePriceTypeId);//移动面值id，由易赏平台提供
			map.put("ltPrizeId", ltPrizeId);//联通奖品id，由易赏平台提供
			map.put("ltPrizePriceTypeId", ltPrizePriceTypeId);//联通面值id，由易赏平台提供
			map.put("dxPrizeId", dxPrizeId);//电信奖品id，由易赏平台提供
			map.put("dxPrizePriceTypeId", dxPrizePriceTypeId);//电信面值id，由易赏平台提供
			map.put("orderId", orderId);//订单id，由易赏平台提供
			map.put("sign", YishangUtil.getSign(map, key));
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 查询接口--手机号码充值限制
	 * @param phone 电话号码
	 * @return
	 * @throws IOException 
	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String,Object> canRecharge(String phone,String userId,String key,String url) throws IOException {
//		Map<String,Object> resultMap=null;
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("service", "canRecharge");//服务接口
//		map.put("phone", phone);//客户订单号
//		map.put("userId", userId);
//		map.put("sign", YishangUtil.getSign(map, key));
//		String result = YishangUtil.send(url, map);//{"message":"请求成功","result":"10000","canRecharge":"yes"}
//		resultMap=(Map<String,Object>)JSON.parse(result);
//		return resultMap;
//	}
	/**
	 * 充值接口--通用兑换接口（话费等）
	 * @param phone   手机号,必填 
	 * @param customOrderCode  客户订单号,必填 
	 * @param orderId 订单 id，由易赏平台提供,必填
	 * @param prizeId 奖品 id，由易赏平台提供,非必填
	 * @param prizePriceTypeId 奖品面值 id，由易赏平台提供,必填
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static  Map<String,Object>  getAward(String userId,String key,String url,
//			String customOrderCode,String phone,String prizeId,String prizePriceTypeId,String orderId){
//		Map<String,Object> resultMap=null;
//		try{
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("service", "getAward");//服务接口
//			map.put("phone", phone);//电话
//			map.put("operation", "recharge");//operation直充标记，默认recharge
//			map.put("customOrderCode", customOrderCode);//客户订单号
//			map.put("userId", userId);//用户id，由易赏平台提供
//			map.put("prizeId", prizeId);//奖品id，由易赏平台提供
//			map.put("prizePriceTypeId", prizePriceTypeId);//面值id，由易赏平台提供
//			map.put("orderId", orderId);//订单id，由易赏平台提供
//			map.put("sign", YishangUtil.getSign(map, key));
//			String result = YishangUtil.send(url, map);//{"customOrderCode":"1461205935295","code":"HFA4936747AK6","couponCode":"","result":"10000","message":"请求成功"}
//			resultMap=(Map<String,Object>)JSON.parse(result);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return resultMap;
//	}
	
	
	/**
	 * 兑换查询接口
	 * @param customOrderCode 	客户订单号,必填
	 * @param orderId 			订单id，由易赏平台提供,必填
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object>  queryStatus(String customOrderCode,String orderId,
			  String userId,String key,String url){
		Map<String,Object> resultMap=null;
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("service", "queryStatus");//服务接口
			map.put("customOrderCode", customOrderCode);//客户订单号
			map.put("userId", userId);//用户id，由易赏平台提供
			map.put("orderId", orderId);//订单id，由易赏平台提供
			map.put("sign", YishangUtil.getSign(map, key));
			String result = YishangUtil.send(url, map);
			resultMap=(Map<String,Object>)JSON.parse(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	public static void main(String[] args) {
		String userId="3655";
		String key="EBteDrk4AdN1GxQ+o-PB!10R#YUD0PmA";
		String url="http://api.1shang.com/service/apiService";
		String customOrderCode="ys_14793898212012711";
		String orderId="3753";

		Map<String,Object> res=queryStatus(customOrderCode,orderId,
				userId,key,url);
		System.out.println(JSON.toJSONString(res));
		if(res!=null&& YishangConst.result_order_no_exist.equals(res.get("result"))){
			System.out.println("customOrderCode不存在");
		}


	}
}
