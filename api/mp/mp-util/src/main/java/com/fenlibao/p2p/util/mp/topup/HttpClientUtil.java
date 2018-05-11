package com.fenlibao.p2p.util.mp.topup;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http工具类
 * @author yangzengcai
 * @date 2016年2月17日
 */
public class HttpClientUtil {

	private static final Logger logger = LogManager.getLogger(HttpClientUtil.class);
	
	/**
	 * http post 提交
	 * @param param (key=value&key1=value2)
	 * @param actionUrl (http://example.com)
	 * @return
	 * @throws IOException 
	 */
	public static String doPost(String param, String actionUrl) throws IOException {
		String result = "";
		logger.debug("HttpClient请求参数：" + param);
		try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
			 
            StringEntity entity = new StringEntity(param,"utf-8");
            entity.setContentEncoding("UTF-8"); 
            
            HttpPost httpPost = new HttpPost(actionUrl);
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom()
            		.setSocketTimeout(40000).setConnectTimeout(40000)
            		.build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            
            try (CloseableHttpResponse response = httpclient.execute(httpPost);) {
            	 HttpEntity entitys = response.getEntity();
                 if ("OK".equals(response.getStatusLine().getReasonPhrase())
                     && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                 {
                     result = EntityUtils.toString(entitys, "utf-8");
                     logger.debug("HttpClient返回结果：" + result);
                 }
                 EntityUtils.consume(entitys);
            } finally {
            	httpPost.releaseConnection();
			}
		}
		return result;
	}
	
}
