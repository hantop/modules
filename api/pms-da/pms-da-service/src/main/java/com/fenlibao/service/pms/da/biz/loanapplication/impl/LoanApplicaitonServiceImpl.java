package com.fenlibao.service.pms.da.biz.loanapplication.impl;

import com.fenlibao.dao.pms.da.biz.loanapplication.LoanApplicationMapper;
import com.fenlibao.model.pms.da.biz.LoanApplication;
import com.fenlibao.model.pms.da.biz.form.LoanApplicationEditForm;
import com.fenlibao.service.pms.da.biz.loanapplication.LoanApplicaitonService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
@Service
public class LoanApplicaitonServiceImpl implements LoanApplicaitonService {

    @Resource
    private LoanApplicationMapper loanApplicationMapper;

    @Override
    public int updateProcessingStatus(int id, String processingOpinion, int processingStatus, int nopassReason) {
        return loanApplicationMapper.updateProcessingStatus(id, processingOpinion, processingStatus, nopassReason);
    }

    @Override
    public int updateLoanApplication(LoanApplicationEditForm loanApplicationEditForm) {
        return loanApplicationMapper.updateLoanApplication(loanApplicationEditForm);
    }

    @Override
    public LoanApplication getLoanApplicationById(int id) {
        return loanApplicationMapper.getLoanApplicationById(id);
    }

    @Override
    public List<LoanApplication> getLoanApplications(Date startDate, Date endDate, String phonenum, String processingStatus, RowBounds bounds) {
        return loanApplicationMapper.getLoanApplications(startDate, endDate, phonenum, processingStatus, bounds);
    }
}
