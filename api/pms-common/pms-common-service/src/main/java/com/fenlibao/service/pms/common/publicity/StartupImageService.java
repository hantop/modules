package com.fenlibao.service.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.StartupImageFile;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageVO;

import java.util.List;

/**
 * 启动页
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
public interface StartupImageService {
    /**
     * 删除启动图
     *
     * @param ids
     */
    void deleteStartupImages(List<String> ids);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 新增启动图
     *
     * @param startupImageEditVO
     */
    void addStartupImage(StartupImageEditVO startupImageEditVO);

    /**
     * 更新启动图
     *
     * @param startupImageEditVO
     * @return
     */
    int updateStartupImage(StartupImageEditVO startupImageEditVO);

    /**
     * 获取启动图对应的文件
     *
     * @param startupId
     * @return
     */
    List<StartupImageFile> getStartupImageFiles(String startupId);

    /**
     * 获取启动图对应的客户端类型
     *
     * @param startupId
     * @return
     */
    List<Integer> getClientTypes(String startupId);

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
    List<StartupImageVO> getStartupImages(String name, String clientType);
}
