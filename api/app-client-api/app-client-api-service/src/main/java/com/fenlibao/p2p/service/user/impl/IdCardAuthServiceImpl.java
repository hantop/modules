package com.fenlibao.p2p.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.enums.user.UserAuthEnum;
import com.fenlibao.p2p.model.form.BindIdCardForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.NciicDmService;
import com.fenlibao.p2p.service.user.IdCardAuthService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.ud.PartnerRequest;
import com.fenlibao.thirdparty.guoxin.GxConst;
import com.fenlibao.thirdparty.guoxin.GxWebServices;
import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityResponseSingle;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 实名认证服务类
 *
 * @author Mingway.Xu
 * @date 2016/11/7 14:07
 */
@Service
public class IdCardAuthServiceImpl implements IdCardAuthService {
    private static final Logger logger= LogManager.getLogger(IdCardAuthService.class);

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private NciicDmService nciicDmService;

    @Override
    public HttpResponse realNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable {
        HttpResponse tempResponse = response;
        response = guoxinRealNameAuth(userInfo, bindIdCardForm, response);
        if (response.getCode().equals(ResponseCode.USER_AUTH_UN_MATCH.getCode()) || response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
            return response;
        } else {
            return udRealNameAuth(userInfo, bindIdCardForm, tempResponse);
        }
    }

    /**
     * 实名认证操作
     *
     * @param userInfo 系统用户信息
     * @param form     身份证信息
     * @return
     */
    @Override
    public Map<String, Object> guoxinAuthUserRealNameAndIdcard(UserInfo userInfo, BindIdCardForm form) throws Throwable {
        Map<String, Object> resultMap = null;
        Map<String, Object> temMap = null;
        String bankCarNum = form.getIdCardNum();
        String orderRules = "GX" + System.currentTimeMillis() + bankCarNum.substring(bankCarNum.length() - 4);

        GxWebServices gxWebServices = new GxWebServices();

        String key = Config.get("gx.key");

        String url = Config.get("gx.invoke.url");

        String username = Config.get("gx.username");
        String password = Config.get("gx.password");

        /******【身份】*********/
        String name = "";
        String identity = "";

        name = form.getIdCardFullName();
        identity = form.getIdCardNum();

        String xml = gxWebServices.gxRealNameAuthentication(name, identity, key, url, username, password);
        IdentityResponseSingle identityResponseSingle = gxWebServices.xmlElements(xml);

        if(identityResponseSingle!=null)
            logger.info("[********GXAuthInfo********]-->authmessage status:"+identityResponseSingle.getAuthmessageStatus()+" ,message status:"+identityResponseSingle.getMessageStatus());

        if (identityResponseSingle!=null) {

            String authmessageStatus = identityResponseSingle.getAuthmessageStatus();

            resultMap = new HashMap<>();
            temMap = new HashMap<>();
            resultMap.put("authmessageStatus",authmessageStatus);
            if (GxConst.authmessageStatus_success.equals(authmessageStatus)) {

                String messageStatus = identityResponseSingle.getMessageStatus();
                resultMap.put("messageStatus", messageStatus);
                if(GxConst.messageStatus_success.equals(messageStatus)){
                    resultMap.put("status",identityResponseSingle.getAuthStatus());
                    resultMap.put("retMsg", identityResponseSingle.getAuthResult());
                    resultMap.put("retCode", identityResponseSingle.getAuthmessageStatus());

                    temMap.put("result", (Integer) resultMap.get("status")+1);
                    temMap.put("name_card", form.getIdCardFullName());
                    temMap.put("ret_code", resultMap.get("retCode"));
                    temMap.put("product_id", null);
                    temMap.put("sign_type", null);
                    temMap.put("ret_msg", resultMap.get("retMsg"));
                    temMap.put("outorder_no", orderRules);

                    temMap.put("id_card", StringHelper.encode(bankCarNum));

                    temMap.put("order_fee", GxConst.ORDER_FEE);
                    temMap.put("order_no", null);
                    temMap.put("userId", userInfo.getUserId());
                    temMap.put("useType", "1");

                    userInfoService.insertlianLianAuth(temMap);
                }
            } else {
                resultMap.put("retCode",identityResponseSingle.getAuthmessageStatus());
                resultMap.put("retMsg", identityResponseSingle.getAuthmessageValue());
            }

        } else {

            return resultMap;
        }

        return resultMap;
    }

