package com.fenlibao.p2p.sms.persistence;

import com.fenlibao.p2p.sms.domain.MediaItem;

public interface MediaItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MediaItem record);

    int insertSelective(MediaItem record);

    MediaItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MediaItem record);

    int updateByPrimaryKeyWithBLOBs(MediaItem record);

    int updateByPrimaryKey(MediaItem record);
}