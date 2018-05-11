package com.fenlibao.service.weixin.service;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.dao.pms.weixin.RobotMsgMapper;
import com.fenlibao.model.pms.weixin.RobotMsg;
import com.fenlibao.model.pms.weixin.form.RobotMsgForm;
import com.fenlibao.service.weixin.component.HttpComponent;
import com.fenlibao.service.weixin.defines.Env;
import com.fenlibao.service.weixin.message.Button;
import com.fenlibao.service.weixin.message.Token;
import com.fenlibao.service.weixin.message.WeixinButton;
import com.fenlibao.service.weixin.message.WxMsg;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bogle on 2016/3/1.
 */
@Service
public class WeixinService {

    private static final Logger log = LoggerFactory.getLogger(WeixinService.class);
    private static final String BASE_ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s"; //获取base_token

    private static String SNSAPI = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    private static final String MNEU_GET_API = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";

    private static final String MENU_SET_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    /**
     * 免登陆功能
     */
    private static final String CLICK_LOGIN_BIND = "CLICK_LOGIN_BIND";
    /**
     * 微信客服
     */
    private static final String CLICK_WEIXIN_CUSTOMER = "CLICK_WEIXIN_CUSTOMER";
    private static final String CLICK_PRE = "CLICK_";

    private static final List<String> NO_EDIT_KEY = Arrays.asList(CLICK_LOGIN_BIND, CLICK_WEIXIN_CUSTOMER);

    private static final String BUTTON_TYPE_VIEW = "view";
    private static final String BUTTON_TYPE_CLICK = "click";

    private Token testToken;

    private String testAppId;//应用ID

    private String testAppSecret;//应用密钥

    private String testIndex;//首页url

    private String testBindUrl; // 绑定账户url

    private String testSnsapiUrl;//授权中转url


    private Token prodToken;

    private String prodAppId;//应用ID

    private String prodAppSecret;//应用密钥

    private String prodIndex;//首页url

    private String prodBindUrl; // 绑定账户url

    private String prodSnsapiUrl;//授权中转url

    @Autowired
    private HttpComponent httpComponent;

    @Autowired
    private RobotMsgMapper robotMsgMapper;

    private int index = 100;

    @PostConstruct
    public void init() {
        Config.loadProperties();

        try {
            this.index = Integer.parseInt(Config.get("weixin.count"));
        } catch (NumberFormatException e) {
            log.error("配置微信请求次数有误,配置内容:{}", Config.get("weixin.count"));
        }
        // 测试环境配置
        this.testAppId = Config.get("test.weixin.appId");
        this.testAppSecret = Config.get("test.weixin.appSecret");
        this.testIndex = Config.get("test.weixin.index");
        this.testBindUrl = Config.get("test.weixin.bind.account");
        this.testSnsapiUrl = Config.get("test.weixin.snsapiUrl");


        //生产环境配置
        this.prodAppId = Config.get("prod.weixin.appId");
        this.prodAppSecret = Config.get("prod.weixin.appSecret");
        this.prodIndex = Config.get("prod.weixin.index");
        this.prodBindUrl = Config.get("prod.weixin.bind.account");
        this.prodSnsapiUrl = Config.get("prod.weixin.snsapiUrl");
    }

    private Token getToken(Env env) throws Exception {
        if (env == null) env = Env.test;
        Token token = this.testToken;
        String appId = testAppId;
        String appSecret = testAppSecret;
        if (env == Env.prod) {
            token = prodToken;
            appId = prodAppId;
            appSecret = prodAppSecret;
        }
        //token是否已经过期，如果已过期，就重新请求一个
        if (isExpiresIn(token)) {
            String url = String.format(BASE_ACCESS_TOKEN_API_URL, appId, appSecret);
            log.info("\n--------------------------------------------------------------\ngetToken:\nenv:{}\nurl:{}\nappId:{}", env, url, appId);
            token = this.getToken(url, 0, env);
            handleException(token.getErrcode());
            token.setCreateTime(System.currentTimeMillis());
            if (env == Env.prod) {
                this.prodToken = token;
            } else {
                this.testToken = token;
            }
        }

        return token;
    }