    @Override
    public HttpResponse guoxinRealNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable {

        Map<String,Object> resultMap = guoxinAuthUserRealNameAndIdcard(userInfo, bindIdCardForm);
        if(resultMap!=null&& !resultMap.isEmpty() && resultMap.size() > 0){
            if(GxConst.authmessageStatus_success.equals(resultMap.get("authmessageStatus"))){
                Integer status = (Integer) resultMap.get("status");
                if (GxConst.messageStatus_success.equals(resultMap.get("messageStatus"))) {
                    if (GxConst.authStatus_YZ.equals(status)) {
                        //保存实名认证信息
                        String returnCode = saveRealNameAuthentication(bindIdCardForm);
                        response.setCodeMessage(StringUtils.isNotEmpty(returnCode) ? ResponseCode.USER_AUTH_REALNAME_FAIL : ResponseCode.SUCCESS);

                    } else if (GxConst.authStatus_BYZ.equals(status)) {
                        response.setCodeMessage(ResponseCode.USER_AUTH_UN_MATCH.getCode(), ResponseCode.USER_AUTH_UN_MATCH.getMessage());
                    } else if (GxConst.authStatus_NULL.equals(status)) {
                        response.setCodeMessage(ResponseCode.GUOXIN_LIB_NULL);
                    }
                } else {
                    logger.info("[********GXAuthError********]-->authmessage status:"+resultMap.get("retCode")+" ,message:"+resultMap.get("retMsg"));
                    response.setCodeMessage(ResponseCode.GUOXIN_CHANNEL_ERROR);
                }
            }else {
                logger.info("[********GXAuthError********]-->authmessage status:"+resultMap.get("retCode")+" ,message:"+resultMap.get("retMsg"));
                response.setCodeMessage(ResponseCode.GUOXIN_CHANNEL_ERROR);
            }
        } else {
            response.setCodeMessage(ResponseCode.GUOXIN_CHANNEL_ERROR);
        }

        return response;
    }

    @Override
    public HttpResponse udRealNameAuth(UserInfo userInfo, BindIdCardForm bindIdCardForm, HttpResponse response) throws Throwable {
        Map<String,Object> resultMap = edRealNameAuthentication(userInfo, bindIdCardForm);    // 有盾网实名认证
        if (!resultMap.isEmpty() && resultMap.size() > 0) {
            String retCode = (String) resultMap.get("retCode");
            if (InterfaceConst.SMRZ_CORRECT_RETCODE.equals(retCode)) {
                String score = (String) resultMap.get("score");
                if (InterfaceConst.SMRZ_ONE_SCORE.equals(score)) {    //实名认证成功了
                    String returnCode = saveRealNameAuthentication(bindIdCardForm);
                    if (StringUtils.isNotBlank(returnCode)) {
                        response.setCodeMessage(returnCode, ResponseCode.USER_AUTH_REALNAME_FAIL.getMessage());
                    }else {
                        response.setCodeMessage(ResponseCode.SUCCESS);
                    }
                } else {
                    String errMsg = "请核实姓名或者身份证信息是否输入正确";

                    response.setCodeMessage(ResponseCode.USER_AUTH_REALNAME_FAIL.getCode(), errMsg);
                }
            } else {
                response.setCodeMessage(ResponseCode.FAILURE);
            }
        } else {
            response.setCodeMessage(ResponseCode.FAILURE);
        }

        return response;
    }

