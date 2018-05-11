package com.fenlibao.platform.model;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.util.AES;
import com.fenlibao.platform.common.util.AESForTP;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class BusinessInfo {

    private static Config config = ConfigFactory.create(Config.class);

    private Integer id;
    private Integer requestId;//请求id
    private Integer userId;//
    private String idNo;//用户身份证号
    private String idName;//用户姓名
    private String phoneNum;//用户电话号码
    private String bankcardNo;//银行卡号
    private String appid;//商户id
    public BusinessInfo() {

    }

    public BusinessInfo(int requestId, int userId,  String idNo, String idName, String phoneNum,String appid,String bankcardNo) {
        this.requestId = requestId;
        this.userId = userId;
        this.idNo = idNo;
        this.idName = idName;
        this.phoneNum = phoneNum;
        this.appid = appid;
        this.bankcardNo = bankcardNo;
    }




    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]\\d{8}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public Response verifyBusinessInfo(String key) throws Exception {
        if (StringUtils.isBlank(appid)
                || StringUtils.isBlank(idNo)
                || StringUtils.isBlank(idName)
                || StringUtils.isBlank(phoneNum)
                || StringUtils.isBlank(phoneNum)
                ||StringUtils.isBlank(bankcardNo)) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }

        try {
            idName = AESForTP.decode(key, idName);
            idNo = AESForTP.decode(key, idNo);
            phoneNum = AESForTP.decode(key, phoneNum);
            bankcardNo = AESForTP.decode(key,bankcardNo);
        } catch (Exception e) {
            return Response.LOAN_DECODE_ERROR;
        }


        if (!Pattern.matches(REGEX_MOBILE, phoneNum) || !Pattern.matches(REGEX_ID_CARD, idNo)) {
            return Response.IDCARD_PARAMETERS_ERROR;
        }

        //身份证加密
        idNo = AES.getInstace().encrypt(idNo);
        bankcardNo =  AES.getInstace().encrypt(bankcardNo);
        return Response.RESPONSE_SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        BusinessInfo.config = config;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }
}
