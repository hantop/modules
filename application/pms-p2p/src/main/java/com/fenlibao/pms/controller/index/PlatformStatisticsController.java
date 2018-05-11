package com.fenlibao.pms.controller.index;

import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.model.pms.da.platformstatistics.Platformstatistics;
import com.fenlibao.model.pms.da.platformstatistics.PlatformstatisticsTotal;
import com.fenlibao.service.pms.da.platformstatistics.PlatformStatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 平台每日统计数据
 * Created by chenzhixuan on 2015/12/1.
 */
@RestController
@RequestMapping("index")
public class PlatformStatisticsController {
    private static final Logger log = LoggerFactory.getLogger(PlatformStatisticsController.class);

    @Resource
    private PlatformStatisticsService platformStatisticsService;

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView platformStatisticsIndex() {
        ModelAndView view = new ModelAndView("index");
        // 平台累计数据
        PlatformstatisticsTotal platformstatisticsTotal = platformStatisticsService.getPlatformstatisticsTotal();
        return view.addObject("platformstatisticsTotal", platformstatisticsTotal);
    }

    /**
     * 根据历史类型获取数据
     * @param historyType
     * @return
     */
    @RequestMapping(value = "platformstatistics", method = RequestMethod.GET)
    public Platformstatistics getPlatformstatistics(String historyType) {
        Date startTime = null;
        Date endTime = null;
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        if (StringUtils.isBlank(historyType) || historyType.equals("yesterday")) {
            calendarStart.add(Calendar.DATE, -1);
            calendarEnd.add(Calendar.DATE, -1);
        } else if(historyType.equals("lastweek")) {
            // 上周的周一
            calendarStart = DateUtil.getPreviousMonday();
            // 上周的周日
            calendarEnd = (Calendar) calendarStart.clone();
            calendarEnd.add(Calendar.DATE, 6);
        } else if(historyType.equals("lastmonth")) {
            // 上个月的第一天
            calendarStart.add(Calendar.MONTH, -1);
            calendarStart.set(Calendar.DATE, 1);
            // 上个月的最后一天
            calendarEnd.add(Calendar.MONTH, -1);
            calendarEnd.set(Calendar.DATE, calendarEnd.getActualMaximum(calendarEnd.DAY_OF_MONTH));
        }
        // 开始时间设置为23:59:59
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        // 结束时间设置为23:59:59
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        startTime = calendarStart.getTime();
        endTime = calendarEnd.getTime();
        return platformStatisticsService.getPlatformstatistics(startTime, endTime);
    }
}