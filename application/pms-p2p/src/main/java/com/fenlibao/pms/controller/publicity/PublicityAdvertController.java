package com.fenlibao.pms.controller.publicity;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.common.clienttype.ClientType;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.common.global.SystemType;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.model.pms.common.publicity.AdvertImageFile;
import com.fenlibao.model.pms.common.publicity.form.AdvertImageEditForm;
import com.fenlibao.model.pms.common.publicity.form.AdvertImageForm;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.AdvertImageVO;
import com.fenlibao.model.pms.common.screentype.ScreenType;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.component.qiniu.QiniuRet;
import com.fenlibao.pms.component.qiniu.QiniuUpload;
import com.fenlibao.pms.controller.base.BaseController;
import com.fenlibao.service.pms.common.clienttype.ClientTypeService;
import com.fenlibao.service.pms.common.publicity.AdvertImageService;
import com.fenlibao.service.pms.common.screentype.ScreenTypeService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
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
 * 广告图
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
@RestController
@RequestMapping("publicity/advert")
public class PublicityAdvertController extends BaseController {
    @Resource
    private AdvertImageService advertImageService;

    @Resource
    private ClientTypeService clientTypeService;

    @Resource
    private ScreenTypeService screenTypeService;

    private static final Logger logger = LoggerFactory.getLogger(PublicityAdvertController.class);

    private static final int NEW_FILE_NAME_SIZE = 32;

    private static final String QINIU_CLIENT_BUCKET = Config.get("qiniu.client.bucket");

    private static final String QINIU_SOURCE_DOMAIN = Config.get("qiniu.source.domain");

