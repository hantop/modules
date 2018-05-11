package com.fenlibao.thirdparty.yishang;

/**
 * 易赏-常量配置  
 * 
 * @author junda.feng 2016-4-20
 * 
 */
public interface YishangConst {
	
	
	String canRecharge_result_success ="10000";//手机号码限制查询 成功
	
	String canRecharge_success ="yes";//手机号码限制查询  可以充值
	
	String getAward_result_success ="10000";//充值成功
	
	String resultCode_success ="10000";//回调成功
	
	String resultCode_fail ="10001";//回调失败
	
	String resultCode_push_repeat ="10002";////推送重复
	
	String resultCode_code_error ="10003";//code 错误
	
	String resultCode_code_no_exist ="10004";//customOrderCode 不存在
	
	String resultCode_phone_error ="10005";//phone 错误
	
	String result_state_charge_get="API_CHARGE_GET";//发放成功（直充奖品，如：话费）
	
	String result_state_get="API_GET";//发放成功（非直充)
	
	String result_state_error="API_ERROR";//发放失败（直充奖品，如：话费）
	
	String result_state_inchage="IN_CHARGE";//处理中（直充奖品，如：话费）

	String result_order_no_exist="12";//customOrderCode 不存在


}
