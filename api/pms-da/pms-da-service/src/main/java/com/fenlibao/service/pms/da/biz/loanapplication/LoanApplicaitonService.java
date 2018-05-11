package com.fenlibao.service.pms.da.biz.loanapplication;

import com.fenlibao.model.pms.da.biz.LoanApplication;
import com.fenlibao.model.pms.da.biz.form.LoanApplicationEditForm;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface LoanApplicaitonService {

    /**
     * 更新处理状态
     *  @param id
     * @param processingOpinion
     * @param processingStatus
     * @param nopassReason
     */
    int updateProcessingStatus(int id, String processingOpinion, int processingStatus, int nopassReason);

    /**
     * 修改借款申请
     *
     * @param loanApplicationEditForm
     */
    int updateLoanApplication(LoanApplicationEditForm loanApplicationEditForm);

    /**
     * 根据ID获取借款申请
     *
     * @param id
     * @return
     */
    LoanApplication getLoanApplicationById(int id);

    /**
     * 查询借款申请列表
     *
     * @param startDate
     * @param endDate
     * @param phonenum
     * @param processingStatus
     * @param bounds
     * @return
     */
    List<LoanApplication> getLoanApplications(Date startDate, Date endDate, String phonenum, String processingStatus, RowBounds bounds);
}