    /**
     * 
     * @param url 请求token的url
     * @param index 用于记录因请求失败而递归调用的次数
     * @param env 正式/测试环境
     * @return token
     * @throws Exception
     */
    private Token getToken(String url, int index, Env env) throws Exception {
        Token token = this.get(url, Token.class);
        try {
            if (token.getErrcode() != 0) {
                clearToken(env);
                handleException(token.getErrcode());
            }
        } catch (Exception e) {
            clearToken(env);
            log.error("\n--------------------------------------------------------------\ngetToken:\nevn:{}\nurl:{}\nmessage:{}", env, url, e.getMessage());
        } finally {
            if (index < this.index && token.getErrcode() != 0) {
                clearToken(env);
                return this.getToken(url, ++index, env);
            }
        }
        return token;
    }

    /**
     * 获取当前公众号菜单
     * @param env 正式/测试环境
     * @return 
     * @throws Exception
     */
    public List<Button> getButton(Env env) throws Exception {
    	//请求获取当前公众号菜单的url
        String url = String.format(MNEU_GET_API, this.getToken(env).getAccessToken());
        log.info("\n--------------------------------------------------------------\ngetButton:\nevn:{}\nurl:{}", env, url);
        //请求获取当前公众号菜单
        WeixinButton menu = getWeixinButton(env, url, 0);
        handleException(menu.getErrcode());
        List<Button> buttons = menu.getMenu().getButtons();
        //对接收到的菜单和按钮设置补充属性
        this.getButton(buttons);
        return buttons;
    }

    /**
     * 
     * @param env 正式/测试环境
     * @param url 请求获取当前公众号菜单的url
     * @param index 记录当前因请求失败而递归执行的次数
     * @return 微信公众号菜单
     */
    private WeixinButton getWeixinButton(Env env, String url, int index) {
        WeixinButton menu = this.get(url, WeixinButton.class);
        try {
            if (menu.getErrcode() != 0) {
                clearToken(env);
                handleException(menu.getErrcode());
            }
        } catch (Exception e) {
            clearToken(env);
            log.error("\n--------------------------------------------------------------\ngetWeixinButton:\nevn:{}\nurl:{}\nmessage:{}", env, url, e.getMessage());
        } finally {
            if (index < this.index && menu.getErrcode() != 0) {
                clearToken(env);
                return getWeixinButton(env, url, ++index);
            }
        }
        return menu;
    }


    @Transactional(rollbackFor = Exception.class)
    public WxMsg publishMenu(Env env, List<Button> buttons) throws Exception {
        sortButtons(buttons);
        for (Button button : buttons) {
            if (button.getSubButton() != null) {
                sortButtons(button.getSubButton());
            }
        }

        if (env == null) env = Env.test;
        publishWeixinMenu(env, buttons);
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("button", buttons);
        return publishMenu(env, jsonObject);
    }

    private WxMsg publishMenu(Env env, Map<String, Object> jsonObject) throws Exception {
        String url = String.format(MENU_SET_URL, this.getToken(env).getAccessToken());
        log.info("\n--------------------------------------------------------------\npublishMenu:\nevn:{}\nurl:{}", env, url);
        WxMsg wxMsg = publishMenu(env, jsonObject, url, 0);
        handleException(wxMsg.getErrcode());
        return wxMsg;
    }

    private WxMsg publishMenu(Env env, Map<String, Object> jsonObject, String url, int index) {
        WxMsg wxMsg = this.post(url, jsonObject, WxMsg.class);
        try {
            if (wxMsg.getErrcode() != 0) {
                clearToken(env);
                handleException(wxMsg.getErrcode());
            }
        } catch (Exception e) {
            clearToken(env);
            log.error("\n--------------------------------------------------------------\npublishMenu:\nevn:{}\nurl:{}\nmessage:{}", env, url, e.getMessage());
        } finally {
            if (index < this.index && wxMsg.getErrcode() != 0) {
                clearToken(env);
                return publishMenu(env, jsonObject, url, ++index);
            }
        }
        return wxMsg;
    }

