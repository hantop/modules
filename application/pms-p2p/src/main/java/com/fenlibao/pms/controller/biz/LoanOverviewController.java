package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.KV;
import com.fenlibao.model.pms.da.biz.LoanOverview;
import com.fenlibao.model.pms.da.biz.form.LoanOverviewForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.common.producttype.ProductTypeService;
import com.fenlibao.service.pms.da.biz.loanOverview.LoanOverviewService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("biz/loanOverview")
public class LoanOverviewController {
    private static final Logger LOG = LoggerFactory.getLogger(LoanOverviewController.class);

    @Autowired
    private LoanOverviewService loanOverviewService;
    @Autowired
    private ProductTypeService productTypeService;

    @RequiresPermissions("loanOverview:view")
    @RequestMapping(value = "")
    public ModelAndView loanList(HttpServletRequest request,
    		LoanOverviewForm loanOverviewForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
    	boolean fromPage=Boolean.parseBoolean(request.getParameter("fromPage")==null?"false":request.getParameter("fromPage")) ;
    	Date d=new Date();
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    	Calendar rightNow = Calendar.getInstance();
    	rightNow.setTime(d);
    	rightNow.add(Calendar.DAY_OF_YEAR, -30);
    	//点击菜单访问页面时的默认日期范围和排序规则
    	if(!fromPage){
    		loanOverviewForm.setTenderStartDate(format.format(rightNow.getTime()));
    		loanOverviewForm.setTenderEndDate(format.format(d));
    		loanOverviewForm.setSort("originOrder");
    	}
        RowBounds bounds = new RowBounds(page, limit);
        List<LoanOverview> list = loanOverviewService.getLoanList(loanOverviewForm, bounds);
        PageInfo<LoanOverview> paginator = new PageInfo<>(list);
        Map<String, String> totalMap = loanOverviewService.getLoanTotal(loanOverviewForm);
        // 产品类型
        List<KV<String, String>> productTypes = productTypeService.getProductTypes();
        return new ModelAndView("biz/loanOverview/index")
                .addObject("loanOverviewForm",loanOverviewForm)
                .addObject("productTypes", productTypes)
                .addObject("totalMap",totalMap)
                .addObject("paginator", paginator).addObject("loanOverviewList", list);
    }
    
    @RequiresPermissions("loanOverview:export")
    @RequestMapping(value = "export")
    public void exportLoanList(
    		LoanOverviewForm loanOverviewForm, HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        List<LoanOverview> list = loanOverviewService.getLoanList(loanOverviewForm, bounds);
        String headers[] = {"借款标题", "借款账户", "借款人姓名", "合同金额(元)", "放款金额(元)", "应还本金(元)", "应还利息(元)", "成交服务费(应还利息中已含)", "其他费用(逾期/提前还款产生)", "借款端利率","投资端利率","期限","发标日期","满标日期","放款日期","还款日期","还款方式","经办人","状态"};
        String fieldNames[] = {"bidTitle", "borrowerAccount", "borrowerName", "borrowAmount", "loanAmount", "principal", "interest", "transactionFee", "otherFee", "borrowRate","investRate","cycle","tenderDate","fullDate","loanDate","repayDate","repayMode","operator","bidStatus"};
        POIUtil.export(response, headers, fieldNames, list, "借款总览信息");        
    }
}
