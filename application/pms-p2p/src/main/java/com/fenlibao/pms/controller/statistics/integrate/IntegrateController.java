package com.fenlibao.pms.controller.statistics.integrate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.statistics.integrate.Integrate;
import com.fenlibao.model.pms.da.statistics.integrate.form.IntegrateForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.statistics.integrate.IntegrateService;

@Controller
@RequestMapping("statistics/integrate")
public class IntegrateController {

	@Resource
	private IntegrateService integrateService;

	/**
	 * 积分列表
	 * 
	 * @return
	 */
	@RequiresPermissions("statisticsIntegrate:view")
	@RequestMapping
	public ModelAndView integrateList(IntegrateForm integrateForm, String cmd,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		RowBounds bounds = new RowBounds(page, limit);
		Date startDate = null;
		Date endDate = null;
		List<Integrate> integrateList = new ArrayList<>();
		// ==============================
		if (cmd == null || cmd.equals("DESC")) {
			cmd = "DESC";
			integrateForm.setCmd(cmd);
		} else {
			cmd = "ASC";
		}
		// 积分的开始和结束时间(一开始进入页面的时候就会出现统计数据,这里的默认搜索一定是会带条件的)
		String startDateStr = integrateForm.getStartDate();
		String endDateStr = integrateForm.getEndDate();
		String defaultDateStr = null;
		String yesterdayDateStr = null;
		if (StringUtils.isEmpty(startDateStr)) {
			// 默认是昨天
			defaultDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
			integrateForm.setStartDate(defaultDateStr);
			startDate = DateUtil.StringToDate(defaultDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		} else {
			startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		}
		if (StringUtils.isEmpty(endDateStr)) {
			yesterdayDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
			integrateForm.setEndDate(yesterdayDateStr);
			endDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		} else {
			endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}
		if (integrateForm != null) {
			if (!StringUtils.hasLength(integrateForm.getProductCode())) {
				integrateForm.setProductCode("");
			}
			if (!StringUtils.hasLength(integrateForm.getTypeName())) {
				integrateForm.setTypeName("");
			}
			integrateList = this.integrateService.query(startDate, endDate, integrateForm.getProductCode(),
					integrateForm.getTypeName(), cmd, bounds);
		}
		PageInfo<Integrate> paginator = new PageInfo<>(integrateList);
		return new ModelAndView("statistics/integrate/index").addObject("integrateList", integrateList)
				.addObject("integrateForm", integrateForm).addObject("paginator", paginator);
	}

	@RequiresPermissions("statisticsIntegrate:export")
	@RequestMapping("/export")
	public void integrateExport(IntegrateForm integrateForm, String cmd, HttpServletResponse response) {
		RowBounds bounds = new RowBounds();
		Date startDate = null;
		Date endDate = null;
		List<Integrate> integrateList = new ArrayList<>();
		// ==============================
		if (cmd == null || cmd.equals("desc")) {
			cmd = "DESC";
		} else {
			cmd = "ASC";
		}
		// 积分的开始和结束时间(一开始进入页面的时候就会出现统计数据,这里的默认搜索一定是会带条件的)
		String startDateStr = integrateForm.getStartDate();
		String endDateStr = integrateForm.getEndDate();
		String defaultDateStr = null;
		String yesterdayDateStr = null;
		if (StringUtils.isEmpty(startDateStr)) {
			// 默认是昨天
			defaultDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
			integrateForm.setStartDate(defaultDateStr);
			startDate = DateUtil.StringToDate(defaultDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		} else {
			startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		}
		if (StringUtils.isEmpty(endDateStr)) {
			yesterdayDateStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
			integrateForm.setEndDate(yesterdayDateStr);
			endDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		} else {
			endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}
		if (integrateForm != null) {
			if (!StringUtils.hasLength(integrateForm.getProductCode())) {
				integrateForm.setProductCode("");
			}
			if (!StringUtils.hasLength(integrateForm.getTypeName())) {
				integrateForm.setTypeName("");
			}
			integrateList = this.integrateService.query(startDate, endDate, integrateForm.getProductCode(),
					integrateForm.getTypeName(), cmd, bounds);
		} 
		List<IntegrateExport> integrateExport = new ArrayList<>();
		IntegrateExport ie;
		for (Integrate item : integrateList) {
			ie = new IntegrateExport();
			ie.integrateCost = item.getIntegrateCost();
			ie.productCode = item.getproductCode();
			ie.purchaseNumber = item.getPurchaseNumber();
			ie.restStock = item.getRestStock();
			ie.saleAmount = item.getSaleAmount();
			ie.totalAmount = item.getTotalAmount();
			ie.typeName = item.getTypeName();
			integrateExport.add(ie);
		}
		String headers[] = { "商品编码", "商品名称", "剩余库存", "商品销量", "购买人数", "消费总积分", "总现金" };
		String fieldNames[] = { "productCode", "typeName", "restStock", "saleAmount", "purchaseNumber", "integrateCost",
				"totalAmount" };
		POIUtil.export(response, headers, fieldNames, integrateExport, "积分统计信息");
	}

	private class IntegrateExport {
		private String productCode;// 商品编码
		private String typeName;// 商品名称(对应的积分类型表中的积分类型名称)
		private Integer restStock;// 剩余库存
		private int saleAmount;// 商品销量(对应的是在会员积分表中的numbers)
		private int purchaseNumber;// 购买人数(只统计用户id)
		private int integrateCost;// 消费总积分
		private BigDecimal totalAmount;// 总现金(部分商品是积分+现金购买)
	}
}
