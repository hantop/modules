package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.model.pms.da.biz.form.LoanManageForm;
import com.fenlibao.model.pms.da.biz.viewobject.BidVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import com.fenlibao.service.pms.da.biz.loanmanage.LoanManageService;
import com.fenlibao.service.pms.da.cs.logInfo.LogInfoService;
import com.fenlibao.service.pms.da.exception.BidVerificationException;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发标管理
 * <p>
 * Created by chenzhixuan on 2017/1/18.
 */
@RestController
@RequestMapping("biz/loanmanage")
public class LoanManageController {
    private static final Logger LOG = LoggerFactory.getLogger(LoanManageController.class);
    @Autowired
    private LoanManageService loanManageService;
    @Autowired
    private BidTypeService bidTypeService;
    @Autowired
    private LogInfoService logInfoService;

    @RequiresPermissions("loanManage:view")
    @RequestMapping
    public ModelAndView index(LoanManageForm loanManageForm,
                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                              @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) throws Throwable {
        ModelAndView mav = new ModelAndView("biz/loanmanage/index");
        RowBounds bounds = new RowBounds(page, limit);
        List<BidVO> list = loanManageService.search(loanManageForm, bounds);
        PageInfo<BidVO> paginator = new PageInfo<>(list);
        List<BidType> bidTypes = bidTypeService.getBidTypes(new ArrayList<String>() {{
            add("XFXD");
        }});
        return mav
                .addObject("list", list)
                .addObject("bidTypes", bidTypes)
                .addObject("loanManageForm", loanManageForm)
                .addObject("paginator", paginator);
    }

    @RequiresPermissions("loanManage:release")
    @RequestMapping(value = "release", method = RequestMethod.POST)
    public Map<String, Object> release(int loanId) {
        Map<String, Object> resultMap = new HashMap<>();
        String code = "200";
        String message = null;
        String errorMessage = "内部错误";
        try {
            code = loanManageService.release(loanId);
            if(code != null){
                if(code.equals("1000")){
                    message = "发布中,请留意标的状态";
                }else{
                    message = "发布成功";
                }
                logInfoService.addUserLog(loanId,1);
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
            logInfoService.addUserLog(loanId,0);
        }catch (BidVerificationException e) {
            message = e.getMessage();
            resultMap.put("message", message);
            logInfoService.addUserLog(loanId,0);
        } catch (Exception e) {
            LOG.error("PMS发标异常", e);
            message = errorMessage;
            code = "500";
            resultMap.put("code", code);
            resultMap.put("message", message);
            logInfoService.addUserLog(loanId,0);
        }
        return resultMap;
    }


    @RequiresPermissions("loanManage:sealedBidding")
    @RequestMapping(value = "sealedBidding", method = RequestMethod.POST)
    public Map<String, Object> sealedBidding(int loanId) {
        Map<String, Object> resultMap = new HashMap<>();
        String code = "200";
        String message = null;
        String errorMessage = "内部错误";
        try {
            loanManageService.sealedBidding(loanId);
        } catch (Exception e) {
            LOG.error("PMS封标异常", e);
            message = errorMessage;
            code = "500";
            resultMap.put("code", code);
            resultMap.put("message", message);
            return resultMap;
        }
        return resultMap;
    }

}
