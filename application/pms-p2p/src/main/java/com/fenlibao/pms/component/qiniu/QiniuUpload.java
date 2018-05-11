package com.fenlibao.pms.component.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 七牛存储
 * Created by Lullaby on 2015-12-29 17:47
 */
public class QiniuUpload {

    private static final Logger logger = LoggerFactory.getLogger(QiniuUpload.class);

    private String filename;

    private String bucket;

    public QiniuUpload(String bucket) {
        this.bucket = bucket;
    }

    public QiniuUpload(String filename, String bucket) {
        this.filename = filename;
        this.bucket = bucket;
    }

    private String getUpToken() {
        Auth auth = QiniuAuth.INSTANCE.getInstance();
        return auth.uploadToken(bucket);
    }

    public Response upload(byte[] data, String filname) {
        UploadManager uploadManager = QiniuUploadManager.INSTANCE.getInstance();
        Response res;
        try {
            res = uploadManager.put(data, filname, getUpToken());
//            QiniuRet ret = res.jsonToObject(QiniuRet.class);
        } catch (QiniuException e) {
            res = e.response;
            // 请求失败时简单状态信息
            logger.error(res.toString());
            try {
                // 响应的文本信息
                logger.error(res.bodyString());
            } catch (QiniuException eee) {
                //ignore
                logger.error(eee.response.toString());
            }
        }
        return res;
    }

    public Response upload(byte[] data) {
        UploadManager uploadManager = QiniuUploadManager.INSTANCE.getInstance();
        Response res;
        try {
            res = uploadManager.put(data, filename, getUpToken());
        } catch (QiniuException e) {
            res = e.response;
            logger.error(res.toString());
            try {
                logger.error(res.bodyString());
            } catch (QiniuException eee) {
                logger.error(eee.response.toString());
            }
        }
        return res;
    }

    public void delete(String key, String bucket) throws QiniuException {
        BucketManager bucketManager = new BucketManager(QiniuAuth.INSTANCE.getInstance());
        bucketManager.delete(bucket, key);
    }

    public void delete(String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(QiniuAuth.INSTANCE.getInstance());
        bucketManager.delete(bucket, key);
    }

}
