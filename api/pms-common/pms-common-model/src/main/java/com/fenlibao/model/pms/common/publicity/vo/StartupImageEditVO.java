package com.fenlibao.model.pms.common.publicity.vo;

import com.fenlibao.model.pms.common.publicity.StartupImageFile;

import java.util.List;

/**
 * 启动页编辑
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
public class StartupImageEditVO {
    private String id;
    private String name;// 名称
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private String createUser;// 创建人账号
    private List<Integer> clientTypes;// 客户端类型列表
    private List<StartupImageFile> startupImageFiles;// 启动图文件列表
    private List<String> relativePaths;// 相对路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public List<Integer> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<Integer> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public List<StartupImageFile> getStartupImageFiles() {
        return startupImageFiles;
    }

    public void setStartupImageFiles(List<StartupImageFile> startupImageFiles) {
        this.startupImageFiles = startupImageFiles;
    }

    public List<String> getRelativePaths() {
        return relativePaths;
    }

    public void setRelativePaths(List<String> relativePaths) {
        this.relativePaths = relativePaths;
    }
}