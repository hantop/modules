package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.plan.XWPlanDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCreditTransferApply;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlanCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.credit.CreditTransferApply_Status;
import com.fenlibao.p2p.model.xinwang.enums.credit.SysCredit_Transfering;
import com.fenlibao.p2p.model.xinwang.enums.plan.SysPlan_Type;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentWay;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayInTransaction;
import com.fenlibao.p2p.util.api.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2018/1/29.
 */
@Service
public class XWRepayInTransactionImpl implements XWRepayInTransaction {

    @Resource
    XWProjectDao projectDao;

    @Resource
    SysRepayDao repayDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    SysCreditDao creditDao;

    @Resource
    SysCouponManageDao couponDao;

    @Resource
    XWPlanDao planDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysOrderService orderService;

    @Resource
    PTCommonDao ptCommonDao;

    @Resource
    PTCommonService ptCommonService;

    @Resource
    XWCreditService xwCreditService;

    @Resource
    SysOrderManageDao orderManageDao;
    private Logger LOG = LogManager.getLogger(this.getClass());


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<XWRepaymentPlan> modifyRepaymentPlan(Integer projectId, List<XWRepaymentPlan> originList, XWProjectInfo projectInfo, XWProjectExtraInfo projectExtraInfo, XWProjectRate projectRate, Integer currentTerm, Date currentDate, boolean modifyDatabase, XWProjectPrepaymentConfig xwProjectPrepaymentConfig, SysRepayOperationType type)throws Exception{
        List<XWRepaymentPlan> repaymentPlanToInsert=new ArrayList<>();
        List<XWRepaymentPlan> repaymentPlanToUpdate=new ArrayList<>();
        List<XWRepaymentPlan> currentTermPrepaymentPlans=new ArrayList<>();//用于返回显示
        //借款周期总天数
        int daysOfCycle = DateUtil.daysOfTwo(projectExtraInfo.getBearInterestDate(), projectExtraInfo.getEndDate());
        //从起息到还款日为止的借款总天数
        int loanDays = DateUtil.daysOfTwo(projectExtraInfo.getBearInterestDate(), currentDate);
        //针对次日启息和当天按提前还款
        if(loanDays<0){
            loanDays=0;
        }
        //往期总天数
        int preTermsDaySum= DateUtil.sumDaysOfNaturalMonth(projectExtraInfo.getBearInterestDate(), currentTerm-1);
        //当前期借款天数
        int  currentTermLoanDays=loanDays-preTermsDaySum;



        //剩余天数
        int leftDays=daysOfCycle-loanDays;
        //最后一个月的天数
        int lastNaturalMonthDays=DateUtil.daysOfLastNaturalMonth(projectExtraInfo.getBearInterestDate(),daysOfCycle);



        HashMap<Integer,InterestFeeEntity> mapInterestFeeEntity = new HashMap<Integer,InterestFeeEntity>();//用于后面计算利息管理费  key债权id，value要计算利息管理费的记录


        for(XWRepaymentPlan item:originList){
            //剩余本金
            Map<String,Object> remainPrincipalParams=new HashMap<>();
            remainPrincipalParams.put("creditId",item.getCreditId());
            remainPrincipalParams.put("term",item.getTerm());
            BigDecimal remainPrincipalOfCredit=repayDao.getRemainPrincipalOfCredit(remainPrincipalParams);

            //日利息
            BigDecimal interestOfDay=remainPrincipalOfCredit.multiply(projectRate.getRepaymentRate()).divide(new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, RoundingMode.HALF_UP);

            InterestFeeEntity interestFeeEntity = mapInterestFeeEntity.get(item.getCreditId())==null?(new InterestFeeEntity()):mapInterestFeeEntity.get(item.getCreditId());//先从mapInterestFeeEntity拿，拿不到就new一个
            interestFeeEntity.setCreditId(item.getCreditId());
            interestFeeEntity.setRemainPrincipalOfCredit(remainPrincipalOfCredit);
            interestFeeEntity.setInterestOfDay(interestOfDay);
            interestFeeEntity.setCurrentTermLoanDays(currentTermLoanDays);
            interestFeeEntity.setInterestManageRate(projectRate.getInterestManagementRate());


            if (item.getFeeType() == SysTradeFeeCode.TZ_LX) {
                interestFeeEntity.setInterestPlan(item);
                //备份利息
                XWRepaymentPlan interestBackup=new XWRepaymentPlan();
                interestBackup.setProjectId(projectId);
                interestBackup.setPayerId(item.getPayerId());
                interestBackup.setPayeeId(item.getPayeeId());
                interestBackup.setFeeType(SysTradeFeeCode.TZ_LX_BF);
                interestBackup.setTerm(item.getTerm());
                interestBackup.setAmount(item.getAmount());
                interestBackup.setDueDate(item.getDueDate());
                interestBackup.setRepayState(RepaymentPlan_RepayState.YH);
                interestBackup.setActualRepayTime(currentDate);
                interestBackup.setCreditId(item.getCreditId());
                repaymentPlanToInsert.add(interestBackup);


                interestFeeEntity.setInterestId(item.getId());
                interestFeeEntity.setOrginalInterst(interestBackup.getAmount());


                if(type==SysRepayOperationType.PREPAY){
                    SysCredit credit = creditDao.getCreditInfoById(item.getCreditId());
                    BigDecimal originCreditAmount = credit.getOriginCreditAmount();

                    LOG.info("生成提前还款违约金的还款计划");
                    //添加提前还款违约金
                    this.addPenalty(xwProjectPrepaymentConfig,leftDays,interestOfDay,currentTermPrepaymentPlans,lastNaturalMonthDays,item,projectId,repaymentPlanToInsert,projectInfo,originCreditAmount);
                }


            }
            else if(item.getFeeType() == SysTradeFeeCode.TZ_JX){
                //修改投资加息
                //加息券
                SysCredit credit=creditDao.getCreditInfoById(item.getCreditId());
                XWUserCoupon userCoupon=couponDao.getUserCouponByTenderId(credit.getTenderId());
                if(userCoupon!=null){
                    BigDecimal tenderIncreaseInterest=remainPrincipalOfCredit.multiply(userCoupon.getScope()).divide(new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, RoundingMode.HALF_UP).multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
                    item.setAmount(tenderIncreaseInterest);
                }
                else{
                    item.setAmount(BigDecimal.ZERO);
                }
                repaymentPlanToUpdate.add(item);
                interestFeeEntity.setInvestAddInterest(item.getAmount());
            }
            else if(item.getFeeType() == SysTradeFeeCode.BID_JX){
                //修改标加息
                if(projectRate.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO)>0){
                    BigDecimal projectIncreaseInterest=remainPrincipalOfCredit.multiply(projectRate.getProjectRaiseInterestRate()).divide(new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, RoundingMode.HALF_UP).multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
                    item.setAmount(projectIncreaseInterest);
                }
                else{
                    item.setAmount(BigDecimal.ZERO);
                }
                repaymentPlanToUpdate.add(item);

                interestFeeEntity.setBidAddInterest(item.getAmount());
            }
            else if(item.getFeeType() == SysTradeFeeCode.LX_GLF){
                interestFeeEntity.setManageFeeId(item.getId());
                interestFeeEntity.setManageFeePaln(item);
            }
            if(item.getFeeType() != SysTradeFeeCode.TZ_LX&&item.getFeeType() != SysTradeFeeCode.LX_GLF){
                //不是利息、利息管理费，无需调整，添加到返回信息
                currentTermPrepaymentPlans.add(item);
            }

            mapInterestFeeEntity.put(item.getCreditId(),interestFeeEntity);

        }


