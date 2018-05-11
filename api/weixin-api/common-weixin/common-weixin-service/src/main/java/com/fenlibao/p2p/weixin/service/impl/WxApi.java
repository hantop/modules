package com.fenlibao.p2p.weixin.service.impl;

import com.fenlibao.p2p.weixin.component.HttpComponent;
import com.fenlibao.p2p.weixin.component.WeixinApiUrl;
import com.fenlibao.p2p.weixin.component.WeixinInfo;
import com.fenlibao.p2p.weixin.crypt.AesException;
import com.fenlibao.p2p.weixin.crypt.WXMsgCrypt;
import com.fenlibao.p2p.weixin.defines.*;
import com.fenlibao.p2p.weixin.exception.WeixinException;
import com.fenlibao.p2p.weixin.message.*;
import com.fenlibao.p2p.weixin.message.template.TemplateMsg;
import com.fenlibao.p2p.weixin.message.ticket.ReqTicket;
import com.fenlibao.p2p.weixin.service.IMessageHandler;
import com.fenlibao.p2p.weixin.service.IWxApi;
import com.fenlibao.p2p.weixin.xstream.XStreamUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by Bogle on 2015/11/19.
 */
@Service
public class WxApi implements IWxApi, ApplicationContextAware {

    private final static Logger log = LoggerFactory.getLogger(WxApi.class);

    private final static String AES = "aes";
    private final static String RAW = "raw";

    @Autowired
    private WeixinInfo weixinInfo;

    @Autowired
    private WXMsgCrypt wxMsgCrypt;

    @Autowired
    private WeixinApiUrl wexinApiUrl;//url管理

    @Autowired(required = false)
    private IMessageHandler messageHandler;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private HttpComponent httpComponent;

    private IWxApi wxApi;

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{weixinInfo.getTokenKey(), timestamp, nonce};
        String tmpStr = this.sha1Hex(arr);
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        boolean flag = tmpStr != null ? tmpStr.equals(signature) : false;
        log.info("接入状态：{}", flag);
        return flag;
    }

    @Override
    public Map<String, String> signature(String url) {
        Map<String, String> ret = new HashMap<>();
        Ticket ticket;
        try {
            ticket = wxApi.getJsapiTicket();
            if (ticket == null) {
                return ret;
            }
            String nonceStr = this.createNonceStr();
            String timestamp = this.createTimestamp();
            String sign = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
            String signature = String.format(sign, ticket.getTicket(), nonceStr, timestamp, url);
            signature = DigestUtils.sha1Hex(signature);
            ret.put("nonceStr", nonceStr);
            ret.put("timestamp", timestamp);
            ret.put("signature", signature);
            ret.put("appId", weixinInfo.getAppId());
        } catch (WeixinException e) {
            log.error("code:{},message:{}", e.getErrcode(), e.getMessage());
        }
        return ret;
    }


    @Override
    @Cacheable(value = "wechat:token:access", keyGenerator = "keyGenerator")
    public Token getToken() throws WeixinException {
        log.info("http token...");
        //基础access_token
        String url = String.format(wexinApiUrl.getBaseAccessTokenApiUrl(weixinInfo.getAppId(), weixinInfo.getAppSecret()));
        Token token = this.get(url, Token.class);
        return token;
    }

    @Override
