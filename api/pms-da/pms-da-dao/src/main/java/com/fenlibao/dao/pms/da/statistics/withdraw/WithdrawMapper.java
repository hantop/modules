package com.fenlibao.dao.pms.da.statistics.withdraw;

import com.fenlibao.model.pms.da.statistics.invest.Withdraw;
import com.fenlibao.model.pms.da.statistics.invest.form.WithdrawForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
public interface WithdrawMapper {

    List<Withdraw> findWithdraw(WithdrawForm withdrawForm, RowBounds bounds);

    Withdraw getWithdrawTotal(WithdrawForm withdrawForm);
}
