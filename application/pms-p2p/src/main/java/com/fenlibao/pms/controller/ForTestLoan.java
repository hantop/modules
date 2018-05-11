package com.fenlibao.pms.controller;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWProjectService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanInTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanTransationService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
@Controller
public class ForTestLoan {
    @Resource
    XWMakeLoanService makeLoanService;
    @Resource
    XWProjectDao projectDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    PTCommonDao commonDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    SysOrderService orderService;

    @Resource
    XWProjectService projectService;

    @Resource
    XWMakeLoanTransationService makeLoanTransationService;

    @Resource
    XWRepayTransactionService repayTransactionService;

    @Resource
    XWMakeLoanInTransactionService inTransactionService;
    @Resource
    SysCreditDao creditDao;

    @Autowired
    private XWMakeLoanService xwMakeLoanService;




    @RequestMapping(value = "forLoan")
    public ModelAndView fortest() {



//        this.addXWRepaymentPlanWithServiceFee();
//        this.testMakeLoan();

//        makeLoanTransationService.testTransacation();
        testSend();
        return null;

    }
    public void testSend(){
        Integer bidId=2158151;
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);

        XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo(bidId);

        XWProjectRate projectRate = projectDao.getProjectRateById(bidId);

        List<SysCredit> credits = creditDao.getCreditInfoByProjectId(bidId);
        makeLoanTransationService.sendLetterAfterMakeLoan(projectInfo);
    }


    public void addXWRepaymentPlanWithServiceFee(){
        int bidId = 2157893;

        XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);

        XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo(bidId);

        XWProjectRate projectRate = projectDao.getProjectRateById(bidId);

        List<SysCredit> credits = creditDao.getCreditInfoByProjectId(bidId);
        inTransactionService.addXWRepaymentPlanWithServiceFee(projectInfo,credits.get(0),extraInfo.getBearInterestDate(),projectRate, extraInfo);//生成平台
    }

    public void testgenerateRepaymentPlan() {
        try {
            int bidId = 2157893;
            XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);
            //改变新网标状态

            XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo(bidId);

            XWProjectRate projectRate = projectDao.getProjectRateById(bidId);

            makeLoanTransationService.generateRepaymentPlan(projectInfo, extraInfo.getBearInterestDate(), extraInfo.getEndDate(), extraInfo, projectRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMakeLoan(){
        try {
            xwMakeLoanService.makeLoanApply(2158201);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void platformMakeLoan(){
        try {
            makeLoanTransationService.platformMakeLoan("20180206160916f686bb10-7",16992113);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIfFinish (){
        try {
            makeLoanService.checkIfFinish(2158254);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
