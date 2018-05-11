package com.fenlibao.pms.controller.finance.accountManagetment;

import com.fenlibao.common.pms.util.Validator;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.model.pms.da.finance.T6101Extend;
import com.fenlibao.model.pms.da.finance.enums.PlatformRole;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.WithdrawType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.account.UnbindBankCardRequestParams;
import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.xinwang.account.XWUnbindBankCardService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.pay.XWRechargeProcessService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.fenlibao.service.pms.da.cs.guarantor.GuarantorService;
import com.fenlibao.service.pms.da.finance.accountManagement.AccountManagementFinanceService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务管理——平台账户管理
 */
@RestController
@RequestMapping("finance/accountmanagement")
public class AccountManagementFinanceController {

    private final static Logger logger = LoggerFactory
            .getLogger(AccountManagementFinanceController.class);

    private static final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 网关接口后缀
     */
    private final static String GATEWAY = "/gateway";

    @Autowired
    XWRechargeProcessService rechargeProcessService;

    @Autowired
    XWRechargeService rechargeService;

    @Autowired
    XWWithdrawService withdrawService;

    @Autowired
    private GuarantorService guarantorService;

    @Autowired
    private AccountManagementFinanceService accountManagementService;

    @Autowired
    private XWUnbindBankCardService unbindBankCardService;

    @Autowired
    private XWUserInfoService userInfoService;
    @RequiresPermissions("accountmanagement:view")
    @RequestMapping
    public ModelAndView index() {
        List<T6101Extend> list = accountManagementService.findList();
        Map<String,BigDecimal> balances = new HashMap<String,BigDecimal>();
        Map<String,Integer> isBindBanks = new HashMap<String,Integer>();
        //账户类型:余额
        for (int i = 0; i < list.size(); i++) {
            balances.put(list.get(i).getF03(),list.get(i).getF06());
            isBindBanks.put(list.get(i).getF03(),list.get(i).getIsBindBank());
        }
        return new ModelAndView("finance/accountManagerment/account-list")
                .addObject("balances", balances)
                .addObject("isBindBanks", isBindBanks);
    }
    /**
     * 充值 页面
     * @return
     */
    @RequestMapping(value = "rechargePage",method = RequestMethod.GET)
    public ModelAndView rechargePage(@RequestParam(value="accountType", required=false) String accountType,
                                     @RequestParam(value="userId", required=false) String userId,
                                     @RequestParam(value="balance", required=false) String balance){//余额
        return new ModelAndView("finance/accountManagerment/account-recharge")
                .addObject("accountType", accountType)
                .addObject("balance", balance);
    }
    /**
     * 充值 成功
     * @return
     */
    @RequestMapping(value = "rechargeSuccess")
    public ModelAndView rechargeSuccess(){
        return new ModelAndView("finance/accountManagerment/rechargeSuccess");
    }
    /**
     * 提现 页面
     * @return
     */
    @RequestMapping(value = "withdrawPage",method = RequestMethod.GET)
    public ModelAndView withdrawPage(@RequestParam(value="accountType", required=false) String accountType,
                                     @RequestParam(value="balance", required=false) String balance,Integer isBindBank){//余额
        return new ModelAndView("finance/accountManagerment/account-withdraw")
                .addObject("accountType", accountType)
                .addObject("balance", balance)
                .addObject("isBindBank", isBindBank);
    }
    /**
     * 提现 成功
     * @return
     */
    @RequestMapping(value = "withdrawSuccess")
    public ModelAndView withdrawSuccess(){
        return new ModelAndView("finance/accountManagerment/withdrawSuccess");
    }
    /**
     * 划拨 页面
     */
    @RequestMapping(value = "transferPage",method = RequestMethod.GET)
    public ModelAndView transferPage(@RequestParam(value="accountType", required=false) String accountType,
                                     @RequestParam(value="userId", required=false) String userId,
                                     @RequestParam(value="balance", required=false) String balance){//余额

        return new ModelAndView("finance/accountManagerment/account-transfer")
                .addObject("accountType", accountType)
                .addObject("balance", balance);
    }

