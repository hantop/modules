package com.fenlibao.pms.controller.statistics.invest;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.statistics.invest.FirstInvest;
import com.fenlibao.model.pms.da.statistics.invest.Withdraw;
import com.fenlibao.model.pms.da.statistics.invest.form.FirstInvestForm;
import com.fenlibao.model.pms.da.statistics.invest.form.WithdrawForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.first.FirstInvestService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
@Controller
@RequestMapping("statistics/firstInvest")
public class FirstInvestController {

    @Autowired
    private FirstInvestService firstInvestService;

    @RequestMapping
    @RequiresPermissions("firstInvest:view")
    public ModelAndView list(
            FirstInvestForm firstInvestForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        if (firstInvestForm.getDef() == null || firstInvestForm.getDef()) {
            //默认日期为昨天
            firstInvestForm.setInvestStartTime(DateUtil.startTime(DateUtil.dateAdd(new Date(), -1)));
            firstInvestForm.setInvestEndTime(DateUtil.endTime(DateUtil.dateAdd(new Date(), -1)));
        }
        if(firstInvestForm.getInvestEndTime() != null) {
            firstInvestForm.setInvestEndTime(DateUtil.endTime(firstInvestForm.getInvestEndTime()));
        }
        if(firstInvestForm.getInvestStartTime() != null) {
            firstInvestForm.setInvestStartTime(DateUtil.endTime(DateUtil.dateAdd(firstInvestForm.getInvestStartTime(),-1)));
        }
        if (firstInvestForm.getRegEndTime() != null) {
            firstInvestForm.setRegEndTime(DateUtil.endTime(firstInvestForm.getRegEndTime()));
        }
        List<FirstInvest>  list = this.firstInvestService.findFirstInvest(firstInvestForm,new RowBounds(page, limit));
        if(firstInvestForm.getInvestStartTime() != null) {
            firstInvestForm.setInvestStartTime(DateUtil.endTime(DateUtil.dateAdd(firstInvestForm.getInvestStartTime(),1)));
        }
        PageInfo<FirstInvest> paginator = new PageInfo<>(list);
        return new ModelAndView("statistics/first/index")
                .addObject("list", list)
                .addObject("paginator", paginator)
//                .addObject("firstInvestTotal", firstInvestTotal)
                .addObject("firstInvestForm", firstInvestForm);
    }

    @RequiresPermissions("firstInvest:export")
    @RequestMapping(value = "export")
    public void withdrawExport(HttpServletResponse response, FirstInvestForm firstInvestForm, @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        if(firstInvestForm.getInvestEndTime() != null) {
            firstInvestForm.setInvestEndTime(DateUtil.endTime(firstInvestForm.getInvestEndTime()));
        }
        if(firstInvestForm.getInvestStartTime() != null) {
            firstInvestForm.setInvestStartTime(DateUtil.endTime(DateUtil.dateAdd(firstInvestForm.getInvestStartTime(),-1)));
        }
        if (firstInvestForm.getRegEndTime() != null) {
            firstInvestForm.setRegEndTime(DateUtil.endTime(firstInvestForm.getRegEndTime()));
        }
        List<FirstInvest> list = firstInvestService.findFirstInvest(firstInvestForm,new RowBounds(0, limit));
        String headers[] = {"手机号码", "姓名", "首投日期", "注册日期", "首投金额", "投资期限"};
        String fieldNames[] = {"phoneNum", "realName", "createTime", "regtime", "money", "limitTime"};
        POIUtil.export(response, headers, fieldNames, list, "首投信息");
    }

}
