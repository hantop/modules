package com.fenlibao.p2p.controller.v_1.v_1_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.Information;
import com.fenlibao.p2p.model.entity.notice.InformationDetail;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.InformationDetailVO;
import com.fenlibao.p2p.model.vo.notice.InformationVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.axis.utils.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */
@RestController("v_1_0_0/InformationController")
@RequestMapping("information")
public class InformationController {
    private static final Logger logger= LogManager.getLogger(ArticleController.class);

    @Resource
    NoticeService noticeService;

    /**
     * 行业资讯
     * @param request
     * @param paramForm
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getArticleList(HttpServletRequest request,
                                @ModelAttribute BaseRequestForm paramForm,
                                @RequestParam(required = false, value = "informationType") String informationType,
                                Integer page, Integer limit) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            List<InformationVO> voList = new ArrayList<InformationVO>();
            PageBounds pageBounds=new PageBounds(page, limit);
            Map map = new HashMap<>();
            if(!StringUtils.isEmpty(informationType)){
                map.put("informationType",informationType);
            }
            List<Information> infoList = noticeService.getInformationList(informationType, pageBounds);
            Pager pager = new Pager(infoList);
            if(infoList != null && infoList.size() >0){
                for(Information info:infoList) {
                    InformationVO vo = new InformationVO();
                    vo.setInformationId(info.getInformationId());
                    vo.setInformationTitle(info.getInformationTitle());
                    String url= FileUtils.getPicURL(info.getImgcode(), Config.get("static.file.url"));
                    vo.setImgUrl(url);
                    vo.setIntroduction(info.getIntroduction());
                    vo.setPublishTime(info.getPublishTime());
                    vo.setSource(info.getSource());
                    vo.setChannel(info.getChannel());
                    //拼详情链接
                    vo.setInformationDetailUrl("/information/detail?clientType=7&informationId="+info.getInformationId());
                    voList.add(vo);
                }
            }
            pager.setItems(voList);
            response.setData(CommonTool.toMap(pager));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }

    /**
     * 1.5.2.	行业资讯详细
     * @param  paramForm
     * @param  informationId
     * @return response
     * @throws Exception
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getArticle(@ModelAttribute BaseRequestForm paramForm,
                            @RequestParam(required = false, value = "informationType") String informationType,
                            String informationId) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate() || StringUtils.isEmpty(informationId)){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            InformationDetailVO vo = new InformationDetailVO();
            Map map =new HashMap<>();
            if(!StringUtils.isEmpty(informationId)){

                map.put("informationId",Integer.valueOf(informationId));
            }
            if(!StringUtils.isEmpty(informationType)){
                map.put("informationType",informationType);
            }

            InformationDetail infoDetail = noticeService.getInformationDetail(map);
            if(infoDetail!=null){
                String contentString = com.fenlibao.p2p.util.StringHelper.format(infoDetail.getContent(), Config.get("static.file.url"));
                vo.setContent(contentString);
                vo.setIntroduction(infoDetail.getIntroduction());
                vo.setPublishTime(infoDetail.getPublishTime());
                vo.setSource(infoDetail.getSource());
                vo.setInformationId(infoDetail.getInformationId());
                vo.setInformationTitle(infoDetail.getInformationTitle());
                vo.setKeyword(infoDetail.getKeyword());
                String url= FileUtils.getPicURL(infoDetail.getImgcode(), Config.get("static.file.url"));
                vo.setImgUrl(url);
                vo.setChannel(infoDetail.getChannel());
                //上一条 下一条
                vo.setPreInformationId(infoDetail.getPreInformationId());
                vo.setPreInformationTitle(infoDetail.getPreInformationTitle());
                vo.setNextInformationId(infoDetail.getNextInformationId());
                vo.setNextInformationTitle(infoDetail.getNextInformationTitle());
            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }
}
