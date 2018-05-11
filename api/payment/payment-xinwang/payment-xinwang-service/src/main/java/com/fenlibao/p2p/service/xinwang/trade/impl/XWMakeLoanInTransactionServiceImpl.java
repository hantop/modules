package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.plan.XWPlanDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentWay;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanInTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2018/2/1.
 */

@Service
public class XWMakeLoanInTransactionServiceImpl implements XWMakeLoanInTransactionService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    SysCreditDao creditDao;

    @Resource
    XWPlanDao planDao;

    @Resource
    SysCouponManageDao couponDao;

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    SysOrderService orderService;
    @Override
    public void addXWCapitalFlowOfServiceFee(XWProjectInfo projectInfo, XWProjectExtraInfo extraInfo) {
        if(projectInfo.getCollectType()==1){//前置收费，则添加收取平台服务费的流水记录
            LOG.debug("前置收费，生成服务费流水");
            List<XWCapitalFlow> t6102sToInsert = new ArrayList<>();//资金流水
            List<XWFundAccount> t6101sToUpdate = new ArrayList<>();//资金账户

            XWProjectRate projectRate=projectDao.getProjectRateById(extraInfo.getId());
            BigDecimal serviceFee = projectInfo.getProjectAmount().multiply(projectRate.getTransactionServiceRate()).setScale(2, RoundingMode.HALF_UP);

            LOG.info("标【{}】，成交服务费【{}】",projectInfo.getProjectNo(),serviceFee);


            // 借款人往来账户
            XWFundAccount borrowerWLZH = accountDao.getFundAccount(projectInfo.getBorrowerUserId(), SysFundAccountType.XW_BORROWERS_WLZH);

            // 新网平台收入账户
            XWFundAccount platformIncomeWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_INCOME_WLZH);


            // 扣减借款人账户
            borrowerWLZH.setAmount(borrowerWLZH.getAmount().subtract(serviceFee));
            // 增加平台往来账户资金
            platformIncomeWLZH.setAmount(platformIncomeWLZH.getAmount().add(serviceFee));

            if (borrowerWLZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(), String.format(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage(), borrowerWLZH.getUserId()));
            }


            XWCapitalFlow t6102Borrower = new XWCapitalFlow();//借款人支出流水
            t6102Borrower.setFundAccountId(borrowerWLZH.getId());
            t6102Borrower.setTadeType(SysTradeFeeCode.CJFWF);
            t6102Borrower.setOtherFundAccountId(platformIncomeWLZH.getId());
            t6102Borrower.setExpenditure(serviceFee);
            t6102Borrower.setBalance(borrowerWLZH.getAmount());
            t6102Borrower.setRemark(String.format("成交服务费:%s，标题：%s", projectInfo.getProjectCode(), projectInfo.getProjectName()));
            t6102Borrower.setProjectId(extraInfo.getId());
            t6102sToInsert.add(t6102Borrower);



            XWCapitalFlow t6102PlatformIncome = new XWCapitalFlow();//平台收入流水
            t6102PlatformIncome.setFundAccountId(platformIncomeWLZH.getId());
            t6102PlatformIncome.setTadeType(SysTradeFeeCode.CJFWF);
            t6102PlatformIncome.setOtherFundAccountId(borrowerWLZH.getId());
            t6102PlatformIncome.setIncome(serviceFee);
            t6102PlatformIncome.setBalance(platformIncomeWLZH.getAmount());
            t6102PlatformIncome.setRemark(String.format("成交服务费:%s，标题：%s", projectInfo.getProjectCode(), projectInfo.getProjectName()));
            t6102PlatformIncome.setProjectId(extraInfo.getId());
            t6102sToInsert.add(t6102PlatformIncome);
            LOG.debug(t6102PlatformIncome.toString());


            t6101sToUpdate.add(platformIncomeWLZH);
            t6101sToUpdate.add(borrowerWLZH);

            for (XWFundAccount funcAccount : t6101sToUpdate) {
                Map<String, Object> fundAccountParams = new HashMap<>();
                fundAccountParams.put("userId", funcAccount.getUserId());
                fundAccountParams.put("type", funcAccount.getFundAccountType());
                fundAccountParams.put("amount", funcAccount.getAmount());
                accountDao.updateFundAccount(fundAccountParams);
            }


            commonDao.batchInsertT6102(t6102sToInsert);

            LOG.debug(t6102Borrower.toString());
        }

    }

    @Override
    public ArrayList<XWRepaymentPlan> addXWRepaymentPlanWithServiceFee(XWProjectInfo projectInfo, SysCredit credit, Date endDate, XWProjectRate projectRate, XWProjectExtraInfo extraInfo) {

        ArrayList<XWRepaymentPlan> t6252s = new ArrayList<>();


        if(projectRate.getTransactionServiceRate().compareTo(BigDecimal.ZERO)==1){
            int termsCount = (projectInfo.getMonthProjectPeriod()==null||projectInfo.getMonthProjectPeriod()==0)?1:projectInfo.getMonthProjectPeriod();//天标时月周期为0
            int termIndex=1;
            BigDecimal serviceFeeAmount = projectInfo.getProjectAmount().multiply(projectRate.getTransactionServiceRate()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal termFeeAmount = serviceFeeAmount.divide(new BigDecimal(termsCount),2,RoundingMode.HALF_UP);//分期的平均服务费
            BigDecimal lastTermFee =serviceFeeAmount.subtract(termFeeAmount.multiply(new BigDecimal(termsCount-1))) ;//最后一期的费用，保证所有期数总和=serviceFeeAmount

            if(projectInfo.getRepaymentWay().equals(RepaymentWay.YCFQ)){//一次付清
                lastTermFee=serviceFeeAmount;
                termsCount=1;
            }
            for (int term = termIndex; term <= termsCount; term++) {
                Date date = null;//应还日期
                try {

                    date = new Date(DateUtil.rollNaturalMonth(endDate.getTime(), term));
                    if(projectInfo.getRepaymentWay().equals(RepaymentWay.YCFQ)){
                        //一次付清
                        date = extraInfo.getEndDate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(projectInfo.getBorrowerUserId());
                t6252.setPayeeId(SysCommonConsts.PLATFORM_USER_ID);
                t6252.setFeeType(SysTradeFeeCode.CJFWF);
                t6252.setTerm(term);
                t6252.setAmount(term==termsCount?lastTermFee:termFeeAmount);//最后一期特殊处理
                t6252.setDueDate(projectInfo.getCollectType()==1?(new Date()):date);
                t6252.setRepayState(projectInfo.getCollectType()==1? RepaymentPlan_RepayState.YH:RepaymentPlan_RepayState.WH);//前置：已还 后置：未还
                t6252.setActualRepayTime(projectInfo.getCollectType()==1?(new Date()):null);
                t6252.setCreditId(credit.getId());
                t6252s.add(t6252);
            }

            if(!CollectionUtils.isEmpty(t6252s)){
                projectDao.batchInsertRepaymentPlan(t6252s);// 服务费的还款计划
            }

        }else{
            LOG.info("成交服务费率为0，不生成服务费的还款计划，成交服务费率：【{}】",projectRate.getTransactionServiceRate());
        }
        return t6252s;
    }
}
