package com.fenlibao.service.pms.da.statistics.withdraw;

import com.fenlibao.dao.pms.da.statistics.withdraw.WithdrawMapper;
import com.fenlibao.model.pms.da.statistics.invest.Withdraw;
import com.fenlibao.model.pms.da.statistics.invest.form.WithdrawForm;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
@Service
public class WithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    /**
     * 获取体现信息
     *
     * @param withdrawForm
     * @param bounds
     * @return
     */
    public List<Withdraw> findWithdraw(WithdrawForm withdrawForm, RowBounds bounds) {
        return withdrawMapper.findWithdraw(withdrawForm, bounds);
    }

    /**
     * 获取总人数和提现总金额
     * @param withdrawForm
     * @return
     */
    public Withdraw getWithdrawTotal(WithdrawForm withdrawForm) {
        return withdrawMapper.getWithdrawTotal(withdrawForm);
    }
}
