package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.AccessoryInfo;

public interface AccessoryInfoDao {

    public AccessoryInfo getAccessoryInfo(int id);

    public int insertAccessoryInfo(AccessoryInfo info);

}
