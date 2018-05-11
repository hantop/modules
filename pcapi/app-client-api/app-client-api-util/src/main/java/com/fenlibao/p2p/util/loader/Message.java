package com.fenlibao.p2p.util.loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Message {

	private static Properties props = new Properties();

	public static void message() {
		Message message = new Message();
		message.loadMessage();
	}

	private void loadMessage() {
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(
					Config.class.getClassLoader().getResourceAsStream("message.properties"), "UTF-8");
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
	 * 登录失效，请重新登录
	 */
	public static final String MESSAGE_INVALID_LOGIN="message.invalid.login";
	
	/**
	 * 您的帐号在另一台设备登录，请重新登录
	 */
	public static final String MESSAGE_OTHER_DEVICE="message.other.device";
	
	/**
	 * 很抱歉！您的帐号不能正常使用，请联系客服
	 */
	public static final String MESSAGE_UNAVAILABLE_ACCOUNT="message.unavailable.account";
	
	/**
	 * Internal server error
	 */
	public static final String STATUS_12138="12138";
	
	/**
	 * 请求失败
	 */
	public static final String STATUS_213="213";
	
	/**
	 * 服务器异常
	 */
	public static final String STATUS_500="500";
	
	/**
	 * 未登录
	 */
	public static final String STATUS_1001="1001";
	
	/**
	 * 会话过期
	 */
	public static final String STATUS_1002="1002";
	
	/**
	 * 验证码错误
	 */
	public static final String STATUS_1003="1003";
	
	/**
	 * 验证码过期
	 */
	public static final String STATUS_1004="1004";
	
	/**
	 * 未获取到openticket
	 */
	public static final String STATUS_1010="1010";
	
	/**
	 * openticket不匹配
	 */
	public static final String STATUS_1011="1011";
	
	/**
	 * openticket过期
	 */
	public static final String STATUS_1012="1012";
	
	/**
	 * 参数为空
	 */
	public static final String STATUS_1013="1013";
	
	/**
	 * 当日验证错误次数已达上限
	 */
	public static final String STATUS_1005="1005";
	
	/**
	 * 手机号码已存在
	 */
	public static final String STATUS_1006="1006";
	
	/**
	 * 邮箱已存在
	 */
	public static final String STATUS_1007="1007";
	
	/**
	 * 此功能今天发送手机验证码次数已达上限
	 */
	public static final String STATUS_1008="1008";
	
	/**
	 * 用户名不存在
	 */
	public static final String STATUS_1014="1014";
	
	/**
	 * 密码错误
	 */
	public static final String STATUS_1015="1015";
	
	/**
	 * 密码格式错误
	 */
	public static final String STATUS_1016="1016";
	/**
	 * 新旧密码一致
	 */
	public static final String STATUS_1017="1017";
	/**
	 * 您的手机号码已注册，请点击登录
	 */
	public static final String STATUS_1018="1018";
	/**
	 * 手机号码格式不正确
	 */
	public static final String STATUS_1019="1019";
	/**
	 * 此用户名已存在 
	 */	
	public static final String STATUS_1020="1020";
	/**
	 * 参数类型错误
	 */
	public static final String STATUS_1021="1021";
	/**
	 * 用户名格式不正确
	 */
	public static final String STATUS_1022="1022";
	/**
	 * 用户名不可更改
	 */
	public static final String STATUS_1023="1023";
	/**
	 * 用户不存在（注意，不是用户名不存在）
	 */
	public static final String STATUS_1024="1024";
	/**
	 * 充值异常
	 */
	public static final String STATUS_1025="1025";
	/**
	 * 用户安全认证信息不存在
	 */
	public static final String STATUS_1026="1026";
	/**
	 * 身份认证或手机认证不通过
	 */
	public static final String STATUS_1027="1027";
	/**
	 * 参数错误
	 */
	public static final String STATUS_1028="1028";
	/**
	 * token验证不通过
	 */
	public static final String STATUS_1029="1029";
	/**
	 * 提现异常
	 */
	public static final String STATUS_1030="1030";
	/**
	 * 操作失败
	 */
	public static final String STATUS_1031="1031";
}