    private void clearToken(Env env) {
        if (env == Env.prod) {
            this.prodToken = null;
        } else if (env == Env.test) {
            this.testToken = null;
        } else {
            this.prodToken = null;
            this.testToken = null;
        }
    }


    private List<Button> sortButtons(List<Button> buttons) {
        Collections.sort(buttons, new Comparator<Button>() {
            @Override
            public int compare(Button o1, Button o2) {
                return o1.getSort() - o2.getSort();
            }
        });

        return buttons;
    }

    private void handleException(int errcode) {
        WxCode[] values = WxCode.values();
        for (WxCode status : values) {
            if (errcode != 0 && status.getErrorcode() == errcode) {
                log.error("微信错误消息：" + status.getErrmsg());
                clearToken(null);
                throw new RuntimeException(status.getErrmsg());
            }
        }
    }


    private void publishWeixinMenu(Env env, List<Button> buttons) {
        if (env == null) env = Env.test;
        for (Button button : buttons) {
            if (button.getSubButton() != null && !button.getSubButton().isEmpty()) {
                publishWeixinMenu(env, button.getSubButton());
            } else {
            	//如果菜单时click类型
                if (BUTTON_TYPE_CLICK.equals(button.getType())) {
                    RobotMsg robotMsg = this.robotMsgMapper.selectByKey(button.getKey(), env.toString());
                    //如果数据库里面已经有与这个菜单对应的自动回复消息，更新自动回复消息
                    if (robotMsg != null) {
                        if (!NO_EDIT_KEY.contains(button.getKey())) {
                            if (null == button.getContent()) {
                                button.setContent("");
                            }
                            this.robotMsgMapper.updateByKey(button.getKey(), button.getContent(), env.toString());
                        }
                    } 
                    //如果数据库里面没有与这个菜单对应的自动回复消息，插入自动回复消息
                    else {
                        if (!NO_EDIT_KEY.contains(button.getKey())) {
                            robotMsg = new RobotMsg(new Date(), "text", env.toString(), button.getContent(), RobotMsg.Type.CLICK);
                            if (button.getKey() != null) {
                                robotMsg.setKeyword(button.getKey());
                                this.robotMsgMapper.insertSelective(robotMsg);
                            } else {
                                this.robotMsgMapper.insertSelective(robotMsg);
                                robotMsg.setKeyword(CLICK_PRE + robotMsg.getId());
                                button.setKey(robotMsg.getKeyword());
                                this.robotMsgMapper.updateByPrimaryKeySelective(robotMsg);
                            }
                        }
                    }
                } 
                //如果菜单是view类型
                else if (BUTTON_TYPE_VIEW.equals(button.getType())) {
                    if (!button.getName().equals("我要出借") && !button.getName().equals("绑定账户")) {
                        button.setUrl(this.snsapiUrl(env, button.getUrl()));
                    } else if (button.getName().equals("我要出借")) {
                        button.setUrl(this.snsapiHomeUrl(env));
                    } else if (button.getName().equals("绑定账户")) {
                        button.setUrl(this.snsapiBindUrl(env));
                    }
                }
            }
        }
    }

