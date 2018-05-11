package com.fenlibao.platform.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public final static String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
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
        	//logger.error(e.getMessage(),e);
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
        	//logger.error(e.getMessage(),e);
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
	 *月差
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthSpace(String date1, String date2) {
		int result = 0;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
			result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
			result = result == 0 ? 1 : Math.abs(result);
		}catch (Exception e){
			result = 0;
		}

		return result ;

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
		 
}
