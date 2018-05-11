package com.fenlibao.pms.controller.statistics.invest;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.KV;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.model.pms.da.statistics.invest.InvestForm;
import com.fenlibao.model.pms.da.statistics.invest.InvestInfo;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.common.base.EnumService;
import com.fenlibao.service.pms.common.producttype.ProductTypeService;
import com.fenlibao.service.pms.da.statistics.invest.InvestService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 统计管理 用户信息 充值和投资 Created by Louis Wang on 2015/11/6.
 */
@Controller
@RequestMapping("statistics/invest")
public class InvestController {
    @Resource
    private InvestService investService;
    @Resource
    private ProductTypeService productTypeService;
    @Resource
    private EnumService enumService;

    @RequiresPermissions("statisticsInvest:view")
    @RequestMapping
    public ModelAndView statisticslist(InvestForm investForm,
                                       @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                       @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        // 是否首投
        boolean isFirstInvest = false;
        if (investForm.getInvestStartTime() == null) {
            // 默认日期为昨天
            investForm.setInvestStartTime(DateUtil.startTime(DateUtil.dateAdd(new Date(), -1)));
        }
        if (investForm.getInvestEndTime() == null) {
            // 默认日期为昨天
            investForm.setInvestEndTime(DateUtil.endTime(DateUtil.dateAdd(new Date(), -1)));
        } else {
            investForm.setInvestEndTime(DateUtil.endTime(investForm.getInvestEndTime()));
        }
        if (investForm.getRegEndTime() != null) {
            investForm.setRegEndTime(DateUtil.endTime(investForm.getRegEndTime()));
        }
        if (investForm.getFirstInvestStr() != null) {
            isFirstInvest = true;
        }
        investForm.setFirstInvest(isFirstInvest);
        // 根据条件查询数据类型
        List<InvestInfo> list = investService.getInvestStatistics(investForm, bounds);
        PageInfo<InvestInfo> paginator = new PageInfo<>(list);
        InvestInfo investTotal = investService.getInvestTotal(investForm);
        // 产品类型
        List<KV<String, String>> productTypes = productTypeService.getProductTypes();
        Iterator<KV<String, String>> iter = productTypes.iterator();
        while(iter.hasNext()){
            KV<String, String> kv = iter.next();
            if(kv.getValue().equals("计划")){
                iter.remove();
            }
        }
        List<Map<String, String>> productTypeExtend = investService.getProductTypeExtend();
        for (Map<String, String> map: productTypeExtend) {
            KV<String, String> planProductType = new KV<>();
            planProductType.setKey(map.get("key"));
            planProductType.setValue(map.get("value"));
            if(!productTypes.contains(planProductType)){
                productTypes.add(planProductType);
            }
        }
        // 标的类型
        List<TEnum> bidTypes = enumService.getEnum("bid", "bid_type");
        // 标的状态
        List<TEnum> statuses = enumService.getEnum("bid", "bid_status");

        return new ModelAndView("statistics/invest/index")
                .addObject("list", list)
                .addObject("productTypes", productTypes)
                .addObject("bidTypes", bidTypes)
                .addObject("statuses", statuses)
                .addObject("paginator", paginator)
                .addObject("investTotal", investTotal)
                .addObject("invest", investForm);
    }

