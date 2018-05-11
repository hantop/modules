package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.biz.form.RepaymentForm;
import com.fenlibao.model.pms.da.biz.viewobject.RepaymentVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectPrepaymentConfig;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.vo.RepayBudgetVO;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWProjectService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanTransationService;
import com.fenlibao.service.pms.da.biz.repayment.RepaymentService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 存管还款
 */
@RestController
@RequestMapping("biz/repayment")
public class RepaymentController {
    private static final Logger logger = LoggerFactory.getLogger(RepaymentController.class);

    @Autowired
    private RepaymentService repaymentService;

    @RequiresPermissions("repayment:view")
    @RequestMapping
    public ModelAndView index(RepaymentForm repaymentForm,
                          @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                          @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit){
    ModelAndView mav = new ModelAndView("biz/repayment/index");
    RowBounds bounds = new RowBounds(page, limit);
    List<RepaymentVO> repaymentList = repaymentService.getRepaymentList(repaymentForm, bounds);
    PageInfo<RepaymentVO> paginator = new PageInfo<>(repaymentList);
    return mav.addObject("list", repaymentList)
            .addObject("repaymentForm", repaymentForm)
            .addObject("paginator", paginator);
    }

    @RequestMapping(value = "repayDetailInfo", method = RequestMethod.POST)
    public Map<String, Object> preRepayInfo(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation) {
        Map<String, Object> resultMap = new HashMap<>();
        String resultCode = "0000";
        String message = null;
        String errorMsg = "内部错误";
        resultMap.put("message", message);
        try {
            resultMap = repaymentService.getRepayDetailInfo(bidId, repayMethod, isPreRepay, isSubrogation);
        }catch (XWTradeException te) {
            // 前缀为1的异常不直接返回到前端
            resultCode = te.getCode();
            if (!resultCode.substring(0).equals("1")) {
                message = te.getMessage();
            } else {
                message = errorMsg;
            }
            resultMap.put("message", message);
        }catch (BidVerificationException e){
            message = e.getLocalizedMessage();
            resultMap.put("message", message);
            resultMap.put("erroCode",500);
        }catch (Throwable e){
            message = errorMsg;
            resultMap.put("message", message);
            logger.error("[pms还款管理->获取还款详情：]", e);
        }
        return resultMap;
    }

    /**
     *
     * @param bidId
     * @param repayMethod
     * @param isPreRepay
     * @param isSubrogation
     * @param penaltyFlag 是否收取违约金
     * @param penaltyType 违约金收取方式
     * @param penaltyAmount 自行设置的违约金金额
     * @param penaltyDivideRate 违约金分成金额
     * @param originalPenalty 原违约金总额
     * @return
     */
    @RequiresPermissions("repayment:doRepay")
    @RequestMapping(value = "doRepay", method = {RequestMethod.POST})
    public Map<String, String> doRepay(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation,

    Integer penaltyFlag,Integer penaltyType,BigDecimal penaltyAmount,BigDecimal penaltyDivideRate,BigDecimal originalPenalty
    ) {
        Map<String, String> resultMap = new HashMap<>();
        String resultCode = "0000";
        String errorMsg = "内部错误";
        String message = null;
        XWProjectPrepaymentConfig xwProjectPrepaymentConfig = null;
        try{
            if(isPreRepay){
                try {
                    xwProjectPrepaymentConfig = new XWProjectPrepaymentConfig();
                    xwProjectPrepaymentConfig.setBidId(bidId);
                    xwProjectPrepaymentConfig.setCreateTime(new Date());
                    xwProjectPrepaymentConfig.setOriginalPenalty(originalPenalty);
                    xwProjectPrepaymentConfig.setPenaltyType(penaltyType);
                    xwProjectPrepaymentConfig.setPenaltyFlag(penaltyFlag);
                    xwProjectPrepaymentConfig.setPenaltyDivideRate(penaltyDivideRate.divide(new BigDecimal(100)));
                    xwProjectPrepaymentConfig.setPenaltyAmount(penaltyAmount);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("初始化提前还款配置出错。。");
                }
            }

            resultCode = repaymentService.doRepay(bidId, repayMethod, isPreRepay, isSubrogation,xwProjectPrepaymentConfig);
            resultMap.put("resultCode", resultCode);
            resultMap.put("message", message);
        }catch (XWTradeException te) {
            // 前缀为1的异常不直接返回到前端
            resultCode = te.getCode();
            if (!resultCode.substring(0).equals("1")) {
                message = te.getMessage();
            } else {
                message = errorMsg;
            }
            resultMap.put("resultCode", resultCode);
            resultMap.put("message", message);
        }catch (Throwable e){
            resultCode = "500";
            message = errorMsg;
            resultMap.put("resultCode", resultCode);
            resultMap.put("message", message);
            logger.error("[pms还款管理->还款异常：]", e);
        }
        return resultMap;
    }

