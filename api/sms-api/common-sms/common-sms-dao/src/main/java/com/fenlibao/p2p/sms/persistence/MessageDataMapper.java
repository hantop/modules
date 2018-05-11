package com.fenlibao.p2p.sms.persistence;


import com.fenlibao.p2p.sms.domain.MessageData;

import java.util.List;

public interface MessageDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageData record);

    int insertSelective(MessageData record);

    MessageData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageData record);

    int updateByPrimaryKey(MessageData record);

    int insertBatch(List<MessageData> msgDatas);
}