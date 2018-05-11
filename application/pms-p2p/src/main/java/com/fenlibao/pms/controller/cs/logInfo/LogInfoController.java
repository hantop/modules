package com.fenlibao.pms.controller.cs.logInfo;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.cs.LogInfo;
import com.fenlibao.model.pms.da.cs.form.LogInfoForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.logInfo.LogInfoService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("cs/logInfo")
public class LogInfoController {
	private static final Logger LOG = LoggerFactory.getLogger(LogInfoController.class);

	@Resource
	private LogInfoService logInfoService;

	@RequestMapping
	public ModelAndView index() {
		return new ModelAndView("cs/logInfo/index").addObject("logInfoForm", new LogInfoForm());
	}

	@RequestMapping(value = "/getDetailInfo")
	public ModelAndView getDetailInfo(LogInfoForm logInfoForm,
									  @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
									  @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		RowBounds bounds = new RowBounds(page, limit);
		if(logInfoForm.getConductType() == 0){
			logInfoForm.setConductType(1);// 默认查询登录/登出tab
		}
		List<LogInfo> logInfoList = logInfoService.getLogInfoList(logInfoForm, bounds);
		PageInfo<LogInfo> paginator = new PageInfo<>(logInfoList);
		return new ModelAndView("cs/logInfo/logInfo")
				.addObject("logInfoForm", logInfoForm)
				.addObject("getLoginInfo", logInfoForm.getConductType())
				.addObject("paginator", paginator)
				.addObject("logInfoList", logInfoList);
	}
}

