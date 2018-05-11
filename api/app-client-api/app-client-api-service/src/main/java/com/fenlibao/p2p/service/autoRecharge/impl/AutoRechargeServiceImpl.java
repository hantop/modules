package com.fenlibao.p2p.service.autoRecharge.impl;

import com.fenlibao.p2p.dao.autoRecharge.AutoRechargeDao;
import com.fenlibao.p2p.model.entity.TransferApplication;
import com.fenlibao.p2p.service.autoRecharge.AutoRechargeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
@Service
public class AutoRechargeServiceImpl implements AutoRechargeService {
    @Resource
    private AutoRechargeDao autoRecharge;

    public List<TransferApplication> findHardList(){
        return autoRecharge.findHardList();
    }

    public int update(TransferApplication ta){
        return autoRecharge.update(ta);
    }
}
