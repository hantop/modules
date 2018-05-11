package com.fenlibao.model.pms.common.publicity.form;

import java.util.List;

/**
 * 启动图编辑
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
public class StartupImageEditForm {
    private String id;
    private String name;// 名称
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private List<Integer> clientTypes;// 客户端类型列表
    private List<String> publishNames;// 发布名称
    private List<String> originalNames;// 源文件名称
    private List<Byte> screenTypes;// 客户端屏幕分辨率类型
    private List<Long> fileSizes;// 文件大小
    private List<String> fileTypes;// 文件类型
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

    public List<Integer> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<Integer> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public List<String> getPublishNames() {
        return publishNames;
    }

    public void setPublishNames(List<String> publishNames) {
        this.publishNames = publishNames;
    }

    public List<String> getOriginalNames() {
        return originalNames;
    }

    public void setOriginalNames(List<String> originalNames) {
        this.originalNames = originalNames;
    }

    public List<Byte> getScreenTypes() {
        return screenTypes;
    }

    public void setScreenTypes(List<Byte> screenTypes) {
        this.screenTypes = screenTypes;
    }

    public List<Long> getFileSizes() {
        return fileSizes;
    }

    public void setFileSizes(List<Long> fileSizes) {
        this.fileSizes = fileSizes;
    }

    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public List<String> getRelativePaths() {
        return relativePaths;
    }

    public void setRelativePaths(List<String> relativePaths) {
        this.relativePaths = relativePaths;
    }
}