package com.fenlibao.pms.component.qiniu;

import com.fenlibao.common.pms.util.loader.Config;
import com.qiniu.util.Auth;

/**
 * Created by Lullaby on 2015-12-29 18:23
 */
public enum QiniuAuth {

    INSTANCE;

    private final String accessKey = Config.get("qiniu.access.key");

    private final String secretKey = Config.get("qiniu.secret.key");

    private Auth auth;

    QiniuAuth() {
        auth = Auth.create(accessKey, secretKey);
    }

    public Auth getInstance() {
        return auth;
    }

}
