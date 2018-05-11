package com.fenlibao.dao.pms.da.biz.afterLoan;

import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.afterLoan.BorrowInfoAfterLoan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */
public interface AfterLoanMapper {

    public BorrowInfoAfterLoan getBorrowInfoAferLoanByBidId(int bidId);

    public  void saveOrUpdateBorrowerInfo(BorrowInfoAfterLoan borrowInfoAfterLoan);

    public List<LoanList> afterLoanList(@Param("borrowerName")String borrowerName, @Param("borrowerAccount")String borrowerAccount, RowBounds rowBounds);
}
