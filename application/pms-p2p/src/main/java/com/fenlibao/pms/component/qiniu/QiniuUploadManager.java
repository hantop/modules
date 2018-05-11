package com.fenlibao.pms.component.qiniu;

import com.qiniu.storage.UploadManager;

/**
 * Created by Lullaby on 2015-12-29 18:20
 */
public enum QiniuUploadManager {

    INSTANCE;

    private UploadManager instance;

    QiniuUploadManager() {
        instance = new UploadManager();
    }

    public UploadManager getInstance() {
        return instance;
    }

}
