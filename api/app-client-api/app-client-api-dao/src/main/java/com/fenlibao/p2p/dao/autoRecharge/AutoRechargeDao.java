package com.fenlibao.p2p.dao.autoRecharge;

import com.fenlibao.p2p.model.entity.TransferApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface AutoRechargeDao {

    public List<TransferApplication> findHardList();

    public int update(TransferApplication ta);
}
