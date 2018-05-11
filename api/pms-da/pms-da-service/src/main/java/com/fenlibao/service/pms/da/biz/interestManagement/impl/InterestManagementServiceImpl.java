package com.fenlibao.service.pms.da.biz.interestManagement.impl;

import com.fenlibao.dao.pms.da.biz.interestManagement.InterestManagementMapper;
import com.fenlibao.model.pms.da.biz.form.InterestManagementForm;
import com.fenlibao.model.pms.da.biz.interestManagement.InterestManagementRecord;
import com.fenlibao.service.pms.da.biz.interestManagement.InterestManagementService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterestManagementServiceImpl implements InterestManagementService{

    @Resource
    private InterestManagementMapper interestManagementMapper;

    @Override
    public List<InterestManagementRecord> getInterestManagementRecordList(RowBounds rowBounds) {
        List<InterestManagementRecord> interestManagementRecordList = interestManagementMapper.getInterestManagementRecordList(rowBounds);
        if(interestManagementRecordList.size() > 0){
            List<Integer> currentIds = new ArrayList<>();
            List<Map<String, Object>> currentPercents = new ArrayList<>();
            for (InterestManagementRecord temp: interestManagementRecordList) {
                // 修改前记录id
                if(!currentIds.contains(temp.getPid())){
                    currentIds.add(temp.getPid());
                }
            }
            if(currentIds.size() > 0){
                currentPercents = interestManagementMapper.getCurrentPercents(currentIds);
            }
            for (InterestManagementRecord temp: interestManagementRecordList) {
                if (temp != null){
                    // 修改后利息管理费
                    if(temp.getUpdatePercent().compareTo(BigDecimal.ZERO) >= 0){
                        temp.setUpdatePercentStr(temp.getUpdatePercent().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%");
                    }
                    // 修改前利息管理费
                    if(temp.getPid() > 0){
                        temp.setCurrentPercentStr(getCurrentStrByPidAndRecordId(currentPercents, temp.getPid(), temp.getRecordId()));
                    }
                }
            }
        }
        return interestManagementRecordList;
    }

    @Override
    public InterestManagementForm getInterestManagementForm(Integer state) {
        InterestManagementForm form = interestManagementMapper.getInterestManagementForm(state);
        if(form != null && form.getCurrentPercent().compareTo(BigDecimal.ZERO) >= 0){
            form.setCurrentPercentStr(form.getCurrentPercent().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%");
        }
        return form;
    }

    @Override
    public InterestManagementRecord getAuditInterestManagementInfo() {
        InterestManagementRecord interestManagementRecord = interestManagementMapper.getAuditInterestManagementInfo();
        if(interestManagementRecord != null){
            interestManagementRecord.setUpdatePercentStr(interestManagementRecord.getUpdatePercent().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%");
            // 更新前已启用利息管理费比例
            if(interestManagementRecord.getPid() > 0){
                BigDecimal currentPercent = interestManagementMapper.getCurrentPercentByPid(interestManagementRecord.getPid());
                if(currentPercent.compareTo(BigDecimal.ZERO) >= 0){
                    interestManagementRecord.setCurrentPercentStr(currentPercent.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%");
                }
            }
            return interestManagementRecord;
        }
        return null;
    }

    @Transactional
    @Override
    public void updateInterestManagementLogic(InterestManagementForm interestManagementForm) {
        interestManagementForm.setUpdatePercent(interestManagementForm.getUpdatePercent().divide(new BigDecimal(100)).setScale(4, RoundingMode.HALF_UP));
        Map<String, Object> interestManagementParamMap = new HashMap<>();
        interestManagementParamMap.put("interestManagementCostPercent", interestManagementForm.getUpdatePercent());
        interestManagementParamMap.put("state", 0);
        // 新增待审核利息管理费
        int interestManagementId = interestManagementMapper.addInterestManagement(interestManagementParamMap);
        if(interestManagementId > 0){
            // 操作记录
            InterestManagementRecord interestManagementRecord = new InterestManagementRecord();
            // 当前用户名
            String operator = (String) SecurityUtils.getSubject().getPrincipal();
            interestManagementRecord.setOperator(operator);// 操作人
            interestManagementRecord.setPid(Integer.valueOf(interestManagementForm.getCurrentPercentId()));
            interestManagementRecord.setSid(Integer.valueOf(interestManagementParamMap.get("interestManagementId").toString()));
            saveInterestManagementRecord(interestManagementRecord);
        }
    }

    @Override
    public void saveInterestManagementRecord(InterestManagementRecord interestManagementRecord) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pid", interestManagementRecord.getPid());
        paramMap.put("sid", interestManagementRecord.getSid());
        paramMap.put("operator", interestManagementRecord.getOperator());
        interestManagementMapper.saveInterestManagementRecord(paramMap);
    }

    @Transactional
    @Override
    public void changeInterestState(int recordId, int id, int state) {
        // 修改利息管理费状态
        interestManagementMapper.changeInterestState(id, state);
        // 修改操作记录
        String auditor = (String) SecurityUtils.getSubject().getPrincipal();
        interestManagementMapper.updateInterestManagementRecord(recordId, state, auditor);
    }

    private String getCurrentStrByPidAndRecordId(List<Map<String, Object>> currentPercents, Integer pid, Integer recordId) {
        if(currentPercents.size() > 0){
            for (Map<String, Object> map: currentPercents) {
                if (pid.toString().equals(map.get("pid").toString()) && recordId.toString().equals(map.get("recordId").toString())) {
                    return new BigDecimal(map.get("currentPercent").toString()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%";
                }
            }
        }
        return  null;
    }

}
