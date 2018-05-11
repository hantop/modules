package com.fenlibao.service.pms.da.cs.xieyi.impl;

import com.fenlibao.dao.pms.da.biz.xieyi.XieyiMapper;
import com.fenlibao.model.pms.da.biz.xieyi.XieyiInfo;
import com.fenlibao.service.pms.da.cs.xieyi.XieyiService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
@Service
public class XieyiServiceImpl implements XieyiService {

    @Autowired
    private XieyiMapper xieyiMapper;

    @Override
    public List<XieyiInfo> getXieyis(RowBounds bounds) {
        return xieyiMapper.getXieyis(bounds);
    }

    @Transactional
    @Override
    public boolean deleteById(String id) {
        xieyiMapper.deleteById(id);
        xieyiMapper.deleteByRecordId(id);
        return true;
    }

    @Transactional
    @Override
    public Long saveXieyiInfo(String uploadUser, String origName, String reName, long size, String xieyiType, Date yesterday, String realPath, String pkData) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", xieyiType);
        params.put("yesterday", yesterday);
        params.put("realPath", realPath);
        params.put("pkData", pkData);
        xieyiMapper.saveDownloadRecord(params);
        Long id = (Long)params.get("id");
        xieyiMapper.saveEntrustUpload(id, origName, reName, size, "pdf", new Date(),uploadUser);
        return id;
    }

    @Override
    public Map<String, Object> getPlatformUserNoByAccount(String account) {
        return xieyiMapper.getPlatformUserNoByAccount(account);
    }
}
