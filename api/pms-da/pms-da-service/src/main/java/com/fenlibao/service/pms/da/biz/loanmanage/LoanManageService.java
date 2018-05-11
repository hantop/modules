package com.fenlibao.service.pms.da.biz.loanmanage;

import com.fenlibao.model.pms.da.biz.form.LoanManageForm;
import com.fenlibao.model.pms.da.biz.viewobject.BidVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public interface LoanManageService {
    /**
     * <dl>
     * 描述：借款管理.
     * </dl>
     *
     * @return
     * @throws Throwable
     */
    List<BidVO> search(LoanManageForm loanManageForm, RowBounds bounds) throws Throwable;

    /**
     * 发标
     */
    String release(int loanId) throws Exception;

    /**
     * 封标
     */
    void sealedBidding(int loanId) throws Exception;
}
