package com.fenlibao.service.pms.common.publicity.impl;

import com.fenlibao.dao.pms.common.publicity.AdvertImageMapper;
import com.fenlibao.model.pms.common.global.SystemType;
import com.fenlibao.model.pms.common.publicity.AdvertImageFile;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageVO;
import com.fenlibao.service.pms.common.publicity.AdvertImageService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 广告图
 * <p>
 * Created by chenzhixuan on 2016/5/11.
 */
@Service
public class AdvertImageServiceImpl implements AdvertImageService {
    @Resource
    private AdvertImageMapper advertImageMapper;

    @Override
    public void updateStatus(String id, Integer status) {
        advertImageMapper.updateStatus(id, status);
    }

    @Transactional
    @Override
    public void deleteAdvertImages(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            for (String advertImageId : ids) {
                // 删除广告图对应的版本
                advertImageMapper.deleteAdvertImageVersions(advertImageId);
                // 删除广告图对应的客户端
                advertImageMapper.deleteAdvertImageClientTypes(advertImageId);
                // 删除广告图文件
                advertImageMapper.deleteAdvertImageFiles(advertImageId);
                // 删除广告图
                advertImageMapper.deleteAdvertImage(advertImageId);
            }
        }
    }

    @Override
    public List<AdvertImageFile> getAdImgFiles(String adImgId) {
        return advertImageMapper.getAdImgFiles(adImgId);
    }

    @Override
    public List<Integer> getClientTypes(String adImgId) {
        return advertImageMapper.getClientTypes(adImgId);
    }

    @Override
    public List<String> getAdImageVersions(String adImgId) {
        return advertImageMapper.getAdImageVersions(adImgId);
    }

    @Transactional
    @Override
    public void addAdvertImage(AdvertImageEditVO advertImageEdit) {
        // 新增广告图
        // 广告图ID
        advertImageMapper.addAdvertImage(advertImageEdit);
        String advertImageId = advertImageEdit.getId();
        // 新增广告图对应的版本
        advertImageMapper.addAdvertImageVersions(advertImageId, advertImageEdit.getVersions());
        // 新增广告图对应的客户端
        advertImageMapper.addAdvertImageClientTypes(advertImageId, advertImageEdit.getClientTypes());
        // 新增广告图文件
        advertImageMapper.addAdvertImageFiles(advertImageId, advertImageEdit.getCreateUser(), advertImageEdit.getAdImgFiles());
    }

    @Transactional
    @Override
    public int updateAdvertImage(AdvertImageEditVO advertImageEdit) {
        // 修改广告图
        int updateResult = advertImageMapper.updateAdvertImage(advertImageEdit);
        // 广告图ID
        String advertImageId = advertImageEdit.getId();
        // 删除广告图对应的版本
        advertImageMapper.deleteAdvertImageVersions(advertImageId);
        // 新增广告图对应的版本
        advertImageMapper.addAdvertImageVersions(advertImageId, advertImageEdit.getVersions());
        // 删除广告图对应的客户端
        advertImageMapper.deleteAdvertImageClientTypes(advertImageId);
        // 新增广告图对应的客户端
        advertImageMapper.addAdvertImageClientTypes(advertImageId, advertImageEdit.getClientTypes());
        // 删除广告图文件
        advertImageMapper.deleteAdvertImageFiles(advertImageId);
        // 新增广告图文件
        advertImageMapper.addAdvertImageFiles(advertImageId, advertImageEdit.getCreateUser(), advertImageEdit.getAdImgFiles());
        return updateResult;
    }

    @Override
    public List<AdvertImageVO> getAdvertImages(String name, String advertType, int systemType, RowBounds rowBounds) {
        List<AdvertImageVO> advertImages = advertImageMapper.getAdvertImages(name, advertType, systemType, rowBounds);
        for (AdvertImageVO advertImage : advertImages) {
            advertImage.setSystemTypeLabel(SystemType.getByValue(advertImage.getSystemType()).getLabel());
        }
        return advertImages;
    }

    @Override
    public AdvertImageVO getAdvertImage(String id) {
        AdvertImageVO advertImage = advertImageMapper.getAdvertImage(id);
        advertImage.setSystemTypeLabel(SystemType.getByValue(advertImage.getSystemType()).getLabel());
        return advertImage;
    }
}
