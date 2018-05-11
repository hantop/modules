package com.fenlibao.pms.controller.finance.cash;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.finance.CashRedpacket;
import com.fenlibao.model.pms.da.finance.form.CashRedpacketForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.finance.cash.CashRedpacketService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 现金红包
 * Created by Bogle on 2016/1/13.
 */
@RestController
@RequestMapping("finance/cash")
public class CashRedpacketController {

    @Autowired
    private CashRedpacketService cashRedpacketService;

    /**
     * 现金红包
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("cashPacket:view")
    @RequestMapping("cashList")
    public ModelAndView cashList(
            Boolean defTime,
            CashRedpacketForm cashRedpacketForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        if(defTime != null && defTime) {
            // 统计日期: 统计日期的区间,默认统计上个自然月
            Calendar calTime = Calendar.getInstance();
            //设置天数为-1天，表示当月减一天即为上一个月的月末时间
            calTime.set(Calendar.DAY_OF_MONTH, -1);
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),calTime.get(Calendar.DAY_OF_MONTH),23,59,59);
            cashRedpacketForm.setEndTime(calTime.getTime());
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),1,0,0,0);
            cashRedpacketForm.setStartTime(calTime.getTime());
        }
        if(cashRedpacketForm.getEndTime() != null) {
            Date endTime = cashRedpacketForm.getEndTime();
            endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
            cashRedpacketForm.setEndTime(endTime);
        }
        RowBounds bounds = new RowBounds(page, limit);
        Map.Entry<BigDecimal,List<CashRedpacket>> entry = this.cashRedpacketService.findCashListByPager(cashRedpacketForm, bounds);
        PageInfo<CashRedpacket> paginator = new PageInfo<>(entry.getValue());
        return new ModelAndView("finance/cash/cash-list")
                .addObject("list", entry.getValue())
                .addObject("sumMoney", entry.getKey())
                .addObject("paginator", paginator)
                .addObject("cashRedpacket", cashRedpacketForm);
    }

    @RequiresPermissions("cashPacket:export")
    @RequestMapping("export")
    public void export(CashRedpacketForm cashRedpacketForm,HttpServletResponse response) {
        if(cashRedpacketForm.getEndTime() != null) {
            Date endTime = cashRedpacketForm.getEndTime();
            endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
            cashRedpacketForm.setEndTime(endTime);
        }

        List<CashRedpacket> list = this.cashRedpacketService.findAllCashList(cashRedpacketForm);
        CashRedpacket cashRedpacket = new CashRedpacket();
        cashRedpacket.setSendTime("总成本：");
        cashRedpacket.setPhoneNum(String.valueOf(this.cashRedpacketService.sumCashMoney(cashRedpacketForm)));
        list.add(cashRedpacket);
        POIUtil.export(response, new String[]{"发送日期", "用户手机号", "发送金额"},
                new String[]{"sendTime", "phoneNum", "money"}, list, "现金红包");
    }
}
