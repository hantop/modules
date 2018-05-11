package com.fenlibao.dao.pms.da.statistics.invest;

import com.fenlibao.model.pms.da.statistics.invest.Register;
import com.fenlibao.model.pms.da.statistics.invest.form.RegisterForm;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bogle on 2015/12/30.
 */
public interface RegMapper {

    /**
     * 注册信息统计
     *
     * @param register
     * @param bounds
     * @return
     */
    List<Register> findRegList(RegisterForm register, RowBounds bounds);

    List<Register> findAllRegList(RegisterForm register);

    BigDecimal getInvestMoneyTotal(RegisterForm register);

    BigDecimal getInvestSumTotal(RegisterForm register);

    RegisterForm getMoneyTotal(RegisterForm register);
}
