package com.fenlibao.p2p.util.dm.http;

import com.fenlibao.p2p.model.dm.message.RequestDocument;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.message.body.RequestBody;
import com.fenlibao.p2p.model.dm.message.header.RequestHeader;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.security.DES3EncryptAndDecrypt;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 华兴专用
 * Created by zcai on 2016/8/26.
 */
public class HXHttpUtil {

    public static String doPost(String content) throws IOException {
        return doPost( HXUtil.CONFIG.formSubmitUrl(), content);
    }

    public static String doPost(String url, String content) throws IOException {
        URL postUrl = new URL(url);
        HttpURLConnection urlcon = null;
        DataOutputStream output = null;
        BufferedReader buffer = null;
        InputStreamReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            urlcon = (HttpURLConnection)postUrl.openConnection();
            int contentLength = content.getBytes().length;
            urlcon.setConnectTimeout(1000*15);
            urlcon.setReadTimeout(1000*60*2);
            urlcon.setRequestMethod("POST");  //post请求方式
            urlcon.setUseCaches(false);       //post请求不能使用缓存
            urlcon.setRequestProperty("Content-Length", String.valueOf(contentLength));
            urlcon.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            urlcon.setDoInput(true);  //默认为true
            urlcon.setDoOutput(true); //默认为true
            output =  new DataOutputStream(urlcon.getOutputStream());
            output.writeBytes(content);
            output.flush();

            in = new InputStreamReader(urlcon.getInputStream());
            buffer =new BufferedReader(in);
            String str = null;
            while((str=buffer.readLine())!=null){
                result.append(str);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (buffer != null) {
                buffer.close();
            }
            if (output != null) {
                output.close();
            }
            if (urlcon != null) {
                urlcon.disconnect();
            }
        }
        return result.toString();
    }


    public static void main(String[] args) throws Exception {
        test0();
    }

    private static void test0() throws Exception {
        String url = "http://183.63.131.106:40013/extService/ghbExtService.do"; //挡板
        RequestHeader header = new RequestHeader("P2P046", "20160830175420006", "20160830", "113120", "");
        String busiData = "<MERCHANTID>FLB</MERCHANTID><APPID>PC</APPID><TRSTYPE>0</TRSTYPE><ACNO></ACNO><MOBILE_NO>13751763759</MOBILE_NO><EXT_FILED1></EXT_FILED1><EXT_FILED2></EXT_FILED2><EXT_FILED3></EXT_FILED3>";
        String data = DES3EncryptAndDecrypt.encrypt(busiData);
        RequestBody body = new RequestBody("OGW00041", data);
        RequestDocument requestDocument = new RequestDocument(body, header);
        String param = MessageUtil.getRequestMessage(requestDocument);
        System.out.println("param >>>>>>>>>>>>>>");
        System.out.println(param);
        System.out.println("result >>>>>>>>>>>>>>");
        String result = HXHttpUtil.doPost(url, param);
        System.out.println(result);
        MessageUtil.checkSign(result);
        ResponseDocument resp = MessageUtil.getXMLDocument(ResponseDocument.class, result);
        String enPath = resp.getBody().getXMLPARA();
        if (StringUtils.isNotBlank(enPath)) {
            String pathData = DES3EncryptAndDecrypt.decrypt(enPath);
            System.out.println(pathData);
        }
    }

}
