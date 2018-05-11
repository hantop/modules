package com.fenlibao.thirdparty.yishang.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fenlibao.thirdparty.common.util.HttpClientUtil;


/**
 * 易赏充值接口
 * @author junda.feng
 *
 */
public class YishangUtil {
	private static final Logger logger = LogManager.getLogger(YishangUtil.class);
	
	/**
	 * 生成sign
	 * 
	 * @param map
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSign(Map<String,String> map,String key) throws UnsupportedEncodingException{
		String[] nameArr = map.keySet().toArray(new String[]{});

		Arrays.sort(nameArr);
		
		StringBuilder builder = new StringBuilder();
		
		for(String name:nameArr){
			if("sign".equals(name)) continue;
			if(map.get(name) == null) continue;
			builder.append(map.get(name));
		}
		
		return Md5(builder.toString()+key,"UTF-8");
	}
	
	/**
	 * md5加密
	 * 
	 * @param plainText
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String Md5(String plainText,String charset) throws UnsupportedEncodingException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(charset));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace();

		}
		return "null";
	}
	
	/**
	 * 发送请求，使用httpClient
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 */
	public static String send(String url ,Map<String,String> paramMap) throws IOException{
		 String result =null;
	        StringBuffer params = new StringBuffer();
	        
	        for(String key : paramMap.keySet()){
	        	 params.append(key);
		            params.append("=");
		            params.append(URLEncoder.encode(paramMap.get(key),"UTF-8"));
		            params.append("&");
	        }
	       
	        if (params.length() > 0){
	            params = params.deleteCharAt(params.length() - 1);
	        }
	        
	         result = HttpClientUtil.doPost(params.toString(), url);
	       
		 return result;
		
	}
	
	/**
	 * 发送请求，建议使用httpClient
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public static String send2(String url ,Map<String,String> paramMap){
		
		HttpURLConnection url_con = null;
	    String responseContent = null;
	    
	    long time = System.currentTimeMillis();
	    long thread = Thread.currentThread().getId();
	    
	    try{
	        StringBuffer params = new StringBuffer();
	        
	        for(String key : paramMap.keySet()){
	        	 params.append(key);
		            params.append("=");
		            params.append(URLEncoder.encode(paramMap.get(key),"UTF-8"));
		            params.append("&");
	        }
	       
	        if (params.length() > 0){
	            params = params.deleteCharAt(params.length() - 1);
	        }
	        
	        URL reqUrl = new URL(url+"?"+params);
	        logger.info("[Thread-"+thread+"]--"+time+"--send request : " + reqUrl +"?"+ params);
//	        System.out.println("[Thread-"+thread+"]--"+time+"--send request : " + reqUrl +"?"+ params);//记录日志，建议使用log4j
	        
	        url_con = (HttpURLConnection) reqUrl.openConnection();
	        url_con.setRequestMethod("POST");
	        url_con.setConnectTimeout(10000);//
	        url_con.setReadTimeout(10000);//
	        InputStream in = url_con.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	        String line = null;
	        StringBuffer tempStr = new StringBuffer();
	        String crlf=System.getProperty("line.separator");
	        
	        int i = 0;
	        while ((line = reader.readLine()) != null)
	        {
	        	if(i>0) tempStr.append(crlf);
	        	tempStr.append(line);
	        }
	        responseContent = tempStr.toString();
	        reader.close();
	        in.close();
	    }catch (IOException e){
	    	e.printStackTrace();
	    }finally{
	        if (url_con != null){
	            url_con.disconnect();
	        }
	    }
	    logger.info("[Thread-"+thread+"]--"+time+"--get response:"+responseContent);
//	    System.out.println("[Thread-"+thread+"]--"+time+"--get response:"+responseContent);//记录响应日志，建议使用log4j
	    return responseContent;
		
	}
	
	
}
