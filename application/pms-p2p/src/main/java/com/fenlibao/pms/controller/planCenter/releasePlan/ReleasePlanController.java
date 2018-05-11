package com.fenlibao.pms.controller.planCenter.releasePlan;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.da.global.InvestPlanStatus;
import com.fenlibao.model.pms.da.planCenter.PlanMarketingSetting;
import com.fenlibao.model.pms.da.planCenter.form.PlanMarketSettingForm;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.biz.investplan.InvestPlanService;
import com.fenlibao.service.pms.da.biz.investplan.PlanBidService;
import com.fenlibao.service.pms.da.planCenter.ReleasePlanService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */
@RestController
@RequestMapping("planCenter/releasePlan")
public class ReleasePlanController {
    private static final Logger LOG = LoggerFactory.getLogger(ReleasePlanController.class);

    @Autowired
    private ReleasePlanService releasePlanService;
    @Autowired
    private InvestPlanService investPlanService;
    @Autowired
    private PlanBidService planBidService;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "termination", method = RequestMethod.POST)
    public HttpResponse termination(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            String operator = (String) SecurityUtils.getSubject().getPrincipal();
            PmsUser pmsUser = userDetailsService.findByUsername(operator);
            planBidService.terminationPlan(id, pmsUser.getId());
        } catch (Exception e) {
            LOG.error("【计划终止异常】:", e);
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "invalid", method = RequestMethod.POST)
    public HttpResponse invalid(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.invalid(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "release", method = RequestMethod.POST)
    public HttpResponse release(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.release(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "cancelTimingRelease", method = RequestMethod.POST)
    public HttpResponse cancelTimingRelease(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.cancelTimingRelease(id, InvestPlanStatus.AWAIT_RELEASE.getKey());
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "timingRelease", method = RequestMethod.POST)
    public HttpResponse timingRelease(Integer id, String releaseTime, Integer displayBefore) {
        HttpResponse response = new HttpResponse();
        Date releaseDate = null;
        Date displayDate = null;
        String releaseTimeStr = releaseTime;
        if(!StringUtils.isEmpty(releaseTimeStr)) {
            releaseDate = DateUtil.StringToDate(releaseTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if (displayBefore != null && displayBefore >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(releaseDate);
            cal.add(Calendar.HOUR, -displayBefore);
            displayDate = cal.getTime();
        }
        try {
            investPlanService.timingRelease(id, releaseDate, displayDate);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    public HttpResponse audit(Integer id, boolean isPass) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.audit(id, isPass);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "recommend", method = RequestMethod.POST)
    public HttpResponse recommend(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.recommend(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "sticktop", method = RequestMethod.POST)
    public HttpResponse sticktop(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.sticktop(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "cancelRecommend", method = RequestMethod.POST)
    public HttpResponse cancelRecommend(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.cancelRecommend(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "cancelSticktop", method = RequestMethod.POST)
    public HttpResponse cancelSticktop(Integer id) {
        HttpResponse response = new HttpResponse();
        try {
            investPlanService.cancelSticktop(id);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("releasePlan:view")
    @RequestMapping(value = "index")
    public ModelAndView plans(PlanMarketSettingForm planMarketSettingForm,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        String startTimeStr = planMarketSettingForm.getTimeStart();
        String endTimeStr = planMarketSettingForm.getTimeEnd();
        if (startTimeStr != "" && !StringUtils.isEmpty(startTimeStr)) {
            startTimeStr = startTimeStr + " 00:00:00";
            planMarketSettingForm.setTimeStartShow(startTimeStr);
        }else{
            planMarketSettingForm.setTimeStart(null);
        }
        if (endTimeStr != "" && !StringUtils.isEmpty(endTimeStr)) {
            endTimeStr = endTimeStr + " 23:59:59";
            planMarketSettingForm.setTimeEndShow(endTimeStr);
        }else{
            planMarketSettingForm.setTimeEnd(null);
        }
        if (planMarketSettingForm.getName() == null || planMarketSettingForm.getName().equals("")) {
            planMarketSettingForm.setName(null);
        }
        List<PlanMarketingSetting> list = releasePlanService.findPlanMarketingSettingPager(planMarketSettingForm, bounds);
        PageInfo<PlanMarketingSetting> paginator = new PageInfo<>(list);
        return new ModelAndView("planCenter/releasePlan/index").addObject("list", list)
                .addObject("paginator", paginator).addObject("planMarketSettingForm", planMarketSettingForm);
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView releasePlanEdit(@RequestParam(required = false, defaultValue = "0") int id) {
        PlanMarketingSetting planMarketingSetting = new PlanMarketingSetting();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            planMarketingSetting = releasePlanService.findPlanMarketingSettingById(id);
        }
        return new ModelAndView("planCenter/releasePlan/edit").addObject("planMarketingSetting", planMarketingSetting).addObject("title", title);
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView releasePlanEdit(@Valid @ModelAttribute("planMarketingSetting") PlanMarketingSetting planMarketingSetting,
                                            BindingResult result) {
        int code = 0;
        String title = "新增";
        if (planMarketingSetting.getId() > 0) {
            title = "编辑";
        }
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("planCenter/releasePlan/edit").addObject("planMarketingSetting", planMarketingSetting)
                    .addObject("title", title);
        }
        try {
            code = releasePlanService.saveOrUpdatePlan(planMarketingSetting);
        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException) {
                LOG.error("保存操作异常");
                e.printStackTrace();
            }else{
                LOG.error("操作异常");
                e.printStackTrace();
            }
        }
        String url = "redirect:/planCenter/releasePlan/edit?success=" + (code > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("planMarketingSetting", planMarketingSetting)
                .addObject("code", code);
        if (code < 0) {
            url = "planCenter/releasePlan/edit";
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView releasePlanView(@RequestParam(required = false, defaultValue = "0") int id) {
        PlanMarketingSetting planMarketingSetting = releasePlanService.findPlanMarketingSettingById(id);
        return new ModelAndView("planCenter/releasePlan/view").addObject("planMarketingSetting", planMarketingSetting);
    }

    @RequiresPermissions("releasePlan:create")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ModelAndView audit(@RequestParam(required = false, defaultValue = "0") int id) {
        PlanMarketingSetting planMarketingSetting = releasePlanService.findPlanMarketingSettingById(id);
        return new ModelAndView("planCenter/releasePlan/audit").addObject("planMarketingSetting", planMarketingSetting);
    }

    @RequiresPermissions(value = {"releasePlan:export"})
    @RequestMapping("export")
    public void exportPlan(PlanMarketSettingForm planMarketSettingForm, HttpServletResponse response) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RowBounds bounds = new RowBounds();
        String startTimeStr = planMarketSettingForm.getTimeStart();
        String endTimeStr = planMarketSettingForm.getTimeEnd();
        if (startTimeStr != "" && !StringUtils.isEmpty(startTimeStr)) {
            startTimeStr = startTimeStr + " 00:00:00";
            planMarketSettingForm.setTimeStartShow(startTimeStr);
        }else{
            planMarketSettingForm.setTimeStart(null);
        }
        if (endTimeStr != "" && !StringUtils.isEmpty(endTimeStr)) {
            endTimeStr = endTimeStr + " 23:59:59";
            planMarketSettingForm.setTimeEndShow(endTimeStr);
        }else{
            planMarketSettingForm.setTimeEnd(null);
        }
        if (planMarketSettingForm.getName() == null || planMarketSettingForm.getName().equals("")) {
            planMarketSettingForm.setName(null);
        }
        List<PlanMarketingSetting> list = releasePlanService.findPlanMarketingSettingPager(planMarketSettingForm, bounds);
        List<DetailExport> export = new ArrayList<>();
        for (PlanMarketingSetting item : list) {
            DetailExport d = new DetailExport();
            d.name = item.getName();
            d.amount = item.getAmount() != null ? item.getAmount().doubleValue() : null;
            String cycleType = item.getCycleType();
            if("m".equals(cycleType)){
                d.cycle = String.valueOf(item.getCycle()) + "个月";
            }else if("d".equals(cycleType)){
                d.cycle = String.valueOf(item.getCycle()) + "天";
            }
            if(item.getType() == 1){//月升计划
                d.investRate = item.getMinYearlyRate()
                        + "%-" + item.getMaxYearlyRate() + "%";
                if(item.getMoIncreaseRate() != null && item.getMoIncreaseRate().compareTo(BigDecimal.ZERO) > 0){
                    d.moOrRaisedRate = item.getMoIncreaseRate() + "%";
                }
                if(item.getRaiseRate() != null && item.getRaiseRate().compareTo(BigDecimal.ZERO) > 0){
                    d.moOrRaisedRate = d.moOrRaisedRate + "/" +
                            item.getRaiseRate() + "%";
                }
            }else if(item.getType() == 2){
                d.investRate = item.getInvestRate() + "%";
                if(item.getRaiseRate() != null && item.getRaiseRate().compareTo(BigDecimal.ZERO) > 0) {
                    d.moOrRaisedRate = item.getRaiseRate() + "%";
                }else{
                    d.moOrRaisedRate = "-";
                }
            }
            d.updateTime = format.format(item.getUpdateTime());
            String status = item.getStatus();
            if ("1".equals(status)) {
                d.status = "待提交";
            } else if ("2".equals(status)) {
                d.status = "待审核";
            } else if ("3".equals(status)) {
                d.status = "待发布";
            } else if ("4".equals(status)) {
                d.status = "投资中";
            } else if ("5".equals(status)){
                d.status = "还款中";
            } else if ("6".equals(status)){
                d.status = "已结清";
            } else if ("7".equals(status)){
                d.status = "已作废";
            } else if ("8".equals(status)){
                d.status = "预发布";
            }
            export.add(d);
        }
        String headers[] = {"计划名称", "计划金额(元)", "计划期限", "投资利率", "利率月增幅/加息", "处理时间", "状态"};
        String fieldNames[] = {"name", "amount", "cycle", "investRate", "moOrRaisedRate", "updateTime", "status"};
        POIUtil.export(response, headers, fieldNames, export, "计划导出");
    }

    private class DetailExport {
        String name;//名称
        Double amount;
        String cycle;//计划期限
        String moOrRaisedRate;//利率月增幅
        String investRate;//投资利率
        String updateTime;//处理时间
        String status;//状态
    }
}
