package com.fenlibao.p2p.util.pay;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

/**
 *  HttpClient请求
 * @author yangzengcai
 * @date 2015年8月29日
 */
public class HttpClientHandler {
	
	protected static final Logger logger = LogManager.getLogger(HttpClientHandler.class);

	 /**
     * 发送请求Json格式
     *
     * @param params
     * @param actionUrl
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPostJson(Map<String, String> params, String actionUrl)
        throws IOException
    {
        // 返回结果
        String result = null;
        // 请求参数
        Gson gs = new Gson();
        String jsonString = gs.toJson(params);
        logger.debug("HttpClient请求参数：" + jsonString);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            //解决中文乱码问题
            StringEntity entity = new StringEntity(jsonString,"utf-8");
            entity.setContentEncoding("UTF-8"); 
            entity.setContentType("application/json");
            
            HttpPost httpPost = new HttpPost(actionUrl);
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000).build();
            httpPost.setConfig(requestConfig);
            
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try
            {
                HttpEntity entitys = response.getEntity();
                if (response.getStatusLine().getReasonPhrase().equals("OK")
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    result = EntityUtils.toString(entitys, "utf-8");
                    logger.debug("HttpClient返回结果：" + result);
                }
                EntityUtils.consume(entitys);
            }
            finally
            {
                httpPost.releaseConnection();
                response.close();
            }
        }
        finally
        {
            httpclient.close();
        }
        return result;
    }
	
}
