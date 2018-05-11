package com.fenlibao.pms.controller.finance.replacementRecharge;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.UserTypeEnum;
import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.finance.form.ReplacementRechargeForm;
import com.fenlibao.model.pms.da.finance.vo.UserRechargeAuthVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.service.pms.da.exception.ExcelException;
import com.fenlibao.service.pms.da.finance.replacementRecharge.ReplacementRechargeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  
@RequestMapping("finance/replacementRecharge")
public class ReplacementRechargeController {
	private static final Logger LOG = LoggerFactory.getLogger(ReplacementRechargeController.class);

    @Resource
    private ReplacementRechargeService replacementRechargeService;
    
    /**
     * 代充值查询列表
     * @param page
     * @param limit
     * @param replacementRechargeForm
     * @return
     */
    @RequiresPermissions("replacementRecharge:view")
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                ReplacementRechargeForm replacementRechargeForm) {
        RowBounds bounds = new RowBounds(page, limit);
        BigDecimal replacementRechargeAccountBalance = replacementRechargeService.getReplacementRechargeAccountBalance();
        List<ReplacementRecharge> replacementRechargeList = replacementRechargeService.getReplacementRechargeList(replacementRechargeForm, bounds);
        PageInfo<ReplacementRecharge> paginator = new PageInfo<>(replacementRechargeList);
        ModelAndView view = new ModelAndView("finance/replacementRecharge/index");
        view.addObject("list", replacementRechargeList);
        view.addObject("replacementRechargeForm", replacementRechargeForm);
        view.addObject("userTypes", UserTypeEnum.values());
        view.addObject("paginator", paginator);
        view.addObject("replacementRechargeAccountBalance", replacementRechargeAccountBalance);
        return view;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView replacementRechargeEdit(@Valid @ModelAttribute("replacementRecharge") ReplacementRecharge replacementRecharge, String type) {
        String title = null;
        if(type != null){
            if(type.equals("GUARANTEECORP")){
                title = "担保账户充值:";
            }else if(type.equals("INVESTOR")){
                title = "投资账户充值:";
            }else if(type.equals("BORROWERS")){
                title = "借款账户充值:";
            }
        }
        return new ModelAndView("finance/replacementRecharge/edit")
                .addObject("title", title).addObject("userTypes", UserTypeEnum.values());
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView backVoucherTypeEdit(@Valid @ModelAttribute("replacementRecharge") ReplacementRecharge replacementRecharge,
                                            BindingResult result) {
        String title = null;
        String operator = (String) SecurityUtils.getSubject().getPrincipal();// 充值经办人
        if(operator.trim() != null){
            replacementRecharge.setRechargeUserName(operator);
        }
        String userRole = replacementRecharge.getUserRole();
        if(userRole != null){
            if(userRole.equals("GUARANTEECORP")){
                title = "担保账户充值:";
            }else if(userRole.equals("INVESTOR")){
                title = "投资账户充值:";
            }else if(userRole.equals("BORROWERS")){
                title = "借款账户充值:";
            }
        }
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("finance/replacementRecharge/edit").addObject("userTypes", UserTypeEnum.values())
                    .addObject("title", title);
        }
        int id = replacementRechargeService.saveReplacementRecharge(replacementRecharge);
        String url = "redirect:/finance/replacementRecharge/edit?success=" + (id > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("userRole", replacementRecharge.getUserRole())
                .addObject("code", id);
        if (id < 0) {
            url = "finance/replacementRecharge/edit";
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    /**
     * 检索用户名称
     * @param userType
     * @param account
     * @return
     */
    @RequestMapping(value = "detectedUserName", method = RequestMethod.POST)
    public Map<String, Object> detectedUserName(String userType, String account, String userRole) {
        Map<String, Object> resultMap = new HashMap<>();
        String code = null;
        UserRechargeAuthVO userRechargeAuthVO = null;
        try {
            if (!StringUtils.isBlank(account) && !StringUtils.isBlank(userType)){
                resultMap = replacementRechargeService.getUserRechargeAuthVO(userType, account, userRole);
            }
        }catch (ExcelException e){
            e.printStackTrace();
            code = "2000";
            resultMap.put("code", code);
        }
        resultMap.put("userRole", userRole);
        return resultMap;
    }

    @RequiresPermissions("replacementRecharge:audit")
    @RequestMapping(value = "audit", method = {RequestMethod.POST})
    public Map<String, String> audit(int flag, ReplacementRecharge replacementRecharge) {
        Map<String, String> resultMap = new HashMap<>();
        String code = null;
        String message = "内部错误";
        boolean same;// 充值与审核不是同一个人
        // 当前用户名
        String operator = (String) SecurityUtils.getSubject().getPrincipal();
        replacementRecharge.setAuditUserName(operator);// 审核经办人
        if(replacementRecharge != null){
            if(operator != null){
                // 校验充值以及审核是否是同一个人
                same = replacementRechargeService.checkAuditAndRechargeUser(replacementRecharge.getId(), operator);
                if(same){
                    replacementRecharge.setAuditUserName(operator);// 审核经办人
                }else{
                    code = "1000";
                    resultMap.put("code", code);
                    return resultMap;
                }
            }
        }
        try {
            code = replacementRechargeService.audit(flag, replacementRecharge);
            resultMap.put("code", code);
        } catch (XWTradeException te) {
            // 前缀为1的异常不直接返回到前端
            code = te.getCode();
            if (!code.substring(0).equals("1")) {
                message = te.getMessage();
                resultMap.put("message", message);
            }
            resultMap.put("code", code);
        } catch (Exception e) {
            LOG.error("[代充值操作异常:]" + e.getMessage(), e);
            code = "500";
            resultMap.put("code", code);
        }
        return resultMap;
    }
    
}
