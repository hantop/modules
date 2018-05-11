package com.fenlibao.dao.pms.da.biz.repayment;

import com.fenlibao.model.pms.da.biz.form.RepaymentForm;
import com.fenlibao.model.pms.da.biz.viewobject.RepaymentVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RepaymentMapper {
    /**
     * 还款列表
     * @param repaymentForm
     * @param bounds
     * @return
     * @throws Throwable
     */
    List<RepaymentVO> getRepaymentList(RepaymentForm repaymentForm,
                                       RowBounds bounds);

    /**
     * 精确查询异常还款标的详情
     * @param repaymentForm
     * @return
     */
    RepaymentVO getErrorRepayment(RepaymentForm repaymentForm);

    /**
     * 查询标的担保账户
     * @param bidId
     * @return
     */
    String getGuaranteePlatformUser(int bidId);

}