    //调用第三方api
    @Override
    public Map<String,Object> edRealNameAuthentication(UserInfo userInfo,BindIdCardForm form) throws Exception {

        Map<String,Object> resultMap = null;

        String url = Config.get("ud.invoke.url");

        String pubKeyStr = Config.get("ud.pubkey");

        String secretKeyStr = Config.get("ud.secretkey");

        String pubKey = null;
        String secretKey = null;

        try {
            pubKey = AES.getInstace().decrypt(pubKeyStr);
            secretKey = AES.getInstace().decrypt(secretKeyStr);

        } catch (Exception ex) {
            logger.info("[********UD  key 解密失败 ********]");
        }

        String productCode = Config.get("ud.product.code");

        Map<String, String> body =  new HashMap<String, String>();
        body.put("id_no", form.getIdCardNum());
        body.put("id_name", form.getIdCardFullName());

        String idCarNum = form.getIdCardNum();
        String orderRules = "UD" + System.currentTimeMillis() + idCarNum.substring(idCarNum.length() - 4);
        String outbuffer = PartnerRequest. apiCall (url, pubKey, secretKey, productCode,orderRules, body);

        /**
         * 日志记录
         */
        logger.info(outbuffer);

        Map authMap = JSONObject.parseObject(outbuffer);

        if (!authMap.isEmpty() && authMap.size() > 0){
            resultMap = new HashMap();

            Map header = (Map<String, String>) authMap.get("header");
            Map resultBody = (Map<String, String>) authMap.get("body");
            String retCode = (String) header.get("ret_code");
            if(!StringUtils.isBlank(retCode)){
                if(InterfaceConst.SMRZ_CORRECT_RETCODE.equals(retCode)){	//返回正确码

                    String score = (String) resultBody.get("status");
                    if(!StringUtils.isBlank(score)){	//返回验证的结果
                        resultMap.put("retCode",retCode);
                        resultMap.put("score",score);
                        if(!InterfaceConst.SMRZ_ZERO_SCORE.equals(score)){
                            try {
                                resultMap.put("retMsg",this.cleanAuthStatus(score, (String) header.get("ret_msg")));

                                Map<String,Object> temMap = new HashMap<>();	//保存操作记录


                                temMap.put("ret_msg",this.cleanAuthStatus(score, (String) header.get("ret_msg")));

                                temMap.put("result",score);
                                temMap.put("name_card",form.getIdCardFullName());
                                temMap.put("ret_code",retCode);
                                temMap.put("product_id",null);
                                temMap.put("sign_type",null);
                                temMap.put("outorder_no",orderRules);
                                temMap.put("id_card",StringHelper.encode(idCarNum));
                                temMap.put("order_fee", InterfaceConst.SMRZ_ORDER_FEE);
                                temMap.put("order_no",resultBody.get("ud_order_no"));
                                temMap.put("userId",userInfo.getUserId());
                                temMap.put("useType", "1"); //用于统计实名认证次数
                                userInfoService.insertlianLianAuth(temMap);

                            }catch (Throwable throwable){
                                throwable.printStackTrace();
                            }
                        }else{
                            resultMap.put("errorMsg",authMap.get("errorMsg"));
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    //保存实名认证信息
    @Override
    public String saveRealNameAuthentication(BindIdCardForm bindIdCardForm) throws Throwable{
        String codeMsg = "";
        String idCardNum = bindIdCardForm.getIdCardNum().toUpperCase();
        boolean state = nciicDmService.check(idCardNum, bindIdCardForm.getIdCardFullName(),true);
        if(state){

            userInfoService.bindIdCard(bindIdCardForm);
        }else {
            codeMsg = ResponseCode.USER_AUTH_REALNAME_FAIL.getCode();
        }
        return codeMsg;
    }

    /**
     * 整合UD过来的认证状态
     * @param udStatus
     * @param retMsg
     * @return
     */
    private String cleanAuthStatus(String udStatus, String retMsg) {
        String resultMsg = null;
        if (udStatus.equals(UserAuthEnum.UD_AUTH_MATCH.getCode())) {
            resultMsg = UserAuthEnum.UD_AUTH_MATCH.getName();
        }else if (udStatus.equals(UserAuthEnum.UD_AUTH_UN_MATCH.getCode())) {
            resultMsg = UserAuthEnum.UD_AUTH_UN_MATCH.getName();
        } else if (udStatus.equals(UserAuthEnum.UD_AUTH_NOT_CONTAIN.getCode())) {
            resultMsg = UserAuthEnum.UD_AUTH_NOT_CONTAIN.getName();
        } else {
            resultMsg = retMsg;
        }
        return resultMsg;
    }
}
