package com.fenlibao.service.pms.da.biz.loanOverview;

import com.fenlibao.model.pms.da.biz.LoanOverview;
import com.fenlibao.model.pms.da.biz.form.LoanOverviewForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface LoanOverviewService {
    List<LoanOverview> getLoanList(LoanOverviewForm loanOverviewForm, RowBounds bounds);

    Map<String, String> getLoanTotal(LoanOverviewForm loanOverviewForm);
}
