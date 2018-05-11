package com.fenlibao.pms.controller.cs.borrowerAccount;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.cs.BorrowerAccountInfo;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.form.BorrowerAccountForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.BorrowerAccountService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("cs/borrower")
public class BorrowerAccountController {

    @Resource
    private BorrowerAccountService borrowerAccountService;

    // 借款用户管理列表
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                BorrowerAccountForm borrowerAccountForm) {
        ModelAndView view = new ModelAndView("cs/borrower/index");
        RowBounds bounds = new RowBounds(page, limit);
        List<BorrowerAccountInfo> borrowerAccountInfoList = borrowerAccountService.getBorrowerAccountInfoList(borrowerAccountForm, bounds);
        PageInfo<BorrowerAccountInfo> paginator = new PageInfo<>(borrowerAccountInfoList);
        view.addObject("list", borrowerAccountInfoList);
        view.addObject("borrowerAccountForm", borrowerAccountForm);
        view.addObject("paginator", paginator);
        return view;
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ModelAndView viewAccount(String userId) {
        BussinessInfo bussinessInfo = borrowerAccountService.getBussinessInfoByUserId(Integer.valueOf(userId));
        return new ModelAndView("cs/borrower/view").addObject("bussinessInfo", bussinessInfo);
    }
}
