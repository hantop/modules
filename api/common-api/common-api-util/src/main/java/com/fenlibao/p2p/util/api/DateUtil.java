package com.fenlibao.p2p.util.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM = "yyyy-MM";

	/**
	 * 日期相差天数
	 *
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public static int daysOfTwo(Date minDate, Date maxDate) throws ParseException {
		minDate = YYYY_MM_DD.parse(YYYY_MM_DD.format(minDate));
		maxDate = YYYY_MM_DD.parse(YYYY_MM_DD.format(maxDate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(minDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(maxDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 自然月推算日期
	 * @param date
	 * @param monthes 不能为负数
	 * @return
	 */
	public static final long rollNaturalMonth(long date, int monthes) throws Exception{
		if (monthes <= 0) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		int firstYear=calendar.get(Calendar.YEAR);
		int firstMonth=calendar.get(Calendar.MONTH);
		int rollDays=0;
		for(int i=0,year=firstYear,month=firstMonth;i<monthes;i++,month++){
			if(month == 12){
				month=0;
				year++;
			}
			rollDays+=dayOfNaturalMonth(year,month);
		}
		calendar.add(Calendar.DAY_OF_YEAR,rollDays);
		return calendar.getTimeInMillis();
	}

	public static int nowInWitchNaturalMonth(Date startDate) throws Exception{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		int i=1;
		while(true){
			long nextNaturalMonth = rollNaturalMonth(startDate.getTime(),i);
			if(c.getTimeInMillis()<nextNaturalMonth){
				break;
			}
			i++;
		}
		return i;
	}

	private static final int dayOfNaturalMonth(int year,int month){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year); 
        c.set(Calendar.DATE, 1);
        c.set(Calendar.MONTH, month); 
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 从起始年月开始推算第n个月后的自然月天数
	 * @param starttime
	 * @param monthes
	 * @return
	 */
	public static final int dayOfTargetNaturalMonth(Date starttime, int monthes) throws Exception{
        Calendar c = Calendar.getInstance();
        c.setTime(starttime);
        int firstYear=c.get(Calendar.YEAR);
        int firstMonth=c.get(Calendar.MONTH);
        int result=dayOfNaturalMonth(firstYear,firstMonth);
		if (monthes <= 0) {
			return result;
		}
		for(int i=1,year=firstYear,month=firstMonth;i<=monthes;i++){
			month=month+1;
			if(month == 12){
				month=0;
				year++;
			}
			if(i==monthes){
				result=dayOfNaturalMonth(year,month);
			}
		}
		return result;
	}
	
	/**
	 * 计算最后一个自然月的天数
	 * @param starttime 借款开始时间
	 * @param daysOfCycle 借款周期
	 * @return
	 */
	public static final int daysOfLastNaturalMonth(Date starttime, int daysOfCycle) throws Exception{
		int sumDaysOfNaturalMonth=0;
        for(int i=0;;i++){
        	sumDaysOfNaturalMonth+=dayOfTargetNaturalMonth(starttime, i);
        	if(daysOfCycle<=sumDaysOfNaturalMonth){
        		return dayOfTargetNaturalMonth(starttime, i);
        	}
        }
	}
	
	/**
	 * 累计自然月天数
	 * @param starttime 开始时间
	 * @param months 自然月个数
	 * @return
	 * @throws Exception
	 */
	public static final int sumDaysOfNaturalMonth(Date starttime, int months) throws Exception{
		int sumDaysOfNaturalMonth=0;
		for(int i=0;i<months;i++){
			sumDaysOfNaturalMonth+=dayOfTargetNaturalMonth(starttime, i);
		}
		return sumDaysOfNaturalMonth;
	}

	/**
	 * 日期天数加减
	 * 
	 * @param timestamp
	 *            被加/减日期
	 * @param days
	 *            天数
	 * @return
	 */
	public static final long dateAdd(long timestamp, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timestamp));
		cal.add(Calendar.DATE, days);
		return cal.getTimeInMillis();
	}

	/**
	 * 时间加减
	 * @param date
	 * @param unit Calendar.MINUTE/YEAR/MONTH/DATE
	 * @param qty
     * @return
     */
	public static Date timeAddOrSub(Date date, int unit, int qty) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(unit, qty);
		return cal.getTime();
	}

	/**
	 * 日期转换成字符串
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换成日期
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str, String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换成字符串
	 * @param date
	 * @return str
	 */
	public static String DateToString(Date date, String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String str = format.format(date);
		return str;
	}

	/**
	 * 传入一个时间，只保留部分并以日期格式返回
	 * @param date
	 * @return
	 */
	public static Date dateRemainPart(Date date, String pattern){
		String dateMonthString = DateUtil.DateToString(date, pattern);
		return DateUtil.StrToDate(dateMonthString, pattern);
	}

	public static boolean isToday(Date date){
		Calendar calendar = Calendar.getInstance();
		int currentWeek = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTime(date);
		int paramWeek = calendar.get(Calendar.DAY_OF_YEAR);
		if(paramWeek==currentWeek){
			return true;
		}
		return false;
	}

	public static synchronized String getYYYYMMDD(Date date) {
		return YYYYMMDD.format(date);
	}
	
	public static synchronized String getYYYYMMDDHHMMSS(Date date) {
		return YYYYMMDDHHMMSS.format(date);
	}
	
	public static synchronized String getYYYY_MM_DD_HH_MM_SS(Date date) {
		return YYYY_MM_DD_HH_MM_SS.format(date);
	}

	public static synchronized String getYYYY_MM_DD(Date date){
		return YYYY_MM_DD.format(date);
	}
}
