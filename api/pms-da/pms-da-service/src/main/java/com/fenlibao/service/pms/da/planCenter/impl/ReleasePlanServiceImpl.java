package com.fenlibao.service.pms.da.planCenter.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.planCenter.ReleasePlanMapper;
import com.fenlibao.model.pms.da.planCenter.PlanMarketingSetting;
import com.fenlibao.model.pms.da.planCenter.form.PlanMarketSettingForm;
import com.fenlibao.service.pms.da.planCenter.ReleasePlanService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
@Service
public class ReleasePlanServiceImpl implements ReleasePlanService {
    private static final Logger LOG = LoggerFactory.getLogger(ReleasePlanServiceImpl.class);

    @Autowired
    private ReleasePlanMapper releasePlanMapper;

    private String preference;
    private String mon;

    @PostConstruct
    public void init() {
        Config.loadProperties();
        preference = Config.get("preference.description");//计划描述
        mon = Config.get("mon.description");
    }

    @Transactional
    @Override
    public int saveOrUpdatePlan(PlanMarketingSetting planMarketingSetting) throws Exception{
        int code = -1;
        String label = "";
        if(planMarketingSetting.getType() == 1){//月升计划
            // 随时退出标识
            planMarketingSetting.setCanQuit(1);
            planMarketingSetting.setDescription(mon);
            planMarketingSetting.setCycle(planMarketingSetting.getMonCycleShow());
            planMarketingSetting.setInvestRate(null);
            planMarketingSetting.setRaiseRate(null);//暂时月升计划不加息
            if(planMarketingSetting.getMaxYearlyRate() != null){
                if(planMarketingSetting.getMaxYearlyRate().compareTo(BigDecimal.ZERO) > 0) {
                    planMarketingSetting.setMaxYearlyRate(planMarketingSetting.getMaxYearlyRate().divide(new BigDecimal(100)));
                }
            }
            if(planMarketingSetting.getMinYearlyRate() != null ){
                if (planMarketingSetting.getMinYearlyRate().compareTo(BigDecimal.ZERO) > 0) {
                    planMarketingSetting.setMinYearlyRate(planMarketingSetting.getMinYearlyRate().divide(new BigDecimal(100)));
                }
            }
            if(planMarketingSetting.getMoIncreaseRate() != null){
                if(planMarketingSetting.getMoIncreaseRate().compareTo(BigDecimal.ZERO) > 0) {
                    planMarketingSetting.setMoIncreaseRate(planMarketingSetting.getMoIncreaseRate().divide(new BigDecimal(100)));
                }
            }
        }else if(planMarketingSetting.getType() == 2){//省心计划
            // 随时退出标识
            planMarketingSetting.setCanQuit(0);
            planMarketingSetting.setDescription(preference);
            planMarketingSetting.setCycle(planMarketingSetting.getPreferenceCycleShow());
            planMarketingSetting.setMinYearlyRate(null);
            planMarketingSetting.setMaxYearlyRate(null);
            planMarketingSetting.setMoIncreaseRate(null);
            if(planMarketingSetting.getInvestRate() != null){
                if(planMarketingSetting.getInvestRate().compareTo(BigDecimal.ZERO) > 0) {
                    planMarketingSetting.setInvestRate(planMarketingSetting.getInvestRate().divide(new BigDecimal(100)));
                }
            }
        }
        if(planMarketingSetting.getRaiseRate() != null){
            if (planMarketingSetting.getRaiseRate().compareTo(BigDecimal.ZERO) > 0) {
                planMarketingSetting.setRaiseRate(planMarketingSetting.getRaiseRate().divide(new BigDecimal(100)));
            }
        }
        if(planMarketingSetting.getId() > 0){
            //计划主表
            code = releasePlanMapper.updateInvestPlanByPrimaryKey(planMarketingSetting);
            if(code > 0) {
                if (planMarketingSetting.getTotalUserAssets() != null || planMarketingSetting.getUserAccumulatedIncome() != null
                        || planMarketingSetting.getUserInvestingAmount() != null || planMarketingSetting.isTargetUser()) {
                    //营销设置表
                   code = this.saveOrUpdateMarketSetting(planMarketingSetting);
                }else{
                    releasePlanMapper.deleteMarketSetting(planMarketingSetting.getId());
                }
                if ((planMarketingSetting.getCustomLabel1() != null && planMarketingSetting.getCustomLabel1().trim() != "")
                        || (planMarketingSetting.getCustomLabel2() != null && planMarketingSetting.getCustomLabel2().trim() != "")
                        || (planMarketingSetting.getComment() != null && planMarketingSetting.getComment().trim() != "")) {
                    label = buildPlanLabel(planMarketingSetting.getCustomLabel1(),planMarketingSetting.getCustomLabel2());
                    planMarketingSetting.setLabel(label);
                    //标签
                    code = this.saveOrUpdateProductLabel(planMarketingSetting);
                }else{
                    releasePlanMapper.deleteProductLabel(planMarketingSetting.getId());
                }
            }
        }else{
            //计划主表
            boolean flag = true;
            while (flag) {
                try{
                    code = releasePlanMapper.insertIntoInvestPlan(planMarketingSetting);
                    flag = false;
                }catch (Exception e){
                    if(e instanceof DataIntegrityViolationException) {
                        Thread.sleep(1000);
                    }else{
                        LOG.error("操作异常", e);
                        throw(e);
                    }
                }
            } ;
            if(code > 0){
                if(planMarketingSetting.getTotalUserAssets() != null || planMarketingSetting.getUserAccumulatedIncome() != null
                        || planMarketingSetting.getUserInvestingAmount() != null || planMarketingSetting.isTargetUser()) {
                    //营销设置表
                    code = this.saveOrUpdateMarketSetting(planMarketingSetting);
                }
                if((planMarketingSetting.getCustomLabel1() != null && planMarketingSetting.getCustomLabel1().trim() != "")
                        || (planMarketingSetting.getCustomLabel2() != null && planMarketingSetting.getCustomLabel2().trim() != "")
                        || (planMarketingSetting.getComment() != null && planMarketingSetting.getComment().trim() != "")){
                    label = buildPlanLabel(planMarketingSetting.getCustomLabel1(),planMarketingSetting.getCustomLabel2());
                    planMarketingSetting.setLabel(label);
                    //标签
                    code = this.saveOrUpdateProductLabel(planMarketingSetting);
                }
            }
        }
        return code;
    }

