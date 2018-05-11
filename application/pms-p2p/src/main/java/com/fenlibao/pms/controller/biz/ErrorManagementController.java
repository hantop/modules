package com.fenlibao.pms.controller.biz;

import com.fenlibao.model.pms.da.biz.viewobject.ErrorEntrustPayVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.service.pms.da.biz.errorManagement.ErrorManagementService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常管理(目前只有委托支付异常)
 */
@RestController
@RequestMapping("biz/errorManagement")
public class ErrorManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorManagementController.class);

    @Autowired
    private ErrorManagementService errorManagementService;

    @RequiresPermissions("errorManagement:view")
    @RequestMapping
    public ModelAndView index() throws Throwable {
        ModelAndView mav = new ModelAndView("biz/errorManagement/index");
        RowBounds bounds = new RowBounds();
        List<ErrorEntrustPayVO> errorEntrustPayVOList = errorManagementService.getErrorEntrustPayList(bounds);
        PageInfo<ErrorEntrustPayVO> paginator = new PageInfo<>(errorEntrustPayVOList);
        return mav.addObject("errorEntrustPayVOList", errorEntrustPayVOList).addObject("paginator", paginator);
    }

    @RequestMapping(value = "handelError/{loanIds}", method = {RequestMethod.POST})
    public Map<String, String> handelError(@PathVariable("loanIds") List<String> loanIds) {
        Map<String, String> resultMap = new HashMap<>();
        String resultCode = "0000";
        String errorMsg = "内部错误";
        String message = null;
        try{
            resultCode = errorManagementService.handelError(loanIds);
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

