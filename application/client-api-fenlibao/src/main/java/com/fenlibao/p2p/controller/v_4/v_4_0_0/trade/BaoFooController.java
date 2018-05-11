package com.fenlibao.p2p.controller.v_4.v_4_0_0.trade;

import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooRechargeService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooWithdrawService;
import com.fenlibao.p2p.service.trade.order.RechargeProcessService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.Validator;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.encrypt.AES;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcai on 2017/2/12.
 */
@RestController("v_4_0_0/BaoFooController")
@RequestMapping(value = "payment/baofoo", headers = APIVersion.v_4_0_0)
public class BaoFooController {

	private static final Logger LOG = LogManager.getLogger(BaoFooController.class);
	
    @Resource
    private BaofooBindCardService bindCardService;
    
    @Resource
    private BaofooWithdrawService withdrawService; 
    @Resource
    private RechargeProcessService rechargeProcessService;
    @Resource
    private BaofooRechargeService baofooRechargeService;
    @Resource
    private UserInfoService userInfoService;
    /**
     * 绑卡
     * @param form
     * @param bankCardNum
     * @param phoneNum
     * @param bankCode
     * @return
     */
    @RequestMapping(value = "bind", method = RequestMethod.POST)
    HttpResponse bind(BaseRequestFormExtend form, String bankCardNum, String phoneNum, String bankCode) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!form.validate()||StringHelper.isEmpty(bankCardNum)||StringHelper.isEmpty(phoneNum)||StringHelper.isEmpty(bankCode)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            LOG.error("绑卡参数有误：userId=[{}],bankCardNum=[{}],phoneNum=[{}],bankCode=[{}]",form.getUserId(),bankCardNum,phoneNum,bankCode);
            return response;
        }

        UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(form.getUserId()));
        if (DateUtil.StrToDate("2017-12-19", DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
            LOG.info("goto_CG");
            response.setCodeMessage(ResponseCode.COMMON_GOTO_CG);
            return response;
        }

        bankCardNum =AES.getInstace().decrypt(bankCardNum);
        phoneNum =AES.getInstace().decrypt(phoneNum);
        if (!Validator.isMobile(phoneNum)) {
            // 手机号码格式不正确
            response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
            return response;
        }
        bindCardService.bindCard(form.getUserId(), bankCardNum, phoneNum, bankCode);
        return response;
    }

    /**
     * 预充值
     * @param form
     * @param amount
     * @param serialNum
     * @return
     */
    @RequestMapping(value = "topup/pre", method = RequestMethod.POST)
    HttpResponse preTopup(BaseRequestFormExtend form, String amount, String serialNum, HttpServletRequest request) throws Exception {
        HttpResponse response = new HttpResponse();

        //UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(form.getUserId()));
        //if (DateUtil.StrToDate("2017-12-19", DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
            LOG.info("goto_CG2");
            response.setCodeMessage(ResponseCode.COMMON_GOTO_CG2);
            return response;
        //}

        /*if (!form.validate() || StringUtils.isBlank(amount)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (!Validator.isAmount(amount)) {
            response.setCodeMessage(ResponseCode.COMMON_PARAM_TYPE_WRONG);
            return response;
        }
        boolean isBinding = bindCardService.validateBind(form.getUserId());
        if (!isBinding) {
            throw new TradeException(TradeResponseCode.PAYMENT_UNBOUND_BANK_CARD);
        }
        int orderId = 0;
        if (StringUtils.isBlank(serialNum)) {
            orderId = rechargeProcessService.addOrder(form.getUserId(), new BigDecimal(amount), PaymentInstitution.BF);
        }
        String userIp = CommonTool.getIpAddr(request);
        serialNum = baofooRechargeService.pre(orderId, serialNum, userIp);
        Map<String, Object> data = new HashMap<>(1);
        data.put("serialNum", serialNum);
        response.setData(data);
        return response;*/
    }

    /**
     * 确认充值
     * @param form
     * @param serialNum
     * @param captcha
     * @return
     */
    @RequestMapping(value = "topup/confirm", method = RequestMethod.POST)
    HttpResponse confirmTopup(BaseRequestFormExtend form, String serialNum, String captcha) throws Exception {
        HttpResponse response = new HttpResponse();

        //UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(form.getUserId()));
        //if (DateUtil.StrToDate("2017-12-19", DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
            LOG.info("goto_CG2");
            response.setCodeMessage(ResponseCode.COMMON_GOTO_CG2);
            return response;
        //}

        /*if (!form.validate() || !StringUtils.isNoneBlank(serialNum, captcha)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        BigDecimal amount = baofooRechargeService.confirm(serialNum, captcha);
        Map<String, Object> data = new HashMap<>(2);
        long tradeTime = (new Date()).getTime();//这时间不重要
        data.put("amount", amount.toString());
        data.put("tradeTime", tradeTime);
        response.setData(data);
        return response;*/
    }

    /**
     * 提现
     * @param form
     * @param amount
     * @param tradePassword
     * @param provinceName
     * @param cityName
     * @param branchName
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    HttpResponse withdraw(BaseRequestFormExtend form, String amount, String tradePassword,
                          String provinceName, String cityName, String branchName) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!form.validate() || !StringUtils.isNoneBlank(amount, tradePassword)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        tradePassword = com.fenlibao.p2p.common.util.encrypt.AES.getInstace().decrypt(tradePassword);
        withdrawService.withdrawApply(form.getUserId(), new BigDecimal(amount), tradePassword);
        return response;
    }
}
