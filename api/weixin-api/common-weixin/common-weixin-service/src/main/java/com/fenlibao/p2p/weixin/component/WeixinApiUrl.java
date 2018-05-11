package com.fenlibao.p2p.weixin.component;

import com.fenlibao.p2p.weixin.defines.Lang;
import com.fenlibao.p2p.weixin.defines.SnsapiScope;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/9/8.
 */
@Component
public final class WeixinApiUrl {

    /**
     * 基础access_token
     * grant_type	是	获取access_token填写client_credential
     * appid	是	第三方用户唯一凭证
     * secret	是	第三方用户唯一凭证密钥，即appsecret
     */
    private static String BASE_ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 网页授权access_token
     * 参数	是否必须	说明
     * appid	是	公众号的唯一标识
     * secret	是	公众号的appsecret
     * code	是	填写第一步获取的code参数
     * grant_type	是	填写为authorization_code
     */
    private static String OAUTH2_ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 网页授权获取用户基本信息
     * appid:公众号的唯一标识
     * redirect_uri:授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * code:返回类型，请填写code,改code由微信生成
     * scope:应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * state:重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    private static String SNSAPI = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    /**
     * 调用微信JS接口的临时票据
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    private static String JSAPI_TICKET_API_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    /**
     * 调微信卡券接口中使用的签名凭证api_ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    private static String JSAPI_CARD_TICKET_URL_API_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=wx_card";

    /**
     * 投放卡券二维码ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    private static String CARD_TICKET_URL_API_URL = "https://api.weixin.qq.com/card/qrcode/create?access_token=%s";

    /**
     * 创建二维码ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    private static String QRCODE_TICKET_URL_API_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    /**
     * 根据二维码获取二维码图片
     * 参数	是否必须	说明
     * ticket	是	获取二维码的ticket
     */
    private static String QRCODE_URL_API_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    /**
     * access_token	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * openid	用户的唯一标识
     * lang	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     */
    private static String SNSAPI_USERINFO_API_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";

    /**
     * 发送模板消息
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    private static String SEND_TEMPLATE_MSG_API_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    /**
     * 自定义菜单
     */
    private static String CUSTOM_MENU_API_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    /**
     * access_token	是	调用接口凭证
     * openid	是	普通用户的标识，对当前公众号唯一
     * lang	否	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     */
    private static String USER_INFO_API_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=%s";


    /**
     * access_token	是	调用接口凭证
     * next_openid	是	第一个拉取的OPENID，不填默认从头开始拉取
     */
    private static String GET_USER_LIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";

    /**
     * grant_type	是	获取access_token填写client_credential
     * appid	是	第三方用户唯一凭证
     * secret	是	第三方用户唯一凭证密钥，即appsecret
     *
     * @return
     */
    public String getBaseAccessTokenApiUrl(String appid, String secret) {
        return String.format(BASE_ACCESS_TOKEN_API_URL, appid, secret);
    }

    /**
     * 网页授权access_token
     * 参数	是否必须	说明
     * appid	是	公众号的唯一标识
     * secret	是	公众号的appsecret
     * code	是	填写第一步获取的code参数
     * grant_type	是	填写为authorization_code
     */
    public String getOauth2AccessTokenApiUrl(String appid, String secret, String code) {
        return String.format(OAUTH2_ACCESS_TOKEN_API_URL, appid, secret, code);
    }

    /**
     * 网页授权获取用户基本信息
     * appid:公众号的唯一标识
     * redirect_uri:授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * code:返回类型，请填写code,改code由微信生成
     * scope:应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * state:重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    public String getSnsapi(String appid, String url, SnsapiScope scope, String state) {
        return String.format(SNSAPI, appid, url, scope, state);
    }


    /**
     * 调用微信JS接口的临时票据
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getJsapiTicketApiUrl(String access_token) {
        return String.format(JSAPI_TICKET_API_URL, access_token);
    }

    /**
     * 调微信卡券接口中使用的签名凭证api_ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getJsapiCardTicketUrlApiUrl(String access_token) {
        return String.format(JSAPI_CARD_TICKET_URL_API_URL, access_token);
    }

    /**
     * 投放卡券二维码ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getCardTicketUrlApiUrl(String access_token) {
        return String.format(CARD_TICKET_URL_API_URL, access_token);
    }

    /**
     * 创建二维码ticket
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getQrcodeTicketUrlApiUrl(String access_token) {
        return String.format(QRCODE_TICKET_URL_API_URL, access_token);
    }

    /**
     * 根据二维码获取二维码图片
     * 参数	是否必须	说明
     * ticket	是	获取二维码的ticket
     */
    public String getQrcodeUrlApiUrl(String ticket) {
        return String.format(QRCODE_URL_API_URL, ticket);
    }

    /**
     * access_token	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * openid	用户的唯一标识
     * lang	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     */
    public String getSnsapiUserinfoApiUrl(String access_token, String openid, Lang lang) {
        return String.format(SNSAPI_USERINFO_API_URL, access_token, openid, lang);
    }

    /**
     * 发送模板消息
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getSendTemplateMsgApiUrl(String access_token) {
        return String.format(SEND_TEMPLATE_MSG_API_URL, access_token);
    }

    /**
     * 自定义菜单创建接口
     * 参数	是否必须	说明
     * access_token	是	基础的access_token
     */
    public String getCustomMenuApiUrl(String access_token) {
        return String.format(CUSTOM_MENU_API_URL, access_token);
    }

    /**
     * 获取用户基本信息（包括UnionID机制）
     * access_token	是	调用接口凭证
     * openid	是	普通用户的标识，对当前公众号唯一
     * lang	否	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     *
     * @param access_token
     * @param openid
     * @param lang
     * @return
     */
    public String getUserInfoApiUrl(String access_token, String openid, Lang lang) {
        return String.format(USER_INFO_API_URL, access_token, openid, lang);
    }

    /**
     * 获取帐号的关注者列表
     *
     * @param access_token 调用接口凭证
     * @param next_openid  第一个拉取的OPENID，不填默认从头开始拉取
     * @return
     */
    public String getUserListUrl(String access_token, String next_openid) {
        return String.format(GET_USER_LIST_URL, access_token, next_openid);
    }
}
