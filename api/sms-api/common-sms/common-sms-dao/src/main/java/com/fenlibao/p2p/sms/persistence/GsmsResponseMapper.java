package com.fenlibao.p2p.sms.persistence;


import com.fenlibao.p2p.sms.domain.GsmsResponse;

import java.util.List;

public interface GsmsResponseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GsmsResponse record);

    int insertSelective(GsmsResponse record);

    GsmsResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GsmsResponse record);

    int updateByPrimaryKey(GsmsResponse record);

    int insertBatch(List<GsmsResponse> gsmsResponses,long id);
}