package com.fenlibao.p2p.controller.v_4.v_4_2_4.notice;

import com.fenlibao.p2p.model.entity.notice.*;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.KnowEarlyVO;
import com.fenlibao.p2p.model.vo.notice.KnowMoreVO;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoDetailVO;
import com.fenlibao.p2p.model.vo.notice.NoticeInfoVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
@RestController("v_4_2_4/SecurityKnowController")
@RequestMapping(value = "loanSecurity", headers = APIVersion.v_4_2_4)
public class SecurityKnowController {

    private static final Logger logger= LogManager.getLogger(NoticeController.class);

    @Resource
    NoticeService noticeService;


    @RequestMapping(value = "getKnowMoreList", method = RequestMethod.GET)
    HttpResponse getKnowMoreList(HttpServletRequest request,
                               @ModelAttribute BaseRequestForm  paramForm,Integer pageNo,Integer pageSize) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            List<KnowMoreVO> voList = new ArrayList<KnowMoreVO>();

            List<KnowMore> infoList = noticeService.getKnowMoreList(pageNo,pageSize);
            //long updateTime = noticeService.getNoticeLastUpdateTime();
            if(infoList != null && infoList.size() >0){
                for(KnowMore info:infoList) {
                    KnowMoreVO vo = new KnowMoreVO();

                    vo.setId(info.getId());
                    vo.setTitle(info.getTitle());
                    vo.setCreateTime(info.getPublishTime().getTime()/1000);
                    //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                    vo.setIntroduction(info.getIntroduction());
                    vo.setContent(info.getContent());
                    vo.setPublishTime(info.getPublishTime().getTime()/1000);
                    voList.add(vo);
                }
            }
            /*response.getData().put("updateTime", updateTime);*/
            response.getData().put("knowMoreList", voList);

            return response;
        }catch(Exception ex){
            throw ex;
        }
    }




    @RequestMapping(value = "getKnowMoreInfo", method = RequestMethod.GET)
    HttpResponse getKnowMoreInfo(HttpServletRequest request,
                               @ModelAttribute BaseRequestForm  paramForm,
                               @RequestParam(required = true, value = "id") String id) throws Exception{

        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate() || StringUtils.isEmpty(id)){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            KnowMoreVO vo = new KnowMoreVO();
            KnowMore infoDetail = noticeService.getKnowMoreDetail(Integer.valueOf(id));
            if(infoDetail!=null){
                vo.setId(infoDetail.getId());
                vo.setTitle(infoDetail.getTitle());

                vo.setCreateTime(infoDetail.getPublishTime().getTime()/1000);

                //String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));

                vo.setContent(infoDetail.getContent());
                vo.setIntroduction(infoDetail.getIntroduction());
            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }


    @RequestMapping(value = "getKnowEarlyList", method = RequestMethod.GET)
    HttpResponse getKnowEarlyList(HttpServletRequest request,
                               @ModelAttribute BaseRequestForm  paramForm,String noticeType,Integer pageNo,Integer pageSize) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            List<KnowEarlyVO> voList = new ArrayList<KnowEarlyVO>();

            List<KnowEarly> infoList = noticeService.geKnowEarlyList(pageNo,pageSize);
            long updateTime = noticeService.getNoticeLastUpdateTime();
            if(infoList != null && infoList.size() >0){
                for(KnowEarly info:infoList) {
                    KnowEarlyVO vo = new KnowEarlyVO();

                    vo.setId(info.getId());
                    vo.setTitle(info.getTitle());
                    vo.setCreateTime(info.getPublishTime().getTime()/1000);
                    //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                    vo.setIntroduction(info.getIntroduction());
                    vo.setImgUrl(info.getImgUrl());
                    vo.setContent(info.getContent());
                    vo.setPublishTime(info.getPublishTime().getTime()/1000);
                    vo.setSource(info.getSource());
                    voList.add(vo);
                }
            }
            /*response.getData().put("updateTime", updateTime);*/
            response.getData().put("knowMoreList", voList);

            return response;
        }catch(Exception ex){
            throw ex;
        }
    }




    @RequestMapping(value = "getKnowEarlyInfo", method = RequestMethod.GET)
    HttpResponse getKnowEarlyInfo(HttpServletRequest request,
                               @ModelAttribute BaseRequestForm  paramForm,
                               @RequestParam(required = true, value = "id") String id) throws Exception{

        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate() || StringUtils.isEmpty(id)){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            KnowEarlyVO vo = new KnowEarlyVO();
            KnowEarly infoDetail = noticeService.getKnowEarlyDetail(Integer.valueOf(id));
            if(infoDetail!=null){
                vo.setId(infoDetail.getId());
                vo.setTitle(infoDetail.getTitle());

                vo.setCreateTime(infoDetail.getPublishTime().getTime()/1000);

               /* String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));

                vo.setContent(contentString);*/
                vo.setContent(infoDetail.getContent());
                vo.setImgUrl(infoDetail.getImgUrl());
                vo.setIntroduction(infoDetail.getIntroduction());
                vo.setSource(infoDetail.getSource());
            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }

    @RequestMapping(value = "getReportList", method = RequestMethod.GET)
    HttpResponse getReportList(HttpServletRequest request,
                                  @ModelAttribute BaseRequestForm  paramForm,String type,Integer pageNo,Integer pageSize) throws Exception {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate()) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            List<ReportDeatil> infoList = noticeService.getRepoetList(pageNo, pageSize,type);

            if (infoList != null && infoList.size() > 0) {
                response.getData().put("reportList", infoList);
            }
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
