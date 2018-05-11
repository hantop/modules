package com.fenlibao.pms.controller.client;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.context.ShiroSessionUtil;
import com.fenlibao.common.pms.util.http.HttpUpload;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.tool.FileUtil;
import com.fenlibao.common.pms.util.tool.TimeUtil;
import com.fenlibao.model.pms.common.client.TStartupImage;
import com.fenlibao.model.pms.common.global.HttpCode;
import com.fenlibao.model.pms.common.global.HttpMessage;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.component.qiniu.QiniuRet;
import com.fenlibao.pms.component.qiniu.QiniuUpload;
import com.fenlibao.pms.controller.base.BaseController;
import com.fenlibao.service.pms.common.client.ClientStartupService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lullaby on 2015-09-21 14:10
 */
@RestController
@RequestMapping("client")
public class ClientStartupController extends BaseController {

    @Resource
    private ClientStartupService clientStartupService;

    private static final Logger logger = LoggerFactory.getLogger(ClientStartupController.class);

    private static final int NEW_FILE_NAME_SIZE = 32;

    private static final String QINIU_CLIENT_BUCKET = Config.get("qiniu.client.bucket");

    private static final String QINIU_SOURCE_DOMAIN = Config.get("qiniu.source.domain");

//    private static final String RESOURCE_SERVER_PATH = Config.get("resource.server.path");

//    private static final String CLIENT_STARTUP_IMAGE_PATH = Config.get("client.startup.image.path");

