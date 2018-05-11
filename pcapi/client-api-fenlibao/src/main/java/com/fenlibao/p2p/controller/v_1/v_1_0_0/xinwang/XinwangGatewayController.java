package com.fenlibao.p2p.controller.v_1.v_1_0_0.xinwang;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.entity.UserBaseInfo;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.WithdrawType;
import com.fenlibao.p2p.model.xinwang.enums.account.AuthList;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.param.account.*;
import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.xinwang.account.*;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnpBindcardService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnterpriseService;
import com.fenlibao.p2p.service.xinwang.pay.XWRechargeProcessService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.Validator;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 新网银行网关接口
 */
@RestController("v_1_0_0/XinwangGatewayController")
@RequestMapping(value = "xinwang/gateway", headers = APIVersion.V_1_0_0)
public class XinwangGatewayController {

    private static final Logger logger= LogManager.getLogger(XinwangGatewayController.class);

    @Resource
    XWPersonalRegisterService personalRegisterService;
    @Resource
    XWRechargeService rechargeService;
    @Resource
    XWWithdrawService withdrawService;
    @Resource
    XWResetPasswordService resetPasswordService;
    @Resource
    XWModifyMobileService modifyMobileService;
    @Resource
    XWUnbindBankCardService unbindBankCardService;
    @Resource
    XWUserInfoService userInfoService;
    @Resource
    XWRechargeProcessService rechargeProcessService;
    @Resource
    XWBindBankcardService bindBankcardService;
    @Resource
    UserInfoService sysUserInfoService;
    @Resource
    BankService bankService;
    @Resource
    XWEnterpriseRegisterService enterpriseRegisterService;
    @Resource
    XWEnterpriseService enterpriseService;
    @Resource
    XWEnpBindcardService enpBindcardService;
    @Resource
    XWActivateStockedUserService activateStockedUserService;
    /**
     * 用户个人注册
     * @param formExtend
     * @return
     */
    @RequestMapping(value = "register",method= RequestMethod.POST)
    HttpResponse personalRegister(@ModelAttribute BaseRequestFormExtend formExtend,
                                  String realName,
                                  String idCardNo,
                                  String phone,
                                  String bankCardNo,
                                  String uri,String userRole) throws Throwable {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                || StringUtils.isBlank(bankCardNo)
                || StringUtils.isBlank(phone)
                ||StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        String realNameParam;
        String idCardNoParam;
        if (StringUtils.isBlank(realName) || StringUtils.isBlank(idCardNo)) {
            UserBaseInfo baseInfo = sysUserInfoService.getuserBaseInfo(formExtend.getUserId());
            if (baseInfo == null
                    || baseInfo.getUserName() == null
                    || baseInfo.getCardIDEncrypt() == null) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
            }
            realNameParam = baseInfo.getUserName();
            idCardNoParam = StringHelper.decode(baseInfo.getCardIDEncrypt());
        } else {
            realNameParam = realName;//AES.getInstace().decrypt(realName);
            idCardNoParam = idCardNo;//AES.getInstace().decrypt(idCardNo);
        }