    @RequiresPermissions(value = {"repayment:export"})
    @RequestMapping("export")
    public void exportRepayment(RepaymentForm repaymentForm, HttpServletResponse response) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");RowBounds bounds = new RowBounds();
        List<RepaymentVO> repaymentList = repaymentService.getRepaymentList(repaymentForm, bounds);
        List<DetailExport> export = new ArrayList<>();
        for (RepaymentVO item : repaymentList) {
            DetailExport d = new DetailExport();
            d.title = item.getTitle();
            d.borrowUserAccount = item.getBorrowUserAccount();
            d.borrowUserName = item.getBorrowUserName();
            d.gatheringUserAccount = item.getReceiptAccount() != null ? item.getReceiptAccount() : "-";
            d.gatheringUserName = item.getReceiptName() != null ? item.getReceiptName() : "-";
            String repayMethod = item.getRepayMethod();
            if(repayMethod.equals("DEBX")){
                d.repayMethod = "等额本息";
            }else if(repayMethod.equals("MYFX")){
                d.repayMethod = "每月付息,到期还本";
            }else if(repayMethod.equals("YCFQ")){
                d.repayMethod = "本息到期一次付清";
            }else if(repayMethod.equals("DEBJ")){
                d.repayMethod = "等额本金";
            }
            d.repayMoney = item.getRepayMoney();
            d.termDisplay = String.valueOf(item.getCurrentTerm()) + "/" + item.getTotalTerm();
            d.distanceRefund = item.getDistanceRefund();
            d.repayDay = format.format(item.getRepayDay());
            d.status = item.getStatus();
            export.add(d);
        }
        String headers[] = {"标的名称", "借款账号", "借款用户", "收款账号", "收款用户", "还款方式", "借款金额（元）", "本期期数",
                "距离还款日", "还款日", "状态"};
        String fieldNames[] = {"title", "borrowUserAccount", "borrowUserName", "gatheringUserAccount", "gatheringUserName",
                "repayMethod", "repayMoney", "termDisplay", "distanceRefund", "repayDay", "status"};
        POIUtil.export(response, headers, fieldNames, export, "还款列表导出");
    }

    private class DetailExport {
        String borrowUserAccount;// 借款用户账号
        String borrowUserName;// 借款用户名
        String gatheringUserAccount;// 收款用户账号
        String gatheringUserName;// 收款用户名
        String title;// 标的名称
        String repayMoney;// 还款金额,实际借款金额
        int distanceRefund;// 距离还款日
        String repayDay;// 还款日
        String repayMethod;// 还款方式
        String termDisplay;// 当前期/总期数
        String status;// 标的状态
    }

    /**
     * 提前还款配置页
     * @param bidId
     * @param repayMethod
     * @param isPreRepay
     * @param isSubrogation
     * @return
     */
    @RequestMapping(value = "configPrepayment")
    public ModelAndView configPrepayment(int bidId, String title, String repayMethod, boolean isPreRepay, boolean isSubrogation, Date repayDay, String distanceRefund) {
        ModelAndView view = new ModelAndView("biz/repayment/config_prepayment");
        Map<String, Object> resultMap  = null;
        try {
            resultMap = this.preRepayInfo( bidId,  repayMethod,  isPreRepay,  isSubrogation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.addObject("bidId",bidId);
        view.addObject("title",title);
        view.addObject("repayMethod",repayMethod);
        view.addObject("isPreRepay",isPreRepay);
        view.addObject("isSubrogation",isSubrogation);
        view.addObject("distanceRefund",distanceRefund);
        view.addObject("repayDay",repayDay);
        view.addObject("data",resultMap);

        RepayBudgetVO repayBudgetVO =(RepayBudgetVO) resultMap.get("repayBudgetVO");
        if(repayBudgetVO==null){
            resultMap.put("repayBudgetVO",new RepayBudgetVO());
        }else{
            resultMap.put("erroCode",1111);

        }

        return view;
    }

}

