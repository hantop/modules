package com.fenlibao.p2p.util.xinwang;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.List;

public class HttpUtil {

	public static final Logger LOG = LogManager.getLogger(HttpUtil.class);

	/**
	 * http post请求传参的方法 返回实体
	 */
	public static CloseableHttpResponse post(String url,List<BasicNameValuePair> params) throws Exception {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(10000).setConnectionRequestTimeout(10000)//链接建立的超时时间；从connetcion pool中获得一个connection的超时时间
		        .setSocketTimeout(120000).build();//响应超时时间
		httppost.setConfig(requestConfig);
		
		
		if (null != params) {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
			httppost.setEntity(uefEntity);
		}
		response = httpclient.execute(httppost);
		return response;
	}
	
	public static void main(String[] args) {
		CloseableHttpResponse response = null;
		 String result = "";
		String url="http://localhost:8080/TestDBMaster/test/hello.do";
		try {
			response=post(url, null);
			result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}
}
