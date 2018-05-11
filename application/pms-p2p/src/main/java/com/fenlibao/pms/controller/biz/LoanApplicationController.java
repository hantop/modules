package com.fenlibao.pms.controller.biz;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.model.pms.da.biz.LoanApplication;
import com.fenlibao.model.pms.da.biz.form.LoanApplicationEditForm;
import com.fenlibao.model.pms.da.biz.form.LoanApplicationForm;
import com.fenlibao.model.pms.da.biz.viewobject.LoanApplicationVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.common.base.EnumService;
import com.fenlibao.service.pms.da.biz.loanapplication.LoanApplicaitonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 借款申请管理
 *
 * Created by chenzhixuan on 2016/4/11.
 */
@RestController
@RequestMapping("biz/loanapplication")
public class LoanApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(LoanApplicationController.class);
    @Resource
    private LoanApplicaitonService loanApplicaitonService;
    @Resource
    private EnumService enumService;

    @RequestMapping(value = "nopassReasonContent", method = RequestMethod.GET)
    public String getNopassReasonContentByKey(String key) {
        return JSONObject.toJSONString(enumService.getEnumValue("loan_application", "nopass_reason_content", key));
    }

    @RequiresPermissions("loanapplication:edit")
    @RequestMapping(value = "pass", method = RequestMethod.POST)
    public HttpResponse pass(int id, String processingOpinion) {
        HttpResponse response = new HttpResponse();
        int result = loanApplicaitonService.updateProcessingStatus(id, processingOpinion, 1, 0);
        if (result < 1) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("loanapplication:edit")
    @RequestMapping(value = "nopass", method = RequestMethod.POST)
    public HttpResponse nopass(int id, String processingOpinion, int nopassReason) {
        HttpResponse response = new HttpResponse();
        int result = loanApplicaitonService.updateProcessingStatus(id, processingOpinion, 2, nopassReason);
        if (result < 1) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ModelAndView viewPage(int id) {
        ModelAndView mav = new ModelAndView("biz/loanapplication/view");
        LoanApplication loanApplication = loanApplicaitonService.getLoanApplicationById(id);
        LoanApplicationVO viewObject = new LoanApplicationVO();
        // \n替换为br让前端页面显示换行
        String processingOpinion = loanApplication.getProcessingOpinion();
        if (StringUtils.isNotBlank(processingOpinion)) {
            loanApplication.setProcessingOpinion(processingOpinion.replaceAll("\r\n", "<br/>").replaceAll(" ", "&nbsp;"));
        }
        viewObject.setPhonenum(loanApplication.getPhonenum());
        viewObject.setContacts(loanApplication.getContacts());
        viewObject.setAmountRange(loanApplication.getAmountRange());
        viewObject.setDistrictFullName(loanApplication.getDistrictFullName());
        viewObject.setAnnualIncome(loanApplication.getAnnualIncome());
        viewObject.setHasRoom(loanApplication.getHasRoom() == 1 ? "是" : "否");
        viewObject.setHasCar(loanApplication.getHasCar() == 1 ? "是" : "否");
        viewObject.setCreateTime(DateUtil.getDateTime(loanApplication.getCreateTime()));
        viewObject.setUpdateTime(DateUtil.getDateTime(loanApplication.getUpdateTime()));
        viewObject.setProcessingStatus(loanApplication.getProcessingStatus());
        // 如果为不通过且理由不为0(其它)则显示填写的意见，否则显示枚举对应的意见
        if (loanApplication.getProcessingStatus() == 2) {
            int nopassReason = loanApplication.getNopassReason();
            // 其它
            if (nopassReason == 0) {
                viewObject.setNopassReasonTitle("其它");
                viewObject.setProcessingOpinion(loanApplication.getProcessingOpinion());
            } else {
                viewObject.setNopassReasonTitle(enumService.getEnumValue("loan_application", "nopass_reason_title", String.valueOf(nopassReason)));
                viewObject.setProcessingOpinion(enumService.getEnumValue("loan_application", "nopass_reason_content", String.valueOf(nopassReason)));
            }
        } else {
            viewObject.setProcessingOpinion(loanApplication.getProcessingOpinion());
        }
        mav.addObject("loanApplication", viewObject);
        return mav;
    }

    @RequiresPermissions("loanapplication:edit")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView editPage(int id) {
        ModelAndView mav = new ModelAndView("biz/loanapplication/edit");
        LoanApplication loanApplication = loanApplicaitonService.getLoanApplicationById(id);
        // 月收入枚举
        List<String> annualIncomes = getEnumValues("loan_application", "annual_income");
        // 借款金额范围
        List<String> loanAmountRanges = getEnumValues("loan_application", "amount_range");
        // 不通过原因标题
        List<TEnum> nopassReasonTitles = getEnums("loan_application", "nopass_reason_title");
        mav.addObject("loanApplication", loanApplication);
        mav.addObject("annualIncomes", annualIncomes);
        mav.addObject("amountRanges", loanAmountRanges);
        mav.addObject("nopassReasonTitles", nopassReasonTitles);
        return mav;
    }

    @RequiresPermissions("loanapplication:edit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView edit(
            @Valid @ModelAttribute("loanApplication") LoanApplicationEditForm loanApplicationEditForm,
            BindingResult result) {
        int id = loanApplicationEditForm.getId();
        String editUrl = "biz/loanapplication/edit?id=" + id;
        String title = "编辑";
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView(editUrl)
                    .addObject("loanApplicationEditForm", loanApplicationEditForm)
                    .addObject("title", title);
        }
        int flag = loanApplicaitonService.updateLoanApplication(loanApplicationEditForm);
        String url = "redirect:/" + editUrl + "&success=" + (flag > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("code", flag);
        if (flag < 0) {
            url = editUrl;
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequiresPermissions("loanApplication:view")
    @RequestMapping
    public ModelAndView loanApplicationList(
            LoanApplicationForm loanApplicationForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        ModelAndView mav = new ModelAndView("biz/loanapplication/index");
        RowBounds bounds = new RowBounds(page, limit);
        Date startDate = null;
        Date endDate = null;
        String startDateStr = loanApplicationForm.getStartDate();
        String endDateStr = loanApplicationForm.getEndDate();
        if(!StringUtils.isEmpty(startDateStr)) {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)) {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        List<LoanApplication> loanApplicationList = loanApplicaitonService.getLoanApplications(
                startDate, endDate, loanApplicationForm.getPhonenum(), loanApplicationForm.getProcessingStatus(), bounds);
        PageInfo<LoanApplication> paginator = new PageInfo<>(loanApplicationList);
        // 不通过原因标题
        List<TEnum> nopassReasonTitles = getEnums("loan_application", "nopass_reason_title");
        mav.addObject("nopassReasonTitles", nopassReasonTitles);
        return mav
                .addObject("loanApplicationList", loanApplicationList)
                .addObject("loanApplicationForm", loanApplicationForm)
                .addObject("paginator", paginator);
    }

    @RequiresPermissions("loanapplication:export")
    @RequestMapping("/export")
    public void export(LoanApplicationForm loanApplicationForm, HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        Date startDate = null;
        Date endDate = null;
        String startDateStr = loanApplicationForm.getStartDate();
        String endDateStr = loanApplicationForm.getEndDate();
        if(!StringUtils.isEmpty(startDateStr)) {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)) {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        List<LoanApplication> loanApplicationList = loanApplicaitonService.getLoanApplications(
                startDate, endDate, loanApplicationForm.getPhonenum(), loanApplicationForm.getProcessingStatus(), bounds);
        List<LoanApplicationExport> exports = new ArrayList<>();
        LoanApplication loanApplication;
        LoanApplicationExport export;
        for (int i = 0; i< loanApplicationList.size(); i++) {
            export = new LoanApplicationExport();
            loanApplication = loanApplicationList.get(i);
            export.no = i + 1;
            export.phonenum = loanApplication.getPhonenum();
            export.contacts = loanApplication.getContacts();
            export.amountRange = loanApplication.getAmountRange();
            export.districtFullName = loanApplication.getDistrictFullName();
            export.annualIncome = loanApplication.getAnnualIncome();
            export.hasRoom = loanApplication.getHasCar() == 1 ? "是" : "否";
            export.hasCar = loanApplication.getHasCar() == 1 ? "是" : "否";
            export.createTime = DateUtil.getDateTime(loanApplication.getCreateTime());
            switch (loanApplication.getProcessingStatus()) {
                case 0:
                    export.processingStatus = "未处理";
                    break;
                case 1:
                    export.processingStatus = "通过";
                    break;
                case 2:
                    export.processingStatus = "不通过";
                    break;
            }
            exports.add(export);
        }
        String headers[] = {"序号", "联系人姓名", "借款金额", "手机号码", "所在区域", "月收入", "是否有车", "是否有房", "状态", "申请时间"};
        String fieldNames[] = {"no", "contacts", "amountRange", "phonenum", "districtFullName", "annualIncome", "hasCar", "hasRoom", "processingStatus", "createTime"};
        POIUtil.export(response, headers, fieldNames, exports, "借款申请信息");
    }

    private List<TEnum> getEnums(String table, String column) {
        return enumService.getEnum(table, column);
    }

    private List<String> getEnumValues(String table, String column) {
        List<TEnum> enums = getEnums(table, column);
        List<String> enumValues = new ArrayList<>();
        for (TEnum tEnum : enums) {
            enumValues.add(tEnum.getEnumValue());
        }
        return enumValues;
    }

    private class LoanApplicationExport {
        public int no;// 序号
        public String phonenum;// 手机号
        public String contacts;// 联系人
        public String amountRange;// 借款金额范围
        public String districtFullName;// 所在区域全称
        public String annualIncome;// 年收入（仅记录收入范围）
        public String hasRoom;// 是否有房（0=否，1=有）
        public String hasCar;// 是否有车（0=否，1=有）
        public String createTime;// 创建时间
        public String processingStatus;// 处理状态（0=未处理，1=通过，2=不通过）
    }
}
