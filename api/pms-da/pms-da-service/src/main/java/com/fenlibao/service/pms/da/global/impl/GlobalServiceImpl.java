package com.fenlibao.service.pms.da.global.impl;

import com.fenlibao.dao.pms.da.global.GlobalMapper;
import com.fenlibao.model.pms.da.Bank;
import com.fenlibao.service.pms.da.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Service
public class GlobalServiceImpl implements GlobalService {

    @Autowired
    private GlobalMapper globalMapper;

    @Override
    public List<Bank> getBankList(){
        return globalMapper.getBankList();
    }
}
