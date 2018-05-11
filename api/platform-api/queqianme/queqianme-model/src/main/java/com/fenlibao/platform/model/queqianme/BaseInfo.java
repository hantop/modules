package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.common.util.AES;
import com.fenlibao.platform.common.util.AESForTP;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.Response;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 风控模型（用户基本信息）
 */
public class BaseInfo {

    private static Config config = ConfigFactory.create(Config.class);

    private int id;
    private String idNo;//用户身份证号
    private String idName;//用户姓名
    private String phoneNum;//用户电话号码
    private int sex;//性别
    private String marryStatus;//婚姻状况
    private String location;//身份证归属地
    private String address;//居住地
    private String qq;//qq号





    public BaseInfo() {

    }

    public BaseInfo(String baseInfo) throws Exception{
        Map<String,Object> baseMap  = Jackson.getMapFormString(baseInfo);
        if(baseMap!=null){
            this.qq = (String) baseMap.get("qq");
            this.location = (String)baseMap.get("location");
            this.address = (String)baseMap.get("address");
            this.marryStatus = (String)baseMap.get("marryStatus");
            this.idName = (String)baseMap.get("idName");
            this.phoneNum = (String)baseMap.get("phoneNum");
            this.idNo = (String)baseMap.get("idNo");
            this.sex = (Integer)baseMap.get("sex");
        }

    }



    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]\\d{8}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public Response verifyBaseInfo(String key) throws Exception {
        if (StringUtils.isBlank(qq)
                || sex < 0
                || StringUtils.isBlank(idNo)
                || StringUtils.isBlank(idName)
                || StringUtils.isBlank(phoneNum)
                ||StringUtils.isBlank(address)
                ||StringUtils.isBlank(location)
                ||StringUtils.isBlank(marryStatus)) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }

        if(!marryStatus.equals("已婚")&&!marryStatus.equals("未婚")){
            return Response.SYSTEM_ERROR_PARAMETERS;
        }

        //解密
        try {
            idName = AESForTP.decode(key, idName);
            idNo = AESForTP.decode(key, idNo);
            phoneNum = AESForTP.decode(key, phoneNum);
        } catch (Exception e) {
            return Response.LOAN_DECODE_ERROR;
        }

        if (!Pattern.matches(REGEX_MOBILE, phoneNum) || !Pattern.matches(REGEX_ID_CARD, idNo)) {
            return Response.IDCARD_PARAMETERS_ERROR;
        }


        //身份证加密
        /*idNo = AES.getInstace().encrypt(idNo);*/
        try {

            idNo =  idNo.substring(0, 2)+"************" + idNo.substring(idNo.length()-4,idNo.length());
        }catch (Throwable e){

        }

        return Response.RESPONSE_SUCCESS;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMarryStatus() {
        return marryStatus;
    }

    public void setMarryStatus(String marryStatus) {
        this.marryStatus = marryStatus;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