    @Transactional
    @Override
    public int saveOrUpdateMarketSetting(PlanMarketingSetting planMarketingSetting) throws Exception{
        int code = releasePlanMapper.getMarketSettingByProductTypeAndId(planMarketingSetting);
        if(code > 0){
            code = releasePlanMapper.updateMarkeSettingByPlanId(planMarketingSetting);
        }else{
            code = releasePlanMapper.insertIntoMarkeSetting(planMarketingSetting);
        }
        return code;
    }

    @Override
    public int saveOrUpdateProductLabel(PlanMarketingSetting planMarketingSetting) throws Exception{
        int code = releasePlanMapper.getProductLabelByProductTypeAndId(planMarketingSetting);
        if(code > 0){
            code = releasePlanMapper.updateProductLabelByPlanId(planMarketingSetting);
        }else{
            code = releasePlanMapper.insertIntoProductLabel(planMarketingSetting);
        }
        return code;
    }

    @Override
    public List<PlanMarketingSetting> findPlanMarketingSettingPager(PlanMarketSettingForm planMarketSettingForm, RowBounds bounds) {
        List<PlanMarketingSetting> list = releasePlanMapper.findPlanMarketingSettingPager(planMarketSettingForm, bounds);
        for (PlanMarketingSetting planMarketSetting: list) {
            planMarketSetting.setName(planMarketSetting.getName() + planMarketSetting.getNumber());
            if(planMarketSetting.getRaiseRate() != null && planMarketSetting.getRaiseRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketSetting.setRaiseRate(planMarketSetting.getRaiseRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
            }
            if(planMarketSetting.getType() == 1){
                if(planMarketSetting.getMinYearlyRate() != null && planMarketSetting.getMinYearlyRate().compareTo(BigDecimal.ZERO) > 0){
                    planMarketSetting.setMinYearlyRate(planMarketSetting.getMinYearlyRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }
                if(planMarketSetting.getMaxYearlyRate() != null && planMarketSetting.getMaxYearlyRate().compareTo(BigDecimal.ZERO) > 0){
                    planMarketSetting.setMaxYearlyRate(planMarketSetting.getMaxYearlyRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }
                if(planMarketSetting.getMoIncreaseRate() != null && planMarketSetting.getMoIncreaseRate().compareTo(BigDecimal.ZERO) > 0){
                    planMarketSetting.setMoIncreaseRate(planMarketSetting.getMoIncreaseRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }
            }else if(planMarketSetting.getType() == 2){
                if(planMarketSetting.getInvestRate() != null && planMarketSetting.getInvestRate().compareTo(BigDecimal.ZERO) > 0){
                    planMarketSetting.setInvestRate(planMarketSetting.getInvestRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
                }
            }
        }
        return list;
    }

    @Override
    public PlanMarketingSetting findPlanMarketingSettingById(int id) {
        PlanMarketingSetting planMarketingSetting = releasePlanMapper.findPlanMarketingSettingById(id);
        planMarketingSetting.setAmount(planMarketingSetting.getAmount().setScale(0));
        if(planMarketingSetting.getType() == 1){//月升计划
            planMarketingSetting.setMonCycleShow(planMarketingSetting.getCycle());
            if(planMarketingSetting.getInvestRate() != null && planMarketingSetting.getInvestRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketingSetting.setInvestRate(null);
            }
            if(planMarketingSetting.getMinYearlyRate() != null && planMarketingSetting.getMinYearlyRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketingSetting.setMinYearlyRate(planMarketingSetting.getMinYearlyRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                planMarketingSetting.setMinYearlyRateStr(planMarketingSetting.getMinYearlyRate().toString());
            }
            if(planMarketingSetting.getMaxYearlyRate() != null && planMarketingSetting.getMaxYearlyRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketingSetting.setMaxYearlyRate(planMarketingSetting.getMaxYearlyRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                planMarketingSetting.setMaxYearlyRateStr(planMarketingSetting.getMaxYearlyRate().toString());
            }
            if(planMarketingSetting.getMoIncreaseRate() != null && planMarketingSetting.getMoIncreaseRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketingSetting.setMoIncreaseRate(planMarketingSetting.getMoIncreaseRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                planMarketingSetting.setMoIncreaseRateStr(planMarketingSetting.getMoIncreaseRate().toString());
            }
        }else if(planMarketingSetting.getType() == 2){//省心计划
            planMarketingSetting.setPreferenceCycleShow(planMarketingSetting.getCycle());
            if(planMarketingSetting.getInvestRate() != null && planMarketingSetting.getInvestRate().compareTo(BigDecimal.ZERO) > 0){
                planMarketingSetting.setInvestRate(planMarketingSetting.getInvestRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }
        }
        if(planMarketingSetting.getRaiseRate() != null && planMarketingSetting.getRaiseRate().compareTo(BigDecimal.ZERO) > 0){
            planMarketingSetting.setRaiseRate(planMarketingSetting.getRaiseRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
        }

        if(planMarketingSetting.getLabel() != null && planMarketingSetting.getLabel() != ""){
            String[] label = planMarketingSetting.getLabel().split(",");
            if(label.length >= 2){
                planMarketingSetting.setCustomLabel1(label[0]);
                planMarketingSetting.setCustomLabel2(label[1]);
            }else {
                planMarketingSetting.setCustomLabel1(label[0]);
            }
        }
        return planMarketingSetting;
    }

    //标签
    public String buildPlanLabel(String label1,String label2){
        StringBuilder label = new StringBuilder("");
        if(!StringHelper.isEmpty(label1)){
            if(label.length()>0){
                label.append(",");
            }
            label.append(StringHelper.trim(label1));
        }
        if(!StringHelper.isEmpty(label2)){
            if(label.length()>0){
                label.append(",");
            }
            label.append(StringHelper.trim(label2));
        }
        return label.toString();
    }
}
