package com.fenlibao.service.pms.da.biz.loanList;

import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.form.LoanListForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */
public interface LoanListService {
    /*
    * 放款列表
    * */
    List<LoanList> getLoanList(LoanListForm loanListForm, RowBounds bounds) throws Exception;

    /**
     * 放款
     * @param loanId
     * @throws Exception
     */
    public String makeALoanApply(int loanId) throws Exception;

    /**
     * 流标
     * @param loanId 标ID
     * @throws Exception
     */
    public String flowBidApply(int loanId) throws Exception;

    /**
     * 贷后管理列表
     * @param loanListForm
     * @param bounds
     * @return
     * @throws Exception
     */
    public List<LoanList> afterLoanList(LoanListForm loanListForm, RowBounds bounds) throws Exception;

}
