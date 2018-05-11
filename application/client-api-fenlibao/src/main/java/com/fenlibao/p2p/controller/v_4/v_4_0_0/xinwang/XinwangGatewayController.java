package com.fenlibao.p2p.controller.v_4.v_4_0_0.xinwang;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.EnterpriseRegisterForm;
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
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.param.account.*;
import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.service.xinwang.account.*;
import com.fenlibao.p2p.service.xinwang.entrust.XWEntrustImportUserService;
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
@RestController("v_4_0_0/XinwangGatewayController")
@RequestMapping(value = "xinwang/gateway", headers = APIVersion.v_4_0_0)
public class XinwangGatewayController {

    private static final Logger logger= LogManager.getLogger(XinwangGatewayController.class);

    @Resource
    XWPersonalRegisterService personalRegisterService;
    @Resource
    XWRechargeService rechargeService;
    @Resource
    XWWithdrawService withdrawService;
    @Resource
    IWithdrawService sysWithdrawService;
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
    XWActivateStockedUserService activateStockedUserService;


    @Resource
    XWEntrustImportUserService xwEntrustImportUserService;
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
                                  String uri) throws Throwable {
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
            realNameParam = AES.getInstace().decrypt(realName);
            idCardNoParam = AES.getInstace().decrypt(idCardNo);
        }

        try {
            StringBuilder sb = new StringBuilder().append(AuthList.TENDER).append(",").append(AuthList.CREDIT_ASSIGNMENT);
            PersonalRegisterRequestParams params=new PersonalRegisterRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setRealName(realNameParam);
            params.setUserRole(UserRole.INVESTOR.getCode());
            params.setIdCardNo(idCardNoParam);
            params.setBankcardNo(AES.getInstace().decrypt(bankCardNo));
            params.setMobile(AES.getInstace().decrypt(phone));
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
    public HttpResponse resetPassword(@ModelAttribute BaseRequestFormExtend formExtend, String uri) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        try {
            ResetPasswordRequestParams params = new ResetPasswordRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(UserRole.INVESTOR.getCode());
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
    public HttpResponse modifyMobile(@ModelAttribute BaseRequestFormExtend formExtend, String uri) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            ModifyMobileRequestParams params = new ModifyMobileRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(UserRole.INVESTOR.getCode());
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
    public HttpResponse unbindBankcard(@ModelAttribute BaseRequestFormExtend formExtend, String uri) {
        HttpResponse response = new HttpResponse();
        if(!formExtend.validate() ||  StringUtils.isBlank(uri)){
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            UnbindBankCardRequestParams params = new UnbindBankCardRequestParams();
            params.setUserId(formExtend.getUserId());
            params.setUserRole(UserRole.INVESTOR.getCode());
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
     * @param uri
     * @return
     */
    @RequestMapping(value = "recharge", method = RequestMethod.POST)
    HttpResponse doRecharge(@ModelAttribute BaseRequestFormExtend formExtend, String amount, String uri) {
        HttpResponse response = new HttpResponse();
        int userId = formExtend.getUserId();
        if (!formExtend.validate()
                || StringUtils.isBlank(amount)
                || StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (!Validator.isAmount(amount)) {
            response.setCodeMessage(ResponseCode.COMMON_PARAM_TYPE_WRONG);
            return response;
        }
        String requestNo = null;
        try {
        requestNo = XinWangUtil.createRequestNo();
        int orderId = rechargeProcessService.addOrder(userId,UserRole.INVESTOR, new BigDecimal(amount),requestNo, Source.YH);//添加系统订单
        Map<String, Object> sendData = rechargeService.doRecharge(userId, UserRole.INVESTOR, uri, orderId, requestNo, PaymentMode.SWIFT, null);
        response.setData(sendData);
        }catch (APIException ae) {
            logger.error(String.format("充值失败,requestNo:[%s]", requestNo), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("充值失败,requestNo:[%s]", requestNo), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 提现
     * @param formExtend
     * @param amount
     * @param uri
     * @return
     */
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    HttpResponse doWithdraw(@ModelAttribute BaseRequestFormExtend formExtend, String amount, String uri) {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                || StringUtils.isBlank(amount)
                || StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (!Validator.isAmount(amount)) {
            response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return response;
        }
        String requestNo = null;
        try {
            WithdrawParam params = new WithdrawParam();
            params.setUserId(formExtend.getUserId());
            params.setPlatformUserNo(UserRole.INVESTOR.getCode() + formExtend.getUserId());
            params.setUserRole(UserRole.INVESTOR.getCode());
            params.setRedirectUrl(uri);
            params.setAmount(amount);
            DateTime dateTime = new DateTime();
            params.setExpired(dateTime.plusMinutes(10).toString("yyyyMMddHHmmss"));
            params.setWithdrawType(WithdrawType.NORMAL_URGENT.name());
            Map<String, Object> sendData = withdrawService.withdrawApply(params);
            response.setData(sendData);
        }catch (APIException ae) {
            logger.error(String.format("提现失败,requestNo:[%s]", requestNo), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error(String.format("提现失败,requestNo:[%s]", requestNo), e);
            response.setCodeMessage(ResponseCode.FAILURE);
        }
        return response;
    }

    /**
     * 绑定银行卡
     * @param formExtend
     * @param uri
     * @return
     */
    @RequestMapping(value = "bindBankCard",method= RequestMethod.POST)
    HttpResponse bindBankcard(@ModelAttribute BaseRequestFormExtend formExtend,String uri) {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                ||StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            Map<String, Object> sendData = bindBankcardService.getBindcardParam(formExtend.getUserId(), uri,UserRole.INVESTOR);
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
     * 迁移用户激活
     * @param formExtend
     * @return
     */
    @RequestMapping(value = "activateStockedUser",method= RequestMethod.POST)
    HttpResponse activateStockedUser(@ModelAttribute BaseRequestFormExtend formExtend,
                                  String uri) throws Throwable {
        HttpResponse response = new HttpResponse();
        if (!formExtend.validate()
                ||StringUtils.isBlank(uri)
                ) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        String platformUserNo = UserRole.INVESTOR.getCode() + formExtend.getUserId();
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


    @RequestMapping(value = "enterpriseRegister",method= RequestMethod.POST)
    HttpResponse enterpriseRegister(@ModelAttribute EnterpriseRegisterForm params) {
        HttpResponse response = new HttpResponse();
        try {
            xwEntrustImportUserService.entrustImportUser(params.getUserId(),params.getRealName(),params.getIdCardType(),params.getIdCardNo(), params.getMobile(), params.getBankcardNo(), params.getUserRole());
        } catch (APIException ae) {
            response.setCode(ae.getCode());
            logger.error("委托开户失败",ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("委托开户失败",e);
            /*response.setCodeMessage(e, e.getMessage());*/
        }
        return response;
    }
}