    /**
     * 启动页图片列表页
     * @param page 页码
     * @param limit 页数
     * @param image 条件对象
     * @return 列表页
     */
    @RequiresPermissions("clientStartup:view")
    @RequestMapping("startup")
    public ModelAndView startup(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            TStartupImage image) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView view = new ModelAndView("client/startup/index");
        List<TEnum> enums = getEnum("t_startup_image");
        List<TStartupImage> list = clientStartupService.getStartupImageList(image, bounds);
        List<Map<String, Object>> result = new ArrayList<>(list.size());
        for (TStartupImage item : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("originalName", item.getOriginalName());
            map.put("publishName", item.getPublishName());
            map.put("fileSize", FileUtil.getHumanReadableSize(item.getFileSize()));
            map.put("clientType", getEnumValue("f_client_type", item.getClientType(), enums));
            map.put("screenType", getEnumValue("f_screen_type", item.getScreenType(), enums));
            map.put("status", item.getStatus());
            map.put("statusDesc", getEnumValue("f_status", item.getStatus(), enums));
            map.put("fullPath", item.getServerPath().concat(item.getRelativePath()));
            map.put("createTime", TimeUtil.dateToString(item.getCreateTime()));
            result.add(map);
        }
        PageInfo<TStartupImage> paginator = new PageInfo<>(list);
        view.addObject("list", result)
            .addObject("enums", enums)
            .addObject("paginator", paginator)
            .addObject("image", image);
        return view;
    }

    /**
     * 上传客户端启动页图片
     *
     * @param file 已封装文件对象
     */
    @RequiresPermissions("clientStartup:upload")
    @RequestMapping(value = "startup/upload", method = RequestMethod.POST)
    void uploadStartupPics(HttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> params = RequestUtil.getAllParameters(request);
        String fileServerPath = Config.get("resource.server.path");
        String clientStartupPath = Config.get("client.startup.image.path");
        String path = fileServerPath + clientStartupPath;
        HttpUpload upload;
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFilename);
            String publishName = RandomStringUtils.randomAlphanumeric(NEW_FILE_NAME_SIZE).concat(".").concat(suffix);
            upload = new HttpUpload(path, publishName);
            boolean flag = upload.upladFile(file.getInputStream());
            if (flag) {
                String relativePath = clientStartupPath.concat(publishName);
                long fileSize = file.getSize();
                String fileType = file.getContentType();
                String createUser = String.valueOf(ShiroSessionUtil.getCurrentSession().getPrincipal());
                TStartupImage image = new TStartupImage();
                image.setOriginalName(originalFilename);
                image.setPublishName(publishName);
                image.setServerPath(fileServerPath);
                image.setRelativePath(relativePath);
                image.setClientType(params.get("clientType") == null ? (byte) 0 : Byte.valueOf(params.get("clientType")));
                image.setScreenType(params.get("screenType") == null ? (byte) 0 : Byte.valueOf(params.get("screenType")));
                image.setFileSize(fileSize);
                image.setFileType(fileType);
                image.setStatus(params.get("fileStatus") == null ? (byte) 0 : Byte.valueOf(params.get("fileStatus")));
                image.setCreateUser(createUser);
                clientStartupService.saveStartupImage(image);
            } else {
                throw new RuntimeException("图片上传失败");
            }
        }
    }

    /**
     * 七牛存储
     *
     * @param file 已封装文件对象
     */
    @RequestMapping(value = "startup/upload/qiniu", method = RequestMethod.POST)
    void uploadStartupPicsWithQiniu(HttpServletRequest request,
                           @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> params = RequestUtil.getAllParameters(request);
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFilename);
            String publishName = RandomStringUtils.randomAlphanumeric(NEW_FILE_NAME_SIZE).concat(".").concat(suffix);
            QiniuUpload up = new QiniuUpload(publishName, QINIU_CLIENT_BUCKET);
            Response res = up.upload(file.getBytes());
            if (res.isOK()) {
                QiniuRet ret = res.jsonToObject(QiniuRet.class);
                String qiniuHash = ret.getHash();
                long fileSize = file.getSize();
                String fileType = file.getContentType();
                String createUser = String.valueOf(ShiroSessionUtil.getCurrentSession().getPrincipal());
                TStartupImage image = new TStartupImage();
                image.setOriginalName(originalFilename);
                image.setPublishName(qiniuHash);
                image.setServerPath(QINIU_SOURCE_DOMAIN);
                image.setRelativePath(publishName);
                image.setClientType(params.get("clientType") == null ? (byte) 0 : Byte.valueOf(params.get("clientType")));
                image.setScreenType(params.get("screenType") == null ? (byte) 0 : Byte.valueOf(params.get("screenType")));
                image.setFileSize(fileSize);
                image.setFileType(fileType);
                image.setStatus(params.get("fileStatus") == null ? (byte) 0 : Byte.valueOf(params.get("fileStatus")));
                image.setCreateUser(createUser);
                clientStartupService.saveStartupImage(image);
            } else {
                throw new RuntimeException("图片上传失败");
            }
        }
    }

    /**
     * 删除客户端启动页图片
     * @param ids 主键串
     * @return 响应串
     */
    @RequiresPermissions("clientStartup:delete")
    @RequestMapping("startup/delete")
    HttpResponse deleteStartupImage(@RequestParam(value = "ids", required = true) String ids) {
        int length = ids.length();
        HttpResponse response = new HttpResponse();
        if (length < 1) {
            response.setCodeMessage(HttpCode.INVALID_PARAMETER, HttpMessage.INVALID_PARAMETER);
        } else {
            String[] idArray = ids.split(",");
            List<TStartupImage> list = clientStartupService.getStartupImageListByIds(idArray);
            HttpUpload uploader;
            for (TStartupImage image : list) {
                uploader = new HttpUpload(image.getServerPath().concat(image.getRelativePath()));
                uploader.deleteFile();
            }
            clientStartupService.deleteStartupImageByIds(idArray);
        }
        return response;
    }

    /**
     * 删除七牛存储资源
     *
     * @param ids 主键串
     * @return 响应串
     */
    @RequestMapping("startup/delete/qiniu")
    HttpResponse deleteStartupImageQiniu(@RequestParam(value = "ids", required = true) String ids) throws QiniuException {
        int length = ids.length();
        HttpResponse response = new HttpResponse();
        if (length < 1) {
            response.setCodeMessage(HttpCode.INVALID_PARAMETER, HttpMessage.INVALID_PARAMETER);
        } else {
            String[] idArray = ids.split(",");
            List<TStartupImage> list = clientStartupService.getStartupImageListByIds(idArray);
            QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
            for (TStartupImage image : list) {
                try {
                    qiniu.delete(image.getRelativePath());
                } catch (QiniuException e) {
                    logger.error("[ClientStartupController.deleteStartupImageQiniu]", e);
                }
            }
            clientStartupService.deleteStartupImageByIds(idArray);
        }
        return response;
    }

    @RequestMapping(value = "startup/item/{id}", method = RequestMethod.GET)
    TStartupImage getStartupImage(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            List<TStartupImage> list = clientStartupService.getStartupImageListByIds(new String[]{id});
            return list.get(0);
        }
        throw new RuntimeException();
    }

    @RequiresPermissions("clientStartup:edit")
    @RequestMapping(value = "startup/update", method = RequestMethod.PUT)
    HttpResponse getStartupImage(TStartupImage image) {
        HttpResponse response = new HttpResponse();
        int result = clientStartupService.updateStartupImage(image);
        if (result != 1) {
            response.setCode(HttpCode.ERROR_CODE);
        }
        return response;
    }

}
