package com.fenlibao.pms.controller.planCenter.planType;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.da.planCenter.PlanType;
import com.fenlibao.model.pms.da.planCenter.form.PlanTypeForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.planCenter.PlanTypeService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 计划模板管理
 */
@RestController
@RequestMapping("planCenter/planType")
public class PlanTypeController {
    private static final Logger LOG = LoggerFactory.getLogger(PlanTypeController.class);

    @Autowired
    private PlanTypeService planTypeService;

   @RequiresPermissions("planType:view")
    @RequestMapping(value = "index")
    public ModelAndView planTypes(PlanTypeForm planTypeForm,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        if (planTypeForm.getTitle() == null || planTypeForm.getTitle().equals("")) {
            planTypeForm.setTitle(null);
        }
        List<PlanType> list = planTypeService.findPlanTypePager(planTypeForm, bounds);
        PageInfo<PlanType> paginator = new PageInfo<>(list);
        return new ModelAndView("planCenter/planType/index").addObject("list", list)
                .addObject("paginator", paginator).addObject("planTypeForm", planTypeForm);
    }

    @RequiresPermissions("planType:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView planTypeEdit(@RequestParam(required = false, defaultValue = "0") int id) {
        PlanType planType = new PlanType();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            planType = planTypeService.findPlanTypeById(id);
        }
        return new ModelAndView("planCenter/planType/edit").addObject("planType", planType).addObject("title", title);
    }

    @RequiresPermissions("planType:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView planTypeEdit(@Valid @ModelAttribute("planType") PlanType planType,
                                            BindingResult result) {
        int code = 0;
        String title = "新增";
        if (planType.getId() > 0) {
         title = "编辑";
        }
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("planCenter/planType/edit").addObject("planType", planType)
                    .addObject("title", title);
        }
        try {
            code = planTypeService.saveOrUpdatePlan(planType);
        }catch (Exception e){
            LOG.error("操作异常");
            e.printStackTrace();
        }
        String url = "redirect:/planCenter/planType/edit?success=" + (code > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("planType", planType)
                .addObject("code", code);
        if (code < 0) {
            url = "planCenter/planType/edit";
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    // 设置启用/停用
    @RequiresPermissions("planType:setStatus")
    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    public HttpResponse setStatus(@RequestParam(required = false, defaultValue = "0") int id, String status) {
        HttpResponse response = new HttpResponse();
        try {
            planTypeService.updatePlanTypeStatus(id, status);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

}
