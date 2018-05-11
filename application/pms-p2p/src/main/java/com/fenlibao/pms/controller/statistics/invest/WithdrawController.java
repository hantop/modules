package com.fenlibao.pms.controller.statistics.invest;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.statistics.invest.Withdraw;
import com.fenlibao.model.pms.da.statistics.invest.form.WithdrawForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.withdraw.WithdrawService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
@Controller
@RequestMapping("statistics/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping
    @RequiresPermissions("withdraw:view")
    public ModelAndView list(
            WithdrawForm withdrawForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        if (withdrawForm.getDef() == null || withdrawForm.getDef()) {
            //默认日期为昨天
            withdrawForm.setWithdrawStartTime(DateUtil.startTime(DateUtil.dateAdd(new Date(), -1)));
            withdrawForm.setWithdrawEndTime(DateUtil.endTime(DateUtil.dateAdd(new Date(), -1)));
        }
        if(withdrawForm.getWithdrawStartTime() != null) {
            withdrawForm.setWithdrawStartTime(DateUtil.endTime(DateUtil.dateAdd(withdrawForm.getWithdrawStartTime(),-1)));
        }
        if (withdrawForm.getWithdrawEndTime() != null) {
            withdrawForm.setWithdrawEndTime(DateUtil.endTime(withdrawForm.getWithdrawEndTime()));
        }
        RowBounds bounds = new RowBounds(page, limit);
        List<Withdraw> list = this.withdrawService.findWithdraw(withdrawForm, bounds);
        Withdraw withdrawTotal = withdrawService.getWithdrawTotal(withdrawForm);
        if(withdrawForm.getWithdrawStartTime() != null) {
            withdrawForm.setWithdrawStartTime(DateUtil.dateAdd(withdrawForm.getWithdrawStartTime(),1));
        }
        PageInfo<Withdraw> paginator = new PageInfo<>(list);
        return new ModelAndView("statistics/withdraw/index")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("withdrawTotal", withdrawTotal)
                .addObject("withdraw", withdrawForm);
    }

    @RequiresPermissions("withdraw:export")
    @RequestMapping(value = "export")
    public void withdrawExport(HttpServletResponse response, WithdrawForm withdrawForm, @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        if (withdrawForm.getWithdrawEndTime() != null) {
            withdrawForm.setWithdrawEndTime(DateUtil.endTime(withdrawForm.getWithdrawEndTime()));
        }
        if(withdrawForm.getWithdrawStartTime() != null) {
            withdrawForm.setWithdrawStartTime(DateUtil.endTime(DateUtil.dateAdd(withdrawForm.getWithdrawStartTime(),-1)));
        }
        RowBounds bounds = new RowBounds(0, limit);
        List<Withdraw> list = withdrawService.findWithdraw(withdrawForm, bounds);
        String headers[] = {"手机号码", "姓名", "提现日期", "提现金额", "累计投资金额", "累计投资次数", "再投金额", "账户余额"};
        String fieldNames[] = {"phoneNum", "realName", "createTime", "withdrawMoney", "investTotalMoney", "investCount", "investing", "surplusMoney"};
        POIUtil.export(response, headers, fieldNames, list, "提现信息");
    }
}