        this.modifyWithInterestManagement(mapInterestFeeEntity,currentTermPrepaymentPlans,repaymentPlanToUpdate);



        if(modifyDatabase){
            if(type== SysRepayOperationType.PREPAY){
                projectDao.saveXWProjectPrepaymentConfig(xwProjectPrepaymentConfig);//保存提前还款违约金配置
            }

            if(!CollectionUtils.isEmpty(repaymentPlanToInsert)){
                projectDao.batchInsertRepaymentPlan(repaymentPlanToInsert);
            }

            for(XWRepaymentPlan item:repaymentPlanToUpdate){
                Map<String,Object> params=new HashMap<>();
                params.put("id",item.getId());
                params.put("amount",item.getAmount());
                repayDao.updateRepaymentPlanById(params);
            }
            //将剩余期的利息和加息，利息管理费，改成提前还，每加一个交易类型都要维护这个update
            Map<String,Object> interestOfRemainTermsParams=new HashMap<>();
            interestOfRemainTermsParams.put("projectId",projectId);
            interestOfRemainTermsParams.put("term",currentTerm);
            repayDao.updateInterestOfRemainTerms(interestOfRemainTermsParams);

            //提前还款，剩余期数0
            Map<String,Object> ProjectExtraInfoMap= new HashMap<>();
            ProjectExtraInfoMap.put("remainTerms",0);
            ProjectExtraInfoMap.put("id",projectId);
            this.projectDao.updateProjectExtraInfo(ProjectExtraInfoMap);
        }
        return currentTermPrepaymentPlans;
    }


    /**
     * 计算利息管理费
     * @param mapInterestFeeEntity
     * @param currentTermPrepaymentPlans
     * @param repaymentPlanToUpdate
     */

    public void modifyWithInterestManagement(HashMap<Integer, InterestFeeEntity> mapInterestFeeEntity, List<XWRepaymentPlan> currentTermPrepaymentPlans, List<XWRepaymentPlan> repaymentPlanToUpdate) {

        for (InterestFeeEntity entity:mapInterestFeeEntity.values()) {

            if(entity.getInterestId()!=null){

                XWRepaymentPlan originalInterest = entity.getInterestPlan();
                XWRepaymentPlan interest = new XWRepaymentPlan();
                BeanUtils.copyProperties(originalInterest,interest);
                interest.setId(entity.getInterestId());
                interest.setFeeType(SysTradeFeeCode.TZ_LX);
                interest.setAmount(entity.getLastInterest());
                interest.setCreditId(entity.getCreditId());
                repaymentPlanToUpdate.add(interest);
                currentTermPrepaymentPlans.add(interest);
            }



            if(entity.getManageFeeId()!=null){//兼容旧数据，旧数据没有利息管理费
                XWRepaymentPlan originalManageFee = entity.getManageFeePaln();
                XWRepaymentPlan manageFee = new XWRepaymentPlan();
                BeanUtils.copyProperties(originalManageFee,manageFee);

                manageFee.setId(entity.getManageFeeId());
                manageFee.setFeeType(SysTradeFeeCode.LX_GLF);
                manageFee.setAmount(entity.getInterestManageFee());
                manageFee.setCreditId(entity.getCreditId());
                repaymentPlanToUpdate.add(manageFee);
                currentTermPrepaymentPlans.add(manageFee);
            }

            if(LOG.isDebugEnabled()){
                LOG.debug(entity.toString());
            }
        }
    }

    /**
     * 添加提前还款违约金
     * @param xwProjectPrepaymentConfig
     * @param leftDays
     * @param interestOfDay
     * @param currentTermPrepaymentPlans
     * @param lastNaturalMonthDays
     * @param item
     * @param projectId
     * @param repaymentPlanToInsert
     * @param projectInfo
     * @param originCreditAmount  债权金额
     */
    public void addPenalty(XWProjectPrepaymentConfig xwProjectPrepaymentConfig, int leftDays, BigDecimal interestOfDay, List<XWRepaymentPlan> currentTermPrepaymentPlans, int lastNaturalMonthDays, XWRepaymentPlan item, Integer projectId, List<XWRepaymentPlan> repaymentPlanToInsert, XWProjectInfo projectInfo, BigDecimal originCreditAmount) {

        BigDecimal penalty=BigDecimal.ZERO;//未分成时的违约金

        if(xwProjectPrepaymentConfig==null){
            xwProjectPrepaymentConfig = new XWProjectPrepaymentConfig();
            xwProjectPrepaymentConfig.setPenaltyFlag(1);
            xwProjectPrepaymentConfig.setPenaltyDivideRate(BigDecimal.ZERO);
        }
        if(xwProjectPrepaymentConfig.getPenaltyFlag()==1){//收取违约金
            if(xwProjectPrepaymentConfig.getPenaltyType()==1){//自行设置
                penalty = xwProjectPrepaymentConfig.getPenaltyAmount().multiply(originCreditAmount).divide(projectInfo.getProjectAmount(),2, RoundingMode.HALF_UP);
            }else{
                if(leftDays>lastNaturalMonthDays){
                    penalty=interestOfDay.multiply(new BigDecimal(30)).setScale(2, RoundingMode.HALF_UP);
                }
                else if(leftDays>7){
                    penalty=interestOfDay.multiply(new BigDecimal(leftDays-7)).setScale(2, RoundingMode.HALF_UP);
                }
                else{
                    penalty=BigDecimal.ZERO;
                }
            }

            LOG.info("债权【{}】获得违约金总金额【{}】",item.getCreditId(),penalty);
            BigDecimal penaltyOfInvester=BigDecimal.ZERO;//分成后用户得到的违约金
            BigDecimal penaltyOfPlattform=BigDecimal.ZERO;//分成后平台得到的违约金
            penaltyOfPlattform = penalty.multiply(xwProjectPrepaymentConfig.getPenaltyDivideRate()).setScale(2,BigDecimal.ROUND_UP);
            penaltyOfInvester = penalty.subtract(penaltyOfPlattform);

            LOG.info("债权【{}】获得违约金金额【{}】,平台分成违约金金额【{}】",item.getCreditId(),penaltyOfInvester,penaltyOfPlattform);

            if(penaltyOfInvester.compareTo(BigDecimal.ZERO)>0){
                XWRepaymentPlan penaltyRepayment= this.addXWRepaymentPlanToWHList(projectId,item,item.getPayeeId(),SysTradeFeeCode.TZ_WYJ,penaltyOfInvester,repaymentPlanToInsert);
                currentTermPrepaymentPlans.add(penaltyRepayment);

            }
            if(penaltyOfPlattform.compareTo(BigDecimal.ZERO)>0){
                XWRepaymentPlan penaltyRepayment= this.addXWRepaymentPlanToWHList(projectId,item,SysCommonConsts.PLATFORM_USER_ID,SysTradeFeeCode.TZ_WYJ_FC,penaltyOfPlattform,repaymentPlanToInsert);
                currentTermPrepaymentPlans.add(penaltyRepayment);
            }
        }


    }


    /**
     * 添加一条借款人的还款计划记录到未还列表
     * @param projectId 标id
     * @param item
     * @param receiveUserId 收款用户
     * @param feeType 交易类型
     * @param amount 交易金额
     * @param repaymentPlanToInsert 列表
     * @return
     */
    public XWRepaymentPlan addXWRepaymentPlanToWHList(Integer projectId, XWRepaymentPlan item, int receiveUserId, int feeType, BigDecimal amount, List<XWRepaymentPlan> repaymentPlanToInsert) {
        XWRepaymentPlan penaltyRepayment=new XWRepaymentPlan();
        penaltyRepayment.setProjectId(projectId);
        penaltyRepayment.setPayerId(item.getPayerId());
        penaltyRepayment.setPayeeId(receiveUserId);
        penaltyRepayment.setFeeType(feeType);
        penaltyRepayment.setTerm(item.getTerm());
        penaltyRepayment.setAmount(amount);
        penaltyRepayment.setDueDate(item.getDueDate());
        penaltyRepayment.setRepayState(RepaymentPlan_RepayState.WH);
        penaltyRepayment.setCreditId(item.getCreditId());
        repaymentPlanToInsert.add(penaltyRepayment);
        return penaltyRepayment;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysCreditToRepay> transfromDataStructure(Integer orderId, Integer projectId, List<XWRepaymentPlan> repaymentPlanList, SysRepayOperationType type, XWProjectInfo projectInfo, XWProjectRate projectRate, Integer currentTerm)throws Exception{
        //封装成以债权为单位
        Map<Integer,SysCreditToRepay> map=new HashMap<>();
        for(XWRepaymentPlan item:repaymentPlanList){
            SysCreditToRepay creditToRepay=map.get(item.getCreditId());
            if(creditToRepay==null){
                creditToRepay=new SysCreditToRepay();
                creditToRepay.setCreditId(item.getCreditId());
                map.put(item.getCreditId(),creditToRepay);
            }
            if(item.getFeeType()==SysTradeFeeCode.TZ_BJ){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setPrincipal(creditToRepay.getPrincipal().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_LX){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setInterest(creditToRepay.getInterest().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_JX){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setTenderIncreaseInterest(creditToRepay.getTenderIncreaseInterest().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.BID_JX){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setProjectIncreaseInterest(creditToRepay.getProjectIncreaseInterest().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_FX){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setOverduePenalty(creditToRepay.getOverduePenalty().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_YQ_SXF){
                //逾期手续费 7020 给平台
                creditToRepay.setOverdueCommission(creditToRepay.getOverdueCommission().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_WYJ){
                creditToRepay.setInvestorId(item.getPayeeId());
                creditToRepay.setPrepayPenalty(creditToRepay.getPrepayPenalty().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.CJFWF){
                creditToRepay.setDealFee(creditToRepay.getDealFee().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.LX_GLF){
                creditToRepay.setInterestServiceFee(creditToRepay.getInterestServiceFee().add(item.getAmount()));
            }
            else if(item.getFeeType()==SysTradeFeeCode.TZ_WYJ_FC){

                creditToRepay.setPenaltyDivide(creditToRepay.getPenaltyDivide().add(item.getAmount()));
            }



        }
        //因为多个债权可能属于同一计划，为了减少查询，所以存到map
        Map<Integer,BigDecimal> planCurrentRateMap=new HashMap<>();
        //实际还款明细列表
        List<SysCreditToRepay> actualRepayDetailList=new ArrayList<>();

        //遍历债权
        for(SysCreditToRepay item:map.values()){
            SysCredit credit=creditDao.getCreditInfoById(item.getCreditId());
            //将转让申请中的债权下架
            if(credit.getTransfering()== SysCredit_Transfering.S){
                Map<String,Object> updateCreditParams=new HashMap<>();
                updateCreditParams.put("id", item.getCreditId());
                updateCreditParams.put("transfering", SysCredit_Transfering.F);
                creditDao.updateCreditInfoById(updateCreditParams);
                SysCreditTransferApply creditTransferApply=creditDao.getTransferingCreditByCreditId(item.getCreditId());
                if(creditTransferApply!=null){
                    //有可能会已经被人购买
                    try {
                        xwCreditService.cancelTransfer(creditTransferApply.getCreditsaleNo());
                    } catch (Exception ex) {
                        ErrorLogParam param = new ErrorLogParam();
                        param.setMethod("XWRepayTransactionServiceImpl.#transfromDataStructure");
                        param.setErrorLog(ex.toString());
                        commonDao.insertErrorLog(param);
                    }
                    Map<String,Object> transferApplyParams=new HashMap<>();
                    transferApplyParams.put("id", creditTransferApply.getId());
                    transferApplyParams.put("status", CreditTransferApply_Status.YJS);
                    creditDao.updateCreditTransferApplyById(transferApplyParams);
                }
            }
            //加息券加息利率
            BigDecimal tenderIncreaseInterestRate=BigDecimal.ZERO;
            XWUserCoupon userCoupon=couponDao.getUserCouponByTenderId(credit.getTenderId());
            if(userCoupon!=null){
                tenderIncreaseInterestRate=userCoupon.getScope();
            }
            //是否属于计划
            SysUserPlanCredit userPlanCredit=planDao.getUserPlanCreditByCreditId(item.getCreditId());
            boolean belongToPlan=userPlanCredit!=null;
            //投资记录
            XWTenderRecord tenderRecord=projectDao.getTenderRecordById(credit.getTenderId());
            //实际算出来的还款金额
            SysCreditToRepay actualRepayDetail=new SysCreditToRepay();
            actualRepayDetail.setCreditId(item.getCreditId());//债权ID
            actualRepayDetail.setOrderId(orderId);//订单ID
            actualRepayDetail.setInvestorId(item.getInvestorId());//投资人平台用户id
            XinwangAccount investorXinwangAccount=accountDao.getXWRoleAccount(item.getInvestorId(), UserRole.INVESTOR);
            actualRepayDetail.setInvestorPlatformUserNo(investorXinwangAccount.getPlatformUserNo());//投资人存管账号
            //债权总利率
            BigDecimal creditTotalRate=projectInfo.getAnnnualInterestRate().add(projectRate.getProjectRaiseInterestRate()).add(tenderIncreaseInterestRate);
            //所属计划利率
            BigDecimal planCurrentRate=BigDecimal.ZERO;
            if(belongToPlan){
                SysUserPlan userPlan = planDao.getUserPlanById(userPlanCredit.getUserPlanId());
                planCurrentRate = planCurrentRateMap.get(userPlan.getPlanId());
                if (planCurrentRate == null) {
                    planCurrentRate = getPlanCurrentRate(userPlan.getPlanId());
                    planCurrentRateMap.put(userPlan.getPlanId(), planCurrentRate);
                }
                actualRepayDetail.setPlanId(userPlan.getPlanId());//关联的计划id
            }
            //到期还款
            if(type==SysRepayOperationType.REPAY){
                //债权加入了计划
                if(belongToPlan){
                    //如果债权总利率大于计划利率，就要扣除息差
                    if(creditTotalRate.compareTo(planCurrentRate)>0){
                        //如果是等额本息
                        if(projectInfo.getRepaymentWay()== RepaymentWay.DEBX){
                            BigDecimal planPrincipal=getDEBXAmountOfTargetTerm(projectInfo.getMonthProjectPeriod(),tenderRecord,planCurrentRate,currentTerm,SysTradeFeeCode.TZ_BJ);
                            BigDecimal planInterest=getDEBXAmountOfTargetTerm(projectInfo.getMonthProjectPeriod(),tenderRecord,planCurrentRate,currentTerm,SysTradeFeeCode.TZ_LX);
                            //如果标的利率大于计划利率
                            if (projectInfo.getAnnnualInterestRate().compareTo(planCurrentRate)>0){
                                actualRepayDetail.setPrincipal(planPrincipal);//本金
                                actualRepayDetail.setInterest(planInterest);//利息
                                actualRepayDetail.setServiceCharge(item.getPrincipal().add(item.getInterest()).subtract(planPrincipal).subtract(planInterest));//平台服务费
                                //标利率比计划利率大时，加息要付给平台，但由于加息是由平台付的，所以加息直接不处理就可以了
                            }
                            //如果标的利率小于等于计划利率
                            else{
                                actualRepayDetail.setPrincipal(item.getPrincipal());
                                actualRepayDetail.setInterest(item.getInterest());
                                //因为债权总利率是比计划利率大的，所以计划利率比标利率多的部分还是属于投资人的加息，由平台付
                                BigDecimal difference=planPrincipal.add(planInterest).subtract(item.getPrincipal()).subtract(item.getInterest());
                                if(difference.compareTo(BigDecimal.ZERO)>0){
                                    actualRepayDetail.setTenderIncreaseInterest(difference);//加息券加息
                                }
                            }
                        }
                        //如果是每月付息
                        else if(projectInfo.getRepaymentWay()==RepaymentWay.MYFX){
                            actualRepayDetail.setPrincipal(item.getPrincipal());
                            BigDecimal planInterest=tenderRecord.getAmount().multiply(planCurrentRate).divide(new BigDecimal(12), 2, BigDecimal.ROUND_DOWN);
                            if(projectInfo.getAnnnualInterestRate().compareTo(planCurrentRate)>0){
                                actualRepayDetail.setInterest(planInterest);
                                actualRepayDetail.setServiceCharge(item.getInterest().subtract(planInterest));
                            }
                            else{
                                actualRepayDetail.setInterest(item.getInterest());
                                BigDecimal difference=planInterest.subtract(item.getInterest());
                                if(difference.compareTo(BigDecimal.ZERO)>0){
                                    actualRepayDetail.setTenderIncreaseInterest(difference);
                                }
                            }
                        }
                        //如果是一次付清
                        else if(projectInfo.getRepaymentWay()==RepaymentWay.YCFQ){
                            actualRepayDetail.setPrincipal(item.getPrincipal());
                            BigDecimal planInterest=BigDecimal.ZERO;
                            if (projectInfo.getMonthProjectPeriod() == 0) {
                                planInterest = tenderRecord.getAmount().multiply(planCurrentRate).multiply(new BigDecimal(projectInfo.getDayProjectPeriod())).divide(
                                        new BigDecimal(365), 2,	BigDecimal.ROUND_HALF_UP);
                            } else {
                                planInterest = tenderRecord.getAmount().multiply(planCurrentRate).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).divide(new BigDecimal(12),
                                        2, BigDecimal.ROUND_HALF_UP);
                            }
                            if(projectInfo.getAnnnualInterestRate().compareTo(planCurrentRate)>0){
                                actualRepayDetail.setInterest(planInterest);
                                actualRepayDetail.setServiceCharge(item.getInterest().subtract(planInterest));
                            }
                            else{
                                actualRepayDetail.setInterest(item.getInterest());
                                BigDecimal difference=planInterest.subtract(item.getInterest());
                                if(difference.compareTo(BigDecimal.ZERO)>0){
                                    actualRepayDetail.setTenderIncreaseInterest(difference);
                                }
                            }
                        }
                    }
                    //如果债权总利率小于等于计划利率
                    else{
                        actualRepayDetail.setPrincipal(item.getPrincipal());
                        actualRepayDetail.setInterest(item.getInterest());
                        actualRepayDetail.setTenderIncreaseInterest(item.getTenderIncreaseInterest());
                        actualRepayDetail.setProjectIncreaseInterest(item.getProjectIncreaseInterest());//标加息
                    }
                    //平台把逾期罚息收了
                    actualRepayDetail.setServiceCharge(actualRepayDetail.getServiceCharge().add(item.getOverduePenalty()));
                }
                //债权没有加入计划
                else{
                    actualRepayDetail.setPrincipal(item.getPrincipal());
                    actualRepayDetail.setInterest(item.getInterest());
                    actualRepayDetail.setTenderIncreaseInterest(item.getTenderIncreaseInterest());
                    actualRepayDetail.setProjectIncreaseInterest(item.getProjectIncreaseInterest());
                    actualRepayDetail.setOverduePenalty(item.getOverduePenalty());//逾期罚息 7004 给投资人
                }
                //逾期手续费
                actualRepayDetail.setOverdueCommission(item.getOverdueCommission());//逾期手续费
            }
            //提前还款
            else{
                //债权加入了计划
                if(belongToPlan){
                    //如果债权总利率大于计划利率，就要扣除息差
                    if(creditTotalRate.compareTo(planCurrentRate)>0){
                        actualRepayDetail.setPrincipal(item.getPrincipal());
                        BigDecimal planInterest = (item.getInterest().add(item.getProjectIncreaseInterest()).add(item.getTenderIncreaseInterest())).multiply(planCurrentRate)
                                .divide(creditTotalRate,2 ,RoundingMode.HALF_UP);
                        if(projectInfo.getAnnnualInterestRate().compareTo(planCurrentRate)>0){
                            actualRepayDetail.setInterest(planInterest);
                            actualRepayDetail.setServiceCharge(item.getInterest().subtract(planInterest));
                        }
                        else{
                            actualRepayDetail.setInterest(item.getInterest());
                            BigDecimal difference=planInterest.subtract(item.getInterest());
                            if(difference.compareTo(BigDecimal.ZERO)>0){
                                actualRepayDetail.setTenderIncreaseInterest(difference);
                            }
                        }
                    }
                    //如果债权总利率小于等于计划利率
                    else{
                        actualRepayDetail.setPrincipal(item.getPrincipal());
                        actualRepayDetail.setInterest(item.getInterest());
                        actualRepayDetail.setTenderIncreaseInterest(item.getTenderIncreaseInterest());
                        actualRepayDetail.setProjectIncreaseInterest(item.getProjectIncreaseInterest());
                    }
                    //平台把违约金收了
                    actualRepayDetail.setServiceCharge(actualRepayDetail.getServiceCharge().add(item.getPrepayPenalty()));
                }
                //债权没有加入计划
                else{
                    actualRepayDetail.setPrincipal(item.getPrincipal());
                    actualRepayDetail.setInterest(item.getInterest());
                    actualRepayDetail.setTenderIncreaseInterest(item.getTenderIncreaseInterest());
                    actualRepayDetail.setProjectIncreaseInterest(item.getProjectIncreaseInterest());
                    actualRepayDetail.setPrepayPenalty(item.getPrepayPenalty());
                }
            }
            //加入返回list
            //成交服务费
            actualRepayDetail.setDealFee(item.getDealFee());
            //提前还款罚息平台分成
            actualRepayDetail.setPenaltyDivide(item.getPenaltyDivide());
            //利息管理费 7035
            actualRepayDetail.setInterestServiceFee(item.getInterestServiceFee());

            actualRepayDetailList.add(actualRepayDetail);
        }
        return actualRepayDetailList;
    }

    /**
     * 因为省心计划和月升计划的利率取的不同字段，而且月生计划的利率是动态的，所以要计算计划当前利率是什么
     * @param planId
     * @return
     * @throws Exception
     */
    private BigDecimal getPlanCurrentRate(Integer planId)throws Exception{
        SysPlan plan=planDao.getPlanById(planId);
        BigDecimal planCurrentRate=BigDecimal.ZERO;
        if(plan.getType().equals(SysPlan_Type.SHENG_XIN.getCode()) ){
            planCurrentRate=plan.getInvestRate();
        }
        else if(plan.getType().equals(SysPlan_Type.YUE_SHENG.getCode())){
            int witchNaturalMonth=DateUtil.nowInWitchNaturalMonth(plan.getBearrateTime());
            planCurrentRate=plan.getMinYearlyRate().add(plan.getMonthIncreaseRate().multiply(new BigDecimal(witchNaturalMonth-1)));
            if(planCurrentRate.compareTo(plan.getMaxYearlyRate())>0){
                planCurrentRate=plan.getMaxYearlyRate();
            }
        }
        return planCurrentRate;
    }



    private BigDecimal getDEBXAmountOfTargetTerm(int totalTerms, XWTenderRecord tenderRecord,BigDecimal annualInterestRate,int targetTerm,int type) throws Exception {
        //月利率
        BigDecimal monthRate = annualInterestRate.setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        //剩余本金
        BigDecimal remainTotal = tenderRecord.getAmount();
        //每月本息和
        BigDecimal monthPayTotal = getDEBXMonthPayTotal(tenderRecord.getAmount(), monthRate, totalTerms);
        List<BigDecimal> list=new ArrayList<>();

        for (int term = 1; term <= totalTerms; term++) {
            //当月利息
            BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal amount=BigDecimal.ZERO;
            if(type==SysTradeFeeCode.TZ_LX)
            {
                // 利息
                if (totalTerms == term) {
                    amount = monthPayTotal.subtract(remainTotal).compareTo(BigDecimal.ZERO) > 0
                            ? monthPayTotal.subtract(remainTotal) : BigDecimal.ZERO;
                } else {
                    amount = interest;
                }
                BigDecimal thisTermBj=monthPayTotal.subtract(amount);
                remainTotal = remainTotal.subtract(thisTermBj);
            }
            else if(type==SysTradeFeeCode.TZ_BJ)
            {
                // 本金
                if (totalTerms == term) {
                    amount = remainTotal;
                } else {
                    amount = monthPayTotal.subtract(interest);
                }
                remainTotal = remainTotal.subtract(amount);
            }
            list.add(amount);
        }
        return list.get(targetTerm-1);
    }

    private BigDecimal getDEBXMonthPayTotal(BigDecimal total, BigDecimal monthRate, int terms) {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate).multiply(tmp).divide(tmp.subtract(new BigDecimal(1)), 2,
                BigDecimal.ROUND_HALF_UP);
    }
}
