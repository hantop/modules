package com.fenlibao.service.pms.da.biz.afterLoan;

import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.afterLoan.BorrowInfoAfterLoan;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */
public interface AfterLoanService {

    public BorrowInfoAfterLoan getBorrowInfoAferLoanByBidId(int bidId);

    public void saveOrUpdateInfo(BorrowInfoAfterLoan borrowInfoAfterLoan);

    /**
     * 贷后管理列表
     * @param borrowerName
     * @param borrowerAccount
     * @return
     * @throws Exception
     */
    public List<LoanList> afterLoanList(String borrowerName, String borrowerAccount,RowBounds bounds) throws Exception;
}
