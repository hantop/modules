package com.fenlibao.common.pms.util.tool;

import com.fenlibao.common.pms.util.http.HttpUpload;
import com.fenlibao.common.pms.util.loader.Config;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Lullaby on 2015/7/24.
 */
public class FileUtil {

    /**
     * 文件上传公共类
     *
     * @param request
     * @param file
     * @return
     * @throws Exception
     * @author echo
     * @version 20150805
     */

    public static boolean uploadFile(HttpServletRequest request, MultipartFile file) {
        boolean flag = false;
        try {
            String fileServerPath = Config.get("resource.server.path");
            String clientStartupPath = Config.get("client.advert.image.path");
            String path = fileServerPath + clientStartupPath;
            HttpUpload upload;
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                upload = new HttpUpload(path, originalFilename);
                flag = upload.upladFile(file.getInputStream());
            } else {
                throw new RuntimeException("请选择要上传的文件！！");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    public static String getHumanReadableSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
