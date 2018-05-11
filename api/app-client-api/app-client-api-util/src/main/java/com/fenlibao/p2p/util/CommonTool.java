package com.fenlibao.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.util.enums.FrontEndChineseContrastEnum;
import com.fenlibao.p2p.util.http.HttpRequest;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class CommonTool {

	/**
	 * 根据 request获取完整的域名，如http://www.abc.com:8080
	 * 如果端口为80则为:http://www.abc.com
	 * @return
	 */
	public static String getDomain(HttpServletRequest request){
		String port  = ""+request.getServerPort();
		if(port.equals("80")){
			port="";
		}else{
			port =":"+ port;
		}
		
		String contextPath = request.getContextPath();
		if(contextPath.equals("/")){
			contextPath="";
		}
		
		String domain ="http://"+ request.getServerName()+port;
		domain+=contextPath;
		return domain;
	}
	
	/**
	 * 获取随机数
	 * @param size 位数
	 * @return
	 */
	public static String randomNumber(int size) {
        final StringBuffer ret = new StringBuffer();
        Random random = new Random();
        char[] codeSequence = "123456789".toCharArray();
        String code = null;
        for (int i = 0; i < size; i++) {
            code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            ret.append(code);
        }
        return ret.toString();
    }
	
	 public static String randomNumber() {
	        Random random = new Random();
	        char[] codeSequence = "123456789".toCharArray();
	        String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
	        return code;
	 }
	
	/**
	 * bean 转换为map
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static final Map<String, Object> toMap(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}
	/**
	 * bean 转换为map
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static final Map<String, Object> toMapDefaultNull(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, null);
				}
			}
		}
		return returnMap;
	}

	public static final Map<String, Object> toMapDefaultNull(Object bean,List<String> ignore)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if(!ignore.contains(propertyName)){
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						returnMap.put(propertyName, null);
					}
				}
			}
		}
		return returnMap;
	}
	/**
	 * 版本号比较 前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * @return
	 */
	public static int comparVersion(String version1,String version2){
	    String[] versionArray1 = version1.split("\\.");
	    String[] versionArray2 = version2.split("\\.");
		if(versionArray1.length < versionArray2.length){
			version1= version1+".0";
			versionArray1 = version1.split("\\.");
		}else if(versionArray1.length > versionArray2.length){
			version2= version2+".0";
			versionArray2 = version1.split("\\.");
		}
		
	    int idx = 0;
	    int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
	    int diff = 0;
	    while (idx < minLength
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
	        ++idx;
	    }
	    //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
	    return diff;
	}
	
	/**
	 * 参数追加到URL后面
	 * @param url 
	 * @param param
	 * @return
	 */
	public static String convertParamToUrl(String url,Map<String,String> param){
		if(param == null){
			return url;
		}
		String beginLetter="?";
		StringBuffer sb = new StringBuffer(url);
		Set<String> key = param.keySet();
		
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if(sb.toString().endsWith(beginLetter)){
				sb.append(s).append("=").append(param.get(s));
			}else{
				if(!sb.toString().contains(beginLetter)){
					sb.append(beginLetter).append(s).append("=").append(param.get(s));
				}else{
					sb.append("&").append(s).append("=").append(param.get(s));
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 验证是否是电信手机号
	 * @param phone
	 * @return
	 */
	public static boolean isTelecom(String phone){
		if(StringUtils.isEmpty(phone)){
			return false;
		}
		String telecoms = "^((133)|(153)|(177)|(18[0,1,9]))\\d{8}$"; 
		if(phone.matches(telecoms)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取客户端真实IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) { 
		String ip = request.getHeader("X-Forwarded-For");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
		if (ip != null && ip.length() > 0 && ip.contains(",")) {
			ip = ip.split(",")[0];
		}
        return ip; 
	 } 
	
	public static Color randomColor(int maxR, int maxG, int maxB) {
        Random random = new Random();
        int r = random.nextInt(maxR);
        int g = random.nextInt(maxG);
        int b = random.nextInt(maxB);
        return new Color(r, g, b);
    }
	
	public static double randomAngle(int max) {
        Random random = new Random();
        int f = (0 - 1) * random.nextInt(2);
        int a = random.nextInt(max + 1);
        return Math.toRadians(f * a);
    }
	
	public static Font randomFont(int height, int maxHeight) {
        Random random = new Random();
        String[] fontFamily = {"Arial", "Courier New"};
        height = height + random.nextInt(maxHeight + 1);
        Font font = new Font(fontFamily[random.nextInt(fontFamily.length)], Font.PLAIN, height);
        return font;
    }
	
	public static String genShortUrl(String longUrl){
		longUrl = java.net.URLEncoder.encode(longUrl);
		String source = Config.get("weibo.api.appkey");
		if(StringUtils.isBlank(source)){
			return source;
		}
		String[] sourceArray = source.split(",");
		for(String key:sourceArray){
			String shortUrl = genShortUrl(key,longUrl);
			if(StringUtils.isNoneBlank(shortUrl)){
				return shortUrl;
			}
		}
		return null;
	}
	
	public static String genShortUrl(String source, String longUrl){
		String actionUrl = Config.get("weibo.api.short.url");
		String result = HttpRequest.sendPost(actionUrl, "source="+source+"&url_long="+longUrl);
		if(result==null || "".equals(result)){
			return "";
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		List<Map<String,Object>> jsonList=(List<Map<String,Object>> )jsonObject.get("urls");
		if(jsonList!=null&&jsonList.size()>0){
			for(Map<String,Object> jsonMap : jsonList){
				int type = (int)jsonMap.get("type");
				boolean resultType = (Boolean) jsonMap.get("result");
				if(type==0&&resultType){
					return String.valueOf(jsonMap.get("url_short"));
				}
			}
		}
		return null;
	}
	
	public static  boolean isChinese(char c) {   
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
	        return true;  
	    }  
	    return false;  
	}

	/**
	 * 传入一个实体类，将所有属性封装成key，value形式的list返回
	 * @param o
	 * @return
	 */
	public static List<Map<String, Object>> voToListMap(Object o){
		List<Map<String, Object>> list = new ArrayList<>();

		for (Field field : o.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Map<String, Object> map = new HashMap<>();
				map.put("key", FrontEndChineseContrastEnum.getFrontEndChineseContrastEnum(field.getName()));
				map.put("value", field.get(o) != null ? field.get(o) : "");
				list.add(map);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 获取请求IP
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if(StringUtils.isNotBlank(ip) && ip.contains(",")){
			ip =  ip.split(",")[0];
		}
		return ip;
	}
}
