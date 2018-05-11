package com.fenlibao.service.pms.da.biz.repayment;

import com.fenlibao.model.pms.da.biz.form.RepaymentForm;
import com.fenlibao.model.pms.da.biz.viewobject.RepaymentVO;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectPrepaymentConfig;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RepaymentService {

    /**
     * 还款列表
     * @param repaymentForm
     * @param bounds
     * @return
     */
    List<RepaymentVO> getRepaymentList(RepaymentForm repaymentForm, RowBounds bounds);

    /**
     * 还款提示框信息
     * @param bidId 标的Id
     * @param repayMethod 还款方式
     * @param isPreRepay 是否提前还款
     * @param isSubrogation 是否担保代偿
     * @return
     * @throws Throwable
     */
    Map<String, Object> getRepayDetailInfo(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation) throws Throwable;

    /**
     * 还款
     * @param bidId
     * @param repayMethod
     * @param isPreRepay
     * @param isSubrogation
     * @return
     * @throws Throwable
     *
     */
    String doRepay(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation,XWProjectPrepaymentConfig xwProjectPrepaymentConfig) throws Throwable;

    /**
     * 异常还款测试版(暂时可替换定时器人工再次发起还款请求)
     * @param bidId
     * @return
     * @throws Throwable
     */
    String doErrorRepay(int bidId) throws Throwable;

    /**
     * 异常放款标的详情
     * @param repaymentForm
     * @return
     */
    RepaymentVO getErrorRepayment(RepaymentForm repaymentForm);
}