    @RequiresPermissions("statisticsInvest:export")
    @RequestMapping(value = "export")
    public void statisticslist(HttpServletResponse response, InvestForm investForm, @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        // 是否首投
        boolean isFirstInvest = false;
        if (investForm.getInvestStartTime() == null) {
            // 默认日期为昨天
            investForm.setInvestStartTime(DateUtil.startTime(DateUtil.dateAdd(new Date(), -1)));
        }
        if (investForm.getInvestEndTime() == null) {
            // 默认日期为昨天
            investForm.setInvestEndTime(DateUtil.endTime(DateUtil.dateAdd(new Date(), -1)));
        } else {
            investForm.setInvestEndTime(DateUtil.endTime(investForm.getInvestEndTime()));
        }
        if (investForm.getRegEndTime() != null) {
            investForm.setRegEndTime(DateUtil.endTime(investForm.getRegEndTime()));
        }
        if (investForm.getFirstInvestStr() != null) {
            isFirstInvest = true;
        }
        investForm.setFirstInvest(isFirstInvest);
        RowBounds bounds = new RowBounds(0, limit);
        // 标的类型
        List<TEnum> bidTypes = enumService.getEnum("bid", "bid_type");
        // 标的状态
        List<TEnum> statuses = enumService.getEnum("bid", "bid_status");
        Map<String, String> bidTypeMap = new HashMap<>();
        Map<String, String> statusMap = new HashMap<>();
        if (bidTypes != null && bidTypes.size() > 0) {
            for (TEnum t : bidTypes) {
                bidTypeMap.put(t.getEnumKey(), t.getEnumValue());
            }
        }
        if (statuses != null && statuses.size() > 0) {
            for (TEnum t : statuses) {
                statusMap.put(t.getEnumKey(), t.getEnumValue());
            }
        }

        List<InvestInfo> list = investService.getInvestStatistics(investForm, bounds);
        List<InvestInfoExport> exports = new ArrayList<>();
        for (InvestInfo investInfo : list) {
            InvestInfoExport investInfoExport = new InvestInfoExport();
            BeanUtils.copyProperties(investInfo, investInfoExport);
            if (bidTypeMap.containsKey(investInfoExport.getBidType())) {
                investInfoExport.setBidType(bidTypeMap.get(investInfoExport.getBidType()));
            }
            if (statusMap.containsKey(investInfoExport.getStatus())) {
                investInfoExport.setStatus(statusMap.get(investInfoExport.getStatus()));
            }
            exports.add(investInfoExport);
        }

        String headers[] = {"手机号码", "投资日期", "注册日期", "姓名", "渠道来源", "投资金额", "累计投资金额", "累计投资次数", "账户余额", "产品类型", "标的类型", "投资期限", "状态"};
        String fieldNames[] = {"phoneNum", "createTime", "regtime", "realName", "channelName", "investMoney", "investTotalMoney", "investCount", "surplusMoney",
                "productType", "bidType", "limitTime", "status"};
        POIUtil.export(response, headers, fieldNames, exports, "投资统计");
    }

    private class InvestInfoExport {
        String phoneNum;
        String regtime;
        String channelName;
        String realName;
        BigDecimal investMoney;
        BigDecimal investTotalMoney;
        Integer investCount;
        BigDecimal surplusMoney;
        String investType;
        String createTime;
        String limitTime;
        /**
         * 产品类型
         */
        private String productType;

        /**
         * 标的类型
         */
        private String bidType;

        /**
         * 标的状态
         */
        private String status;

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getRegtime() {
            return regtime;
        }

        public void setRegtime(String regtime) {
            this.regtime = regtime;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public BigDecimal getInvestMoney() {
            return investMoney;
        }

        public void setInvestMoney(BigDecimal investMoney) {
            this.investMoney = investMoney;
        }

        public BigDecimal getInvestTotalMoney() {
            return investTotalMoney;
        }

        public void setInvestTotalMoney(BigDecimal investTotalMoney) {
            this.investTotalMoney = investTotalMoney;
        }

        public Integer getInvestCount() {
            return investCount;
        }

        public void setInvestCount(Integer investCount) {
            this.investCount = investCount;
        }

        public BigDecimal getSurplusMoney() {
            return surplusMoney;
        }

        public void setSurplusMoney(BigDecimal surplusMoney) {
            this.surplusMoney = surplusMoney;
        }

        public String getInvestType() {
            return investType;
        }

        public void setInvestType(String investType) {
            this.investType = investType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getBidType() {
            return bidType;
        }

        public void setBidType(String bidType) {
            this.bidType = bidType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(String limitTime) {
            this.limitTime = limitTime;
        }
    }

    /**
     * @param page   当前页数
     * @param limit  条数
     * @param userId 用户id
     * @return
     * @author wangyunjing
     * @Date 20151201
     * @todo 用户统计信息的推广明细详情
     */
    @RequiresPermissions("statisticsInvest:details")
    @RequestMapping(value = "details", method = RequestMethod.GET)
    public ModelAndView statisticsDetails(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            String userId) {

        RowBounds bounds = new RowBounds(page, limit);

        // 查询推广用户的相关信息
        InvestInfo userInvest = investService.getUserInvestDetails(userId);
        // 查询被推广用户的相关信息
        List<InvestInfo> userRankList = investService.getUserRankingDetails(userId, bounds);
        PageInfo<InvestInfo> paginator = new PageInfo<>(userRankList);
        return new ModelAndView("statistics/invest/details").addObject("paginator", paginator)
                .addObject("userInvest", userInvest).addObject("userRankList", userRankList);
    }

}
