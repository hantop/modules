package com.fenlibao.service.pms.da.biz.afterLoan.impl;

import com.fenlibao.dao.pms.da.biz.afterLoan.AfterLoanMapper;
import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.afterLoan.BorrowInfoAfterLoan;
import com.fenlibao.service.pms.da.biz.afterLoan.AfterLoanService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */
@Service
public class AfterLoanServiceImpl implements  AfterLoanService {
    @Resource
    private AfterLoanMapper afterLoanMapper;
    @Override
    public BorrowInfoAfterLoan getBorrowInfoAferLoanByBidId(int bidId) {
        return afterLoanMapper.getBorrowInfoAferLoanByBidId(bidId);
    }

    @Override
    public void saveOrUpdateInfo(BorrowInfoAfterLoan borrowInfoAfterLoan) {
        afterLoanMapper.saveOrUpdateBorrowerInfo(borrowInfoAfterLoan);
    }

    @Override
    public List<LoanList> afterLoanList(String borrowerName, String borrowerAccount, RowBounds rowBounds) throws Exception {
        return afterLoanMapper.afterLoanList( borrowerName,  borrowerAccount,rowBounds);
    }
}
