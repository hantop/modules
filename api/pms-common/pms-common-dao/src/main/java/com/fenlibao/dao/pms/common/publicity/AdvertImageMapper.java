package com.fenlibao.dao.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.AdvertImageFile;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 广告图
 * <p>
 * Created by chenzhixuan on 2016/5/11.
 */
public interface AdvertImageMapper {
    /**
     * 修改可用性
     *
     * @param id
     * @param status
     */
    void updateStatus(@Param("id") String id, @Param("status") Integer status);

    /**
     * 删除广告图
     *
     * @param advertImageId
     */
    void deleteAdvertImage(String advertImageId);

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
     */
    void addAdvertImage(AdvertImageEditVO advertImageEdit);

    /**
     * 新增广告图对应的客户端
     *
     * @param advertImageId
     * @param clientTypes
     */
    void addAdvertImageClientTypes(@Param("advertImageId") String advertImageId, @Param("clientTypes") List<Integer> clientTypes);

    /**
     * 删除广告图对应的客户端
     *
     * @param advertImageId
     */
    void deleteAdvertImageClientTypes(String advertImageId);

    /**
     * 删除广告图对应的版本
     *
     * @param advertImageId
     */
    void deleteAdvertImageVersions(String advertImageId);

    /**
     * 删除广告图文件(多个)
     *
     * @param advertImageId
     */
    void deleteAdvertImageFiles(String advertImageId);

    /**
     * 新增广告图文件(多个)
     *
     * @param advertImageId
     * @param createUser
     * @param adImgFiles
     */
    void addAdvertImageFiles(
            @Param("advertImageId") String advertImageId,
            @Param("createUser") String createUser,
            @Param("adImgFiles") List<AdvertImageFile> adImgFiles);

    /**
     * 修改广告图
     *
     * @param advertImageEditVO
     * @return
     */
    int updateAdvertImage(AdvertImageEditVO advertImageEditVO);

    /**
     * 获取广告图列表
     * @param name
     * @param advertType
     * @param systemType
     * @param bounds  @return
     */
    List<AdvertImageVO> getAdvertImages(@Param("name") String name,
                                        @Param("advertType") String advertType,
                                        @Param("systemType") int systemType,
                                        RowBounds bounds);

    /**
     * 新增广告图版本(多个)
     *  @param advertImageId
     * @param versions
     */
    void addAdvertImageVersions(@Param("advertImageId") String advertImageId, @Param("versions") List<String> versions);

    /**
     * 根据ID获取广告图
     *
     * @param id
     * @return
     */
    AdvertImageVO getAdvertImage(String id);
}
