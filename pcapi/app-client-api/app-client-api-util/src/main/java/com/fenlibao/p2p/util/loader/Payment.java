package com.fenlibao.p2p.util.loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 支付相关变量
 * @author yangzengcai
 * @date 2015年8月26日
 */
public class Payment {

	private static Properties props = new Properties();

	public static void payment() {
		Payment payment = new Payment();
		payment.loadPayment();
	}

	private void loadPayment() {
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(
					Config.class.getClassLoader().getResourceAsStream("payment.properties"), "UTF-8");
			props.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String get(String key) {
		return props.getProperty(key);
	}

	public static String get(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static Object get(Object obj) {
		return props.get(obj);
	}
	
	/**
	 * 商户号（正式）
	 */
	public static final String OID_PARTNER = "oid_partner";
	/**
	 * 商户号(测试)
	 */
	public static final String OID_PARTNER_TEST = "oid_partner_test";
	/**
	 * 快捷商户号(测试)
	 */
	public static final String OID_PARTNER_QUICK_TEST = "oid_partner_quick_test";
	/**
	 * 签名方式
	 */
	public static final String SIGN_TYPE = "sign_type";
	/**
	 * 商户业务类型
	 */
	public static final String BUSI_PARTNER = "busi_partner";
	/**
	 * 请求应用标识
	 */
	public static final String APP_REQUEST = "app_request";
	/**
	 * 充值金额一分钱
	 */
	public static final String MONEY_ORDER = "money_order";
	/**
	 * 充值结果服务器异步通知地址
	 */
	public static final String NOTIFY_URL_RECHARGE = "notify_url_recharge";
	/**
	 * 连连快捷支付异步通知地址
	 */
	public static final String NOTIFY_URL_QUICK_PAYMENT = "notify_url_quick_payment";
	/**
	 * 支付结束回显地址
	 */
	public static final String URL_RETURN = "url_return";
	/**
	 * 快捷支付结束回显地址
	 */
	public static final String URL_RETURN_QUICK_PAYMENT = "url_return_quick_payment";
	/**
	 * 连连页面颜色
	 */
	public static final String BG_COLOR = "bg_color";
	/**
	 * 连连字体颜色
	 */
	public static final String FONT_COLOR = "font_color";
	/**
	 * 提现签名方式
	 */
	public static final String SIGN_TYPE_WITHDRAW = "sign_type_withdraw";

/////////////////////////////////////////////
	
	/**
	 * 提现结果服务器异步通知地址
	 */
	public static final String WITHDRAW_NOTIFY_URL = "WITHDRAW_NOTIFY_URL";

	//########支付变量##############

	//#### 充值 #########
	/**
	 * 充值表单提交地址
	 */
	public static final String WAP_RECHARGE_FORMURL = "WAP_RECHARGE_FORMURL";
	/**
	 * 快捷支付form表单提交路径
	 */
	public static final String WAP_QUICK_PAYMENT_FORMURL = "WAP_QUICK_PAYMENT_FORMURL";

	/**
	 * 是否是支付模块测试
	 */
	public static final String IS_PAY_TEST = "IS_PAY_TEST";
	/**
	 * 充值测试金额
	 */
	public static final String RECHARGE_TEST_AMOUNT = "RECHARGE_TEST_AMOUNT";
	/**
	 * 充值是否需要实名认证
	 */
	public static final String CHARGE_MUST_NCIIC = "CHARGE_MUST_NCIIC";
	/**
	 * 充值是否需要手机认证
	 */
	public static final String CHARGE_MUST_PHONE = "CHARGE_MUST_PHONE";
	/**
	 * 充值是否需要邮箱认证
	 */
	public static final String CHARGE_MUST_EMAIL = "CHARGE_MUST_EMAIL";
	/**
	 * 充值最低金额(元)
	 */
	public static final String CHARGE_MIN_AMOUNT = "CHARGE_MIN_AMOUNT";
	/**
	 * 充值最高金额(元)
	 */
	public static final String CHARGE_MAX_AMOUNT = "CHARGE_MAX_AMOUNT";
	/**
	 * 充值最高手续费（元）
	 */
	public static final String CHARGE_MAX_POUNDAGE = "CHARGE_MAX_POUNDAGE";
	/**
	 * 用户充值手续费率
	 */
	public static final String CHARGE_RATE = "CHARGE_RATE";
	/**
	 * 充值是否需要设置交易密码
	 */
	public static final String CHARGE_MUST_WITHDRAWPSD = "CHARGE_MUST_WITHDRAWPSD";
	/**
	 * 连连支付收取平台充值手续费率
	 */
	public static final String CHARGE_RATE_LIANLIAN = "CHARGE_RATE_LIANLIAN";
	/**
	 * 提现: 最低提取金额（单位：元）
	 */
	public static final String WITHDRAW_MIN_FUNDS = "WITHDRAW_MIN_FUNDS";
	/**
	 * #提现: 最高提取金额（单位：元）
	 */
	public static final String WITHDRAW_MAX_FUNDS = "WITHDRAW_MAX_FUNDS";
	/**
	 * 提现: 大于或等于此金额时，提现需经过审核（单位：元）
	 */
	public static final String WITHDRAW_LIMIT_FUNDS = "WITHDRAW_LIMIT_FUNDS";
	/**
	 * 是否使用云通讯接口
	 */
	public static final String SMS_IS_USE_YTX = "SMS_IS_USE_YTX";
	/**
	 * 提现手续费:按比例计算,每笔收取其金额的比例, 如0.001
	 */
	public static final String WITHDRAW_POUNDAGE_PROPORTION = "WITHDRAW_POUNDAGE_PROPORTION";
	/**
	 * #提现手续费: 扣除方式(true:内扣，从提现金额里面扣，false：外扣，提现金额+提现手续费)
	 */
	public static final String TXSXF_KCFS = "TXSXF_KCFS";
	/**
	 * #提现: 手续费计算方式, ED:按额度(默认);BL:按比例
	 */
	public static final String WITHDRAW_POUNDAGE_WAY = "WITHDRAW_POUNDAGE_WAY";
	/**
	 * 提现手续费:按额度计算,每笔(1-5万)收取的手续费
	 */
	public static final String WITHDRAW_POUNDAGE_1_5 = "WITHDRAW_POUNDAGE_1_5";
	/**
	 * 提现手续费:按额度计算,每笔(5-20万)收取的手续费
	 */
	public static final String WITHDRAW_POUNDAGE_5_20 = "WITHDRAW_POUNDAGE_5_20";
	/**
	 * "推广: 是否开启推广,true:开启,false,不开启"
	 */
	public static final String ACCOUNT_SFTG = "ACCOUNT_SFTG";
	/**
	 * 连连支付_RSA加密公钥
	 */
	public static final String RSA_PB_KEY = "RSA_PB_KEY";
	/**
	 * #提现固定手续费1元
	 */
	public static final String WITHDRAW_POUNDAGE_1_RMB = "WITHDRAW_POUNDAGE_1_RMB";
	/**
	 * #连连支付_RSA加密私钥
	 */
	public static final String RSA_P_KEY = "RSA_P_KEY";
	/**
	 * #提现申请地址
	 */
	public static final String WITHDRAW_APPLY = "WITHDRAW_APPLY";
	/**
	 * #连连支付_Md5_加密KEY（正式）
	 */
	public static final String MD5_KEY = "MD5_KEY";
	/**
	 * #连连快捷
	 */
	public static final String MD5_KEY_QUICK = "MD5_KEY_QUICK";
	/**
	 * #连连快捷
	 */
	public static final String MD5_KEY_QUICK_TEST = "MD5_KEY_QUICK_TEST";
	/**
	 * #连连支付_Md5_加密KEY，测试用
	 */
	public static final String MD5_KEY_TEST = "MD5_KEY_TEST";
	/**
	 * 连连订单结果查询地址
	 */
	public static final String ORDER_RESULT_QUERY_URL  ="ORDER_RESULT_QUERY_URL";
	/**
	 * 是否是测试账号(true:是测试账号，false:是正式账号)
	 */
	public static final String IS_ACCOUNT_TEST = "IS_ACCOUNT_TEST";
	/**
	 * 提现服务模拟URL
	 */
	public static final String WITHDRAW_SERVER_TEST_URL = "WITHDRAW_SERVER_TEST_URL";
	/**
	 * 线下充值时间间隔（单位：小时）
	 */
	public static final String WITHDRAW_OFFLINE_INTERVAL = "WITHDRAW_OFFLINE_INTERVAL";
	
	/**
	 * #支持的银行编码
	 */
	public static final String BANK_CODES = "BANK_CODES";
	
	/**
	 * #不需要支行信息的银行编码
	 */
	public static final String NOT_REQUIRED_BANK_BRANCH_INFO = "NOT_REQUIRED_BANK_BRANCH_INFO";
	
	public static final String IS_QUICK_TEST = "IS_QUICK_TEST";
	
	/**
	 * 退款api
	 */
	public static final String LLP_REFUND_URL = "LLP_REFUND_URL";
	/**
	 * 退款回调api
	 */
	public static final String NOTIFY_URL_QUICK_REFUND = "notify_url_quick_refund";
	/**
	 * 连连快捷正式商户
	 */
	public static final String OID_PARTNER_QUICK = "oid_partner_quick";
	/**
	 * web认证支付提交路径
	 */
	public static final String WEB_RECHARGE_FORMURL = "WEB_RECHARGE_FORMURL";
	
}
