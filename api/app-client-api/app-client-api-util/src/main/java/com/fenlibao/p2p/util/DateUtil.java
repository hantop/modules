package com.fenlibao.p2p.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	private static final Logger logger=LogManager.getLogger(com.fenlibao.p2p.util.DateUtil.class);
	
	public static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public final static String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT = "yyyy-MM-dd";

	
	/**
	 * 时间转字符串yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static synchronized String getSimpleDateTime(Date date) {
        return simpleDateTimeFormat.format(date);
    }
	
	/**
	 * 时间转换为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static synchronized Date getDateFromDate(final Date date) {
        try {
            return dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
        	logger.error(e.getMessage(),e);
        }
        return null;
    }
	
	public static synchronized String getDate(Date date) {
        return dateFormat.format(date);
    }
	
	public static Date getDate(long timestamp){
		try {
			String d = dateFormat.format(timestamp);
			return dateFormat.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 日期转换成字符串yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static synchronized String getDateTime(Date date) {
        try {
            return null == date ? null : dateTimeFormat.format(date);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }
        return null;
    }
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date nowDate() {
        final Calendar now_cal = new GregorianCalendar();
        return now_cal.getTime();
    }
	
	 /**
	  * 字符串转换到时间格式
	  * @param dateStr 需要转换的字符串
	  * @param formatStr 需要格式的目标字符串 举例 yyyy-MM-dd
	  * @return Date 返回转换后的时间
	  * @throws ParseException 转换异常
	  */
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
	
	 /**
	  * 时间戳转换成时间格式yyyy-MM-dd HH:mm:ss
	  * @param timestamp 以毫秒为单位(废弃掉该函数，用timestampToDateBySec)
	  * @return
	  * @throws ParseException
	  */
	 public static Date timestampToDate(Long timestamp) {
		 try{
			 String d=dateTimeFormat.format(timestamp);
			 Date date=dateTimeFormat.parse(d);
			 return date;
		 }catch(ParseException e){
			 e.printStackTrace();
		 }
		 return null;
	 }
	 
	 /**
	  * 时间戳转换成时间格式yyyy-MM-dd HH:mm:ss
	  * @param timestamp 以秒为单位
	  * @return
	  * @throws ParseException
	  */
	 public static Date timestampToDateBySec(Long timestamp) {
		 try{
			 String d=dateTimeFormat.format(timestamp*1000);
			 Date date=dateTimeFormat.parse(d);
			 return date;
		 }catch(ParseException e){
			 e.printStackTrace();
		 }
		 return null;
	 }


	/**
	 * Date 转换成 Unix标准时间戳
 	 * @param date
	 * @return
     */
	public static long dateToTimestampToSec(Date date) {
		try{
			if(date==null){
				return 0;
			}
			return date.getTime()/1000;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	 
	 
	 /**
	  * 日期天数加减
	  * @param date 被加/减日期
	  * @param days 天数
	  * @return
	  */
	 public static Date dateAdd(Date date, int days) {
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DATE, days);

			Date newDate = cal.getTime();
			return newDate;
	}
	 
	 /**
	  * 日期时间添加秒数
	  * @param dateTime
	  * @param seconds
	  * @return
	  */
	 public static Date secondAdd(Date dateTime, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add (Calendar.SECOND, seconds);
		return calendar.getTime();
	 }

	/**
	 * 日期时间添加秒数
	 * @param dateTime
	 * @param hours
	 * @return
	 */
	public static Date hourAdd(Date dateTime, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add (Calendar.HOUR, hours);
		return calendar.getTime();
	}
	 
	 /**
	  * 日期时间添加秒数
	  * @param dateTime
	  * @param seconds
	  * @return
	  */
	 public static Timestamp secondAddToTimestamp(Date dateTime, int seconds) {
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(dateTime);
		 calendar.add (Calendar.SECOND, seconds);
		 return new Timestamp(calendar.getTimeInMillis());
	 }
	 
	 /**
	  * 月的加减
	  * @param date   被加/减日期
	  * @param month  月
	  * @return
	  */
	 public static Date monthAdd(Date date, int month) {
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.MONTH, month);

			Date newDate = cal.getTime();
			return newDate;
	}
	 /**
	  * 获取日期的天
	  * @param date
	  * @return
	  */
	 public static int getDayOfMonth(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 
		 int day=cal.get(Calendar.DAY_OF_MONTH);
		 return day;
	 }
	 
	 /**
	  * 获取日期的月
	  * @param date
	  * @return
	  */
	 public static int getMonthOfYear(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 
		 int month=cal.get(Calendar.MONTH)+1;
		 return month;
	 }
	 
	 /**
	  * 返回当前时间字符串格式（包括毫秒）
	  * <p>2015-08-26 18:29:12 103 -> 20150826182912103
	  * @return
	  */
	 public static String getCurrentDateTimeStr()
	  {
	    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    Date date = new Date();
	    String timeString = dataFormat.format(date);
	    dataFormat = null;
	    date = null;
	    return timeString;
	  }
	 

		/**
		 * 返回目标时间与当前系统时间相差多少分钟（忽略秒）
		 * @param target
		 * @return
		 */
		public static int getDifferenceMin(long target) {
			long time = System.currentTimeMillis() - target;
			time = time / 1000;
			int m = (int) (time / 60 % 60);
			int h = (int) (time / 3600);
			int result = m + h * 60;
			time = 0;
			m = 0;
			h = 0;
			return result;
		}
		
		/**
		 * 计算两个日期之间相差的 自然 天数(只算日期，忽略时间)
		 * <p>别忽略闰年！
		 * @param startDate
		 * @param endDate
		 * @return
		 */
		 public static Integer getDayBetweenDates(Date startDate,Date endDate) {
			 startDate = DateUtil.getDateFromDate(startDate);
			 endDate = DateUtil.getDateFromDate(endDate);
			  Calendar calendar = Calendar.getInstance();
			  calendar.setTime(startDate);
			  long start = calendar.getTimeInMillis();
			  calendar.setTime(endDate);
			  long end = calendar.getTimeInMillis();
			  long BetweenDates = (end - start) / (1000*3600*24);
			  Integer days = Integer.parseInt(String.valueOf(BetweenDates));
			  return days;
		}


	/**
	 * 计算两个日期之间相差的 自然 天数(不忽略时间)
	 * <p>别忽略闰年！
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer getDayBetweenDates2(Date startDate,Date endDate) {
//		startDate = DateUtil.getDateFromDate(startDate);
//		endDate = DateUtil.getDateFromDate(endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		float start = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		float end = calendar.getTimeInMillis();
		float BetweenDates = (end - start) / (1000*3600*24);
		if(BetweenDates<1f)return 0;
		Integer days =(int) BetweenDates ;
		return days;
	}

		 /**
		  * 获取没有毫秒的时间戳
		  * @param target
		  * @return
		  */
		 public static long getNoMillisecondTimestamp(Date target) {
			return target.getTime() / 1000;
		 }
		 
		 /**
		  * 时间上分钟的加减
		  * @param date
		  * @param minute
		  * @return
		  */
		 public static Date minuteAdd(Date date, int minute) {
				Calendar cal = Calendar.getInstance();
		        cal.setTime(date);
		        cal.add(Calendar.MINUTE, minute);

				Date newDate = cal.getTime();
				return newDate;
		 }

	/**
	 * long类型转Date对象,如果是后一个日期对象去一天中的最后时间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Date handleTime(long startTime,long endTime){
		Date date1=new Date(startTime);
		Date date2=new Date(endTime);
		if(isEquelDay(date1,date2)){
			return new Date(getLastTimeOfToday(date2).getTime());
		}else{
			return date2;
		}
	}

	/**
	 * 判断两个Date对象是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEquelDay(Date date1,Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		if(calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR)&&calendar1.get(Calendar.MONTH)==calendar2.get(Calendar.MONTH)&&calendar1.get(Calendar.DAY_OF_MONTH)==calendar2.get(Calendar.DAY_OF_MONTH))
			return true;
		return false;
	}

	/**
	 * 获取一天中的最后一个Date对象
	 * @param date
	 * @return
	 */
	public static Date getLastTimeOfToday(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), 23, 59,59);
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	/**
	 * 计算某一天距离当前时间天数差距--上一个还款日到当前时间
	 * @param date
	 * @return
	 */
	public static int daysToLastRepaymentDay(Date date,Date now){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(now);
		calendar1.set(calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH));
		if(calendar1.get(Calendar.DAY_OF_MONTH) <= calendar2.get(Calendar.DAY_OF_MONTH)){
			long l=calendar2.getTimeInMillis()-calendar1.getTimeInMillis();
			return new Long(l/(1000*60*60*24)).intValue();
		}else{
			calendar1.add(Calendar.MONTH,-1);
			long l=calendar2.getTimeInMillis()-calendar1.getTimeInMillis();
			return new Long(l/(1000*60*60*24)).intValue();
		}
	}

	/**
	 * 计算某一天距离当前时间天数差距--距离下一个还款日天数
	 * @return
	 */
	public static int daysToNextRepaymentDay(Date end,Date now){
		Calendar endDay = Calendar.getInstance();
		endDay.setTime(end);
		Calendar nowDay = Calendar.getInstance();
		nowDay.setTime(now);
		endDay.set(nowDay.get(Calendar.YEAR),nowDay.get(Calendar.MONTH),endDay.get(Calendar.DAY_OF_MONTH));
		if(nowDay.get(Calendar.DAY_OF_MONTH) <= endDay.get(Calendar.DAY_OF_MONTH)){
			long l=endDay.getTimeInMillis()-nowDay.getTimeInMillis();
			return new Long(l/(1000*60*60*24)).intValue();
		}else{
			endDay.add(Calendar.MONTH,1);
			long l=endDay.getTimeInMillis()-nowDay.getTimeInMillis();
			return new Long(l/(1000*60*60*24)).intValue();
		}
	}

	/**
	 * 计算某一天距离上个月同一天天数
	 * @return
	 */
	public static int daysToLastMonth(Date now){
		Calendar day = Calendar.getInstance();
		day.setTime(now);
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTime(now);
		lastMonth.add(Calendar.MONTH,-1);
		long l=day.getTimeInMillis()-lastMonth.getTimeInMillis();
		return new Long(l/(1000*60*60*24)).intValue();
	}

	/**
	 * 日期相差天数
	 *
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public static int daysOfTwo(Date minDate, Date maxDate) throws ParseException {
		if(minDate == null || maxDate == null){
			return 0;
		}
		minDate = dateFormat.parse(dateFormat.format(minDate));
		maxDate = dateFormat.parse(dateFormat.format(maxDate));
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

	private static final int dayOfNaturalMonth(int year,int month){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, month);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断两个日期相差的月份
	 * If difference is 8 months 1 days result 8 months.
	 *
	 * @website: http://stackoverflow.com/questions/27181307/how-to-find-number-of-months-between-two-date-range-in-java-using-calender-api
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int monthsBetweenDates(Date startDate, Date endDate){

		Calendar start = Calendar.getInstance();
		start.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		int monthsBetween = 0;
		int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);

		if(dateDiff<0) {
			int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
			dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrrow)-start.get(Calendar.DAY_OF_MONTH);
			monthsBetween--;

			if(dateDiff>0) {
				monthsBetween++;
			}
		}
		else {
			monthsBetween++;
		}
		monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
		monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;
		return --monthsBetween;
	}

	public static void main(String[] args) {
		String start = "02/01/2012";
		String end = "01/02/2012";
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			System.out.println(monthsBetweenDates(sdf.parse(start), sdf.parse(end)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
