package com.fenlibao.pms.controller.channel.origin;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.channel.ChannelUserStatistics;
import com.fenlibao.model.pms.da.channel.InvestStatistics;
import com.fenlibao.model.pms.da.channel.RechargeStatistics;
import com.fenlibao.model.pms.da.channel.RedpacketStatistics;
import com.fenlibao.model.pms.da.channel.form.ChannelDetailForm;
import com.fenlibao.model.pms.da.channel.form.ChannelStatisticsForm;
import com.fenlibao.model.pms.da.channel.vo.ChannelStatisticsVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.channel.channel.ChannelService;
import com.fenlibao.service.pms.da.channel.origin.ChannelStatisticsService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 渠道统计
 * Created by chenzhixuan on 2015/11/12.
 */
@Controller
@RequestMapping("channel/origin")
public class ChannelStatisticsController {
    @Resource
    private ChannelStatisticsService channelStatisticsService;
    @Resource
    private ChannelService channelService;

    private static final Logger LOG = LoggerFactory.getLogger(ChannelStatisticsController.class);

    @RequiresPermissions("channelStatisticsDetail:view")
    @RequestMapping(value = "detail")
    public ModelAndView detail(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                               @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                               ChannelDetailForm channelDetailForm) {
        ModelAndView modelAndView = new ModelAndView("channel/origin/detail");
        RowBounds bounds = new RowBounds(page, limit);
        String channelId = channelDetailForm.getChannelId();
        String startDateStr = channelDetailForm.getStartDate();
        String endDateStr = channelDetailForm.getEndDate();
        BigDecimal minimumMoney = channelDetailForm.getMinimumMoney();
        BigDecimal maximumMoney = channelDetailForm.getMaximumMoney();

        Date startDate = null;
        Date endDate = null;
        if(!StringUtils.isEmpty(startDateStr)){
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)){
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        List<ChannelUserStatistics> channelUserStatisticses = channelStatisticsService.getChannelUserStatisticses(minimumMoney, maximumMoney, channelId, startDate, endDate, bounds);
        /*// 身份证解密
        for (ChannelUserStatistics c : channelUserStatisticses) {
            try {
                c.setIdCard(StringHelper.decode(c.getIdCardEncrypt()));
            } catch (Throwable t) {
                logger.error("[渠道用户详情解密身份证号码异常:]" + t.getMessage(), t);
            }
        }*/
        PageInfo<ChannelUserStatistics> paginator = new PageInfo<>(channelUserStatisticses);
        // 获取渠道对应的用户总计
        ChannelUserStatistics channelUserTotal = channelStatisticsService.getChannelUserTotal(minimumMoney, maximumMoney, channelId, startDate, endDate);
        if(channelUserTotal != null) {
            modelAndView
                    .addObject("rechargeSum", channelUserTotal.getRechargeSum())
                    .addObject("investCount", channelUserTotal.getInvestCount())
                    .addObject("investSum", channelUserTotal.getInvestSum())
                    .addObject("activeRedpacketSum", channelUserTotal.getActiveRedpacketSum());
        }
        return modelAndView
                .addObject("channelUserStatisticses", channelUserStatisticses)
                .addObject("paginator", paginator)
                .addObject("channelDetailForm", channelDetailForm);
    }
    
