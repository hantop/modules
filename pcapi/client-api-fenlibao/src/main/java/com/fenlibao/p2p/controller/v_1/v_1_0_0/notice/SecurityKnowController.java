package com.fenlibao.p2p.controller.v_1.v_1_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.Information;
import com.fenlibao.p2p.model.entity.notice.KnowEarly;
import com.fenlibao.p2p.model.entity.notice.KnowMore;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.KnowEarlyDetailVO;
import com.fenlibao.p2p.model.vo.notice.KnowEarlyVO;
import com.fenlibao.p2p.model.vo.notice.KnowMoreDetailVO;
import com.fenlibao.p2p.model.vo.notice.KnowMoreVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.PageList;
import com.fenlibao.p2p.util.paginator.domain.Pager;
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
@RestController("v_1_0_0/SecurityKnowController")
@RequestMapping(value = "loanSecurity")
public class SecurityKnowController {

    private static final Logger logger= LogManager.getLogger(NoticeController.class);

    @Resource
    NoticeService noticeService;


    @RequestMapping(value = "getKnowMoreList", method = RequestMethod.GET)
    HttpResponse getKnowMoreList(HttpServletRequest request,
                                 @ModelAttribute BaseRequestForm paramForm, Integer pageNo, Integer pageSize) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            List<KnowMoreVO> voList = new ArrayList<KnowMoreVO>();
            PageBounds pageBounds=new PageBounds(pageNo, pageSize);
            List<KnowMore> infoList = noticeService.getKnowMoreList(pageBounds);
            //infoList.addAll(infoList1);
            Pager pager = new Pager(infoList);
            if(infoList != null && infoList.size() >0){
                for(KnowMore info:infoList) {
                    KnowMoreVO vo = new KnowMoreVO();

                    vo.setInformationId(info.getId());
                    vo.setInformationTitle(info.getTitle());
                    vo.setPublishTime(info.getPublishTime().getTime()/1000);
                    //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                    vo.setSource(info.getSource());
                    vo.setIntroduction(info.getIntroduction());
                    voList.add(vo);
                }
            }
            /*response.getData().put("updateTime", updateTime);*/
            //response.getData().put("knowMoreList", voList);
            pager.setItems(voList);
            response.setData(CommonTool.toMap(pager));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }




    @RequestMapping(value = "getKnowMoreInfo", method = RequestMethod.GET)
    HttpResponse getKnowMoreInfo(HttpServletRequest request,
                                 @ModelAttribute BaseRequestForm paramForm,
                                 @RequestParam(required = true, value = "id") String id) throws Exception{

        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate() || StringUtils.isEmpty(id)){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            KnowMoreDetailVO vo = new KnowMoreDetailVO();
            KnowMore infoDetail = noticeService.getKnowMoreDetail(Integer.valueOf(id));
            if(infoDetail!=null){

                vo.setId(infoDetail.getId());
                vo.setTitle(infoDetail.getTitle());
                vo.setPublishTime(infoDetail.getPublishTime().getTime()/1000);
                vo.setContent(infoDetail.getContent());
                //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                vo.setSource(infoDetail.getSource());
                vo.setIntroduction(infoDetail.getIntroduction());
                vo.setImgUrl(infoDetail.getImgUrl());
                vo.setNextId(infoDetail.getNextId());
                vo.setNextTitle(infoDetail.getNextTitle());
                vo.setPreId(infoDetail.getPreId());
                vo.setPreTitle(infoDetail.getPreTitle());
                vo.setUrl(infoDetail.getUrl());
            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }


    @RequestMapping(value = "getKnowEarlyList", method = RequestMethod.GET)
    HttpResponse getKnowEarlyList(HttpServletRequest request,
                                  @ModelAttribute BaseRequestForm paramForm, String noticeType, Integer pageNo, Integer pageSize) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            List<KnowEarlyVO> voList = new ArrayList<KnowEarlyVO>();
            PageBounds pageBounds=new PageBounds(pageNo, pageSize);
            List<KnowEarly> infoList = noticeService.geKnowEarlyList(pageBounds);
            Pager pager = new Pager(infoList);
            if(infoList != null && infoList.size() >0){
                for(KnowEarly info:infoList) {
                    KnowEarlyVO vo = new KnowEarlyVO();

                    vo.setInformationId(info.getId());
                    vo.setInformationTitle(info.getTitle());
                    vo.setPublishTime(info.getPublishTime().getTime()/1000);
                    //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                    vo.setIntroduction(info.getIntroduction());
                    vo.setImgUrl(info.getImgUrl());
                    vo.setSource(info.getSource()   );
                    voList.add(vo);
                }
            }
            /*response.getData().put("updateTime", updateTime);*/
            pager.setItems(voList);
            response.setData(CommonTool.toMap(pager));
          /*  response.getData().put("knowMoreList", voList);*/
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }




    @RequestMapping(value = "getKnowEarlyInfo", method = RequestMethod.GET)
    HttpResponse getKnowEarlyInfo(HttpServletRequest request,
                                  @ModelAttribute BaseRequestForm paramForm,
                                  @RequestParam(required = true, value = "id") String id) throws Exception{

        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate() || StringUtils.isEmpty(id)){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            KnowEarlyDetailVO vo = new KnowEarlyDetailVO();
            KnowEarly infoDetail = noticeService.getKnowEarlyDetail(Integer.valueOf(id));
            if(infoDetail!=null){

                vo.setId(infoDetail.getId());
                vo.setTitle(infoDetail.getTitle());
                vo.setContent(infoDetail.getContent());
                vo.setIntroduction(infoDetail.getIntroduction());
                vo.setPublishTime(infoDetail.getPublishTime().getTime()/1000);
                //vo.setNoticeDetailUrl(Config.get("noticeDetail.url")+info.getNoticeId());
//					String s=info.getSummary();
//					s=subString(s,40);//20中文或40字符
                vo.setSource(infoDetail.getSource());
                vo.setIntroduction(infoDetail.getIntroduction());
                vo.setImgUrl(infoDetail.getImgUrl());
                vo.setNextId(infoDetail.getNextId());
                vo.setNextTitle(infoDetail.getNextTitle());
                vo.setPreId(infoDetail.getPreId());
                vo.setPreTitle(infoDetail.getPreTitle());
                vo.setUrl(infoDetail.getUrl());

            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }
}
