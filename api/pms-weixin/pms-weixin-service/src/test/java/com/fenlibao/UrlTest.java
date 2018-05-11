package com.fenlibao;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Bogle on 2016/3/3.
 */
public class UrlTest {

    @Test
    public void url() {
        System.out.println(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc2fe74bcfdf7913d&redirect_uri=http%3A%2F%2Ffenlibao.tunnel.qydev.com%2Fweixin%2FredirectUrl%3FredirectUrl%3Dhttps%253a%252f%252fweixin.fenlibao.com%252f%253ffenlicore_c%253dTheme%2526fenlicore_a%253dshowPage%2526m%253dFenlibao%2526go%253dshowSpecial01&response_type=code&scope=snsapi_base&state=false#wechat_redirect"));
    }

    @Test
    public void deUrl() {
        System.out.println(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx25162193c94710fb&redirect_uri=http%3A%2F%2Fteaccounts.fenlibao.com%2Fweixin%2FredirectUrl%3FredirectUrl%3Dhttp%253A%252F%252Fblog.csdn.net%252Fseng3018%252Farticle%252Fdetails%252F6690527&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"));
    }
}