    @RequiresPermissions("channelStatisticsDetail:export")
    @RequestMapping(value = "detail/export")
    public void detailExport(HttpServletResponse response,ChannelDetailForm channelDetailForm) {
    	RowBounds bounds = new RowBounds();
    	String channelId = channelDetailForm.getChannelId();
    	String startDateStr = channelDetailForm.getStartDate();
    	String endDateStr = channelDetailForm.getEndDate();
        BigDecimal minimumMoney = channelDetailForm.getMinimumMoney();
        BigDecimal maximumMoney = channelDetailForm.getMaximumMoney();
        Date startDate = null;
    	Date endDate = null;
        if(!StringUtils.isEmpty(startDateStr)){
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)){
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
    	List<ChannelUserStatistics> channelUserStatisticses = channelStatisticsService.getChannelUserStatisticses(minimumMoney, maximumMoney, channelId, startDate, endDate, bounds);
        
    	List<ChannelDetailExportObject> list=new ArrayList<ChannelDetailExportObject>();
    	for (ChannelUserStatistics c : channelUserStatisticses) {
    		ChannelDetailExportObject item=new ChannelDetailExportObject();
    		item.phoneNum=c.getPhoneNum();
    		item.nickname=c.getNickname();
    		item.name=c.getName();
    		item.isBindBankcard=c.getIsBindBankcard()?"是":"否";
    		item.isAuth=c.getIsAuth()?"是":"否";
            item.idCard = c.getIdCard();
            /*try {
            	item.idCard=StringHelper.decode(c.getIdCardEncrypt());
            } catch (Throwable t) {
                logger.error("[渠道用户详情解密身份证号码异常:]" + t.getMessage(), t);
            }*/
            item.rechargeSum=c.getRechargeSum()==null?"":""+c.getRechargeSum();
            item.investCount=""+c.getInvestCount();
            item.investSum=c.getInvestSum()==null?"":""+c.getInvestSum();
            item.activeRedpacketSum=c.getActiveRedpacketSum()==null?"":""+c.getActiveRedpacketSum();
            list.add(item);
        }
    	String headers[]={"手机号码","用户名","姓名","是否绑卡","实名认证","身份证号码","充值金额","投资次数","投资金额","激活红包金额"};
    	String fieldNames[]={"phoneNum","nickname","name","isBindBankcard","isAuth","idCard","rechargeSum","investCount","investSum","activeRedpacketSum"};
    	POIUtil.export(response, headers,fieldNames, list);
    }
    
    private class ChannelDetailExportObject{
    	public String phoneNum;
    	public String nickname;
    	public String name;
    	public String isBindBankcard;
    	public String isAuth;
    	public String idCard;
    	public String rechargeSum;
    	public String investCount;
    	public String investSum;
    	public String activeRedpacketSum;
    } 

