package com.fenlibao.dao.pms.da.biz.loanmanage;

import com.fenlibao.model.pms.da.biz.form.LoanManageForm;
import com.fenlibao.model.pms.da.biz.viewobject.BidVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface LoanManageMapper {
    /**
     * <dl>
     * 描述：借款管理.
     * </dl>
     *
     * @return
     * @throws Throwable
     */
    List<BidVO> search(LoanManageForm loanManageForm,
                       RowBounds bounds) throws Throwable;

//    T6230 getLoan(int loanId);

    void sealedBidding(int loanId);

    /**
     * 查询委托支付标授权订单状态
     * @param bidIds
     * @return
     */
    List<Map<String, Object>> getEntrustPayBidState(@Param("bidIds") List<Integer> bidIds);

    BidVO getProjictInfoByLoanId(@Param("loanId")int loanId);

    /**
     * 查询标的信息
     * @param loanId
     * @return
     */
    BidVO getBidInfoByLoanId(@Param("loanId")int loanId);
}
