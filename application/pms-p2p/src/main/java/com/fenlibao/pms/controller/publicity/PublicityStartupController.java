package com.fenlibao.pms.controller.publicity;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.common.clienttype.ClientType;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.common.publicity.StartupImageFile;
import com.fenlibao.model.pms.common.publicity.form.StartupImageEditForm;
import com.fenlibao.model.pms.common.publicity.form.StartupImageForm;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageEditVO;
import com.fenlibao.model.pms.common.publicity.vo.StartupImageVO;
import com.fenlibao.model.pms.common.screentype.ScreenType;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.component.qiniu.QiniuRet;
import com.fenlibao.pms.component.qiniu.QiniuUpload;
import com.fenlibao.pms.controller.base.BaseController;
import com.fenlibao.service.pms.common.clienttype.ClientTypeService;
import com.fenlibao.service.pms.common.publicity.StartupImageService;
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
 * 启动图
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
@RestController
@RequestMapping("publicity/startup")
public class PublicityStartupController extends BaseController {

//    @Resource
//    private ClientStartupService clientStartupService;

    @Resource
    private StartupImageService startupImageService;

    @Resource
    private ScreenTypeService screenTypeService;

    @Resource
    private ClientTypeService clientTypeService;

    private static final Logger logger = LoggerFactory.getLogger(PublicityStartupController.class);

    private static final int NEW_FILE_NAME_SIZE = 32;

    private static final String QINIU_CLIENT_BUCKET = Config.get("qiniu.client.bucket");

    private static final String QINIU_SOURCE_DOMAIN = Config.get("qiniu.source.domain");

