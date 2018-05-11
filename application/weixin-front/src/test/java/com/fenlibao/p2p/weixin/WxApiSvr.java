package com.fenlibao.p2p.weixin;

import com.fenlibao.p2p.Application;
import com.fenlibao.p2p.weixin.component.JSONUtil;
import com.fenlibao.p2p.weixin.defines.ActionName;
import com.fenlibao.p2p.weixin.defines.Lang;
import com.fenlibao.p2p.weixin.defines.MenuType;
import com.fenlibao.p2p.weixin.defines.SnsapiScope;
import com.fenlibao.p2p.weixin.exception.WeixinException;
import com.fenlibao.p2p.weixin.message.*;
import com.fenlibao.p2p.weixin.message.template.TemplateMsg;
import com.fenlibao.p2p.weixin.message.template.TemplateMsgData;
import com.fenlibao.p2p.weixin.message.ticket.ReqTicket;
import com.fenlibao.p2p.weixin.repository.SubscribeRepository;
import com.fenlibao.p2p.weixin.service.Constants;
import com.fenlibao.p2p.weixin.service.IWxApi;
//import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Bogle on 2015/11/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class WxApiSvr {

    private final static Logger log = LoggerFactory.getLogger(WxApiSvr.class);

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private IWxApi wxApi;

//    @Autowired
//    private StringEncryptor stringEncryptor;
//
//    //    @Test
//    public void encrypt() {
//        log.info(stringEncryptor.encrypt("develop.fenlibao.com"));
//    }

    //    @Test
    public void objectMapper() throws IOException {
        String json = "{\"access_token\":\"yG5PSiSFSp5PuP_KU3UBQqXNZBoGIEe6eFL0mxUPPGjOjgUBOLhcsqgimPKZlVRZEKVeN0kKW-HjsYF5nWQYbNJKEPv_Qe6oGPquqG0atJMWNEcACAMDU\",\"expires_in\":7200}";
        Token token = JSONUtil.readValue(json.getBytes(), Token.class);
        println(token);
    }

    //@Test
    public void encode(){
    	println(URLEncoder.encode("https://weixin.fenlibao.com/?fenlicore_c=Theme&fenlicore_a=showPage&m=Fenlibao&go=newLife"));
    }
    
    //@Test
    public void sniaUrl() {
        log.info(wxApi.snsapiUrl("https://accounts.fenlibao.com/login/open", SnsapiScope.snsapi_base, "STATE"));//开启
        log.info(wxApi.snsapiUrl("https://accounts.fenlibao.com/login/auto", SnsapiScope.snsapi_base, "STATE"));//登陆
        log.info(wxApi.snsapiUrl("https://accounts.fenlibao.com/login/cancel", SnsapiScope.snsapi_base, "STATE"));//取消
    }

    /**
     * 基础token
     */
    //@Test
    public void getToken() throws WeixinException {
        Token token = this.wxApi.getToken();
        println(token);
    }


    //     @Test
    public void getOauth2Token() throws WeixinException {
        Token token = this.wxApi.getToken("code2");
        println(token);
    }

    //     @Test
    public void getJsapiTicket() throws WeixinException {
        Ticket ticket = this.wxApi.getJsapiTicket();
        println(ticket);
    }

    /**
     * 生成二维码
     * @throws WeixinException
     */
//    @Test
    public void getQrcode() throws WeixinException {
        ReqTicket reqTicket = new ReqTicket(ActionName.QR_LIMIT_STR_SCENE, "pcgwwx");
        byte[] bytes = this.wxApi.getQrcode(reqTicket);
        println(bytes);
    }

    //@Test
    public void getFans() throws WeixinException {
        Fans fans = this.wxApi.getFans("odEb6siS99FSNrccWh0V0v6DNb4Y", Lang.zh_CN);
        println(fans);
    }

