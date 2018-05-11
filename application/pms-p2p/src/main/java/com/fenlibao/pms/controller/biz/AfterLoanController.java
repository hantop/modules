package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.afterLoan.BorrowInfoAfterLoan;
import com.fenlibao.model.pms.da.biz.form.LoanListForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.biz.afterLoan.AfterLoanService;
import com.fenlibao.service.pms.da.biz.loanList.LoanListService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 贷后管理
 * Created by Administrator on 2017/11/21.
 */
@RestController
@RequestMapping("biz/afterLoan")
public class AfterLoanController {
    private static final Logger logger = LoggerFactory.getLogger(AfterLoanController.class);
    @Autowired
    private AfterLoanService afterLoanService;

    /**
     * 贷后信息列表
     * @param request
     * @param page
     * @param limit
     * @param borrowerName
     * @param borrowerAccount
     * @return
     */
    @RequestMapping("aferLoanList")
    @RequiresPermissions("aferLoan:get")
    public ModelAndView aferLoanList(HttpServletRequest request,

                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                              String borrowerName, String borrowerAccount) {


        List<LoanList> list = new ArrayList<>();
        RowBounds bounds = new RowBounds(page, limit);

        try{
            list = afterLoanService.afterLoanList(borrowerName, borrowerAccount,bounds);

        }catch (Exception e){
            e.printStackTrace();
        }

        PageInfo<LoanList> paginator = new PageInfo<LoanList>(list);

        return new ModelAndView("biz/afterLoan/aferLoanList")
                .addObject("borrowerName", borrowerName)
                .addObject("borrowerAccount", borrowerAccount)
                .addObject("list",list).addObject("paginator", paginator);
    }


    /**
     * 打开编辑页面
     * @param loanlist
     * @return
     */
    @RequestMapping(value = "editBorrowerInfo")
//    @RequiresPermissions("aferLoan:edit")
    public ModelAndView editBorrowerInfo(LoanList loanlist) {
        BorrowInfoAfterLoan info = afterLoanService.getBorrowInfoAferLoanByBidId(loanlist.getBidId());
        if(info==null){
            info= new BorrowInfoAfterLoan();
            info.setBidId(loanlist.getBidId());
            info.setFinanceDetail("未见异常");
            info.setLawsuitDetail("未见异常");
            info.setOverdueDetail("未见异常");
            info.setUseDetail("未见异常");
            info.setRepayAbility("未见异常");
            info.setPunishDetail("未见异常");
        }
        return new ModelAndView("biz/afterLoan/editBorrowerInfo")
                .addObject("info", info)
                .addObject("loanlist", loanlist);
    }

    /**
     * 保存贷后管理信息
     * @param borrowInfoAfterLoan
     * @return
     */
    @RequestMapping(value = "saveOrUpdate")
    @RequiresPermissions("aferLoan:saveOrUpdate")
    @ResponseBody
    public HashMap saveOrUpdate(BorrowInfoAfterLoan borrowInfoAfterLoan) {

        HashMap<String,Object> map = new HashMap<>();
        Date now = new Date();
        try {
            BorrowInfoAfterLoan info = afterLoanService.getBorrowInfoAferLoanByBidId(borrowInfoAfterLoan.getBidId());
            map.put("primaryKey",borrowInfoAfterLoan.getBidId());

            if(info!=null){//更新发布期数
                Date updateTime = info.getUpdateTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(updateTime);
                int monthOld = cal.get(Calendar.MONTH);
                cal.setTime(now);
                int monthNow = cal.get(Calendar.MONTH);

                if(now.after(updateTime)&&(monthOld!=monthNow)){//不是上次更新的同一个月，发布期数+1
                    borrowInfoAfterLoan.setPublishTimes(info.getPublishTimes()+1);
                }else{
                    borrowInfoAfterLoan.setPublishTimes(info.getPublishTimes());
                }
                map.put("type","update");
                logger.info("更新贷后信息，标id为：[{}]",borrowInfoAfterLoan.getBidId());
            }else {
                borrowInfoAfterLoan.setPublishTimes(1);
                map.put("type","insert");
                logger.info("添加贷后信息，标id为：[{}]",borrowInfoAfterLoan.getBidId());
            }

            borrowInfoAfterLoan.setUpdateTime(now);//更新时间
            borrowInfoAfterLoan.setCreateTime(info==null?now:info.getCreateTime());
            afterLoanService.saveOrUpdateInfo(borrowInfoAfterLoan);
            map.put("success",true);
            map.put("msg","操作成功");
            return map;

        }catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑贷后信息出现异常，标id为：[{}]",borrowInfoAfterLoan.getBidId());
            map.put("success",false);
            map.put("msg",e.getCause());
            return map;
        }

    }
}
