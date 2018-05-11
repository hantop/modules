package com.fenlibao.p2p.util.mp.topup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 手机充值订单工具类
 * @author yangzengcai
 * @date 2016年2月19日
 */
public class OrderUtil {

	private static final String JF_PREFIX = "jf";//劲峰
	private static final String YS_PREFIX = "ys";//易赏
	
	private static final String TIME_FORMAT = "yyyyMMddHHmmss";
	private static final String UNDERLINE = "_";
	private static final SimpleDateFormat SDF = new SimpleDateFormat(TIME_FORMAT);
	
	/**
	 * 获取订单流水号
	 * @param orderId
	 * @param orderCreateTime
	 * @return
	 */
	public static String getOrderNo_JF() {
		return JF_PREFIX + UNDERLINE + nowDate()+randomNumber(4);
	}
	/**
	 * 获取订单流水号--易赏
	 * @param orderId
	 * @param orderCreateTime
	 * @return
	 */
	public static String getOrderNo_YS() {
		return YS_PREFIX + UNDERLINE + nowDate()+randomNumber(4);
	}
	
	
	public static long nowDate() {
        final Date date = new Date();
        return date.getTime();
    }
	
	
	
	/**
	 * 获取随机数
	 * @param size 位数
	 * @return
	 */
	public static String randomNumber(int size) {
        final StringBuffer ret = new StringBuffer();
        Random random = new Random();
        char[] codeSequence = "1234567890".toCharArray();
        String code = null;
        for (int i = 0; i < size; i++) {
            code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            ret.append(code);
        }
        return ret.toString();
    }
	
	public static Date StringToDate(String dateStr,String formatStr){
		  DateFormat sdf = new SimpleDateFormat(formatStr);
		  Date date = null;
		  try {
			  date = sdf.parse(dateStr);
		  } catch (ParseException e) {
			  e.printStackTrace();
		  }
		  return date;
    }
	
	
	public static void main(String[] args) {
		
		Date t = OrderUtil.StringToDate("20160224160730", "yyyyMMddHHmmss");
		System.out.println(t.getTime());
	}
	
}
