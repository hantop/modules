package com.fenlibao.service.pms.common.client;

import com.fenlibao.model.pms.common.client.TAdvertImage;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ClientAdvertService {

    List<TAdvertImage> getAdvertImageList(TAdvertImage image, RowBounds bounds);

    int saveAdvertImage(TAdvertImage image);

    List<TAdvertImage> getAdvertImageListByIds(String[] idArray);

    int deleteAdvertImageByIds(String[] idArray);

    int updateAdvertImage(TAdvertImage image);

}
