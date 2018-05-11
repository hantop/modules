package com.fenlibao.p2p.sms.persistence;


import com.fenlibao.p2p.sms.domain.MTPack;

public interface MTPackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MTPack record);

    int insertSelective(MTPack record);

    MTPack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MTPack record);

    int updateByPrimaryKey(MTPack record);
}