package com.fenlibao.service.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.AdvertImageFile;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 广告图
 * <p>
 * Created by chenzhixuan on 2016/5/11.
 */
public interface AdvertImageService {
    /**
     * 修改可用性
     *
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 删除广告图
     *
     * @param ids
     */
    void deleteAdvertImages(List<String> ids);

    /**
     * 获取广告图对应的文件
     *
     * @param adImgId
     * @return
     */
    List<AdvertImageFile> getAdImgFiles(String adImgId);

    /**
     * 获取广告图对应的客户端类型
     *
     * @param adImgId
     * @return
     */
    List<Integer> getClientTypes(String adImgId);

    /**
     * 获取广告图对应的版本号
     *
     * @param adImgId
     * @return
     */
    List<String> getAdImageVersions(String adImgId);

    /**
     * 新增广告图
     *
     * @param advertImageEdit
     * @return
     */
    void addAdvertImage(AdvertImageEditVO advertImageEdit);

    /**
     * 修改广告图
     *
     * @param advertImageEdit
     * @return
     */
    int updateAdvertImage(AdvertImageEditVO advertImageEdit);

    /**
     * 获取广告图列表
     * @param name
     * @param advertType
     * @param systemType
     * @param rowBounds  @return
     */
    List<AdvertImageVO> getAdvertImages(String name, String advertType, int systemType, RowBounds rowBounds);

    /**
     * 根据ID获取广告图
     *
     * @param id
     * @return
     */
    AdvertImageVO getAdvertImage(String id);
}
