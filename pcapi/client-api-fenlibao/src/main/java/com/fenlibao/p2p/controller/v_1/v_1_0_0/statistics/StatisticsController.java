package com.fenlibao.p2p.controller.v_1.v_1_0_0.statistics;
import com.fenlibao.p2p.model.entity.Statistic.ReportDeatil;
import com.fenlibao.p2p.model.entity.Statistic.Statistics;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.StatisticVO;
import com.fenlibao.p2p.service.statistics.StatisticService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 平台数据统计
 * Created by Administrator on 2016/11/21.
 */
@RestController("v_1_0_0/StatisticsController")
@RequestMapping("statistics")
public class StatisticsController {

    private static final Logger logger= LogManager.getLogger(StatisticsController.class);

    @Resource
    StatisticService statisticService;

    @RequestMapping(value = "list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getArticle(@ModelAttribute BaseRequestForm paramForm) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            StatisticVO vo = new StatisticVO();
            Statistics statistic = statisticService.getStatisticList();
            if(statistic!=null){
                vo.setInvestMoeny(statistic.getInvestMoeny());
                vo.setInvestNum(statistic.getInvestNum());
                vo.setProfitMoneyForInvestor(statistic.getProfitMoneyForInvestor());
                vo.setReceivableMoney(statistic.getReceivableMoney());
                vo.setRegisterNum(statistic.getRegisterNum());
                vo.setTurnoverFee(statistic.getTurnoverFee());
            }
            response.setData(CommonTool.toMap(vo));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }


    /**
     * 获取财务年度审计列表
     * @param paramForm
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "reportList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse reportList(@ModelAttribute BaseRequestForm paramForm,String type) throws Exception{
        HttpResponse response = new HttpResponse();
        try{
            if(!paramForm.validate()){
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            //报告列表
            PageBounds pageBounds=new PageBounds(1, 10);
            List<ReportDeatil> reportList = this.statisticService.getReportList(type,pageBounds);
            Pager pager = new Pager(reportList);

            response.setData(CommonTool.toMap(pager));
            return response;
        }catch(Exception ex){
            throw ex;
        }
    }
}
