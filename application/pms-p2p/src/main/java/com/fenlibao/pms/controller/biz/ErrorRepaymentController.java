package com.fenlibao.pms.controller.biz;

import com.fenlibao.model.pms.da.biz.form.RepaymentForm;
import com.fenlibao.model.pms.da.biz.viewobject.RepaymentVO;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.service.pms.da.biz.repayment.RepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 存管异常还款(用于测试环境中不使用定时器进行的人工操作,根据存管提供的异常还款接口实现)
 */
@RestController
@RequestMapping("biz/errorRepayment")
public class ErrorRepaymentController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorRepaymentController.class);

    @Autowired
    private RepaymentService repaymentService;

    //    @RequiresPermissions("loanManage:view")
    @RequestMapping
    public ModelAndView index(RepaymentForm repaymentForm) throws Throwable {
        ModelAndView mav = new ModelAndView("biz/errorRepayment/index");
        RepaymentVO errorRepayment = new RepaymentVO();
        if(repaymentForm.getTitle() != null && repaymentForm.getBorrowUserAccount() != null){
            errorRepayment = repaymentService.getErrorRepayment(repaymentForm);
        }
        return mav.addObject("errorRepayment", errorRepayment)
                .addObject("repaymentForm", repaymentForm);
    }

    @RequestMapping(value = "doErrorRepay", method = {RequestMethod.POST})
    public Map<String, String> doRepay(int bidId) {
        Map<String, String> resultMap = new HashMap<>();
        String resultCode = "0000";
        String errorMsg = "内部错误";
        String message = null;
        try{
            resultCode = repaymentService.doErrorRepay(bidId);
        }catch (XWTradeException te) {
            // 前缀为1的异常不直接返回到前端
            resultCode = te.getCode();
            if (!resultCode.substring(0).equals("1")) {
                message = te.getMessage();
            } else {
                message = errorMsg;
            }
        }catch (Throwable e){
            resultCode = "500";
            message = errorMsg;
            logger.error("[异常还款管理->异常还款异常：]", e);
        }
        resultMap.put("resultCode", resultCode);
        resultMap.put("message", message);
        return resultMap;
    }

}

