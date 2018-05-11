package com.fenlibao.service.pms.common.client.impl;

import com.fenlibao.dao.pms.common.client.ClientStartupMapper;
import com.fenlibao.model.pms.common.client.TStartupImage;
import com.fenlibao.service.pms.common.client.ClientStartupService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lullaby on 2015/7/27.
 */
@Service
public class ClientStartupServiceImpl implements ClientStartupService {

    @Resource
    private ClientStartupMapper clientStartupMapper;

    public int saveStartupImage(TStartupImage image) {
        return clientStartupMapper.saveStartupImage(image);
    }

    public List<TStartupImage> getStartupImageList(TStartupImage image, RowBounds bounds) {
        return clientStartupMapper.getStartupImageList(image, bounds);
    }

    public List<TStartupImage> getStartupImageListByIds(String[] idArray) {
        return clientStartupMapper.getStartupImageListByIds(idArray);
    }

    public int deleteStartupImageByIds(String[] idArray) {
        return clientStartupMapper.deleteStartupImageByIds(idArray);
    }

    public int updateStartupImage(TStartupImage image) {
        return clientStartupMapper.updateStartupImage(image);
    }

}