    /**
     * 对接收到的菜单和按钮的设置补充属性
     * @param buttons
     */
    public void getButton(List<Button> buttons) {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            //给每个按钮的sort属性设上值
            button.setSort(i + 1);
            //是菜单节点
            if (!button.getSubButton().isEmpty()) {
                getButton(button.getSubButton());
            } 
            //是按钮节点
            else {
            	//如果按钮是view类型，把真正的跳转地址截出来再赋值给url属性
                urlMatcher(button);
                //如果这个按钮是click类型，根据button key取出相应的自动回复内容，设到content属性里面
                clickContent(button);
            }
        }
    }

    public String snsapiHomeUrl(Env env) {
        String url = testIndex;
        String appId = testAppId;
        if (env == Env.prod) {
            url = this.prodIndex;
            appId = prodAppId;
        }
        url = URLEncoder.encode(url);
        return String.format(SNSAPI, appId, url, "snsapi_base", "STATE");
    }

    private String snsapiBindUrl(Env env) {
        String url = testBindUrl;
        String appId = testAppId;
        if (env == Env.prod) {
            url = this.prodBindUrl;
            appId = prodAppId;
        }
        url = URLEncoder.encode(url);
        return String.format(SNSAPI, appId, url, "snsapi_base", "STATE");
    }

    public String snsapiUrl(Env env, String url) {
        if (url == null) url = "";
        url = url.trim();
        if (!url.startsWith("http") && !url.startsWith("https")) {
            url = "http://" + url;
        }
        if (url.indexOf("?") == -1) {
            url += "?charset=utf-8";
        }
        url = URLEncoder.encode(url);
        if (env == Env.prod) {
            url = prodSnsapiUrl + url;
        } else {
            url = testSnsapiUrl + url;
        }
        url = URLEncoder.encode(url);
        String appId = testAppId;
        if (env == Env.prod) {
            appId = prodAppId;
        }
        return String.format(SNSAPI, appId, url, "snsapi_base", "STATE");
    }

    private boolean isExpiresIn(Token token) {
        if (token == null) return true;
        if (((System.currentTimeMillis() - token.getCreateTime()) / 1000) > token.getExpiresIn() - 600) {
            return true;
        }
        return false;
    }

    /**
     * 如果按钮是view类型，把真正的跳转地址截出来再赋值给url属性
     * @param button
     */
    private void urlMatcher(Button button) {
        if (BUTTON_TYPE_VIEW.equals(button.getType())) {
            String linkUrl = URLDecoder.decode(button.getUrl());
            Pattern pattern = Pattern.compile("redirectUrl=.+&response_type");
            Matcher matcher = pattern.matcher(linkUrl);
            if (matcher.find()) {
                String value = matcher.group();
                value = value.substring("redirectUrl=".length(), value.length() - "&response_type".length());
                value = URLDecoder.decode(value);
                button.setUrl(value);
            }
        }
    }

    /**
     * 如果这个按钮是click类型，根据button key取出相应的自动回复内容，设到content属性里面
     * @param button
     */
    private void clickContent(Button button) {
        if (BUTTON_TYPE_CLICK.equals(button.getType())) {
            RobotMsg robotMsg = this.robotMsgMapper.selectByKey(button.getKey(), Env.test.toString());
            if (robotMsg != null) {
                String content = robotMsg.getContent();
//                if (content != null)
//                    content = content.replaceAll("\"",  "\\\"");
//                    content = content.replaceAll("'", "\\\'");
                button.setContent(content);
            }
        }
    }

    private <T> T get(String url, Class<T> clazz) {
        return httpComponent.get(url, clazz);
    }

    private <T> T post(String url, Object request, Class<T> clazz) {
        return httpComponent.post(url, request, clazz);
    }

    public List<RobotMsg> selectRobotMsgByType(RobotMsg robotMsg, RowBounds bounds) {
        return this.robotMsgMapper.selectContentMsgByType(robotMsg, bounds);
    }

    public int updateByPrimaryKeySelective(RobotMsg robotMsg) {
        return robotMsgMapper.updateByPrimaryKeySelective(robotMsg);
    }

    public int insertSelective(RobotMsg robotMsg) {
        return robotMsgMapper.insertSelective(robotMsg);
    }

    public RobotMsg selectByPrimaryKey(int id) {
        return robotMsgMapper.selectByPrimaryKey(id);
    }

    public int deleteByPrimaryKeys(List<Integer> msgs) {
        return robotMsgMapper.deleteByPrimaryKeys(msgs);
    }
}
