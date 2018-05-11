package com.fenlibao.pms.controller.publicity;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.common.publicity.KnowEarly;
import com.fenlibao.model.pms.common.publicity.KnowMore;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.service.xinwang.common.RedisUtilService;
import com.fenlibao.pms.component.qiniu.QiniuRet;
import com.fenlibao.pms.component.qiniu.QiniuUpload;
import com.fenlibao.service.pms.common.publicity.KnowEarlyService;
import com.fenlibao.service.pms.common.publicity.KnowMoreService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 *
 网贷课堂
 * Created by Administrator on 2018/3/2.
 */
@Controller
@RequestMapping("publicity")
public class ClassKnowController {


    @Autowired
    private KnowMoreService knowMoreService;

    @Autowired
    private KnowEarlyService knowEarlyService;



    private static final int NEW_FILE_NAME_SIZE = 32;

    private static final String QINIU_CLIENT_BUCKET = Config.get("qiniu.client.bucket");

    private static final String QINIU_SOURCE_DOMAIN = Config.get("qiniu.source.domain");


    Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 网贷知多点-查询列表
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "class/knowmoreList")
    @RequiresPermissions("knowMore:view")
    public ModelAndView knowMoreList(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                     KnowMore knowMore,String startTime,String endTime){
        ModelAndView view = new ModelAndView("publicity/class/knowmore_list");
        RowBounds bounds = new RowBounds(page, limit);
        try {
            List<KnowMore> knowMoreList = knowMoreService.getKnowMoreList(knowMore,bounds,startTime,endTime);
            PageInfo<KnowMore> paginator = new PageInfo<>(knowMoreList);
            view.addObject("knowMoreList",knowMoreList);
            view.addObject("knowMore",knowMore);
            view.addObject("startTime",startTime);
            view.addObject("endTime",endTime);
            view.addObject("paginator", paginator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 网贷知多点-新增或者修改页面
     * @param knowMore
     * @return
     */
    @RequiresPermissions("knowMore:edit")
    @RequestMapping(value = "class/toUpateKnowMore")
    public ModelAndView toUpateKnowMore(KnowMore knowMore){
        ModelAndView view = new ModelAndView("publicity/class/knowmore_update");
        RowBounds bounds = new RowBounds(1, 1);
        try {
            knowMore.setStatus(-1);
            List<KnowMore> knowMoreList = knowMoreService.getKnowMoreList(knowMore,bounds,null,null);
            if(!CollectionUtils.isEmpty(knowMoreList)){
                knowMore=knowMoreList.get(0);
            }
            view.addObject("knowMore",knowMore);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;


    }

    /**
     * 网贷知多点-新增或者修改
     * @param knowMore
     * @return
     */
    @RequiresPermissions("knowMore:edit")
    @RequestMapping(value = "class/editKnowMore")
    @ResponseBody
    public HashMap editKnowMore(KnowMore knowMore){
        HashMap result = new HashMap();
        try {
            Date now = new Date();
            if(knowMore.getId()==0){
                knowMore.setCreateTime(now);
            }
            if(knowMore.getStatus()==1){
                Subject subject = SecurityUtils.getSubject();
                knowMore.setPublisherId(subject.getPrincipal().toString());
                knowMore.setPublishTime(now);
            }
            knowMore.setAllPicUploade(knowMore.getAllPicUploade().replaceAll(QINIU_SOURCE_DOMAIN,""));
            knowMoreService.editKnowMore(knowMore);
            String content =knowMore.getContent();
            this.clearNoUsePicFromEditor(knowMore.getAllPicUploade(),content,"");
            result.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("errorMsg",e.toString());
        }


        return result;
    }


    /**
     * 网贷知多点-删除
     * @param knowMore
     * @param ids
     * @return
     */
    @RequiresPermissions("knowMore:delete")
    @RequestMapping(value = "class/deleteKnowMore")
    @ResponseBody
    public HashMap deleteKnowMore(KnowMore knowMore,String ids){
        HashMap result = new HashMap();

        try {
            QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
            String[] idList=ids.split(",");
            List<KnowMore> knowMoreList = knowMoreService.getKnowEarlyListByIds(idList);
            this.deleteQiniuFileOfKnowMore(knowMoreList);
            knowMoreService.deleteKnowMore(idList);
            result.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("errorMsg",e.toString());
        }
        return result;
    }
    /**
     * 删除网贷知多点时，删除对应的传过的所有文件
     * @param knowMoreList
     */
    public void deleteQiniuFileOfKnowMore(List<KnowMore> knowMoreList){
        QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
        List<KnowMore> fail = new ArrayList<>();
        for (KnowMore knowMore:knowMoreList) {
            try {
                deleteStartupImageQiniu(knowMore.getAllPicUploade());
            } catch (Exception e) {
                fail.add(knowMore);
            }
        }

    }


    /**
     * 风险早知道
     * @param page
     * @param limit
     * @param knowEarly
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "class/riskKnowEarlyList")
    @RequiresPermissions("knowEarly:view")
    public ModelAndView riskKnowEarlyList(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                      KnowEarly knowEarly, String startTime, String endTime){
        ModelAndView view = new ModelAndView("publicity/class/knowearly_list");
        RowBounds bounds = new RowBounds(page, limit);
        try {
            List<KnowEarly> knowEarlyList = knowEarlyService.getKnowEarlyList(knowEarly,bounds,startTime,endTime);
            PageInfo<KnowEarly> paginator = new PageInfo<>(knowEarlyList);
            view.addObject("knowEarlyList",knowEarlyList);
            view.addObject("knowEarly",knowEarly);
            view.addObject("startTime",startTime);
            view.addObject("endTime",endTime);
            view.addObject("paginator", paginator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 风险早知道-新增或者修改页面
     * @param knowEarly
     * @return
     */
    @RequiresPermissions("knowEarly:edit")
    @RequestMapping(value = "class/toUpateKnowEarly")
    public ModelAndView toUpateKnowEarly(KnowEarly knowEarly){
        ModelAndView view = new ModelAndView("publicity/class/knowearly_update");
        RowBounds bounds = new RowBounds(1, 1);
        try {
            knowEarly.setStatus(-1);
            List<KnowEarly> knowEarlyList = knowEarlyService.getKnowEarlyList(knowEarly,bounds,null,null);
            if(!CollectionUtils.isEmpty(knowEarlyList)){
                knowEarly=knowEarlyList.get(0);
            }
            view.addObject("knowEarly",knowEarly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 风险早知道-新增或者修改
     * @param knowEarly
     * @return
     */
    @RequiresPermissions("knowEarly:edit")
    @RequestMapping(value = "class/editKnowEarly")
    @ResponseBody
    public HashMap editKnowEarly(KnowEarly knowEarly){
        HashMap result = new HashMap();
        try {
            Date now = new Date();
            if(knowEarly.getId()==0){
                knowEarly.setCreateTime(now);
            }
            if(knowEarly.getStatus()==1){
                Subject subject = SecurityUtils.getSubject();
                knowEarly.setPublisherId(subject.getPrincipal().toString());
                knowEarly.setPublishTime(now);
            }
            knowEarly.setPicServer(QINIU_SOURCE_DOMAIN);


            knowEarly.setAllPicUploade(knowEarly.getAllPicUploade().replaceAll(QINIU_SOURCE_DOMAIN,""));

            knowEarlyService.editKnowEarly(knowEarly);


            String content =knowEarly.getPicNewName()+","+knowEarly.getContent();

            this.clearNoUsePicFromEditor(knowEarly.getAllPicUploade(),content,"");
            result.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("errorMsg",e.toString());
        }
        return result;
    }

    /**
     * 删除富文本内上传了又没用到的图片
     * @param pics
     */
    public void clearNoUsePicFromEditor(String pics,String content,String key){
        try {
            if(StringUtils.isNotBlank(pics)){
                String[]picArray = pics.split(",");

                String needToDelete="";
                for (String pic:picArray) {
                    if(!content.contains(pic)){
                        if(!"null".equals(pic)){
                            needToDelete=needToDelete+","+pic;
                        }
                    }
                }
                this.deleteStartupImageQiniu(needToDelete);
            }

        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }


    /**
     * 风险早知道-删除
     * @param knowEarly
     * @param ids
     * @return
     */
    @RequiresPermissions("knowEarly:delete")
    @RequestMapping(value = "class/deleteKnowEarly")
    @ResponseBody
    public HashMap deleteKnowEarly(KnowEarly knowEarly,String ids){
        HashMap result = new HashMap();

        try {
            String[] idList=ids.split(",");
            List<KnowEarly> knowEarlyList= knowEarlyService.getKnowEarlyListByIds(idList);
            knowEarlyService.deleteKnowEarly(idList);
            this.deleteQiniuFile(knowEarlyList);
            result.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("errorMsg",e.toString());
        }
        return result;
    }

    /**
     * 根据文件名直接删除七牛上的文件
     * @param relativePaths
     * @return
     * @throws QiniuException
     */
    @ResponseBody
    @RequestMapping("/know/class/delete/qiniu")
    HttpResponse deleteStartupImageQiniu(String relativePaths) throws QiniuException {
        HttpResponse response = new HttpResponse();

        if (StringUtils.isEmpty(relativePaths)) {
            response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
            return response;
        } else {
            String[] relativePathArray = relativePaths.split(",");
            QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
            for (String relativePath : relativePathArray) {
                try {
                    if(StringUtils.isNotBlank(relativePath)){
                        qiniu.delete(relativePath);
                        log.debug("删除文件成功【{}】",relativePath);
                    }
                } catch (QiniuException e) {
                    try {//删除出错时再试一次，防止网络原因删不掉
                        qiniu.delete(relativePath);
                    } catch (QiniuException e1) {
                        log.debug("删除文件失败【{}】",relativePath);
                    }

                }
            }
        }
        return response;
    }


    /**
     * 删除风险早知道时，删除对应的传过的所有文件
     * @param knowEarlyList
     */
    public void deleteQiniuFile(List<KnowEarly> knowEarlyList){
        QiniuUpload qiniu = new QiniuUpload(QINIU_CLIENT_BUCKET);
        List<KnowEarly> fail = new ArrayList<>();
        for (KnowEarly knowEarly:knowEarlyList) {
            try {
                deleteStartupImageQiniu(knowEarly.getAllPicUploade());
            } catch (Exception e) {

            }
        }



    }



    /**
     * 富文本编辑器里面的图片上传
     * @param request
     * @param file
     * @param knowId
     * @param fromUrl more网贷知多点，early风险早知道
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "class/qiniu", method = RequestMethod.POST)
    HashMap<String, Object> uploadStartupPicsWithQiniu(HttpServletRequest request,
                                            @RequestParam("imgFile") MultipartFile file,int knowId,String fromUrl) {
        Map<String, String> params = RequestUtil.getAllParameters(request);
        HashMap<String, Object> data = new HashMap<>();
        if (file != null && !file.isEmpty()) {
            // 源文件名称
            String originalFilename = file.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFilename);
            // 发布名称
            String publishName = RandomStringUtils.randomAlphanumeric(NEW_FILE_NAME_SIZE).concat(".").concat(suffix);
            QiniuUpload up = new QiniuUpload(publishName, QINIU_CLIENT_BUCKET);
            try {
                if("early".equals(fromUrl)){
                    KnowEarly knowEarly=new KnowEarly();
                    knowEarly.setId(knowId);
                    knowEarly.setNewUploaded(publishName);
                    knowEarlyService.updateAllPicUploade(knowEarly);
                }else{
                    KnowMore knowMore = new KnowMore();
                    knowMore.setId(knowId);
                    knowMore.setNewUploaded(publishName);
                    knowMoreService.updateAllPicUploade(knowMore);
                }
                Response res = up.upload(file.getBytes());
                if (res.isOK()) {
                    data.put("url",QINIU_SOURCE_DOMAIN+publishName);
                    data.put("error",0);
                    data.put("publishName",publishName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }



    /**
     * 风险早知道-上传列表页图
     * @param request
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "class/uploadListPic", method = RequestMethod.POST)
    HttpResponse uploadListPic(HttpServletRequest request,
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
                    data.put("uploadListPic","uploadListPic");
                    data.put("fullPath", QINIU_SOURCE_DOMAIN + publishName);
                    data.put("fileSize", file.getSize());
                    data.put("fileType", file.getContentType());
                    data.put("screenType", params.get("screenType") == null ? (byte) 0 : Byte.valueOf(params.get("screenType")));
                    response.setData(data);
                } else {
                    response.setCodeMessage(ResponseEnum.FAILED_QINIU_UPLOAD.getCode(), ResponseEnum.FAILED_QINIU_UPLOAD.getMessage());
                }
            } catch (IOException e) {
                response.setCodeMessage(ResponseEnum.INTERAL_SEVER_ERROR.getCode(), ResponseEnum.INTERAL_SEVER_ERROR.getMessage());
            }
        }
        return response;
    }
}