    @RequiresPermissions("channelStatistics:view")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView channelStatisticsList(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            ChannelStatisticsForm channelStatisticsForm) {
        ModelAndView modelAndView = new ModelAndView("channel/origin/index");
        RowBounds bounds = new RowBounds(page, limit);
        String firstChannelId = channelStatisticsForm.getFirstChannelId();
        String secondChannelId = channelStatisticsForm.getSecondChannelId();
        String startDateStr = channelStatisticsForm.getStartDate();
        String endDateStr = channelStatisticsForm.getEndDate();
        String channelName = channelStatisticsForm.getChannelName();
        String channelCode = channelStatisticsForm.getChannelCode();
        Date startDate;
        Date endDate;
        if(StringUtils.isEmpty(startDateStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            channelStatisticsForm.setStartDate(todayDateStr);
            startDate = DateUtil.StringToDate(todayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isEmpty(endDateStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            channelStatisticsForm.setEndDate(todayDateStr);
            endDate = DateUtil.StringToDate(todayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        // 获取渠道统计数据
        List<ChannelStatisticsVO> channelStatisticses = channelStatisticsService.getSortChannelStatisticses(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode, bounds);
        // 注册总计
        BigDecimal registerTotal = channelStatisticsService.getRegisterTotal(firstChannelId, secondChannelId, channelName, channelCode, startDate, endDate);
        // 实名认证总计
        BigDecimal authTotal = channelStatisticsService.getAuthTotal(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode);
        // 充值总计
        RechargeStatistics rechargeTotal = channelStatisticsService.getRechargeTotal(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode);
        // 投资总计
        InvestStatistics investTotal = channelStatisticsService.getInvestTotal(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode);
        // 激活红包总计
        RedpacketStatistics activeRedpacketTotal = channelStatisticsService.getActiveRedpacketTotal(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode);
        PageInfo<ChannelStatisticsVO> paginator = new PageInfo<>(channelStatisticses);
        return modelAndView
                .addObject("channelStatisticses", channelStatisticses)
                .addObject("paginator", paginator)
                .addObject("channelStatisticsForm", channelStatisticsForm)
                .addObject("registerCountTotal", registerTotal)
                .addObject("authCountTotal", authTotal)
                .addObject("rechargeCountTotal", rechargeTotal.getRechargeCount())
                .addObject("rechargeSumTotal", rechargeTotal.getRechargeSum())
                .addObject("investCountTotal", investTotal.getInvestCount())
                .addObject("investSumTotal", investTotal.getInvestSum())
                .addObject("activeRedpacketCountTotal", activeRedpacketTotal.getActiveRedpacketCount())
                .addObject("activeRedpacketSumTotal", activeRedpacketTotal.getActiveRedpacketSum())
                ;
    }

    @RequiresPermissions("channelStatistics:export")
    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void channelStatisticsListExport(HttpServletResponse response,ChannelStatisticsForm channelStatisticsForm) {
    	RowBounds bounds = new RowBounds();
    	String firstChannelId = channelStatisticsForm.getFirstChannelId();
    	String secondChannelId = channelStatisticsForm.getSecondChannelId();
        String startDateStr = channelStatisticsForm.getStartDate();
        String endDateStr = channelStatisticsForm.getEndDate();
        String channelName = channelStatisticsForm.getChannelName();
        String channelCode = channelStatisticsForm.getChannelCode();
        Date startDate = null;
        Date endDate = null;
        if(!StringUtils.isEmpty(startDateStr)){
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)){
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        // 获取渠道统计数据
        List<ChannelStatisticsVO> channelStatisticses = channelStatisticsService.getSortChannelStatisticses(firstChannelId, secondChannelId, startDate, endDate, channelName, channelCode, bounds);
        if(channelStatisticses != null && channelStatisticses.size() > 0) {
            List<ChannelStatisticsExportObject> exportList=new ArrayList<ChannelStatisticsExportObject>();
            for(int i = 0;i < channelStatisticses.size(); i++){
                ChannelStatisticsExportObject item=new ChannelStatisticsExportObject();
                item.channelName=channelStatisticses.get(i).getName()==null?"":channelStatisticses.get(i).getName();
                item.channelCode=channelStatisticses.get(i).getCode()==null?"":channelStatisticses.get(i).getCode();
                item.registerCount=""+channelStatisticses.get(i).getRegisterCount();
                item.authCount=""+channelStatisticses.get(i).getAuthCount();
                item.rechargeCount=""+channelStatisticses.get(i).getRechargeCount();
                item.investCount=""+channelStatisticses.get(i).getInvestCount();
                item.rechargeSum=channelStatisticses.get(i).getRechargeSum()==null?"":""+channelStatisticses.get(i).getRechargeSum();
                item.investSum=channelStatisticses.get(i).getInvestSum()==null?"":""+channelStatisticses.get(i).getInvestSum();
                item.activeRedpacketCount=""+channelStatisticses.get(i).getActiveRedpacketCount();
                item.activeRedpacketSum=channelStatisticses.get(i).getActiveRedpacketSum()==null?"":""+channelStatisticses.get(i).getActiveRedpacketSum();
                exportList.add(item);
            }
            String headers[]={"渠道来源","渠道编号","注册人数","实名验证","充值人数","投资人数","充值金额","投资金额","激活红包人数","激活红包金额"};
            String fieldNames[]={"channelName", "channelCode","registerCount","authCount","rechargeCount","investCount","rechargeSum","investSum","activeRedpacketCount","activeRedpacketSum"};
            POIUtil.export(response, headers,fieldNames, exportList);
        }
    }

    private class ChannelStatisticsExportObject{
    	public String channelName;
    	public String channelCode;
    	public String registerCount;
    	public String authCount;
    	public String rechargeCount;
    	public String investCount;
    	public String rechargeSum;
    	public String investSum;
    	public String activeRedpacketCount;
    	public String activeRedpacketSum;
    }
}
