package com.fenlibao.pms.controller.cs.guarantee;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.GuaranteeInfo;
import com.fenlibao.model.pms.da.cs.RechargePreInfo;
import com.fenlibao.model.pms.da.cs.form.GuaranteeForm;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.cs.form.XWWithdrawForm;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.service.pms.da.cs.GuaranteeService;
import com.fenlibao.service.pms.da.cs.guarantor.GuarantorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zeronx on 2017/6/26.
 */
@RestController
@RequestMapping("cs/guarantee")
public class GuaranteeController {

    private static final Logger LOGGER = LogManager.getLogger(GuaranteeController.class);

    @Autowired
    private GuaranteeService guaranteeService;
    @Autowired
    private GuarantorService guarantorService;

    private static final String GUARANTEE_XW_REDIRECT_URL = Config.get("cookie.valid.path") + "/cs/guarantee";
    private static final String GUARANTEE_XW_RECHARGE_SUC_REDIRECT_URL = Config.get("cookie.valid.path") + "/cs/guarantee/rechargeSuccess";
    private static final String GUARANTEE_XW_WITHDRAW_SUC_REDIRECT_URL = Config.get("cookie.valid.path") + "/cs/guarantee/withdrawSuccess";

    // 担保用户管理列表
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                             GuaranteeForm guaranteeForm) {
        ModelAndView view = new ModelAndView("cs/guarantee/index");
        RowBounds bounds = new RowBounds(page, limit);
        List<GuaranteeInfo> borrowerAccountInfoList = guaranteeService.getGuaranteeInfoList(guaranteeForm, bounds);
        PageInfo<GuaranteeInfo> paginator = new PageInfo<>(borrowerAccountInfoList);
        view.addObject("list", borrowerAccountInfoList);
        view.addObject("guaranteeForm", guaranteeForm);
        view.addObject("paginator", paginator);
        return view;
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ModelAndView viewAccount(String userId) {
        BussinessInfo bussinessInfo = guarantorService.getBussinessInfoByUserId(Integer.valueOf(userId), "GUARANTEECORP", null);
        List<BankCodeInfo> bankCodes = guarantorService.getBankCodes();
        return new ModelAndView("cs/guarantee/view").addObject("bussinessInfo", bussinessInfo)
                .addObject("bankCodes",bankCodes)
                .addObject("userId", userId)
                .addObject("auditStatus", bussinessInfo != null ? bussinessInfo.getAuditStatus() : "")
                .addObject("title", "企业信息查看");
    }

    @RequestMapping(value = "bankcard", method = RequestMethod.GET)
    public ModelAndView bankcard(String userId) {
        RechargeForm bindInfo = new RechargeForm();
        List<BankCodeInfo> bankCodes = guarantorService.getBankCodes();
        return new ModelAndView("cs/guarantee/bankcard")
                .addObject("bindInfo", bindInfo)
                .addObject("bankCodes", bankCodes)
                .addObject("userId", userId);
    }

    @RequestMapping(value = "rechargePre", method = RequestMethod.GET)
    public ModelAndView rechargePre(String userId) {
        RechargeForm rechargeForm = new RechargeForm();
        RechargePreInfo rechargePreInfo = guaranteeService.getRechargePreInfoByUserId(userId);
        List<BankCodeInfo> bankCodes = guarantorService.getBankCodes();
        ModelAndView modelAndView = new ModelAndView("cs/guarantee/recharge");
        modelAndView.addObject("hasUser", false);
        NumberFormat nf = new DecimalFormat(",###.##");
        String balance = null;
        if (rechargePreInfo != null) {
            rechargeForm.setUserId(rechargePreInfo.getUserId());
            rechargeForm.setAccount(rechargePreInfo.getAccount());
            rechargeForm.setName(rechargePreInfo.getName());
            rechargeForm.setBankCode(rechargePreInfo.getBankCode());
            BigDecimal bigDecimal = new BigDecimal(StringUtils.isEmpty(rechargePreInfo.getBalance()) ? "0" : rechargePreInfo.getBalance());
            balance = nf.format(bigDecimal);
            modelAndView.addObject("userType", rechargePreInfo.getUserType());
            modelAndView.addObject("hasUser", true);
        }
        modelAndView.addObject("rechargeForm", rechargeForm).addObject("balance", balance).addObject("bankCodes", bankCodes);
        return modelAndView;
    }

    @RequestMapping(value = "doRecharge", method = RequestMethod.POST)
    public HttpResponse doRecharge(RechargeForm rechargeForm) {
        HttpResponse result = new HttpResponse();
        Map<String, Object> data = null;
        try {
            validateRechargeParam(rechargeForm);
            data = guaranteeService.getRechargeRequestParam(rechargeForm, GUARANTEE_XW_RECHARGE_SUC_REDIRECT_URL);
        } catch (APIException e) {
            LOGGER.error("构造充值请求参数异常：", e.getMessage());
            result.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("构造充值请求参数异: ", e.getMessage());
            result.setCodeMessage("500", "构造参数出错");
        }
        result.setData(data);
        return result;
    }

    @RequestMapping(value = "rechargeSuccess")
    public ModelAndView rechargeSuccess() {
        ModelAndView modelAndView = new ModelAndView("cs/guarantee/rechargeSuccess");
        return modelAndView;
    }

    private void validateRechargeParam(RechargeForm rechargeForm) throws APIException{
        if (rechargeForm == null) {
            throw new APIException(XWResponseCode.EMPTY_PARAM);
        }
        if (rechargeForm.getUserId() == null || StringUtils.isEmpty(rechargeForm.getAmount())
                || StringUtils.isEmpty(rechargeForm.getRechargeWay())) {
            throw new APIException(XWResponseCode.EMPTY_PARAM);
        }
        // 校验金额
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match=pattern.matcher(rechargeForm.getAmount());
        if (!match.matches()) {
            throw new APIException(XWResponseCode.COMMON_PARAM_WRONG.getCode(), "输入的金额有误");
        }

    }

    @RequestMapping(value = "withdrawPre", method = RequestMethod.GET)
    public ModelAndView withdrawPre(String userId) {
        XWWithdrawForm withdrawForm = new XWWithdrawForm();
        ModelAndView modelAndView = new ModelAndView("cs/guarantee/withdraw");
        RechargePreInfo rechargePreInfo = guaranteeService.getRechargePreInfoByUserId(userId);
        modelAndView.addObject("hasUser", false);
        NumberFormat nf = new DecimalFormat(",###.##");
        String balance = null;
        if (rechargePreInfo != null) {
            withdrawForm.setUserId(rechargePreInfo.getUserId());
            withdrawForm.setAccount(rechargePreInfo.getAccount());
            withdrawForm.setName(rechargePreInfo.getName());
            withdrawForm.setBankCode(rechargePreInfo.getBankCode());
            BigDecimal bigDecimal = new BigDecimal(StringUtils.isEmpty(rechargePreInfo.getBalance()) ? "0" : rechargePreInfo.getBalance());
            balance = nf.format(bigDecimal);
            modelAndView.addObject("hasUser", true);
        }
        return modelAndView.addObject("withdrawForm", withdrawForm).addObject("balance", balance);
    }

    @RequestMapping(value = "doWithdraw", method = RequestMethod.POST)
    public HttpResponse doWithdraw(XWWithdrawForm withdrawForm) {
        HttpResponse result = new HttpResponse();
        Map<String, Object> data = null;
        try {
            validateWithdrawParam(withdrawForm);
            data = guaranteeService.getWithdrawRequestParam(withdrawForm, GUARANTEE_XW_WITHDRAW_SUC_REDIRECT_URL);
        } catch (APIException e) {
            LOGGER.error("构造提现请求参数异常：", e.getMessage());
            result.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("构造提现请求参数异常: ", e.getMessage());
            result.setCodeMessage("500", "构造参数出错");
        }
        result.setData(data);
        return result;
    }

    @RequestMapping(value = "withdrawSuccess")
    public ModelAndView withdrawSuccess() {
        ModelAndView modelAndView = new ModelAndView("cs/guarantee/withdrawSuccess");
        return modelAndView;
    }

    private void validateWithdrawParam(XWWithdrawForm withdrawForm) throws APIException{
        if (withdrawForm == null) {
            throw new APIException(XWResponseCode.EMPTY_PARAM);
        }
        if (withdrawForm.getUserId() == null || StringUtils.isEmpty(withdrawForm.getAmount())
                || StringUtils.isEmpty(withdrawForm.getWithdrawType())) {
            throw new APIException(XWResponseCode.EMPTY_PARAM);
        }
        // 校验金额
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match=pattern.matcher(withdrawForm.getAmount());
        if (!match.matches()) {
            throw new APIException(XWResponseCode.COMMON_PARAM_WRONG);
        }
    }

    @RequestMapping(value = "bindBank", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public HttpResponse bindBank(RechargeForm bindInfo) {
        HttpResponse httpResponse = new HttpResponse();
        Map<String,Object> data = null;
        try {
            validateParam(bindInfo);
            data = guaranteeService.bindBank(bindInfo, GUARANTEE_XW_REDIRECT_URL);
        } catch (APIException e) {
            LOGGER.error("构造绑定银行卡请求参数异常：", e.getMessage());
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("构造绑定银行卡请求参数异常: ", e.getMessage());
            httpResponse.setCodeMessage("500", "构造参数出错");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

    private void validateParam(RechargeForm bindInfo) throws Exception {
        if (bindInfo == null) {
            throw new Exception("参数有误");
        }
        if (bindInfo.getUserId() == null || bindInfo.getUserType() == null) {
            throw new Exception("参数有误");
        }
        if ("ORGANIZATION".equals(bindInfo.getUserType())) {
            if (bindInfo.getBankCode() == null || bindInfo.getBankcardNo() == null) {
                throw new Exception("参数有误");
            }
        }
    }

    @RequestMapping(value = "unbindBank", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse unbindBank(String userId) {
        HttpResponse httpResponse = new HttpResponse();
        Map<String,Object> data = null;
        try {
            data = guaranteeService.unbindBank(userId, GUARANTEE_XW_REDIRECT_URL);
        } catch (APIException e) {
            LOGGER.error("构造解绑银行卡请求参数异常：", e.getMessage());
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("构造解绑银行卡请求参数异常: ", e.getMessage());
            httpResponse.setCodeMessage("500", "构造参数出错");
        }
        httpResponse.setData(data);
        return httpResponse;
    }
}
