package com.fenlibao.service.pms.common.client;

import com.fenlibao.model.pms.common.client.TStartupImage;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Lullaby on 2015/7/27.
 */
public interface ClientStartupService {

    int saveStartupImage(TStartupImage image);

    List<TStartupImage> getStartupImageList(TStartupImage image, RowBounds bounds);

    List<TStartupImage> getStartupImageListByIds(String[] idArray);

    int deleteStartupImageByIds(String[] idArray);

    int updateStartupImage(TStartupImage image);

}