    /**
     * 启动页图片列表页
     * @param page 页码
     * @param limit 页数
     * @param startupImageForm 条件对象
     * @return 列表页
     */
    @RequiresPermissions("publicityStartup:view")
    @RequestMapping
    public ModelAndView startup(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            StartupImageForm startupImageForm) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView view = new ModelAndView("publicity/startup/index");
        List<StartupImageVO> list = startupImageService.getStartupImages(startupImageForm.getName(), startupImageForm.getClientType());
        // IOS、Android
        List<ClientType> clientTypes = new ArrayList<>();
        clientTypes.add(clientTypeService.getClientTypeByCode("1"));
        clientTypes.add(clientTypeService.getClientTypeByCode("2"));
        PageInfo<StartupImageVO> paginator = new PageInfo<>(list);
        return view.addObject("list", list)
                .addObject("clientTypes", clientTypes)
                .addObject("paginator", paginator)
                .addObject("startupImageForm", startupImageForm);
    }

    @RequiresPermissions("publicityStartup:upload")
    @RequestMapping(value = "upload/qiniu", method = RequestMethod.POST)
    HttpResponse uploadStartupPicsWithQiniu(HttpServletRequest request,
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
                    logger.error("启动页图片上传七牛失败。");
                    response.setCodeMessage(ResponseEnum.FAILED_QINIU_UPLOAD.getCode(), ResponseEnum.FAILED_QINIU_UPLOAD.getMessage());
                }
            } catch (IOException e) {
                logger.error("启动页图片上传异常:", e.getMessage(), e);
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        }
        return response;
    }


    /**
     * 删除七牛存储资源
     *
     * @param ids 主键串
     * @return 响应串
     */
    @RequiresPermissions("publicityStartup:delete")
    @RequestMapping("delete/qiniu")
    HttpResponse deleteStartupImageQiniu(String relativePaths) throws QiniuException {
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
                    logger.error("[PublicityStartupController.deleteStartupImageQiniu]", e);
                }
            }
        }
        return response;
    }

    @RequiresPermissions("publicityStartup:edit")
    @RequestMapping(value = "status", method = RequestMethod.PUT)
    HttpResponse updateStatus(String id, Integer status) {
        HttpResponse response = new HttpResponse();
        if (StringUtils.isBlank(id)) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
        } else {
            try {
                startupImageService.updateStatus(id, status);
            } catch (Exception e) {
                logger.error("[PublicityStartupController.updateStatus]", e);
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        }
        return response;
    }

    @RequiresPermissions("publicityStartup:edit")
    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    ModelAndView getStartupImage(@PathVariable("id") String id) {
    return getStartupImageMav(id);
}

    @RequiresPermissions("publicityStartup:edit")
    @RequestMapping(value = "new", method = RequestMethod.GET)
    ModelAndView editPage() {
        return getStartupImageMav(null);
    }

    private ModelAndView getStartupImageMav(String id) {
        ModelAndView mav = new ModelAndView("publicity/startup/edit");
        // 是否新增
        boolean isNew;
        StartupImageVO startupImageVO;
        if (StringUtils.isNotEmpty(id)) {
            isNew = false;
            startupImageVO = startupImageService.getStartupImage(id);
            // 获取启动图对应的客户端类型
            startupImageVO.setClientTypes(startupImageService.getClientTypes(id));
            // 获取启动图对应的文件
            List<StartupImageFile> startupImageFiles = startupImageService.getStartupImageFiles(id);
            // 分辨率类型ID以key的启动图文件列表
            Map<Byte, StartupImageFile> startImageFileMap = new HashMap<>();
            for (StartupImageFile e : startupImageFiles) {
                e.setFullPath(e.getServerPath() + e.getRelativePath());
            }
            for (StartupImageFile e : startupImageFiles) {
                startImageFileMap.put(e.getScreenType(), e);
            }
            startupImageVO.setImgFileMap(startImageFileMap);
        } else {
            isNew = true;
            startupImageVO = new StartupImageVO();
        }
        // IOS、Android
        List<ClientType> clientTypes = new ArrayList<>();
        clientTypes.add(clientTypeService.getClientTypeByCode("1"));
        clientTypes.add(clientTypeService.getClientTypeByCode("2"));
        List<ScreenType> screenTypes = screenTypeService.getScreenTypes();
        // 根据code从list里面去掉元素
        _removeScreenTypes(screenTypes, "7");

        return mav.addObject("clientTypes", clientTypes)
                .addObject("screenTypes", screenTypes)
                .addObject("startupImage", startupImageVO)
                .addObject("isNew", isNew);
    }

    /**
     * 根据code从list里面去掉元素
     * @param screenTypes
     * @param code
     */
    private void _removeScreenTypes(List<ScreenType> screenTypes, String code) {
        int index = 0;
        for (int i = 0; i < screenTypes.size(); i++) {
            if (screenTypes.get(i).getClienttypeCode().equals(code)) {
                index = i;
                break;
            }
        }
        screenTypes.remove(index);
    }

    @RequiresPermissions("publicityStartup:edit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    HttpResponse editStartupImage(StartupImageEditForm startupImageEditForm) {
        HttpResponse response = new HttpResponse();
        // 是否新增
        boolean isNew = false;
        if(StringUtils.isBlank(startupImageEditForm.getId())) {
            isNew = true;
        }
        try {
            // 值复制
            StartupImageEditVO startupImageEditVO = new StartupImageEditVO();
            BeanUtils.copyProperties(startupImageEditVO, startupImageEditForm);
            // 设置当前用户名
            String operator = (String) SecurityUtils.getSubject().getPrincipal();
            startupImageEditVO.setCreateUser(operator);
            // 启动图文件列表
            List<StartupImageFile> startupImageFiles = new ArrayList<>();
            // 客户端屏幕分辨率类型
            List<Byte> screenTypes = startupImageEditForm.getScreenTypes();
            List<Integer> clientTypeCodes = new ArrayList<>();
            for (Integer clientTypeCode : startupImageEditForm.getClientTypes()) {
                clientTypeCodes.add(clientTypeCode);
            }

            if (screenTypes != null && screenTypes.size() > 0) {
                StartupImageFile startupImageFile;
                Byte screenTypeId;
                List<String> publishNames = startupImageEditForm.getPublishNames();// 发布名称
                List<String> originalNames = startupImageEditForm.getOriginalNames();// 源文件名称
                List<Long> fileSizes = startupImageEditForm.getFileSizes();// 文件大小
                List<String> fileTypes = startupImageEditForm.getFileTypes();// 文件类型
                List<String> relativePaths = startupImageEditForm.getRelativePaths();// 相对路径
                Integer clientTypeCode;

                for (int i = 0; i < screenTypes.size(); i++) {
                    screenTypeId = screenTypes.get(i);
                    clientTypeCode = screenTypeService.getClientTypeCodeByScreenTypeId(screenTypeId);
                    if (clientTypeCode == 0 || clientTypeCodes.contains(clientTypeCode)) {
                        startupImageFile = new StartupImageFile();
                        startupImageFile.setScreenType(screenTypeId);
                        startupImageFile.setPublishName(publishNames.get(i));
                        startupImageFile.setServerPath(QINIU_SOURCE_DOMAIN);
                        startupImageFile.setRelativePath(relativePaths.get(i));
                        startupImageFile.setOriginalName(originalNames.get(i));
                        startupImageFile.setFileSize(fileSizes.get(i));
                        startupImageFile.setFileType(fileTypes.get(i));
                        startupImageFiles.add(startupImageFile);
                    }
                }
                startupImageEditVO.setStartupImageFiles(startupImageFiles);
            }
            if (isNew) {
                startupImageService.addStartupImage(startupImageEditVO);
            } else {
                int result = startupImageService.updateStartupImage(startupImageEditVO);
                if (result != 1) {
                    response.setCodeMessage(ResponseEnum.OPERATION_ERROR.getCode(), ResponseEnum.OPERATION_ERROR.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error((isNew ? "新增" : "编辑" + "启动图失败:") + e.getMessage(), e);
            response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
        }
        return response;
    }

    @RequiresPermissions("publicityStartup:delete")
    @RequestMapping(value = "delete/{ids}", method = RequestMethod.DELETE)
    HttpResponse deleteStartupImages(@PathVariable("ids") List<String> ids) {
        HttpResponse response = new HttpResponse();
        if (ids == null || ids.size() < 1) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
        } else {
            try {
                startupImageService.deleteStartupImages(ids);
            } catch (Exception e) {
                logger.error("[PublicityStartupController.deleteStartupImages]", e);
            }
        }
        return response;
    }

}
