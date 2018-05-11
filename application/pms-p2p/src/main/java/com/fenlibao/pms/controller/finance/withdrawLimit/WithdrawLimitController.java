package com.fenlibao.pms.controller.finance.withdrawLimit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.finance.WithdrawLimit;
import com.fenlibao.model.pms.da.finance.form.WithdrawLimitForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.account.UserDetailInfoService;
import com.fenlibao.service.pms.da.finance.withdrawLimit.WithdrawLimitService;

@RestController  
@RequestMapping("finance/withdrawLimit")
public class WithdrawLimitController {
	private static final Logger LOG = LoggerFactory.getLogger(WithdrawLimitController.class);

    @Resource
    private WithdrawLimitService withdrawLimitService;
    
    @Resource
	private UserDetailInfoService userDetailInfoService;

    /**
     * 用户提现限制查询列表
     * @param page
     * @param limit
     * @param withdrawLimitForm
     * @return
     */
    @RequiresPermissions("withdrawLimit:search")
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                WithdrawLimitForm withdrawLimitForm) {
        RowBounds bounds = new RowBounds(page, limit);
        String startTimeStr = withdrawLimitForm.getStartTime();
        String endTimeStr = withdrawLimitForm.getEndTime();
        Date startDate;
        Date endDate;
        if (StringUtils.isEmpty(startTimeStr)) {
			//昨天
			String provMonthFirstDayStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));//
			withdrawLimitForm.setStartTime(provMonthFirstDayStr);
			startDate = DateUtil.StringToDate(provMonthFirstDayStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		} else {
			startDate = DateUtil.StringToDate(startTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		}
		if (StringUtils.isEmpty(endTimeStr)) {
			//昨天
			String provMonthLastDayStr = DateUtil.getDate(DateUtil.nowDate());
			withdrawLimitForm.setEndTime(provMonthLastDayStr);
			endDate = DateUtil.StringToDate(provMonthLastDayStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		} else {
			endDate = DateUtil.StringToDate(endTimeStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}
        List<WithdrawLimit> withdrawLimitList = withdrawLimitService.getWithdrawLimitList(withdrawLimitForm, bounds);
        PageInfo<WithdrawLimit> paginator = new PageInfo<>(withdrawLimitList);
        ModelAndView view = new ModelAndView("finance/withdrawLimit/index");
        view.addObject("list", withdrawLimitList);
        view.addObject("withdrawLimitForm", withdrawLimitForm);
        view.addObject("paginator", paginator);
        return view;
    }
    
   /**
    * 用户信息查询
    * @param withdrawLimitForm
    * @param page
    * @param limit
    * @return
    */
    @RequiresPermissions("withdrawLimit:searchUser")
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ModelAndView userDetailList(
    		WithdrawLimit withdrawLimit,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<UserDetail> userDetailList = new ArrayList<>();
        String msg = null;
        try {
        		if ((withdrawLimit.getPhoneNum() != null && withdrawLimit.getPhoneNum() != "")  ) {
		        	userDetailList = userDetailInfoService.getUserDetail(withdrawLimit.getPhoneNum().trim(),null,
		        			null, bounds);
		        	if (userDetailList.size() == 0) {
						msg = "此用户不存在";
					}
				}
		} catch (Exception e) {
			LOG.error("用户信息->用户详细信息查询异常：", e);
			msg = "查询失败，内部错误";
		}
        return new ModelAndView("finance/withdrawLimit/index")
                .addObject("userDetailList", userDetailList)//用户手机号唯一
                .addObject("withdrawLimit", withdrawLimit)
                .addObject("msg", msg);
    }
    
	@RequiresPermissions("withdrawLimit:save")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> replace(
                                @RequestParam(required = true) String telPhone,
                                @RequestParam(required = true) BigDecimal limitMoney,
                                @RequestParam(required = true) int userId,
                                @RequestParam(required = true) String remark) {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        paramMap.put("phoneNum",telPhone.trim());
        try {
        	int id = withdrawLimitService.insertIntoUserWithdrawLimit(userId, limitMoney, remark);
            resultMap.put("doCode","1000");
        }catch (Exception e){
            resultMap.put("doCode","1001");
        }
        return resultMap;
    }
    
    @RequiresPermissions("withdrawLimit:export")
    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response,WithdrawLimitForm withdrawLimitForm) {
        RowBounds bounds = new RowBounds();
        List<WithdrawLimit> withdrawLimitList = withdrawLimitService.getWithdrawLimitList(withdrawLimitForm, bounds);

        List<WithdrawLimitFormExport> withdrawLimitFormExport = new ArrayList<>();
        for (WithdrawLimit withdrawLimit : withdrawLimitList) {
        	WithdrawLimitFormExport rp = new WithdrawLimitFormExport();
            rp.phoneNum = withdrawLimit.getPhoneNum();
            rp.operatorTime = DateUtil.getDateTime(withdrawLimit.getCreatTime());
            rp.operator = withdrawLimit.getOperator();
            rp.limitMoney = withdrawLimit.getLimitMoney();
            withdrawLimitFormExport.add(rp);
        }
        String headers[] = {"用户手机号", "限制提现金额", "操作日期" ,"操作人"};
        String fieldNames[] = {"phoneNum", "limitMoney", "operatorTime", "operator"};
        POIUtil.export(response, headers, fieldNames, withdrawLimitFormExport);
    }

    private class WithdrawLimitFormExport{
        String operatorTime;
        String operator;
        String phoneNum;
        BigDecimal limitMoney;
    }
}
