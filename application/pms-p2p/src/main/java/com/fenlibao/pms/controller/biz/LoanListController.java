package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.form.LoanListForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.service.pms.da.biz.loanList.LoanListService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存管放款/流标
 */
@RestController
@RequestMapping("biz/loanList")
public class LoanListController {
    private static final Logger LOG = LoggerFactory.getLogger(LoanListController.class);

    @Autowired
    private LoanListService loanListService;

    @RequestMapping(value = "loanList-index", method = RequestMethod.GET)
    public ModelAndView accountIndex() {
        return new ModelAndView("biz/loanList/loanList-index");
    }

    @RequiresPermissions("loanList:view")
    @RequestMapping(value = "")
    public @ResponseBody
    Object loanList(HttpServletRequest request,
                    LoanListForm loanListForm,
                    @RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                    @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page + 1, limit);
        List<LoanList> list = new ArrayList<>();
        PageInfo<LoanList> paginator = new PageInfo<LoanList>(list);
        try{
            list = loanListService.getLoanList(loanListForm, bounds);
            paginator = new PageInfo<>(list);
        }catch (Exception e){
            //e.printStackTrace();
        }
        return paginator;
    }

    @RequiresPermissions("loanList:loan")
    @RequestMapping(value = "loan", method = RequestMethod.POST)
    public Map<String, Object> loan(int bId){
        Map<String, Object> resultMap = new HashMap<>();
        String code = "200";
        String message = null;
        String errorMessage = "内部错误";
        try {
            code = loanListService.makeALoanApply(Integer.valueOf(bId));
            if(code.equals("1000")){
                message = "放款中,请留意放款状态!!";
            }else if(code.equals("2000")){
                message = "放款成功";
            }
            resultMap.put("code", code);
            resultMap.put("message", message);
        } catch (XWTradeException te) {
            // 前缀为1的异常不直接返回到前端
            code = te.getCode();
            if (!code.substring(0).equals("1")) {
                message = te.getMessage();
            } else {
                message = errorMessage;
            }
            resultMap.put("code", code);
            resultMap.put("message", message);
        } catch (Exception e) {
            message = errorMessage;
            code = "500";
            resultMap.put("code", code);
            resultMap.put("message", message);
            LOG.error("PMS放款异常", e);
        }
        return resultMap;
    }

    @RequiresPermissions("loanList:notLoan")
    @RequestMapping(value = "notLoan")
    public Map<String, String> notLoan(int bId){
        String code = null;
        String message = null;
        String errorMessage = "内部错误";
        //通知存管，流标
        Map<String, String> resultMap = new HashMap<>();
        if(bId > 0){
            try{
                code = loanListService.flowBidApply(bId);//
                if(code.equals("1000")){
                    message = "流标中,请留意流标状态!!";
                }else if(code.equals("2000")){
                    message = "流标成功";
                }
                resultMap.put("code", code);
                resultMap.put("message", message);
            }catch(XWTradeException e){
                // 前缀为1的异常不直接返回到前端
                code = e.getCode();
                if (!code.substring(0).equals("1")) {
                    message = e.getMessage();
                } else {
                    message = errorMessage;
                }
                resultMap.put("code", code);
                resultMap.put("message", message);
            }catch (Exception e){
                LOG.error("PMS流标异常", e);
                message = errorMessage;
                code = "500";
                resultMap.put("code", code);
                resultMap.put("message", message);
            }
        }
        return resultMap;
    }

}

