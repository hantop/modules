package com.fenlibao.service.pms.da.statistics.invest;

import com.fenlibao.model.pms.da.statistics.invest.Register;
import com.fenlibao.model.pms.da.statistics.invest.form.RegisterForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bogle on 2015/12/30.
 */
public interface RegService {

    /**
     * 注册统计
     * @param register
     * @param bounds
     * @return
     */
    List<Register> findRegList(RegisterForm register, RowBounds bounds);

    /**
     * 查询所有的数据
     * @param register
     * @return
     */
    List<Register> findRegList(RegisterForm register);

    /**
     * 累计投资金额总计
     * @param register
     * @return
     */
    BigDecimal getInvestMoneyTotal(RegisterForm register);

    /**
     * 在投金额
     * @param register
     * @return
     */
    BigDecimal getInvestSumTotal(RegisterForm register);

    /**
     * 统计总计
     * @param register
     * @return
     */
    RegisterForm getMoneyTotal(RegisterForm register);
}