//    @Cacheable(value = "wechat:token:oauth2", keyGenerator = "keyGenerator")
    public Token getToken(String code) throws WeixinException {
        //获取access_token
        String url = wexinApiUrl.getOauth2AccessTokenApiUrl(weixinInfo.getAppId(), weixinInfo.getAppSecret(), code);
        //网页的access_token
        Token oauth2Token = this.get(url, Token.class);
        return oauth2Token;
    }

    /**
     * 获取jsapi_ticket
     *
     * @return
     * @throws WeixinException
     */
    @Override
    @Cacheable(value = "wechat:ticket:jsapi", keyGenerator = "keyGenerator")
    public Ticket getJsapiTicket() throws WeixinException {
        log.info("http getJsapiTicket.....");
        String url = wexinApiUrl.getJsapiTicketApiUrl(this.wxApi.getToken().getAccessToken());
        return this.get(url, Ticket.class);
    }

    @Override
    public Ticket getJsapiTicket(ReqTicket reqTicket) throws WeixinException {
        return null;
    }

    @Override
    public byte[] getQrcode(ReqTicket reqTicket) throws WeixinException {
        String ticketUrl = wexinApiUrl.getQrcodeTicketUrlApiUrl(this.wxApi.getToken().getAccessToken());
        Ticket ticket = this.post(ticketUrl, reqTicket, Ticket.class);
        String qrcodeUrl = wexinApiUrl.getQrcodeUrlApiUrl(ticket.getTicket());
        byte[] bytes = this.get(qrcodeUrl, byte[].class);
        return bytes;
    }

    @Override
    public OauthDefines<String> snsapi(String code) {
        try {
            Token token = this.wxApi.getToken(code);
            OauthDefines oauthDefines = new OauthDefines(WxCode.SUCCESS, token.getOpenid());
            return oauthDefines;
        } catch (Exception e) {
            if (e instanceof WeixinException) {
                WeixinException ex = (WeixinException) e;
                OauthDefines oauthDefines = new OauthDefines(ex.getErrcode());
                oauthDefines.setMessage(ex.getErrcode().getErrmsg());
                log.error("网页授权失败，授权code：{}", code);
                return oauthDefines;
            } else {
                log.error("网页授权异常错误，请检查");
            }
        }
        OauthDefines oauthDefines = new OauthDefines(WxCode.ERROR);
        return oauthDefines;
    }

    @Override
    public String snsapiUrl(String url, SnsapiScope type, String state) {
        url = URLEncoder.encode(url);
        return wexinApiUrl.getSnsapi(weixinInfo.getAppId(), url, type, state);
    }


    @Override
    public Fans getFans(String openid, Lang lang) throws WeixinException {
        Token token = wxApi.getToken();
        String url = wexinApiUrl.getUserInfoApiUrl(token.getAccessToken(), openid, lang);
        return this.getFans(url);
    }

    @Async
    @Override
    public Future<List<TemplateMsg>> sendTmplateMsg(List<TemplateMsg> templateMsgs) throws WeixinException {
        Token token = WxApi.this.wxApi.getToken();
        String url = wexinApiUrl.getSendTemplateMsgApiUrl(token.getAccessToken());
        for (TemplateMsg templateMsg : templateMsgs) {
            TemplateMsg result = null;
            try {
                result = WxApi.this.post(url, templateMsg, TemplateMsg.class);
                templateMsg.setErrcode(result.getErrcode());//错误code
                templateMsg.setErrmsg(result.getErrmsg());//错误消息
                templateMsg.setMsgid(result.getMsgid());//消息id
            } catch (WeixinException e) {
                log.error(e.getMessage());
                templateMsg.setErrcode(e.getErrcode().getErrorcode());
                templateMsg.setErrmsg(e.getMessage());
            }
            WxApi.this.messageHandler.completeTemplateMsg(templateMsg);//发送完成后的后置处理
        }
        return new AsyncResult<>(templateMsgs);
    }


    @Override
    public WxMsg customMenu(List<Button> buttons) throws WeixinException {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("button", buttons);
        Token token = this.wxApi.getToken();
        String url = wexinApiUrl.getCustomMenuApiUrl(token.getAccessToken());
        return this.post(url, jsonObject, WxMsg.class);
    }

    @Override
    public Serializable process(String timestamp, String nonce, String msgSignature, String encryptType, String reqMsg) throws AesException {
        Message message = XStreamUtil.fromXML(reqMsg);
        boolean flag = encryptType != null && AES.equals(encryptType);
        if (flag) { //AES加密
            // 加密模式
            String[] arr = new String[]{this.weixinInfo.getTokenKey(), timestamp, nonce, message.getEncrypt()};
            String devMsgSignature = this.sha1Hex(arr);
            if (devMsgSignature.equals(msgSignature)) {
                String content = wxMsgCrypt.decrypt(message.getEncrypt());
                message = XStreamUtil.fromXML(content);
            } else {
                throw new AesException(AesException.ValidateSignatureError);
            }
        }
        Message result = process(message);
        if (flag) {
            Serializable response = XStreamUtil.toXML(result);
            // 加密
            String encryptedXml = wxMsgCrypt.encrypt(this.createNonceStr(), response == null ? "" : response.toString());
            String timeStamp = this.createTimestamp();
            String signature = sha1Hex(weixinInfo.getTokenKey(), timeStamp, nonce, encryptedXml);
            return generateXml(encryptedXml, signature, timeStamp, nonce);
        }
        return XStreamUtil.toXML(result);
    }


    @Override
    public PageFans getFansListOpenid(String next_openid) throws WeixinException {
        String url = wexinApiUrl.getUserListUrl(this.wxApi.getToken().getAccessToken(), next_openid);
        return this.get(url, PageFans.class);
    }

    @Async
    @Override
    public void sendTmplateMsgToAllFans(List<TemplateMsg> templateMsgs) {
        try {
            PageFans pageFans = new PageFans();
            do {
                pageFans = this.getFansListOpenid(pageFans.getNextpenid() == null ? "" : pageFans.getNextpenid());
                List<String> openids = new ArrayList<>();
                List<TemplateMsg> waitSendings = new ArrayList<>();
                if (pageFans != null && pageFans.getData() != null) {
                    openids.addAll(pageFans.getData().getOpenids());
                }
                for (String openid : openids) {
                    for (TemplateMsg templateMsg : templateMsgs) {
                        TemplateMsg waitSending = new TemplateMsg();
                        BeanUtils.copyProperties(templateMsg, waitSending);
                        waitSending.setTouser(openid);
                        waitSendings.add(waitSending);
                    }
                }
                if (!waitSendings.isEmpty()) {
                    this.sendTmplateMsg(waitSendings);
                }
            } while (pageFans != null && !"".equals(pageFans.getNextpenid()));
        } catch (WeixinException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理消息，判断消息类型，并分类处理
     * @param message
     * @return
     */
    private Message process(Message message) {
        if (message.getMsgType() == MsgType.text) {
            //图文消息
            log.debug("文本消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.image) {
            //图片消息
            log.debug("图片消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.voice) {
            //语音消息
            log.debug("语音消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.video) {
            //视频消息
            log.debug("视频消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.shortvideo) {
            //小视频消息
            log.debug("小视频消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.location) {
            //地理位置消息
            log.debug("地理位置消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.music) {
            //音乐消息
            log.debug("音乐消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.link) {
            //链接消息
            log.debug("链接消息");
            return messageHandler.multiService(message);
        } else if (message.getMsgType() == MsgType.event) {
            //事件推送
            if (message.getEvent() != null) {
                Event event = message.getEvent();
                String eventKey = message.getEventKey();
                if ((event == Event.SCAN) || (eventKey != null && eventKey.startsWith(Event.KEY_QRSCENE.toString()) && event == Event.subscribe)) {
                    /**用户扫描事件*/
                    log.debug("用户扫描事件");
                    return this.messageHandler.subscribe(message);
                } else if (event == Event.subscribe) {
                    /**用户关注事件*/
                    log.debug("用户关注事件");
                    return this.messageHandler.subscribe(message);
                } else if (event == Event.unsubscribe) {
                    /**用户取消关注事件*/
                    log.debug("用户取消关注事件");
                } else if (event == Event.LOCATION) {
                    /**上报地理位置事件*/
                    log.debug("上报地理位置事件");
                } else if (event == Event.CLICK) {
                    /**用户点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报。*/
                    log.debug("用户点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报。");
                    return this.messageHandler.clickEvent(message, this);
                } else if (event == Event.VIEW) {
                    /**点击菜单跳转链接时的事件推送*/
                    log.debug("点击菜单跳转链接时的事件推送");
                } else if (event == Event.scancode_push) {
                    /**扫码推事件的事件推送*/
                    log.debug("扫码推事件的事件推送");
                } else if (event == Event.scancode_waitmsg) {
                    /**扫码推事件且弹出“消息接收中”提示框的事件推送*/
                    log.debug("扫码推事件且弹出“消息接收中”提示框的事件推送");
                } else if (event == Event.pic_sysphoto) {
                    /**弹出系统拍照发图的事件推送*/
                    log.debug("弹出系统拍照发图的事件推送");
                } else if (event == Event.pic_photo_or_album) {
                    /**弹出拍照或者相册发图的事件推送*/
                    log.debug("弹出拍照或者相册发图的事件推送");
                } else if (event == Event.pic_weixin) {
                    /**弹出微信相册发图器的事件推送*/
                    log.debug("弹出微信相册发图器的事件推送");
                } else if (event == Event.location_select) {
                    /**弹出地理位置选择器的事件推送*/
                    log.debug("弹出地理位置选择器的事件推送");
                } else if (event == Event.poi_check_notify) {
                    /**门店审核事件推送*/
                    log.debug("门店审核事件推送");
                } else if (event == Event.card_pass_check) {
                    /**卡券通过审核*/
                    log.debug("卡券通过审核");
                } else if (event == Event.card_not_pass_check) {
                    /**卡券未通过审核*/
                    log.debug("卡券未通过审核");
                } else if (event == Event.user_get_card) {
                    /**领取事件推送*/
                    log.debug("领取事件推送");
                } else if (event == Event.user_del_card) {
                    /**删除事件推送*/
                    log.debug("删除事件推送");
                } else if (event == Event.user_consume_card) {
                    /**核销事件*/
                    log.debug("核销事件");
                } else if (event == Event.user_view_card) {
                    /**核销事件，进入会员卡事件推送*/
                    log.debug("核销事件，进入会员卡事件推送");
                } else if (event == Event.user_enter_session_from_card) {
                    /**从卡券进入公众号会话事件推送*/
                    log.debug("从卡券进入公众号会话事件推送");
                } else if (event == Event.kf_close_session) {
                    /**客服关闭会话*/
                    return this.messageHandler.kfCloseSessionEvent(message);
                } else if (event == Event.TEMPLATESENDJOBFINISH) {
                    /**在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。*/
                    log.debug("在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。");
                    WxApi.this.messageHandler.completeStatusTemplateMsg(message);//发送完成后的后置处理
                }
            }
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.wxApi = applicationContext.getBean(IWxApi.class);
    }


    /**
     * 生成加密的xml消息
     *
     * @param encrypt   加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 生成的xml字符串
     */
    private String generateXml(String encrypt, String signature, String timestamp, String nonce) {
        String format =
                "<xml>\n"
                        + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
                        + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
                        + "<TimeStamp>%3$s</TimeStamp>\n"
                        + "<Nonce><![CDATA[%4$s]]></Nonce>\n"
                        + "</xml>";
        return String.format(format, encrypt, signature, timestamp, nonce);
    }


    /**
     * 排序数组并进行sha1加密
     *
     * @param arr
     * @return
     */
    private String sha1Hex(String... arr) {
        Arrays.sort(arr);
        String devMsgSignature = Arrays.asList(arr).toString().replaceAll(",", "").replaceAll("\\s*", "");
        devMsgSignature = devMsgSignature.substring(1, devMsgSignature.length() - 1);
        devMsgSignature = DigestUtils.sha1Hex(devMsgSignature);
        return devMsgSignature;
    }

    /**
     * 通过url获取Fans信息
     *
     * @param url
     * @return
     * @throws WeixinException
     */
    private Fans getFans(String url) throws WeixinException {
        return this.get(url, Fans.class);
    }

    private String createNonceStr() {
        return RandomStringUtils.random(16, true, true);
    }

    private String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private <T> T get(String url, Class<T> clazz) throws WeixinException {
        try {
            return httpComponent.get(url, clazz);
        } catch (WeixinException e) {
            throw e;
        }
    }

    private <T> T post(String url, Object request, Class<T> clazz) throws WeixinException {
        try {
            return httpComponent.post(url, request, clazz);
        } catch (WeixinException e) {
            throw e;
        }
    }
}
