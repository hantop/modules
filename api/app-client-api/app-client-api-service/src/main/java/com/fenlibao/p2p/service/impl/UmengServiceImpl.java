package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.model.entity.umeng.AndroidNotification;
import com.fenlibao.p2p.model.entity.umeng.UmengNotification;
import com.fenlibao.p2p.model.entity.umeng.android.AndroidUnicast;
import com.fenlibao.p2p.model.entity.umeng.ios.IOSUnicast;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.service.UmengService;
import com.fenlibao.p2p.util.umeng.UmengConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import static com.fenlibao.p2p.common.util.http.HttpHeader.USER_AGENT;

/**
 * Created by Administrator on 2017/4/24.
 */
@Service
public class UmengServiceImpl implements UmengService {

    private static final Logger logger= LogManager.getLogger(UmengServiceImpl.class);

    CloseableHttpClient httpclient = HttpClients.createDefault();
    @Override
    public HttpResponse sendUmengMessage(Map<String, String> param) throws Throwable {
        HttpResponse response = new HttpResponse();
        if(!StringUtils.isEmpty(param.get("bidOrigin"))&&param.get("bidOrigin").equals("ios")){
            IOSUnicast unicast = new IOSUnicast(UmengConfig.get("ios_appKey"), UmengConfig.get("ios_appMasterSecret"));
            //unicast.setDeviceToken( "1ace422ac9a5500f0a0ad7855f34296ad3c01403a399f8d22784e4ed4a74f0d1");
            unicast.setDeviceToken(param.get("deviceToken"));
            //判断用户是邀请人或被邀请人
            if(!StringUtils.isEmpty(param.get("isInviter"))&&param.get("isInviter").equals("inviter")) {
                unicast.setAlert(UmengConfig.get("ios_inviter_alert"));
            }else {
                unicast.setAlert(UmengConfig.get("ios_invitee_alert"));
            }
            unicast.setBadge( 0);
            unicast.setSound( "default");
            unicast.setTestMode();
            unicast.setActivityUrl(UmengConfig.get("notification_text"));
            unicast.setPushType(UmengConfig.get("umeng_pushType"));
            unicast.setModelType(UmengConfig.get("umeng_modelType"));
            int statusCode = send(unicast);
            if (statusCode==200){
                response.setCode(String.valueOf(statusCode));
                response.setMessage("推送成功");
            }else {
                response.setCode(String.valueOf(statusCode));
                response.setMessage("推送失败");
            }
        }else {
            AndroidUnicast unicast = new AndroidUnicast(UmengConfig.get("android_appKey"), UmengConfig.get("android_appMasterSecret"));
            //unicast.setDeviceToken("Ap3cc8O5JLIP1JP-rLddjuTXJ-ceuE25GdtD8g_vVOeF");
            unicast.setDeviceToken(param.get("deviceToken"));
            //判断用户是邀请人或被邀请人
            if(!StringUtils.isEmpty(param.get("isInviter"))&&param.get("isInviter").equals("inviter")) {
                unicast.setTitle(UmengConfig.get("android_inviter_title"));
                unicast.setText(UmengConfig.get("android_inviter_text"));
            }else {
                unicast.setTitle(UmengConfig.get("android_invitee_title"));
                unicast.setText(UmengConfig.get("android_invitee_text"));
            }
            unicast.setTicker(UmengConfig.get("notification_ticker"));
            unicast.goAppAfterOpen();
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            unicast.setProductionMode();
            unicast.setModelType(UmengConfig.get("umeng_modelType"));
            unicast.setPushType(UmengConfig.get("umeng_pushType"));
            //unicast.setExtraField("activi","www");
            unicast.setActivityUrl(UmengConfig.get("open_url"));
            int statusCode = send(unicast);
            if (statusCode==200){
                response.setCode(String.valueOf(statusCode));
                response.setMessage("推送成功");
            }else {
                response.setCode(String.valueOf(statusCode));
                response.setMessage("推送失败");
            }
        }
       return response;
    }

    public int send(UmengNotification msg) throws Exception {
        String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
        msg.setPredefinedKeyValue("timestamp", timestamp);
        String url =UmengConfig.get("request_url_umeng");
        String postBody = msg.getPostBody();
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBody, "UTF-8");
        post.setEntity(se);
        //org.apache.http.HttpResponse response = client.execute(post);
        CloseableHttpResponse response = httpclient.execute(post);
        int status = response.getStatusLine().getStatusCode();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (status == 200) {
          logger.info("推送成功");
        } else {
            logger.error("推送失败");
        }
        return status;
    }
}
