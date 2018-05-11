package com.fenlibao.dao.pms.common.client;

import com.fenlibao.model.pms.common.client.TAdvertImage;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Lullaby on 2015/7/28.
 */
public interface ClientAdvertMapper {

    List<TAdvertImage> getAdvertImageList(TAdvertImage image, RowBounds bounds);

    int saveAdvertImage(TAdvertImage image);

    List<TAdvertImage> getAdvertImageListByIds(String[] idArray);

    int deleteAdvertImageByIds(String[] idArray);

    int updateAdvertImage(TAdvertImage image);

}
