package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.da.biz.form.InterestManagementForm;
import com.fenlibao.model.pms.da.biz.interestManagement.InterestManagementRecord;
import com.fenlibao.model.pms.da.cs.form.ReplacePhoneForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.biz.interestManagement.InterestManagementService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("biz/interestManagement")
public class InterestManagementController {

    private static final Logger logger = LoggerFactory.getLogger(InterestManagementController.class);

    @Resource
    private InterestManagementService interestManagementService;

    // 利息管理费配置列表
    @RequestMapping(value = "")
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                ReplacePhoneForm replacePhoneForm) {
        ModelAndView view = new ModelAndView("biz/interestManagement/index");
        RowBounds bounds = new RowBounds(page, limit);
        // 当前启用利息管理费比例
        InterestManagementForm interestManagementForm = interestManagementService.getInterestManagementForm(1);
        // 待审核记录
        InterestManagementRecord auditInterestManagementInfo = interestManagementService.getAuditInterestManagementInfo();
        // 历史修改记录
        List<InterestManagementRecord> interestManagementRecordList = interestManagementService.getInterestManagementRecordList(bounds);
        PageInfo<InterestManagementRecord> paginator = new PageInfo<>(interestManagementRecordList);
        view.addObject("interestManagementForm", interestManagementForm);
        view.addObject("auditInterestManagementInfo", auditInterestManagementInfo);
        view.addObject("list", interestManagementRecordList);
        view.addObject("replacePhoneForm", replacePhoneForm);
        view.addObject("paginator", paginator);
        return view;
    }

    // 修改当前利息管理费比例
    @RequestMapping(value = "updateInterest", method = RequestMethod.POST)
    public Map<String, Object> updateInterest(InterestManagementForm interestManagementForm) {
        Map<String, Object> resultMap = new HashMap<>();
        String sysCode = "0000";
        interestManagementForm.setUpdatePercent(new BigDecimal(interestManagementForm.getUpdatePercentStr()));
        if(interestManagementForm.getUpdatePercent().compareTo(BigDecimal.ZERO) < 0
                || interestManagementForm.getUpdatePercent().compareTo(BigDecimal.ZERO) > 100){
            sysCode = "0001";// 利息管理费设置比例不合法(0-100)
            resultMap.put("sysCode",sysCode);
            return resultMap;
        }else {
            // 新增待审核利息管理费以及操作记录
            try {
                interestManagementService.updateInterestManagementLogic(interestManagementForm);
            }catch (Exception e){
                logger.error("操作异常: " + e);
                sysCode = "0011";
                resultMap.put("sysCode",sysCode);
                return resultMap;
            }
        }
        resultMap.put("sysCode",sysCode);
        return resultMap;
    }

    @RequestMapping(value = "changeInterestState", method = RequestMethod.POST)
    public HttpResponse changeInterestState(Integer recordId, Integer id, Integer state) {
        HttpResponse response = new HttpResponse();
        try {
            interestManagementService.changeInterestState(recordId, id, state);
        } catch (Exception e) {
            response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
        return response;
    }

}
