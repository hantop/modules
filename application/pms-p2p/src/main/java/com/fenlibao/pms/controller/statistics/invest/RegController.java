package com.fenlibao.pms.controller.statistics.invest;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.statistics.invest.Register;
import com.fenlibao.model.pms.da.statistics.invest.form.RegisterForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.invest.RegService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bogle on 2015/12/30.
 */
@Controller
@RequestMapping("statistics/reg")
public class RegController {

    @Autowired
    private RegService regService;

    @RequiresPermissions("statisticsReg:view")
    @RequestMapping(value = "reg-list")
    public ModelAndView regList(
            RegisterForm register,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        this.parseDate(register);
        RowBounds bounds = new RowBounds(page, limit);
        List<Register> list = regService.findRegList(register, bounds);
        RegisterForm regForm =regService.getMoneyTotal(register);

    //    BigDecimal investMoneyTotal = regService.getInvestMoneyTotal(register);    //累计投资金额
    //    BigDecimal investSumTotal = regService.getInvestSumTotal(register);    //在投金额
        PageInfo<Register> paginator = new PageInfo<>(list);
        return new ModelAndView("statistics/reg/reg-list")
                .addObject("paginator", paginator)
                .addObject("list", list)
                .addObject("regTotal", paginator.getTotal())
                .addObject("regForm", regForm)
                .addObject("reg", register);
    }

    @RequiresPermissions("statisticsReg:export")
    @RequestMapping(value = "reg-export")
    public void regList(
            RegisterForm register,
            HttpServletResponse response
           ) {
        this.parseDate(register);
        List<Register> list = regService.findRegList(register);
        POIUtil.export(response, new String[]{"注册日期", "手机号码", "姓名","实名认证","是否绑卡","累计充值金额","累计投资次数","累计投资金额","在投金额","账户余额","客户端类型","渠道来源"},
                new String[]{"registerTime", "phone", "name","realName","bindBank","fund","investNum","investMoney","investSum","balance","clientType","channelName"}, list, "注册统计");
    }

    private void parseDate(RegisterForm register) {
        if (register.getDef()) {
            Calendar curent = Calendar.getInstance();
            int YY = curent.get(Calendar.YEAR);
            int MM = curent.get(Calendar.MONTH);
            int DD = curent.get(Calendar.DATE);
            curent.set(YY, MM, DD-1, 0, 0, 0);
            register.setStartTime(curent.getTime());
            register.setEndTime(curent.getTime());
        }
        if (register.getEndTime() != null) {
            Date endTime = register.getEndTime();
            endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1000);
            register.setEndTime(endTime);
        }

        String isAuth = register.getIsAuth();
        if (!StringUtils.isBlank(isAuth)){
            if("1".equals(isAuth)){
                register.setRealName("TG");
            }else if("0".equals(isAuth)){
                register.setRealName("BTG");
            }
        }
        BigDecimal minInvestMoney = register.getMinInvestMoney();
    }
}
