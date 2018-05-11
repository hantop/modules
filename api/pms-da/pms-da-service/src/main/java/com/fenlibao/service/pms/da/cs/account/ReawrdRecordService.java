package com.fenlibao.service.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.account.ReawrdRecord;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 奖励记录
 * Created by chenzhixuan on 2016/1/4.
 */
public interface ReawrdRecordService {
    /**
     * 根据手机号分页获取奖励记录列表
     *
     * @param transaction
     * @param bounds
     * @return
     */
    List<ReawrdRecord> findReawrdRecord(TransactionForm transaction, RowBounds bounds);

    /**
     * 获取返现卷
     *
     * @param transaction
     * @param bounds
     * @return
     */
    List<UserRedpackets> getUserRedpackets(TransactionForm transaction, RowBounds bounds);
    /**
     * 获取现金
     *
     * @param transaction
     * @param bounds
     * @return
     */
    List<ReawrdRecord> getCashRedPackets(TransactionForm transaction, RowBounds bounds);
    /**
     * 获取体验金
     *
     * @param transaction
     * @param bounds
     * @return
     */
    List<ReawrdRecord> getUserExperienceGold(TransactionForm transaction, RowBounds bounds);

    /**
     * 获取加息券
     *
     * @param transaction
     * @param bounds
     * @return
     */
    List<ReawrdRecord> getUserRateCoupon(TransactionForm transaction, RowBounds bounds);
}
