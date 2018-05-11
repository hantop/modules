package com.fenlibao.service.pms.da.planCenter.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.planCenter.PlanTypeMapper;
import com.fenlibao.model.pms.da.planCenter.PlanType;
import com.fenlibao.model.pms.da.planCenter.form.PlanTypeForm;
import com.fenlibao.service.pms.da.planCenter.PlanTypeService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
@Service
public class PlanTypeServiceImpl implements PlanTypeService {
    private static final Logger LOG = LoggerFactory.getLogger(PlanTypeServiceImpl.class);

    @Autowired
    private PlanTypeMapper planTypeMapper;

    private String preference;
    private String mon;
    private String isNovice;// 新手计划

    @PostConstruct
    public void init() {
        Config.loadProperties();
        preference = Config.get("preference.description");//计划描述
        mon = Config.get("mon.description");
        isNovice = Config.get("isNovice.description");
    }

    @Override
    public List<PlanType> findPlanTypePager(PlanTypeForm planTypeForm, RowBounds bounds) {
        List<PlanType> planTypes = planTypeMapper.findPlanTypePager(planTypeForm, bounds);
        if (planTypes.size() > 0) {
            for (PlanType planType: planTypes) {
                // 计划利息
                if (planType.getRate() != null){
                    if(planType.getRate().compareTo(BigDecimal.ZERO) > 0) {
                        planType.setRate(planType.getRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                    }
                }else {
                    planType.setRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                }
                // 加息
                if(planType.getRaisedRate() != null){
                    if(planType.getRaisedRate().compareTo(BigDecimal.ZERO) > 0) {
                        planType.setRaisedRate(planType.getRaisedRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                    }
                }else {
                    planType.setRaisedRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                }
            }
        }
        return planTypes;
    }

    @Override
    public int saveOrUpdatePlan(PlanType planType) throws Exception{
        int code = -1;
        if(planType != null){
            if ((planType.getCustomLabel1() != null && planType.getCustomLabel1() != "") || (planType.getCustomLabel2() != null && planType.getCustomLabel2() != "")){
                planType.setLabel(buildPlanLabel(planType.getCustomLabel1(), planType.getCustomLabel2()));
            }
            if(planType.getRate() != null){
                if(planType.getRate().compareTo(BigDecimal.ZERO) > 0) {
                    planType.setRate(planType.getRate().divide(new BigDecimal(100)));
                }
            }
            if(planType.getRaisedRate() != null){
                if(planType.getRaisedRate().compareTo(BigDecimal.ZERO) > 0) {
                    planType.setRaisedRate(planType.getRaisedRate().divide(new BigDecimal(100)));
                }
            }else {
                planType.setRaisedRate(BigDecimal.ZERO);
            }
            code = planTypeMapper.updatePlanTypeByPrimaryKey(planType);
        }
        return code;
    }

    @Override
    public PlanType findPlanTypeById(int id) {
        PlanType planType = planTypeMapper.findPlanTypeById(id);
        if(planType != null){
            if(planType.getRate().compareTo(BigDecimal.ZERO) > 0){
                planType.setRate(planType.getRate().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }
            if(planType.getRaisedRate().compareTo(BigDecimal.ZERO) > 0){
                planType.setRaisedRate(planType.getRaisedRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP));
            }else {
                planType.setRaisedRate(null);
            }
            if (planType.getLabel() != null){
                String[] label = planType.getLabel().split(",");
                if(label.length >= 2){
                    planType.setCustomLabel1(label[0]);
                    planType.setCustomLabel2(label[1]);
                }else {
                    planType.setCustomLabel1(label[0]);
                }
            }
        }
        return planType;
    }

    @Override
    public int updatePlanTypeStatus(int id, String status) throws Exception {
        int code = -1;
        if (id > 0){
            PlanType planType = planTypeMapper.findPlanTypeById(id);
            if(planType != null && !planType.getStatus().equals(status)){
                code = planTypeMapper.setStatus(id, status);
            }
        }
        return code;
    }

    //标签
    public String buildPlanLabel(String label1,String label2){
        StringBuilder label = new StringBuilder("");
        if(!StringHelper.isEmpty(label1)){
            label1 = label1.replaceAll(",", "");
            if(label.length()>0){
                label.append(",");
            }
            label.append(StringHelper.trim(label1));
        }
        if(!StringHelper.isEmpty(label2)){
            label2 = label2.replaceAll(",", "");
            if(label.length()>0){
                label.append(",");
            }
            label.append(StringHelper.trim(label2));
        }
        return label.toString();
    }
}
