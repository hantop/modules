package com.fenlibao.p2p.util.pay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderUtil {

	/**
	 * 流水号前缀
	 */
	public static final String LIANLIAN_PREFIX = "llp"; //认证支付、提现
	public static final String LIANLIAN_PREFIX_QUICK = "llq"; //快捷支付
	public static final String REFUND_PREFIX = "tk"; //退款
	/**
	 * 下划线
	 */
	private static final String UNDERLINE = "_";
	/**
	 * 时间格式
	 */
	private static final String DATE_FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	/**
	 * 测试后缀
	 */
	private static final String TEST_SUFFIX = "test";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_yyyyMMddHHmmss);
	
	public static int getOrderIdByNoOrder(String no_order) {
		if (no_order != null) {
			String[] arr = no_order.split(UNDERLINE);
			if (arr.length > 1) {
				return Integer.parseInt(arr[1]);
			}
		}
		return 0;
	}
	
	/**
	 * 生成用于连连充值的订单ID
	 * @param orderId
	 * @param date （这里的时间必须是从数据获取的订单时间）
	 * @return
	 */
	public static String genLlpRechargeOrderId(int orderId, Date OrderTime) {
		return LIANLIAN_PREFIX + UNDERLINE + orderId + UNDERLINE + sdf.format(OrderTime);
	}
	/**
	 * 连连快捷流水单号
	 * @param orderId
	 * @param date 订单创建时间
	 * @return
	 */
	public static String genLLQuickSN(int orderId, Date OrderTime) {
		return LIANLIAN_PREFIX_QUICK + UNDERLINE + orderId + UNDERLINE + sdf.format(OrderTime);
	}
	
	/**
	 * 生成用于连连充值的订单ID(测试)
	 * @param orderId
	 * @param date （这里的时间必须是从数据获取的订单时间）
	 * @return
	 */
	public static String genLlpRechargeOrderId_Test(int orderId, Date OrderTime) {
		return LIANLIAN_PREFIX + UNDERLINE + orderId + UNDERLINE + sdf.format(OrderTime) + UNDERLINE + TEST_SUFFIX;
	}
	
	/**
	 * 生成用于连连提现的订单ID
	 * @param orderId
	 * @param date （这里的时间必须是从数据获取的订单时间）
	 * @return
	 */
	public static String genLlpWithdrawOrderId(int orderId, Date OrderTime) {
		return LIANLIAN_PREFIX + UNDERLINE + orderId + UNDERLINE + sdf.format(OrderTime);
	}
	
	/**
	 * 生成用于连连提现的订单ID(测试)
	 * @param orderId
	 * @param date （这里的时间必须是从数据获取的订单时间）
	 * @return
	 */
	public static String genLlpWithdrawOrderId_Test(int orderId, Date OrderTime) {
		return LIANLIAN_PREFIX + UNDERLINE + orderId + UNDERLINE + sdf.format(OrderTime) + UNDERLINE + TEST_SUFFIX;
	}
	
	/**
	 * 生成用于连连的userId
	 * @param userId
	 * @return
	 */
	public static String genLlpUserId(int userId) {
		return LIANLIAN_PREFIX + UNDERLINE + userId;
	}
	
	/**
	 * 生成用于连连的userId（测试）
	 * @param userId
	 * @return
	 */
	public static String genLlpUserId_Test(int userId) {
		return LIANLIAN_PREFIX + UNDERLINE + userId + UNDERLINE + TEST_SUFFIX;
	}
	
//-----------------------------------------//
	
	/**
	 * 获取测试用户ID
	 * @param user_id (llp_1234)
	 * @return
	 */
	public static String getLLP_user_id_test(String user_id) {
		return user_id + UNDERLINE + TEST_SUFFIX;
	}
	
	/**
	 * 获取测试流水号
	 * @param no_order
	 * @return
	 */
	public static String getLLP_no_order_test(String no_order) {
		return no_order + UNDERLINE + TEST_SUFFIX;
	}
	
	/**
	 * 生成流水号
	 * @param prefix 流水号前缀
	 * @param orderId 订单ID
	 * @param createTime 订单创建时间
	 * @return
	 */
	public static String genSerialNumber(String prefix, int orderId, Date createTime) {
		return prefix + UNDERLINE + orderId + UNDERLINE + sdf.format(createTime);
	}
	
	/**
	 * @param dateTime
	 * @return dt_order
	 */
	public static String getDtOrder(Date dateTime) {
		return sdf.format(dateTime);
	}
}