//    @Test
    public void sendTemplate() throws WeixinException, ExecutionException, InterruptedException {
//        TemplateMsg templateMsg1 = new TemplateMsg("odEb6sgL93pUBlAqYZj6Ecnt3qpY", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
//        TemplateMsg templateMsg2 = new TemplateMsg("odEb6sh-UADvEFK8cDyCubUXdLx8", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
//        TemplateMsg templateMsg3 = new TemplateMsg("odEb6siS99FSNrccWh0V0v6DNb4Y", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
//        TemplateMsg templateMsg4 = new TemplateMsg("odEb6spNxO9j1zVgEd4HK5qiyh-k", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
//        TemplateMsg templateMsg5 = new TemplateMsg("odEb6svImr1hPa62nlD_ROUBCfno", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
    	TemplateMsg templateMsg6 = new TemplateMsg("odEb6spaynOfUqor-IpjjFuKw4rk", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
    	Map<String, TemplateMsgData> data = new HashMap<>();
        data.put("first", new TemplateMsgData("恭喜你购买成功！", "#173177"));
        data.put("name", new TemplateMsgData("基金", "#173177"));
        data.put("expDate", new TemplateMsgData("2015年12月30日", "#173177"));
        data.put("remark", new TemplateMsgData("您可以点击下方菜单-我的账户，随时查询账户余额", "#173177"));
//        templateMsg1.setData(data);
//        templateMsg2.setData(data);
//        templateMsg3.setData(data);
//        templateMsg4.setData(data);
//        templateMsg5.setData(data);
        templateMsg6.setData(data);

        List<TemplateMsg> templateMsgs = new ArrayList<>();
//        templateMsgs.add(templateMsg1);
//        templateMsgs.add(templateMsg2);
//        templateMsgs.add(templateMsg3);
//        templateMsgs.add(templateMsg4);
//        templateMsgs.add(templateMsg5);
        templateMsgs.add(templateMsg6);
        Future<List<TemplateMsg>> futureResult = wxApi.sendTmplateMsg(templateMsgs);
        List<TemplateMsg> templateMsgs1 = futureResult.get();
        println(templateMsgs1);
        log.info("over..........");
    }

//    @Test
    public void sendWebTemplate() throws WeixinException, ExecutionException, InterruptedException {
        TemplateMsg templateMsg1 = new TemplateMsg("odEb6sgL93pUBlAqYZj6Ecnt3qpY", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        TemplateMsg templateMsg2 = new TemplateMsg("odEb6sh-UADvEFK8cDyCubUXdLx8", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        TemplateMsg templateMsg3 = new TemplateMsg("odEb6siS99FSNrccWh0V0v6DNb4Y", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        TemplateMsg templateMsg4 = new TemplateMsg("odEb6spNxO9j1zVgEd4HK5qiyh-k", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        TemplateMsg templateMsg5 = new TemplateMsg("odEb6svImr1hPa62nlD_ROUBCfno", "CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        Map<String, TemplateMsgData> data = new HashMap<>();
        data.put("first", new TemplateMsgData("恭喜你购买成功！", "#173177"));
        data.put("name", new TemplateMsgData("基金", "#173177"));
        data.put("expDate", new TemplateMsgData("2015年12月30日", "#173177"));
        data.put("remark", new TemplateMsgData("您可以点击下方菜单-我的账户，随时查询账户余额", "#173177"));
        templateMsg1.setData(data);
        templateMsg2.setData(data);
        templateMsg3.setData(data);
        templateMsg4.setData(data);
        templateMsg5.setData(data);

        List<TemplateMsg> templateMsgs = new ArrayList<>();
        templateMsgs.add(templateMsg1);
        templateMsgs.add(templateMsg2);
        templateMsgs.add(templateMsg3);
        templateMsgs.add(templateMsg4);
        templateMsgs.add(templateMsg5);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<List<TemplateMsg>> entity = new HttpEntity<>(templateMsgs, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://teaccounts.fenlibao.com/weixin/sendTmplateMsg";
        List<TemplateMsg> response = restTemplate.postForObject(url, entity, List.class);
        println(response);
    }

    //    @Test
    public void sendTmplateMsgToAllFans() throws WeixinException, ExecutionException, InterruptedException {
        TemplateMsg templateMsg1 = new TemplateMsg("CFAEP6necG9zq-acL_36rQkMFYkXRk2rjvifM-3n8NA", "www.baidu.com", "#FF0000");
        Map<String, TemplateMsgData> data = new HashMap<>();
        data.put("first", new TemplateMsgData("恭喜你购买成功！", "#173177"));
        data.put("name", new TemplateMsgData("基金", "#173177"));
        data.put("expDate", new TemplateMsgData("2015年12月30日", "#173177"));
        data.put("remark", new TemplateMsgData("您可以点击下方菜单-我的账户，随时查询账户余额", "#173177"));
        templateMsg1.setData(data);


        List<TemplateMsg> templateMsgs = new ArrayList<>();
        templateMsgs.add(templateMsg1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<List<TemplateMsg>> entity = new HttpEntity<>(templateMsgs, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8888/weixin/sendTmplateMsgToAllFans";
        FenlibaoApi response = restTemplate.postForObject(url, entity, FenlibaoApi.class);
        println(response);
    }


//    @Test
    public void customMenuTest() throws WeixinException {

        Button btn1 = new Button();
        btn1.setName("进入首页");
        btn1.setType(MenuType.view);
//        String url = this.wxApi.snsapiUrl("https://accounts.fenlibao.com/login/auto", SnsapiScope.snsapi_base,"STATE");
          String url = this.wxApi.snsapiUrl("http://teaccounts.fenlibao.com/login/auto", SnsapiScope.snsapi_base, "STATE");
//        String url = this.wxApi.snsapiUrl("http://fenlibao.tunnel.qydev.com/login/auto", SnsapiScope.snsapi_base, "STATE");

        btn1.setUrl(url);
        btn1.setKey(Constants.CLICK_HOME);

        Button btn2 = new Button();
        btn2.setName("最新活动");

        Button btn2_5 = new Button();
        btn2_5.setName("央企浙江光大入股");
        btn2_5.setType(MenuType.view);
        btn2_5.setUrl("http://mp.weixin.qq.com/s?__biz=MzAwODU3NzUwOA==&mid=403763690&idx=1&sn=3068d5d03e65e1144c33a28071ecc598#rd");

        Button btn2_1 = new Button();
        btn2_1.setName("注册送100元+30话费");
        btn2_1.setType(MenuType.view);
        btn2_1.setUrl("https://weixin.fenlibao.com/?fenlicore_c=User&fenlicore_a=redregisterActExperience&m=Fenlibao&channelCode=&lj=3");
        
        Button btn2_3 = new Button();
        btn2_3.setName("邀请赚现金（全新升级）");
        btn2_3.setType(MenuType.view);
//        btn2_3.setKey(Constants.CLICK_YAOQINGYOUHUI_EVENT);
        btn2_3.setUrl("https://weixin.fenlibao.com/?fenlicore_c=Theme&fenlicore_a=showPage&m=Fenlibao&go=invitepage&title=%E9%82%80%E8%AF%B7%E5%A5%BD%E5%8F%8B");

        Button btn2_4 = new Button();
        btn2_4.setName("官方贴吧");
        btn2_4.setType(MenuType.view);
        btn2_4.setUrl("http://tieba.baidu.com/f?kw=%E5%88%86%E5%88%A9%E5%AE%9D&ie=utf-8");

        btn2.setSubButton(Arrays.asList(new Button[]{btn2_1,btn2_3, btn2_4,btn2_5}));

        Button btn3 = new Button();
        btn3.setName("下载APP");

        Button sub3_1 = new Button();
        sub3_1.setName("免登陆功能");
        sub3_1.setType(MenuType.click);
        sub3_1.setKey(Constants.CLICK_LOGIN_BIND);

        Button sub3_2 = new Button();
        sub3_2.setName("微信客服");
        sub3_2.setType(MenuType.click);
        sub3_2.setKey(Constants.CLICK_WEIXIN_CUSTOMER);

        Button sub3_3 = new Button();
        sub3_3.setName("下载APP");
        sub3_3.setType(MenuType.view);
        sub3_3.setKey(Constants.CLICK_DOWNLOAD);
        sub3_3.setUrl("https://weixin.fenlibao.com/?fenlicore_c=Theme&fenlicore_a=showPage&m=Fenlibao&go=androidDownload");

        btn3.setSubButton(Arrays.asList(new Button[]{sub3_1, sub3_2, sub3_3}));

        List<Button> buttons = Arrays.asList(btn1, btn2, btn3);
        WxMsg message = this.wxApi.customMenu(buttons);
        println(message);
    }


    //@Test
    public void getFansListOpenid() throws WeixinException {
        PageFans pageFans = this.wxApi.getFansListOpenid("");
        println(pageFans);
    }

    private void println(byte[] bytes) {
        File file = new File("D:\\" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void println(Object target) {
        log.info(JSONUtil.writeValueAsString(target));
    }
}