    /**
     * 绑卡 页面
     * @return
     */
    @RequestMapping(value = "bindcardPage", method = RequestMethod.GET)
    public ModelAndView bankcard(@RequestParam(value="accountType", required=false) String accountType) {
        RechargeForm bindInfo = new RechargeForm();
        List<BankCodeInfo> bankCodes = guarantorService.getBankCodes();
        return new ModelAndView("finance/accountManagerment/account-bindcard")
                .addObject("bindInfo", bindInfo)
                .addObject("bankCodes", bankCodes)
                .addObject("accountType", accountType);
    }
    /**
     * 查询 交易记录
     * @return
     */
    @RequestMapping(value = "tradeHistory")
    public ModelAndView tradeHistoryByParam(@RequestParam(value="accountType", required=false) String accountType,
                                            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) Integer page,
                                            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) Integer limit,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        //用户ID
        int userId= 1;
        RowBounds bounds = new RowBounds(page, limit);

        List<Transaction> historys = accountManagementService.findTradeHistory(userId,accountType,startDate,endDate,bounds);
        PageInfo<Transaction> paginator = new PageInfo<>(historys);
        return new ModelAndView("finance/accountManagerment/account-history")
                .addObject("historys", historys)
                .addObject("startDate", startDate)
                .addObject("accountType", accountType)
                .addObject("endDate", endDate)
                .addObject("paginator", paginator);
    }

    /**
     * 交易记录 导出
     * @param response
     * @param accountType
     * @param startDate
     * @param endDate
     */
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void returncashListExport(HttpServletResponse response, @RequestParam(value="accountType", required=false) String accountType,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        //用户ID
        int userId= 1;
        RowBounds bounds = new RowBounds();

        List<Transaction> historys = accountManagementService.findTradeHistory(userId,accountType,startDate,endDate,bounds);
        PageInfo<Transaction> paginator = new PageInfo<>(historys);

        String headers[] = { "时间", "名称", "交易金额（元）" };
        String fieldNames[] = { "tradeTime", "tradeTypeName", "tradeAmount"};

        POIUtil.export(response, headers, fieldNames, historys);
    }


    /**
     * 充值接口
     *
     * @param amount
     * @param uri
     * @return
     */
    @RequestMapping(value = "recharge", method = RequestMethod.POST)
    HttpResponse doRecharge(String amount, String uri, Integer payMode, String bankcode,String F03) {
        HttpResponse response = new HttpResponse();
        int userId = 1;
        if (StringUtils.isBlank(amount)
                || StringUtils.isBlank(uri)
                ) {
            throw new XWTradeException(XWResponseCode.EMPTY_PARAM);
        }
        if (!Validator.isAmount(amount)) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_TYPE_WRONG);
        }
        PaymentMode paymentMode = PaymentMode.parse(payMode);
        String requestNo = null;
        String userRole = F03.substring(3).substring(0,F03.substring(3).indexOf("_WLZH"));

        try {
            PlatformRole platformRole = PlatformRole.parse(userRole);
            requestNo = XinWangUtil.createRequestNo();

            int orderId = rechargeProcessService.addBackstageOrder(userId, new BigDecimal(amount),requestNo, Source.HT);//添加系统订单
            Map<String, Object> sendData = accountManagementService.doRecharge(userId, platformRole, uri, orderId, requestNo, paymentMode,bankcode);
            response.setData(sendData);
        } catch (APIException e) {
            logger.error(String.format("充值失败,requestNo:[%s]", requestNo), e);
            response.setCodeMessage(e.getCode(), e.getMessage());
        }catch(Exception e) {
            logger.error("未知充值错误：", e);
            response.setCodeMessage("500", "请求失败");
        }
        return response;
    }

    /**
     * 提现
     * @param amount
     * @param uri
     * @return
     */
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    HttpResponse doWithdraw( String amount, String uri,String accountType) {
        HttpResponse response = new HttpResponse();
        if ( StringUtils.isBlank(amount)
                || StringUtils.isBlank(uri)
                ) {
            throw new XWTradeException(XWResponseCode.EMPTY_PARAM);
        }
        if (!Validator.isAmount(amount)) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
        }
        String requestNo = null;
        try {
            WithdrawParam params = new WithdrawParam();
            String platformUserNo = PlatformRole.parse(accountType.substring(3).substring(0,accountType.substring(3).indexOf("_WLZH"))).getNo();
            XinwangUserInfo userInfo = userInfoService.queryUserInfo(platformUserNo);

            BigDecimal availableAmount = userInfo.getAvailableAmount();

            if (availableAmount.compareTo(BigDecimal.ZERO)<=0 && availableAmount.compareTo(new BigDecimal(amount))<0){
                throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
            }
            params.setUserId(1);
            //params.setPlatformUserNo(UserRole.INVESTOR.getCode() + formExtend.getUserId());
            params.setPlatformUserNo(platformUserNo);

            params.setUserRole(UserRole.INVESTOR.getCode());
            params.setRedirectUrl(uri);
            //免手续费
            BigDecimal poundage = BigDecimal.ZERO;
            params.setAmount(new BigDecimal(amount).add(poundage).toString());
            DateTime dateTime = new DateTime();
            params.setExpired(dateTime.plusMinutes(30).toString("yyyyMMddHHmmss"));
            params.setWithdrawType(WithdrawType.URGENT.name());
            Map<String, Object> sendData = withdrawService.withdrawApply(params);
            response.setData(sendData);
        }catch (APIException ae) {
            logger.error(String.format("提现失败,requestNo:[%s]", requestNo), ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        } catch (Exception e) {
            logger.error("未知提现错误", e);
            response.setCodeMessage("500", "请求失败");
        }
        return response;
    }

    /**
     * 平台划拨 transfer
     */
    @RequestMapping(value = "transfer",method = RequestMethod.POST)
    HttpResponse doFundsTransfer(String sourceAccountType, String targetAccountType, String amount) throws APIException {
        HttpResponse response = new HttpResponse();
        if (org.apache.commons.lang3.StringUtils.isBlank(sourceAccountType)|| org.apache.commons.lang3.StringUtils.isBlank(targetAccountType) || org.apache.commons.lang3.StringUtils.isBlank(amount)) {
            throw new XWTradeException(XWResponseCode.EMPTY_PARAM);
        }
        if (!Validator.isAmount(amount)) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
        }
        try{
            String sourceUserNo = PlatformRole.parse(sourceAccountType.substring(3).substring(0,sourceAccountType.substring(3).indexOf("_WLZH"))).getNo();
            String targetUserNo = PlatformRole.parse(targetAccountType.substring(3).substring(0,targetAccountType.substring(3).indexOf("_WLZH"))).getNo();
            //查询划出帐号余额
            XinwangUserInfo userInfo = userInfoService.queryUserInfo(sourceUserNo);
            BigDecimal availableAmount = userInfo.getAvailableAmount();

            if (availableAmount.compareTo(BigDecimal.ZERO)<=0 && availableAmount.compareTo(new BigDecimal(amount))<0){
                throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
            }
            BusinessType businessType = new BusinessType();
            businessType.setCode(SysTradeFeeCode.PTHB);
            businessType.setName("平台划拨");
            int requestId = rechargeService.doFundsTransfer(sourceUserNo, targetUserNo, new BigDecimal(amount), businessType);
        }catch (APIException e) {
            logger.error("划拨失败：", e);
            response.setCodeMessage(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.error("未知划拨错误：", e);
            response.setCodeMessage("500", "请求失败");
        }

        return response;
    }

    /**
     * 绑定银行卡
     * @param bindInfo
     * @param accountType
     * @return
     */
    @RequestMapping(value = "bindBank", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse bindBank(RechargeForm bindInfo,String accountType,String redirectUrl) {
        HttpResponse httpResponse = new HttpResponse();
        Map<String,Object> data = null;
        int userId = 1 ;//平台账户的userId都为1
        bindInfo.setUserId(userId);
        try {
            String platformRole = PlatformRole.parse(accountType.substring(3).substring(0,accountType.substring(3).indexOf("_WLZH"))).getNole();
            if (bindInfo == null) {
                throw new XWTradeException(XWResponseCode.EMPTY_PARAM);
            }
            String platformUserNo = PlatformRole.parse(accountType.substring(3).substring(0,accountType.substring(3).indexOf("_WLZH"))).getNo();
            XinwangUserInfo userInfo = userInfoService.queryUserInfo(platformUserNo);

            if (userInfo.getCode().equals("1")){
                throw new XWTradeException(userInfo.getErrorCode(),userInfo.getErrorMessage());
            }

            if (StringUtils.isNotBlank(userInfo.getBankcardNo()) || StringUtils.isNotBlank(userInfo.getBankcode())){
                throw new XWTradeException(XWResponseCode.PAYMENT_IS_BINDING_BAOFOO);
            }
            data = accountManagementService.bindBank(bindInfo,platformRole, redirectUrl);
        } catch (APIException e) {
            e.printStackTrace();
            logger.error(e.getCode(), e.getMessage());
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("绑定银行卡未知错误：", e);
            httpResponse.setCodeMessage("500", "绑定银行卡失败");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

    /**
     * 解绑银行卡
     * @param
     * @return
     */
    @RequestMapping(value = "unbindBankcard", method = RequestMethod.POST)
    public HttpResponse unbindBankcard(String uri, String accountType) {
        HttpResponse response = new HttpResponse();
        try {
            String platformRole = PlatformRole.parse(accountType.substring(3).substring(0,accountType.substring(3).indexOf("_WLZH"))).getNole();
            String platformRoleNo = PlatformRole.parse(accountType.substring(3).substring(0,accountType.substring(3).indexOf("_WLZH"))).getNo();
            XinwangUserInfo userInfo = userInfoService.queryUserInfo(platformRoleNo);

            if (userInfo.getCode().equals("1")){
                throw new XWTradeException(userInfo.getErrorCode(),userInfo.getErrorMessage());
            }

            if (StringUtils.isBlank(userInfo.getBankcardNo()) || StringUtils.isBlank(userInfo.getBankcode())){
                throw new XWTradeException(XWResponseCode.PAYMENT_UNBIND_NO_CARD);
            }
            UnbindBankCardRequestParams params = new UnbindBankCardRequestParams();
            params.setUserId(1);
            params.setUserRole(platformRole);
            params.setUri(uri);
            Map<String, Object> sendData = unbindBankCardService.getUnbindBankCardRequestData(params);
            response.setData(sendData);
        } catch (APIException ae){
            logger.error("解绑银行卡："+ae);
            response.setCodeMessage(ae.getCode(), ae.getMessage());
        }catch (Exception e){
            logger.error("解绑银行卡未知错误:"+ e);
            response.setCodeMessage("500", "解绑银行卡失败");
        }

        return response;
    }
}
