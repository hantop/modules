package com.fenlibao.service.pms.common.client.impl;

import com.fenlibao.dao.pms.common.client.ClientAdvertMapper;
import com.fenlibao.model.pms.common.client.TAdvertImage;
import com.fenlibao.service.pms.common.client.ClientAdvertService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClientAdvertServiceImpl implements ClientAdvertService {

    @Resource
    private ClientAdvertMapper clientAdvertMapper;

    public List<TAdvertImage> getAdvertImageList(TAdvertImage image, RowBounds bounds) {
        return clientAdvertMapper.getAdvertImageList(image, bounds);
    }

    public int saveAdvertImage(TAdvertImage image) {
        return clientAdvertMapper.saveAdvertImage(image);
    }

    public List<TAdvertImage> getAdvertImageListByIds(String[] idArray) {
        return clientAdvertMapper.getAdvertImageListByIds(idArray);
    }

    public int deleteAdvertImageByIds(String[] idArray) {
        return clientAdvertMapper.deleteAdvertImageByIds(idArray);
    }

    public int updateAdvertImage(TAdvertImage image) {
        return clientAdvertMapper.updateAdvertImage(image);
    }

}