        try {
            //开户权限区分借款人和投资人
            StringBuilder sb = new StringBuilder();
            if (userRole.equals(UserRole.INVESTOR.getCode())) {
                sb.append(AuthList.TENDER).append(",").append(AuthList.CREDIT_ASSIGNMENT);
            } else if (userRole.equals(UserRole.BORROWERS.getCode())) {
                sb.append(AuthList.REPAYMENT);
            }
            PersonalRegisterRequestParams params=new PersonalRegisterRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setRealName(realNameParam);
            params.setUserRole(userRole);
            params.setIdCardNo(idCardNoParam);
            params.setBankcardNo(bankCardNo); //AES.getInstace().decrypt(bankCardNo));
            params.setMobile(phone);//AES.getInstace().decrypt(phone));
            params.setUri(uri);
            params.setAuthList(sb.toString());
            Map<String,Object> sendData= personalRegisterService.getPersonalRegisterRequestData(params);
            response.setData(sendData);
        } catch (APIException ae) {
            logger.error(String.format("开户失败,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("开户失败,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 修改交易密码
     * @param
     * @return
     */
    @RequestMapping(value = "modifyPassword",method= RequestMethod.POST)
    public HttpResponse resetPassword(@ModelAttribute BaseRequestFormExtend formExtend, String uri,  String roleCode) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        try {
            UserRole userRole = StringUtils.isNotEmpty(roleCode) ? UserRole.parse(roleCode) : UserRole.BORROWERS;
            ResetPasswordRequestParams params = new ResetPasswordRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(userRole.getCode());
            params.setUri(uri);
            Map<String, Object> sendData = resetPasswordService.getResetPasswordRequestData(params);
            response.setData(sendData);
        } catch (APIException ae){
            logger.error(String.format("修改密码失败,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        }catch (Exception e){
            logger.error(String.format("修改密码失败,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }

        return response;
    }

    /**
     * 预留手机号更新
     * @param
     * @return
     */
    @RequestMapping(value = "modifyPhone", method = RequestMethod.POST)
    public HttpResponse modifyMobile(@ModelAttribute BaseRequestFormExtend formExtend, String uri, String roleCode) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            UserRole userRole = StringUtils.isEmpty(roleCode)?UserRole.INVESTOR:UserRole.parse(roleCode);
            ModifyMobileRequestParams params = new ModifyMobileRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(userRole.getCode());
            params.setUri(uri);
            Map<String, Object> sendData = modifyMobileService.getModifyMobileRequestData(params);
            response.setData(sendData);
        } catch (APIException ae){
            logger.error(String.format("预留手机号更新,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        }catch (Exception e){
            logger.error(String.format("预留手机号更新,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }

        return response;
    }

    /**
     * 解绑银行卡
     * @param
     * @return
     */
    @RequestMapping(value = "unbindBankCard", method = RequestMethod.POST)
    public HttpResponse unbindBankcard(@ModelAttribute BaseRequestFormExtend formExtend, String uri ,String userRole) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            UnbindBankCardRequestParams params = new UnbindBankCardRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(userRole);
            params.setUri(uri);
            Map<String, Object> sendData = unbindBankCardService.getUnbindBankCardRequestData(params);
            response.setData(sendData);
        } catch (APIException ae){
            logger.error(String.format("解绑银行卡,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        }catch (Exception e){
            logger.error(String.format("解绑银行卡,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 充值接口
     *
     * @param formExtend
     * @param amount
     * @return
     */
    @RequestMapping(value = "recharge", method = RequestMethod.POST)
    HttpResponse doRecharge(@ModelAttribute BaseRequestFormExtend formExtend, String amount, String returnUrl, String roleCode,Integer paymodeIndex,String code) throws Exception {
        HttpResponse response = new HttpResponse();
        int userId = formExtend.getUserId();
        if (!formExtend.validate()
                || StringUtils.isBlank(amount)
                || StringUtils.isBlank(returnUrl)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (!Validator.isAmount(amount)) {
            response.setCodeMessage(ResponseCode.COMMON_PARAM_TYPE_WRONG);
            return response;
        }
        String bankcode = null;
        if (StringUtils.isNotEmpty(code)) {
            bankcode = bankService.getXWBankcode(code);
            if (StringUtils.isEmpty(bankcode)) {
                response.setCodeMessage(XWResponseCode.XW_BANKCODE_NOT_SUPPORT);
                return response;
            }
        }
        UserRole userRole = StringUtils.isEmpty(roleCode)?UserRole.INVESTOR:UserRole.parse(roleCode);
        PaymentMode paymentMode = paymodeIndex == null ? PaymentMode.SWIFT : PaymentMode.parse(paymodeIndex);
        String requestNo = XinWangUtil.createRequestNo();
        int orderId;
        if (paymentMode.equals(PaymentMode.WEB)) {
            orderId = rechargeProcessService.addBackstageOrder(userId, new BigDecimal(amount), requestNo, Source.YH);
        } else {
            orderId = rechargeProcessService.addOrder(userId,userRole, new BigDecimal(amount),requestNo, Source.YH);//添加系统订单
        }
        Map<String, Object> sendData = rechargeService.doRecharge(userId, userRole, returnUrl, orderId, requestNo, paymentMode, bankcode);
        response.setData(sendData);
        return response;
    }

    /**
     * 提现
     * @param formExtend
     * @param amount
     * @param returnURI
     * @return
     */
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    HttpResponse doWithdraw(@ModelAttribute BaseRequestFormExtend formExtend, String amount, String returnURI,String roleCode) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                || StringUtils.isBlank(amount)
                || StringUtils.isBlank(returnURI)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        UserRole userRole = StringUtils.isEmpty(roleCode)?UserRole.INVESTOR:UserRole.parse(roleCode);
        WithdrawParam params = new WithdrawParam();
        params.setPlatformUserNo(roleCode+formExtend.getUserId());
        params.setUserId(formExtend.getUserId());
        params.setUserRole(userRole.getCode());
        params.setRedirectUrl(returnURI);
        params.setAmount(amount);
        DateTime dateTime = new DateTime();
        params.setExpired(dateTime.plusMinutes(10).toString("yyyyMMddHHmmss"));
        params.setWithdrawType(WithdrawType.URGENT.name());
        Map<String, Object> sendData = withdrawService.withdrawApply(params);
        response.setData(sendData);
        return response;
    }

    /**
     * 绑定银行卡
     * @param formExtend
     * @param uri
     * @return
     */
    @RequestMapping(value = "bindBankCard",method= RequestMethod.POST)
    HttpResponse bindBankcard(@ModelAttribute BaseRequestFormExtend formExtend, String uri, String roleCode) {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                ||StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            UserRole userRole = StringUtils.isNotEmpty(roleCode) ? UserRole.parse(roleCode) : UserRole.BORROWERS;
            Map<String, Object> sendData = bindBankcardService.getBindcardParam(formExtend.getUserId(), uri, userRole);
            response.setData(sendData);
        } catch (APIException ae) {
            logger.error(String.format("绑定银行卡失败,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("绑定银行卡失败,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 修改企业信息
     * @param formExtend
     * @param enpId
     * @param uri
     * @return
     */
    @RequestMapping(value = "enterprise/modifyInfo", method = RequestMethod.POST)
    HttpResponse modifyEnpInfo(@ModelAttribute BaseRequestFormExtend formExtend, Integer enpId, String userRole, String uri) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate() || StringUtils.isBlank(uri) || enpId == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        UserRole userRole1 = StringUtils.isNotEmpty(userRole) ? UserRole.parse(userRole) : UserRole.BORROWERS;
        try {
            Map<String, Object> sendData = enterpriseService.getModifyInfo(enpId, userRole1, uri);
            response.setData(sendData);
        } catch (APIException ae) {
            logger.error(String.format("修改企业信息,enterpriseId[%s]", enpId), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("修改企业信息,enterpriseId[%s]", enpId), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 企业注册
     * @param formExtend
     * @return
     */
    @RequestMapping(value = "enterpriseRegister",method= RequestMethod.POST)
    HttpResponse enterpriseRegister(@ModelAttribute BaseRequestFormExtend formExtend,
                                    String enterpriseName, String bankLicense, String legal, String idCardType, String legalIdCardNo, String contact, String contactPhone, String bankcardNo, String bankcode, String unifiedCode, String orgNo, String businessLicense, String taxNo, String creditCode, String uri) throws Throwable {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate() || StringUtils.isBlank(bankLicense) || StringUtils.isBlank(legal) || StringUtils.isBlank(idCardType) || StringUtils.isBlank(legalIdCardNo) || StringUtils.isBlank(contact) || StringUtils.isBlank(contactPhone) || StringUtils.isBlank(bankcardNo) || StringUtils.isBlank(bankcode)
                || StringUtils.isBlank(uri)
                || (StringUtils.isBlank(unifiedCode) && (StringUtils.isBlank(orgNo) || StringUtils.isBlank(businessLicense) || StringUtils.isBlank(taxNo)))
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        try {
            EnterpriseRegisterRequestParams params = new EnterpriseRegisterRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setEnterpriseName(enterpriseName);
            params.setBankLicense(bankLicense);
            params.setLegal(legal);
            params.setIdCardType(idCardType);
            params.setLegalIdCardNo(legalIdCardNo);
            params.setContact(contact);
            params.setContactPhone(contactPhone);
            params.setBankcardNo(bankcardNo);
            params.setBankcode(bankcode);
            params.setUnifiedCode(unifiedCode);
            params.setOrgNo(orgNo);
            params.setBusinessLicense(businessLicense);
            params.setTaxNo(taxNo);
            params.setCreditCode(creditCode);
            params.setUri(uri);
            params.setAuthList(AuthList.REPAYMENT.getCode());
            Map<String,Object> sendData= enterpriseRegisterService.getEnterpriseRegisterRequestData(params);
            response.setData(sendData);
        } catch (APIException ae) {
            logger.error(String.format("企业开户失败,userId[%s]", formExtend.getUserId()), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("企业开户失败,userId[%s]", formExtend.getUserId()), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 企业绑卡
     * @param formExtend
     * @param roleCode 用户角色
     * @param bankCardNo AES加密的银行卡
     * @param bankCode t5020.f04
     * @param uri
     * @return
     * @throws APIException
     */
    @RequestMapping(value = "enterprise/bindCard",method= RequestMethod.POST)
    HttpResponse enterpriseBindCard(@ModelAttribute BaseRequestFormExtend formExtend,
                                    String roleCode, String bankCardNo, String bankCode, String uri) throws APIException {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate() || StringUtils.isBlank(uri) || StringUtils.isBlank(bankCode)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        UserRole userRole = null;
        try {
            userRole = StringUtils.isNotEmpty(roleCode) ? UserRole.parse(roleCode) : UserRole.BORROWERS;
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return response;
        }
        Map<String,Object> sendData = enpBindcardService.getBindcardInfo(formExtend.getUserId(),userRole,bankCode,bankCardNo,uri);
        response.setData(sendData);
        return response;
    }

    /**
     * 迁移用户激活
     * @param formExtend
     * @return
     */
    @RequestMapping(value = "activateStockedUser",method= RequestMethod.POST)
    HttpResponse activateStockedUser(@ModelAttribute BaseRequestFormExtend formExtend,
                                     String uri, String userRole) throws Throwable {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                ||StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        UserRole userRole2 = StringUtils.isNotEmpty(userRole) ? UserRole.parse(userRole) : UserRole.BORROWERS;
        String platformUserNo = userRole2.getCode() + formExtend.getUserId();
        try {
            ActivateStockedUserRequestParams params=new ActivateStockedUserRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUri(uri);
            params.setPlatformUserNo(platformUserNo);
            Map<String,Object> sendData= activateStockedUserService.getActivateStockedUserRequestData(params);
            response.setData(sendData);
        } catch (APIException ae) {
            logger.error(String.format("激活失败,userId[%s],platformUserNo[%s]", formExtend.getUserId()),platformUserNo, ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("激活失败,userId[%s],platformUserNo[%s]", formExtend.getUserId()),platformUserNo, e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }
}
