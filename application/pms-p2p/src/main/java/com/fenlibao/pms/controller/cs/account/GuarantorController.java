package com.fenlibao.pms.controller.cs.account;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.form.GuaranteeForm;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.model.pms.da.cs.guarantor.Guarantor;
import com.fenlibao.model.pms.da.cs.guarantor.form.GuarantorForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.util.xinwang.SignatureAlgorithm;
import com.fenlibao.p2p.util.xinwang.SignatureUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.fenlibao.service.pms.da.cs.guarantor.GuarantorService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.terracotta.modules.ehcache.ToolkitInstanceFactoryImpl.LOGGER;

@RestController
@RequestMapping("cs/guarantor")
public class GuarantorController {

    private static final Logger LOG = LoggerFactory.getLogger(GuarantorController.class);

    @Autowired
    private GuarantorService guarantorService;

    private static final String XW_REDIRECT_URL = Config.get("cookie.valid.path") + "/cs/guarantor";
    private static final String XW_MODIFY_ENP_REDIRECT_URL = Config.get("cookie.valid.path") + "/cs/guarantor/callBackModifyEnp";

    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                             GuarantorForm guarantorForm) {
        ModelAndView view = new ModelAndView("cs/guarantor/index");
        RowBounds bounds = new RowBounds(page, limit);
        List<Guarantor> list = guarantorService.getGuarantorList(guarantorForm, bounds);
        PageInfo<Guarantor> paginator = new PageInfo<>(list);
        view.addObject("list", list);
        view.addObject("guarantorForm", guarantorForm);
        view.addObject("paginator", paginator);
        return view;
    }

    @RequestMapping(value = "callBackModifyEnp")
    public ModelAndView callBackModifyEnp(HttpServletRequest request) {
        LOG.info("修改企业信息浏览器携带报文回调url成功，开始处理报文.....");
        final String respData = request.getParameter("respData");
        try {
            // 验签
            PublicKey publicKey = SignatureUtil.getRsaX509PublicKey(Base64
                    .decodeBase64(XinWangUtil.CONFIG.lmPublicKey()));
            boolean verify = SignatureUtil.verify(
                    SignatureAlgorithm.SHA1WithRSA, publicKey, respData,
                    Base64.decodeBase64(request.getParameter("sign")));
            if (verify) {
                LOG.error("处理修改企业信息浏览器携带报文 验签成功...");
                final Map<String, Object> respMap = JSON.parseObject(respData);
                String code = (String) respMap.get("code");
                if ("0".equals(code) && "SUCCESS".equals((String)respMap.get("status"))) {
                    guarantorService.updateAuditStatusToAudit((String)respMap.get("platformUserNo"));
                }
            } else {
                LOG.error("处理修改企业信息浏览器携带报文验签失败");
            }
        } catch (Exception e) {
            LOG.error("处理修改企业信息浏览器携带报文异常", e);
        }
        return list(1, 15, new GuarantorForm());
    }

    /**
     * 企业信息页面
     * @param userId
     * @return
     */
    @RequestMapping(value = "view")
    public ModelAndView viewAccount(String userId, String accountType, String auditStatus) {
        BussinessInfo bussinessInfo = guarantorService.getBussinessInfoByUserId(Integer.valueOf(userId), accountType, auditStatus);
        List<BankCodeInfo> bankCodes = guarantorService.getBankCodes();
        return new ModelAndView("cs/guarantor/add_enterprise").addObject("bussinessInfo", bussinessInfo == null ? new BussinessInfo() : bussinessInfo)
                .addObject("bankCodes",bankCodes)
                .addObject("userId", userId)
                .addObject("auditStatus", bussinessInfo != null ? bussinessInfo.getAuditStatus() : null);
    }

    /**
     * 创建担保人
     * @return
     */
    @RequestMapping(value = "new", method = RequestMethod.GET)
    public ModelAndView newPage() {
        GuaranteeForm userLessInfo = new GuaranteeForm();
        return new ModelAndView("cs/guarantor/create").addObject("userLessInfo", userLessInfo);
    }

    /**
     * 校验用户账号是否已注册分利宝
     * @param accountType
     * @param account
     * @return
     */
    @RequestMapping(value = "validAccount", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> validAccount(String accountType, String account) {
        // 校验是否已注册分利宝
        UserDetailInfo userDetailInfo = guarantorService.validAccount(accountType, account);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isRegister", false);
        resultMap.put("isRegXw", false);
        if (userDetailInfo == null) {
            return resultMap;
        }
        // 校验账号开通新网存管账户的状态（null 即为未开通）(担保账户)
        String status = guarantorService.validAccountIsRegXw(accountType, userDetailInfo.getUserId());
        if (!StringUtils.isEmpty(status)) {
            resultMap.put("isRegXw", true);
            String msg = "AUDIT".equals(status) ? "该账号申请开通担保户正在审核中" :
                         "PASSED".equals(status) ? "该账号已开通担保户" :
                         "BACK".equals(status) ? "该账号申请开通担保户已被回退" :
                         "REFUSED".equals(status) ? "该账号申请开通担保户已被拒绝" : "未知状态";
            resultMap.put("xwStatusMsg", msg);
        }
        resultMap.put("isRegister", true);
        resultMap.put("userId", userDetailInfo.getUserId());
        resultMap.put("name", userDetailInfo.getName());
        return resultMap;
    }

    /**
     * 构造用户注册新网所需要的参数
     * @param userId
     * @return
     */
    @RequestMapping(value = "submitPersonal", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse submitPersonal(String userId) {
        HttpResponse httpResponse = new HttpResponse();
        Map<String,Object> data = null;
        try {
            data = guarantorService.submitPersonal(userId, XW_REDIRECT_URL);
        } catch (APIException e) {
            LOGGER.error("组装用户注册新网请求参数出错");
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("组装用户注册新网请求参数出错");
            httpResponse.setCodeMessage("500", "系统内部错误，请稍后再试");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

    /**
     * 保存企业信息
     * @param bussinessInfo
     * @return
     */
    @RequestMapping(value = "new_enterprise", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse createEnterprise(@ModelAttribute BussinessInfo bussinessInfo) {
        HttpResponse httpResponse = new HttpResponse();
        bussinessInfo.setAuditStatus("WAIT");
        Map<String,Object> data = null;
        try {
            validParams(bussinessInfo);
            data = guarantorService.addEnterprise(bussinessInfo, "");
            httpResponse.setMessage("保存成功！");
        } catch (APIException e) {
            LOG.error("组装向新网请求注册企业信息参数出错");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("保存企业信息异常");
            httpResponse.setCodeMessage("500", "保存失败");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

    private void validParams(BussinessInfo bussinessInfo) {
        if (bussinessInfo != null) {
            if (StringUtils.isEmpty(bussinessInfo.getBusinessLicenseNumber())) {
                bussinessInfo.setBusinessLicenseNumber(null);
            }
            if (StringUtils.isEmpty(bussinessInfo.getTaxID())) {
                bussinessInfo.setTaxID(null);
            }
            if (StringUtils.isEmpty(bussinessInfo.getOrganizationCode())) {
                bussinessInfo.setOrganizationCode(null);
            }
        }
    }

    /**
     * 保存并提交企业信息
     * @param bussinessInfo
     * @return
     */
    @RequestMapping(value = "submit_enterprise", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse submitEnterprise(@ModelAttribute BussinessInfo bussinessInfo) {
        HttpResponse httpResponse = new HttpResponse();
        bussinessInfo.setAuditStatus("AUDIT");
        Map<String,Object> data = null;
        try {
            validParams(bussinessInfo);
            data = guarantorService.addEnterprise(bussinessInfo, XW_REDIRECT_URL);
            httpResponse.setMessage("保存成功！");
        } catch (APIException e) {
            LOG.error("组装向新网请求注册企业信息参数出错");
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("组装向新网请求注册企业信息参数出错");
            httpResponse.setCodeMessage("500", "组装向新网请求注册企业信息参数出错");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

    /**
     * 修改企业信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "updateEnterprise", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse updateEnterprise(String userId) {
        HttpResponse httpResponse = new HttpResponse();
        Map<String,Object> data = null;
        try {
            data = guarantorService.getXwUpdateEnterpriseRequestData(userId, XW_MODIFY_ENP_REDIRECT_URL);
        } catch (APIException e) {
            LOG.error("组装向新网请求修改企业信息参数出错");
            httpResponse.setCodeMessage(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("组装向新网请求修改企业信息参数出错");
            httpResponse.setCodeMessage("500", "系统出现异常，请稍后重试");
        }
        httpResponse.setData(data);
        return httpResponse;
    }

}
