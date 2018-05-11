package com.fenlibao.pms.controller.statistics.authentication;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationForm;
import com.fenlibao.model.pms.da.statistics.authentication.AuthenticationInfo;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.authentication.AuthenticationService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有盾实名认证统计管理
 */
@Controller
@RequestMapping("statistics/authentication")
public class AuthenticationController {

	@Resource
	private AuthenticationService authenticationService;

	@RequiresPermissions("statisticsAuth:view")
	@RequestMapping
	public ModelAndView authenticationList(AuthenticationForm authenticationForm,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
			) {

		Map<String, Object> paramMap = new HashMap<>();
			// 默认昨天
			Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
			String yesterdayDateStr = DateUtil.getDate(yesterdayDate);
			if (StringUtils.isEmpty(authenticationForm.getBeginDate())) {
				authenticationForm.setBeginDate(yesterdayDateStr);
				String startDate = yesterdayDateStr + " 00:00:00";
				paramMap.put("beginDate",startDate);
			}else {
				String startDate = authenticationForm.getBeginDate() + " 00:00:00";
				paramMap.put("beginDate",startDate);
			}
			if (StringUtils.isEmpty(authenticationForm.getEndDate())) {
				authenticationForm.setEndDate(yesterdayDateStr);
				String endDate = yesterdayDateStr + " 23:59:59";
				paramMap.put("endDate",endDate);
			}else {
				String endDate = authenticationForm.getEndDate() + " 23:59:59";
				paramMap.put("endDate",endDate);
			}
		RowBounds bounds = new RowBounds(page, limit);
		List<AuthenticationInfo> authList = authenticationService.getAuthenticationList(paramMap, bounds);
		PageInfo<AuthenticationInfo> paginator = new PageInfo<>(authList);
		AuthenticationInfo authTotal = authenticationService.getAuthenticationTotal(paramMap);
		return new ModelAndView("statistics/authentication/index").addObject("authList",authList)
				.addObject("authTotal",authTotal).addObject("auth", authenticationForm).addObject("paginator", paginator);
	}
}
