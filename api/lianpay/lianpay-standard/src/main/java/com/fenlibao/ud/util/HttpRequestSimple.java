package com.fenlibao.ud.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;


/**
 * 封装HTTP Client调用请求.
 *
 * @author hush@udcredit.com
 * @version 1.0
 * @date 10/29/15
 */
public class HttpRequestSimple {
    private static Log log = LogFactory.getLog(HttpRequestSimple.class);

    private final static String CONN_TIMEOUT = "ConnectTimeout";

    private final static String CONN_FAIL = "CONN_FAIL";

    public String postSendHttp(String url, String body) {
        if (url == null || "".equals(url)) {
            log.error("request url is empty.");
            return null;
        }
        HttpClient httpClient = CustomHttpClient.GetHttpClient();

        httpClient.getParams()
                .setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        HttpPost post = new HttpPost(url);
        HttpResponse resp = null;

        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            StringEntity stringEntity = new StringEntity(body, "UTF-8");

            // 设置请求主体
            post.setEntity(stringEntity);
            // 发起交易
            resp = httpClient.execute(post);
            // 响应分析
            HttpEntity entity = resp.getEntity();

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuffer responseString = new StringBuffer();
            String result = br.readLine();
            while (result != null) {
                responseString.append(result);
                result = br.readLine();
            }

            return responseString.toString();

        } catch (ConnectTimeoutException cte) {
            log.error(cte.getMessage(), cte);
            return null;
        } catch (SocketTimeoutException cte) {
            log.error(cte.getMessage(), cte);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                resp.getEntity().getContent().close();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
