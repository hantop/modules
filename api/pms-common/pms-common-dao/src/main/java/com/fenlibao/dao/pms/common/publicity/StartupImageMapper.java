package com.fenlibao.dao.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.StartupImageFile;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 启动图
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
public interface StartupImageMapper {
    /**
     * 删除启动图
     *
     * @param startupImageId
     */
    void deleteStartupImage(String startupImageId);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     */
    void updateStatus(@Param("id") String id, @Param("status") Integer status);

    /**
     * 删除广告图文件
     *
     * @param startupImageId
     */
    void deleteStartupImageFiles(String startupImageId);

    /**
     * 删除广告图对应的客户端
     *
     * @param startupImageId
     */
    void deleteStartupImageClientTypes(String startupImageId);

    /**
     * 更新启动图
     *
     * @param startupImageEditVO
     * @return
     */
    int updateStartupImage(StartupImageEditVO startupImageEditVO);

    /**
     * 新增启动图文件
     *
     * @param startupImageId
     * @param createUser
     * @param startupImageFiles
     */
    void addStartupImageFiles(@Param("startupImageId") String startupImageId, @Param("createUser") String createUser, @Param("startupImageFiles") List<StartupImageFile> startupImageFiles);

    /**
     * 新增启动图对应的客户端
     *
     * @param startupImageId
     * @param clientTypes
     */
    void addStartupImageClientTypes(@Param("startupImageId") String startupImageId, @Param("clientTypes") List<Integer> clientTypes);

    /**
     * 新增启动图
     *
     * @param startupImageEditVO
     */
    void addStartupImage(StartupImageEditVO startupImageEditVO);

    /**
     * 获取启动图对应的文件
     *
     * @param startpageId
     * @return
     */
    List<StartupImageFile> getStartupImageFiles(String startpageId);

    /**
     * 获取启动图对应的客户端类型
     *
     * @param startpageId
     * @return
     */
    List<Integer> getClientTypes(String startpageId);

    /**
     * 获取启动图
     *
     * @param id
     * @return
     */
    StartupImageVO getStartupImage(String id);

    /**
     * 启动图列表
     *
     * @param name
     * @param clientType
     * @return
     */
    List<StartupImageVO> getStartupImages(@Param("name") String name, @Param("clientType") String clientType);
}
