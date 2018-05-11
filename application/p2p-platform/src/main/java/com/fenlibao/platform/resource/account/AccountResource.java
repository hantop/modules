package com.fenlibao.platform.resource.account;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.service.xinwang.entrust.XWEntrustImportUserService;
import com.fenlibao.p2p.util.api.encrypt.AES;
import com.fenlibao.p2p.util.api.http.HttpClientUtil;
import com.fenlibao.p2p.util.api.http.HttpHeader;
import com.fenlibao.p2p.util.api.http.defines.HttpResult;
import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.enums.ResquestStatus;
import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.AESForTP;
import com.fenlibao.platform.common.util.IPUtil;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.BusinessAgreement;
import com.fenlibao.platform.model.BusinessInfo;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.resource.CommonResource;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.member.MemberService;
import com.fenlibao.platform.util.file.HttpHeadersUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/7/10.
 */
@Path("account")
public class AccountResource extends ParentResource {

    @Inject
    private MemberService memberService;

    @Inject
    private CommonService commonService;


    private static Config config = ConfigFactory.create(Config.class);

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@FormParam("appid") String appid, @FormParam("userName") String userName, @FormParam("userNo") String userNo, @FormParam("userPhone") String userPhone,
                           @FormParam("bankcardNo") String bankcardNo,
                           @FormParam("agreementSignId") String agreementSignId,
                           @FormParam("agreementDocId") String agreementDocId,
                           @FormParam("flbPageNum") String flbPageNum,
                           @FormParam("flbSignX") String flbSignX,
                           @FormParam("flbSignY") String flbSignY,
                           @FormParam("loanPageNum") String loanPageNum,
                           @FormParam("loanSignX") String loanSignX,
                           @FormParam("loanSignY") String loanSignY,
                           @FormParam("agreementNumber") String agreementNumber,
                           @Context HttpServletRequest request) throws Exception {
        Map<String, Object> response = success();
        int requestId = 0;
        int userId = 0;
        Map<String, Object> requestMap = new HashMap<>();
        BusinessInfo businessInfo = null;
        String key = null;
       /*key = commonService.getAesSecret(appid);
        userName = AESForTP.encode(key,userName);
        userNo = AESForTP.encode(key,userNo);
        userPhone = AESForTP.encode(key,userPhone);
        bankcardNo = AESForTP.encode(key,bankcardNo);*/
        BusinessAgreement businessAgreement = null;
        try {
            businessInfo = new BusinessInfo(requestId, userId, userNo, userName, userPhone, appid,bankcardNo);
            businessAgreement = new BusinessAgreement(agreementDocId, agreementSignId,flbPageNum,flbSignX,flbSignY,loanPageNum,loanSignX,loanSignY,agreementNumber);
            //获取密钥redis,校验数据
            key = commonService.getAesSecret(appid);
            //校验参数
            Response res = businessInfo.verifyBusinessInfo(key);
            if (res != Response.RESPONSE_SUCCESS) {
                if (res == Response.SYSTEM_EMPTY_PARAMETERS || bankcardNo == null) {
                    logger.error("{}参数为空...");
                } else if (res == Response.IDCARD_PARAMETERS_ERROR) {
                    logger.error("{}手机号码或身份证格式错误...");
                }
                response = failure(res);
                logger.error("{}数据校验失败...", userPhone);
                return jackson(response);
            }
            //校验协议文件
            res = businessAgreement.verifyAgreement();
            if (res != Response.RESPONSE_SUCCESS) {
                if (res == Response.SYSTEM_EMPTY_PARAMETERS ) {
                    logger.error("{}参数为空...");
                }
                response = failure(res);
                logger.error("{}数据校验失败...", userPhone);
                return jackson(response);
            }
        } catch (Exception e) {
            response = failure(Response.SYSTEM_ERROR_PARAMETERS);
            logger.error("数据校验失败...");
            return jackson(response);
        }
        requestMap.put("phoneNum", businessInfo.getPhoneNum());
        //避免重复提交
        if (!commonService.existsKey(businessInfo.getPhoneNum())) {
            requestMap.put("status", ResquestStatus.STATUS_CG);
            //判断是否已开户
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("phoneNum",businessInfo.getPhoneNum());
            paramMap.put("userName",businessInfo.getIdName());
            paramMap.put("userNo",businessInfo.getIdNo());
            paramMap.put("bankcardNo",businessInfo.getBankcardNo());
            if (commonService.getBusinessUser(paramMap) >= 1) {//已开户
                response = failure(Response.BUSINESS_EXIST_ERROR);
                return jackson(response);
            }

            //用户四要素是否已经提交开户
            paramMap.clear();
            paramMap.put("userNo",businessInfo.getIdNo());
            if(commonService.getBusinessUser(paramMap)>=1){
                response = failure(Response.BUSINESS_EXIST_IDCARD);
                //插入响应错误信息表
                insertErrorMsg(businessInfo,String.valueOf( response.get("code")),null,(String) response.get("message"));
                return jackson(response);
            }
            paramMap.clear();
            paramMap.put("phoneNum",businessInfo.getPhoneNum());
            if(commonService.getBusinessUser(paramMap)>=1){
                response = failure(Response.BUSINESS_EXIST_PHONE);
                //插入响应错误信息表
                insertErrorMsg(businessInfo,String.valueOf(response.get("code")),null,(String) response.get("message"));
                return jackson(response);
            }

            paramMap.clear();
            paramMap.put("bankcardNo",businessInfo.getBankcardNo());
            if(commonService.getBusinessUser(paramMap)>=1){
                response = failure(Response.BUSINESS_EXIST_BANKCARD);
                //插入响应错误信息表
                insertErrorMsg(businessInfo,String.valueOf(response.get("code")),null,(String) response.get("message"));
                return jackson(response);
            }

            //判断是否存在失败或者待处理数据
            requestMap.put("status", "SB");
            requestId=Integer.parseInt(commonService.getBusinessRequest(requestMap)==null?"0":commonService.getBusinessRequest(requestMap));
            if (requestId < 1) {
                //插入开户请求记录数据
                try {
                    requestMap.put("status", ResquestStatus.STATUS_SB.getCode());
                    commonService.addBusinessRequest(requestMap);
                    requestId = Integer.parseInt(String.valueOf(requestMap.get("id")));
                    logger.info("[{}]开户请求入库成功...", businessInfo.getPhoneNum());
                } catch (Exception e) {
                    logger.info("[{}]开户请求入库失败...", businessInfo.getPhoneNum());
                    response = failure();
                    //插入响应错误信息表
                    insertErrorMsg(businessInfo,"500",e.getMessage(),"开户请求入库失败");
                    return jackson(response);
                }

            }



            //提交的手机号码先注册分利宝平台账号
            try {
                userId = memberService.businessRegister(businessInfo.getPhoneNum());
            } catch (Exception e) {
                response = failure();
                logger.error(String.format("注册失败>>>,phone_num=[%s]",
                        businessInfo.getPhoneNum()), e);
                //插入响应错误信息表
                insertErrorMsg(businessInfo,String.valueOf( response.get("code")),e.getMessage(),"注册失败");
                return jackson(response);
            }
            businessAgreement.setUserId(userId);
            //保存委托开户协议签章
            if(commonService.getAgreement(userId)<1) {
                try {
                    //businessAgreement.setUserId(userId);
                    commonService.addBusinessAgreement(businessAgreement);
                } catch (Exception e) {
                    response = failure();
                    logger.error(String.format("保存签章失败>>>,phone_num=[%s]",
                            businessInfo.getPhoneNum()), e);
                    //插入响应错误信息表
                    insertErrorMsg(businessInfo,"500",e.getMessage(),"保存签章失败");
                    return jackson(response);
                }
            }



            //调用新网委托开户
            try {
                JSONObject params = new JSONObject();
                params.put("userId", userId);
                params.put("realName", businessInfo.getIdName());
                params.put("idCardNo", AES.getInstace().decrypt(businessInfo.getIdNo()));
                params.put("mobile", businessInfo.getPhoneNum());
                params.put("bankcardNo",AES.getInstace().decrypt(businessInfo.getBankcardNo()));
                params.put("userRole", "ENTRUST_BORROWERS");
                params.put("idCardType", "PRC_ID");
                HttpResult result = HttpClientUtil.httpsMapPost(config.getXinwangRegisterUrl(), params, HttpHeadersUtil.getgetHeaders());
                JSONObject json = JSONObject.parseObject(new String(result.getBytes()));
                String code = (String) json.get("code");
                String message =  (String) json.get("message");
                if (!code.equals("200")&&!message.contains("用户已存在")) {
                    response.put("code", code);
                    response.put("message", json.get("message"));
                    //插入响应错误信息表
                    insertErrorMsg(businessInfo,String.valueOf( response.get("code")),null,(String) json.get("message"));
                    return jackson(response);
                }
            } catch (Exception e) {
                logger.info("[{}]委托开户失败,新网接口调用异常。。。", businessInfo.getPhoneNum());
                //请求更新为失败
                response = failure(Response.BUSINESS_REGISTER_ERROR);
                //插入响应错误信息表
                insertErrorMsg(businessInfo,String.valueOf( response.get("code")),e.getMessage(),"委托开户失败,新网接口调用异常。");
                return jackson(response);
            }


            //插入开户信息表数据,委托授权协议
            businessInfo.setRequestId(requestId);
            businessInfo.setUserId(userId);

            try {
                commonService.registerAndAgreement(businessInfo,businessAgreement);
                //commonService.addBusinessUserInfo(businessInfo);
                logger.info("[{}]入库开户信息成功...", businessInfo.getPhoneNum());
            } catch (Exception e) {
                logger.info("[{}]入库开户信息失败...", businessInfo.getPhoneNum());
                e.printStackTrace();
                response = failure();
                //插入响应错误信息表
                insertErrorMsg(businessInfo,"500",e.getMessage(),"入库开户信息失败。");
                return jackson(response);
            }

            try{
                requestMap.put("status", ResquestStatus.STATUS_CG.getCode());
                commonService.updateBusinessRequest(requestMap);
            }catch (Exception e) {
                logger.info("[{}]开户成功状态更新失败...", businessInfo.getPhoneNum());
                response = failure();
                //插入响应错误信息表
                insertErrorMsg(businessInfo,"500",e.getMessage(),"开户成功状态更新失败。");
                return jackson(response);
            }

            response.put("code", 0);
            response.put("message", "开户成功");
            return jackson(response);

        } else {
            logger.info("phone_num[{}]重复提交。。。", businessInfo.getPhoneNum());
            response = failure(Response.SYSTEM_REQUEST_FREQUENT);
            return jackson(response);
        }
    }

    private void insertErrorMsg(BusinessInfo info,String code,String msg,String description){
        Map<String,Object> map = new HashMap<>();
        map.put("userNo",info.getIdNo());
        map.put("bankcardNo",info.getBankcardNo());
        map.put("userName",info.getIdName());
        map.put("phone",info.getPhoneNum());
        map.put("code",code);
        map.put("message",msg);
        map.put("description",description);
        this.commonService.addUserExceprionResopnse(map);
    }


}
