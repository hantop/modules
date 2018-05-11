package com.fenlibao.service.pms.common.publicity.impl;

import com.fenlibao.dao.pms.common.publicity.StartupImageMapper;
import com.fenlibao.model.pms.common.publicity.StartupImageFile;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageVO;
import com.fenlibao.service.pms.common.publicity.StartupImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 启动页
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
@Service
public class StartupImageServiceImpl implements StartupImageService {
    @Resource
    private StartupImageMapper startupImageMapper;

    @Transactional
    @Override
    public void deleteStartupImages(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            for (String startupImageId : ids) {
                // 删除启动图对应的客户端
                startupImageMapper.deleteStartupImageClientTypes(startupImageId);
                // 删除启动图文件
                startupImageMapper.deleteStartupImageFiles(startupImageId);
                // 删除启动图
                startupImageMapper.deleteStartupImage(startupImageId);
            }
        }
    }

    @Transactional
    @Override
    public void updateStatus(String id, Integer status) {
        startupImageMapper.updateStatus(id, status);
    }

    @Transactional
    @Override
    public void addStartupImage(StartupImageEditVO startupImageEditVO) {
        // 新增启动图
        // 启动图ID
        startupImageMapper.addStartupImage(startupImageEditVO);
        String startupImageId = startupImageEditVO.getId();
        // 新增启动图对应的客户端
        startupImageMapper.addStartupImageClientTypes(startupImageId, startupImageEditVO.getClientTypes());
        // 新增启动图文件
        startupImageMapper.addStartupImageFiles(startupImageId, startupImageEditVO.getCreateUser(), startupImageEditVO.getStartupImageFiles());
    }

    @Transactional
    @Override
    public int updateStartupImage(StartupImageEditVO startupImageEditVO) {
        // 修改启动图
        int updateResult = startupImageMapper.updateStartupImage(startupImageEditVO);
        String startupImageId = startupImageEditVO.getId();
        // 删除启动图对应的客户端
        startupImageMapper.deleteStartupImageClientTypes(startupImageId);
        // 新增启动图对应的客户端
        startupImageMapper.addStartupImageClientTypes(startupImageId, startupImageEditVO.getClientTypes());
        // 删除启动图文件
        startupImageMapper.deleteStartupImageFiles(startupImageId);
        // 新增启动图文件
        startupImageMapper.addStartupImageFiles(startupImageId, startupImageEditVO.getCreateUser(), startupImageEditVO.getStartupImageFiles());
        return updateResult;
    }

    @Override
    public List<StartupImageFile> getStartupImageFiles(String startupId) {
        return startupImageMapper.getStartupImageFiles(startupId);
    }

    @Override
    public List<Integer> getClientTypes(String startupId) {
        return startupImageMapper.getClientTypes(startupId);
    }

    @Override
    public StartupImageVO getStartupImage(String id) {
        return startupImageMapper.getStartupImage(id);
    }

    @Override
    public List<StartupImageVO> getStartupImages(String name, String clientType) {
        return startupImageMapper.getStartupImages(name, clientType);
    }
}
