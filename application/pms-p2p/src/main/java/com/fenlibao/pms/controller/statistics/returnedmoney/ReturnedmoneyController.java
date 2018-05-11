package com.fenlibao.pms.controller.statistics.returnedmoney;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.statistics.returnedmoney.ReturnedmoneyInfo;
import com.fenlibao.model.pms.da.statistics.returnedmoney.UserRefundStatus;
import com.fenlibao.model.pms.da.statistics.returnedmoney.form.ReturnedmoneyInfoForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.returnedmoney.ReturnedmoneyService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 回款信息
 */
@Controller
@RequestMapping("statistics/returnedmoney")
public class ReturnedmoneyController {

    @Resource
    private ReturnedmoneyService returnedmoneyService;

    @RequiresPermissions("statisticsReturnedmoney:view")
    @RequestMapping
    public ModelAndView returnedmoneyList(
            ReturnedmoneyInfoForm returnedmoneyInfoForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        int totalPerson = 0;
        List<ReturnedmoneyInfo> returnedmoneyList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        String startDateStr = returnedmoneyInfoForm.getStartDate();
        String endDateStr = returnedmoneyInfoForm.getEndDate();
        // 是否首次回款
        boolean isFirstReturnedmoney = false;
        // 回款状态
        Integer status = returnedmoneyInfoForm.getStatus();
        // 当前端checkbox选中时不为null
        if (returnedmoneyInfoForm.getFirstReturnedmoney() != null) {
            isFirstReturnedmoney = true;
        }
        if (StringUtils.isEmpty(startDateStr)) {
            String defaultDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -7));
            returnedmoneyInfoForm.setStartDate(defaultDateStr);
            startDate = DateUtil.StringToDate(defaultDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if (StringUtils.isEmpty(endDateStr)) {
            String yesterdayDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
            returnedmoneyInfoForm.setEndDate(yesterdayDateStr);
            endDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        // 首次回款
        if (isFirstReturnedmoney) {
            List<Integer> receivableUsers = null;
            List<Integer> hasReceivableUsers = null;
            // 用户回款状态
            List<UserRefundStatus> userReturnTypes = returnedmoneyService.getUserReturnTypes(startDate, endDate);
            // 未回款的用户
            // 用户ID，还款记录ID
            Map<Integer, Integer> receivableMap = new HashMap<>();
            // 已回款的用户
            // 用户ID，还款记录ID
            Map<Integer, Integer> hasReceivableMap = new HashMap<>();
            Integer userId;
            String refundStatus;
            Integer refundId;
            // 取出用户的首次回款，
            // 如果用户有已回款，则不再取未回款
            for (UserRefundStatus s : userReturnTypes) {
                userId = s.getUserId();
                refundStatus = s.getRefundStatus();
                refundId = s.getRefundId();
                if (refundStatus.equals("YH")) {
                    hasReceivableMap.put(userId, refundId);
                } else if(refundStatus.equals("WH") && !hasReceivableMap.containsKey(userId)) {
                    receivableMap.put(userId, refundId);
                }
            }
            // 按用户ID获取首次回款列表
            if (receivableMap.size() > 0) {
                receivableUsers = new ArrayList<>(receivableMap.values());
            }
            if (hasReceivableMap.size() > 0) {
                hasReceivableUsers = new ArrayList<>(hasReceivableMap.values());
            }
            if (receivableMap.size() > 0 || hasReceivableMap.size() > 0) {
                // 按还款记录ID获取首次回款列表
                returnedmoneyList = returnedmoneyService.getFirstReturnedmoneys(
                        receivableUsers, hasReceivableUsers, startDate, endDate, bounds);
                // 总人数
                totalPerson = returnedmoneyService.getFirstReturnedmoneysTotal(receivableUsers, hasReceivableUsers, startDate, endDate);
            }
        } else {
            returnedmoneyList = returnedmoneyService.getReturnedmoneys(
                    isFirstReturnedmoney, status, startDate, endDate, bounds);
            // 总人数
            totalPerson = returnedmoneyService.getReturnedmoneysTotal(isFirstReturnedmoney, status, startDate, endDate);
        }
        PageInfo<ReturnedmoneyInfo> paginator = new PageInfo<>(returnedmoneyList);
        return new ModelAndView("statistics/returnedmoney/index").addObject("returnedmoneyList", returnedmoneyList)
                .addObject("returnedmoneyInfoForm", returnedmoneyInfoForm).addObject("paginator", paginator)
                .addObject("totalPerson", totalPerson);
    }


    @RequiresPermissions("statisticsReturnedmoney:export")
    @RequestMapping("/export")
    public void returnedmoneyList(ReturnedmoneyInfoForm returnedmoneyInfoForm, HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        List<ReturnedmoneyInfo> returnedmoneys = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        String startDateStr = returnedmoneyInfoForm.getStartDate();
        String endDateStr = returnedmoneyInfoForm.getEndDate();
        // 是否首次回款
        boolean isFirstReturnedmoney = false;
        // 回款状态
        Integer status = returnedmoneyInfoForm.getStatus();
        // 当前端checkbox选中时不为null
        if (returnedmoneyInfoForm.getFirstReturnedmoney() != null) {
            isFirstReturnedmoney = true;
        }
        if (StringUtils.isEmpty(startDateStr)) {
            String defaultDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -7));
            returnedmoneyInfoForm.setStartDate(defaultDateStr);
            startDate = DateUtil.StringToDate(defaultDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if (StringUtils.isEmpty(endDateStr)) {
            String yesterdayDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
            returnedmoneyInfoForm.setEndDate(yesterdayDateStr);
            endDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        // 首次回款
        if (isFirstReturnedmoney) {
            List<Integer> receivableUsers = null;
            List<Integer> hasReceivableUsers = null;
            // 用户回款状态
            List<UserRefundStatus> userReturnTypes = returnedmoneyService.getUserReturnTypes(startDate, endDate);
            // 未回款的用户
            // 用户ID，还款记录ID
            Map<Integer, Integer> receivableMap = new HashMap<>();
            // 已回款的用户
            // 用户ID，还款记录ID
            Map<Integer, Integer> hasReceivableMap = new HashMap<>();
            Integer userId;
            String refundStatus;
            Integer refundId;
            // 取出用户的首次回款，
            // 如果用户有已回款，则不再取未回款
            for (UserRefundStatus s : userReturnTypes) {
                userId = s.getUserId();
                refundStatus = s.getRefundStatus();
                refundId = s.getRefundId();
                if (refundStatus.equals("YH")) {
                    hasReceivableMap.put(userId, refundId);
                } else if(refundStatus.equals("WH") && !hasReceivableMap.containsKey(userId)) {
                    receivableMap.put(userId, refundId);
                }
            }
            // 按用户ID获取首次回款列表
            if (receivableMap.size() > 0) {
                receivableUsers = new ArrayList<>(receivableMap.values());
            }
            if (hasReceivableMap.size() > 0) {
                hasReceivableUsers = new ArrayList<>(hasReceivableMap.values());
            }
            if (receivableMap.size() > 0 || hasReceivableMap.size() > 0) {
                // 按还款记录ID获取首次回款列表
                returnedmoneys = returnedmoneyService.getFirstReturnedmoneys(
                        receivableUsers, hasReceivableUsers, startDate, endDate, bounds);
            }
        } else {
            returnedmoneys = returnedmoneyService.getReturnedmoneys(
                    isFirstReturnedmoney, status, startDate, endDate, bounds);
        }
        List<ReturnedmoneyInfoExport> returnedmoneyInfoExports = new ArrayList<>();
        ReturnedmoneyInfoExport e;
        for (ReturnedmoneyInfo item : returnedmoneys) {
            e = new ReturnedmoneyInfoExport();
            e.phonenum = item.getPhonenum();
            e.realname = item.getRealname();
            e.loanTitle = item.getLoanTitle();
            if (item.getLoanDeadlineType() == 1) {
                e.loanDeadline = item.getLoanDeadline() + "天";
            } else {
                e.loanDeadline = item.getLoanDeadline() + "月";
            }
            e.principal = item.getPrincipal();
            e.investingAmount = item.getInvestingAmount();
            e.balance = item.getBalance();
            e.shouldReturnDate = DateUtil.getDate(item.getShouldReturnDate());
            if (item.getStatus() == 0) {
                e.status = "全部";
            } else if (item.getStatus() == 1) {
                e.status = "已还款";
            } else if (item.getStatus() == 2) {
                e.status = "未还款";
            }
            returnedmoneyInfoExports.add(e);
        }
        String headers[] = {"手机号码", "姓名", "借款标题", "借款期限", "回款日期", "回款本金", "在投金额", "账户余额", "回款状态"};
        String fieldNames[] = {"phonenum", "realname", "loanTitle", "loanDeadline", "shouldReturnDate", "principal", "investingAmount", "balance", "status"};
        POIUtil.export(response, headers, fieldNames, returnedmoneyInfoExports, "回款信息");
    }


    private class ReturnedmoneyInfoExport {
        public String phonenum;// 投资人手机号
        public String realname;// 投资人姓名
        public String loanTitle;// 借款标题
        public String loanDeadline;// 借款期限
        public BigDecimal principal;// 回款本金
        public BigDecimal investingAmount;// 在投金额
        public BigDecimal balance;// 余额，往来账户余额
        public String shouldReturnDate;// 回款日期
        public String status;// 回款状态 0:全部 1:已回款 2:未回款
    }

}