    /**
     * 广告页图片列表页
     *
     * @param page 页码
     * @param limit 页数
     * @param advertImageForm 条件对象
     * @return 启动页列表页
     */
    @RequiresPermissions("publicityAdvert:search")
    @RequestMapping
    public ModelAndView advert(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            AdvertImageForm advertImageForm) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView view = new ModelAndView("publicity/advert/index");
        // 广告类型枚举
        List<TEnum> advertimageEnums = getEnum("t_advert_image", "advert_type");
        if (advertImageForm.getSystemType() == null) {
            advertImageForm.setSystemType(0);
        }
        List<AdvertImageVO> list = advertImageService.getAdvertImages(
                advertImageForm.getName(), advertImageForm.getAdvertType(), advertImageForm.getSystemType(), bounds);
        for (AdvertImageVO image : list) {
            image.setAdvertType(getEnumValue("advert_type", image.getAdvertType(), advertimageEnums));
        }
        PageInfo<AdvertImageVO> paginator = new PageInfo<>(list);
        view.addObject("list", list)
            .addObject("advertimageEnums", advertimageEnums)
            .addObject("systemTypeEnums", SystemType.values())
            .addObject("paginator", paginator)
            .addObject("advertImageForm", advertImageForm);
        return view;
    }

    @RequiresPermissions("publicityAdvert:upload")
    @RequestMapping(value = "upload/qiniu", method = RequestMethod.POST)
    HttpResponse uploadAdvertPicsWithQiniu(HttpServletRequest request,
                          @RequestParam("file") MultipartFile file) {
        Map<String, String> params = RequestUtil.getAllParameters(request);
        HttpResponse response = new HttpResponse();
        if (file != null && !file.isEmpty()) {
            // 源文件名称
            String originalFilename = file.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFilename);
            // 发布名称
            String publishName = RandomStringUtils.randomAlphanumeric(NEW_FILE_NAME_SIZE).concat(".").concat(suffix);
            QiniuUpload up = new QiniuUpload(publishName, QINIU_CLIENT_BUCKET);
            try {
                Response res = up.upload(file.getBytes());
                if (res.isOK()) {
                    HashMap<String, Object> data = new HashMap<>();

                    QiniuRet ret = res.jsonToObject(QiniuRet.class);
                    String qiniuHash = ret.getHash();
                    data.put("originalFilename", originalFilename);
                    data.put("publishName", qiniuHash);
                    data.put("relativePath", publishName);
                    data.put("fullPath", QINIU_SOURCE_DOMAIN + publishName);
                    data.put("fileSize", file.getSize());
                    data.put("fileType", file.getContentType());
                    data.put("screenType", params.get("screenType") == null ? (byte) 0 : Byte.valueOf(params.get("screenType")));
                    response.setData(data);
                } else {
                    logger.error("广告页图片上传七牛失败。");
                    response.setCodeMessage(ResponseEnum.FAILED_QINIU_UPLOAD.getCode(), ResponseEnum.FAILED_QINIU_UPLOAD.getMessage());
                }
            } catch (IOException e) {
                logger.error("广告页图片上传异常:", e.getMessage(), e);
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        }
        return response;
    }

    @RequiresPermissions("publicityAdvert:edit")
    @RequestMapping(value = "status", method = RequestMethod.PUT)
    HttpResponse updateStatus(String id, Integer status) {
        HttpResponse response = new HttpResponse();
        if (StringUtils.isBlank(id)) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
        } else {
            try {
                advertImageService.updateStatus(id, status);
            } catch (Exception e) {
                logger.error("[PublicityAdvertController.updateStatus]", e);
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        }
        return response;
    }

    /**
     * 删除七牛存储资源
     *
     * @param relativePaths 主键串
     * @return 响应串
     */
    @RequiresPermissions("publicityAdvert:delete")
    @RequestMapping("delete/qiniu")
    HttpResponse deleteAdvertImageQiniu(String relativePaths) throws QiniuException {
        HttpResponse response = new HttpResponse();
        int length = relativePaths.length();
        if (length < 1) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
        } else {
            String[] relativePathArray = relativePaths.split(",");
            QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
            for (String relativePath : relativePathArray) {
                try {
                    qiniu.delete(relativePath);
                } catch (QiniuException e) {
                    logger.error("[PublicityAdvertController.deleteAdvertImageQiniu]", e);
                }
            }
        }
        return response;
    }

    @RequiresPermissions("publicityAdvert:edit")
    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    ModelAndView getAdvertImage(@PathVariable("id") String id) {
        return getAdvertImageMav(id);
    }

    @RequiresPermissions("publicityAdvert:edit")
    @RequestMapping(value = "new", method = RequestMethod.GET)
    ModelAndView editPage() {
        return getAdvertImageMav(null);
    }

    @RequestMapping(value = "screenTypes/{clientTypeCode}", method = RequestMethod.GET)
    private List<ScreenType> getScreenTypesByClientTypeCode(@PathVariable("clientTypeCode") String clientTypeCode) {
        List<ScreenType> screenTypesByClientTypeCode = screenTypeService.getScreenTypesByClientTypeCode(clientTypeCode);
        return screenTypesByClientTypeCode;
    }

    private ModelAndView getAdvertImageMav(String id) {
        ModelAndView mav = new ModelAndView("publicity/advert/edit");
        // 是否新增
        boolean isNew;
        AdvertImageVO advertImage;
        if (StringUtils.isNotEmpty(id)) {
            isNew = false;
            advertImage = advertImageService.getAdvertImage(id);
            // 获取广告图对应的版本号
            advertImage.setVersions(advertImageService.getAdImageVersions(id));
            // 获取广告图对应的客户端类型
            advertImage.setClientTypes(advertImageService.getClientTypes(id));
            // 获取广告图对应的文件
            List<AdvertImageFile> adImgFiles = advertImageService.getAdImgFiles(id);
            // 分辨率类型ID以key的广告图文件列表
            Map<Byte, AdvertImageFile> adImgFileMap = new HashMap<>();
            for (AdvertImageFile e : adImgFiles) {
                e.setFullPath(e.getServerPath() + e.getRelativePath());
            }
            for (AdvertImageFile e : adImgFiles) {
                adImgFileMap.put(e.getScreenType(), e);
            }
            advertImage.setAdImgFileMap(adImgFileMap);
        } else {
            isNew = true;
            advertImage = new AdvertImageVO();
        }
        List<TEnum> advertTypes = super.getEnum("t_advert_image", "advert_type");
        List<TEnum> versions = super.getEnum("t_advert_image", "client_version");
        // 不显示未分配
        List<ClientType> clientTypes = clientTypeService.getClientTypes();
        for (int i = 0; i < clientTypes.size(); i++) {
            if (clientTypes.get(0).getCode().equals("0")) {
                clientTypes.remove(i);
            }
        }
        List<ScreenType> screenTypes = screenTypeService.getScreenTypes();

        return mav.addObject("advertTypes", advertTypes)
                  .addObject("clientTypes", clientTypes)
                  .addObject("screenTypes", screenTypes)
                  .addObject("versions", versions)
                  .addObject("systemTypeEnums", SystemType.values())
                  .addObject("advertImage", advertImage)
                  .addObject("isNew", isNew);
    }

    @RequiresPermissions("publicityAdvert:edit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    HttpResponse editAdvertImage(AdvertImageEditForm advertImageEditForm) {
        HttpResponse response = new HttpResponse();
        // 是否新增
        boolean isNew = false;
        if(StringUtils.isBlank(advertImageEditForm.getId())) {
            isNew = true;
        }
        try {
            // 值复制
            AdvertImageEditVO advertImageEditVO = new AdvertImageEditVO();
            BeanUtils.copyProperties(advertImageEditVO, advertImageEditForm);
            // 设置当前用户名
            String operator = (String) SecurityUtils.getSubject().getPrincipal();
            advertImageEditVO.setCreateUser(operator);
            // 广告图文件列表
            List<AdvertImageFile> adImgFiles = new ArrayList<>();
            // 客户端屏幕分辨率类型
            List<Byte> screenTypes = advertImageEditForm.getScreenTypes();
            List<Integer> clientTypeCodes = new ArrayList<>();
            for (Integer clientTypeCode : advertImageEditForm.getClientTypes()) {
                clientTypeCodes.add(clientTypeCode);
            }

            if (screenTypes != null && screenTypes.size() > 0) {
                AdvertImageFile advertImageFile;
                Byte screenTypeId;
                List<String> publishNames = advertImageEditForm.getPublishNames();// 发布名称
                List<String> originalNames = advertImageEditForm.getOriginalNames();// 源文件名称
                List<Long> fileSizes = advertImageEditForm.getFileSizes();// 文件大小
                List<String> fileTypes = advertImageEditForm.getFileTypes();// 文件类型
                List<String> relativePaths = advertImageEditForm.getRelativePaths();// 相对路径
                Integer clientTypeCode;

                for (int i = 0; i < screenTypes.size(); i++) {
                    screenTypeId = screenTypes.get(i);
                    clientTypeCode = screenTypeService.getClientTypeCodeByScreenTypeId(screenTypeId);
                    if (clientTypeCode == 0 || clientTypeCodes.contains(clientTypeCode)) {
                        advertImageFile = new AdvertImageFile();
                        advertImageFile.setScreenType(screenTypeId);
                        advertImageFile.setPublishName(publishNames.get(i));
                        advertImageFile.setServerPath(QINIU_SOURCE_DOMAIN);
                        advertImageFile.setRelativePath(relativePaths.get(i));
                        advertImageFile.setOriginalName(originalNames.get(i));
                        advertImageFile.setFileSize(fileSizes.get(i));
                        advertImageFile.setFileType(fileTypes.get(i));
                        adImgFiles.add(advertImageFile);
                    }
                }
                advertImageEditVO.setAdImgFiles(adImgFiles);
            }
            if (isNew) {
                advertImageService.addAdvertImage(advertImageEditVO);
            } else {
                int result = advertImageService.updateAdvertImage(advertImageEditVO);
                if (result != 1) {
                    response.setCodeMessage(ResponseEnum.OPERATION_ERROR.getCode(), ResponseEnum.OPERATION_ERROR.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error((isNew ? "新增" : "编辑" + "广告图失败:") + e.getMessage(), e);
            response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("publicityAdvert:delete")
    @RequestMapping(value = "delete/{ids}", method = RequestMethod.DELETE)
    HttpResponse deleteAdvertImages(@PathVariable("ids") List<String> ids) {
        HttpResponse response = new HttpResponse();
        if (ids == null || ids.size() < 1) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
        } else {
            try {
                advertImageService.deleteAdvertImages(ids);
            } catch (Exception e) {
                logger.error("[PublicityAdvertController.deleteAdvertImages]", e);
            }
        }
        return response;
    }

}
