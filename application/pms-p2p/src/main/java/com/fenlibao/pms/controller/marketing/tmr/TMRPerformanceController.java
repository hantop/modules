package com.fenlibao.pms.controller.marketing.tmr;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.exception.ImportExcelException;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.marketing.*;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.marketing.tmr.TMRPerformanceService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营销管理 电销绩效详情
 */
@RestController
@RequestMapping("marketing/tmr")
public class TMRPerformanceController {

    @Resource
    TMRPerformanceService tmrService;

    @RequiresPermissions("marketing:view")
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                             TMRPerformanceForm tmrPerformanceForm) {
        ModelAndView view = new ModelAndView("marketing/tmr");
        RowBounds bounds = new RowBounds(page, limit);
        tmrPerformanceForm.setVisible(true);
        List<TMRPerformanceVO> tmrList = tmrService.getTMRPerformanceList(tmrPerformanceForm,bounds);
        PageInfo<TMRPerformanceVO> paginator = new PageInfo<>(tmrList);
        view.addObject("list", tmrList);
        view.addObject("tmrPerformanceForm", tmrPerformanceForm);
        view.addObject("paginator", paginator);
        view.addObject("total", paginator.getPageSize());
        return view;
    }

    /**
     * 导入营销报表
     *
     * @param file
     * @return
     * @throws Throwable
     */
    @RequiresPermissions("marketing:import")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Map<String, Object> importExcel(@RequestParam("file") MultipartFile file) throws Throwable {
        Map<String, Object> resultMap = new HashMap<>();
        String grantName = file.getOriginalFilename();
        if (grantName.indexOf(".") > 0) {
            grantName = grantName.substring(0, grantName.lastIndexOf("."));
        }
        //查找是否有相同的文件名
        TMRPerformanceVO fileTmp = tmrService.findRecordByFileName(grantName);

        if(fileTmp != null){
            resultMap.put("status","已经存相同的文件名！！！");
        }else {
            String headers[] = {"被叫号码", "结束时间"};
            List<String[]> importList = null;
            try {
                importList = POIUtil.getImportedData(file, headers);
            } catch (ImportExcelException e) {
                e.printStackTrace();
                String fileError = e.getMessage();
                resultMap.put("status",fileError);
                return resultMap;
            }
            //开始插入记录
            TMRPerformanceVO tmr = new TMRPerformanceVO();
            boolean importDo = false;
            tmr.setFileName(grantName);
            try {
                String[] fileNameArr = grantName.split("_");
                /*tmr.setTmrNumber(grantName.substring(0,10));
                tmr.setTmrName(grantName.substring(12));*/
                tmr.setTmrNumber(fileNameArr[0]);
                tmr.setTmrName(fileNameArr[1]);
                tmr.setCreatetime(new Timestamp(System.currentTimeMillis()));
                importDo = true;
            }catch (Exception  e){
                e.printStackTrace();
                resultMap.put("status","文件名格式不正确！！！");
                return resultMap;
            }

            if(importDo){
             //   ret.addAll(tmrService.createTMRPerformance(tmr, importList));
                try{
                    Integer tmrId = tmrService.createTMRPerformance(tmr, importList);
                    if(tmrId != null && tmrId > 0){
                        //判断异常名单信息
                        List<TMRExcelVO> tmrExcelVOs = tmrService.troubleUserInfo(tmrId,new RowBounds());

                        if(tmrExcelVOs != null && tmrExcelVOs.size() > 0){
                            Integer regCount  = 0;
                            Integer repeCount = 0;
                            for (TMRExcelVO tmp : tmrExcelVOs) {
                                String tip = tmp.getMsg();
                                if (tip.indexOf("未注册") >= 0){
                                    regCount++;
                                }
                                if (tip.indexOf("重复") >= 0){
                                    repeCount++;
                                }
                            }
                            resultMap.put("regCount",regCount);
                            resultMap.put("repeCount",repeCount);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultMap.put("status","系统繁忙，请稍后再尝试！！！");
                    return resultMap;
                }

            }
            resultMap.put("status","success");
        }
        return resultMap;
    }

    /**
     * 计算营销报表数据
     *
     * @param id
     * @return
     * @throws Throwable
     */
    @RequiresPermissions("marketing:calculate")
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public Map<String, Object> calculate(
            @RequestParam(required = false, defaultValue = "0") int id
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
           String calResult = tmrService.calculatePerformance(id);
            if("1".equals(calResult)){
                resultMap.put("codeStatus","success");
            }else{
                resultMap.put("codeStatus","noValue");
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("codeStatus","error");
        }

        return resultMap;
    }

    /**
     * 删除营销报表数据
     *
     * @param id
     * @return
     * @throws Throwable
     */
    @RequiresPermissions("marketing:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(
            @RequestParam(required = false, defaultValue = "0") int id
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            tmrService.deletePerformance(id);
            resultMap.put("codeStatus","success");
        }catch (Exception e){
            resultMap.put("codeStatus","error");
        }

        return resultMap;
    }

    /**
     * 异常名单
     *
     * @param id
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/trouble", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView trouble(
            @RequestParam(required = false, defaultValue = "0") int id,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        ModelAndView view = new ModelAndView("marketing/tmr/trouble");
        RowBounds bounds = new RowBounds(page, limit);
        TMRPerformanceVO tmrInfo = tmrService.getTMRinfo(id);
        List<TMRExcelVO> tmrExcelVOs = tmrService.troubleUserInfo(id,bounds);
        PageInfo<TMRExcelVO> paginator = new PageInfo<>(tmrExcelVOs);
        view.addObject("list", tmrExcelVOs);
        view.addObject("info",tmrInfo);
        view.addObject("paginator", paginator);
        view.addObject("total", paginator.getPageSize());
        return view;
    }

    /**
     * 查看详情
     *
     * @return
     * @throws
     */
    @RequiresPermissions("marketing:detail")
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView detail(TMRInvestUserForm investUserForm,
                               @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                               @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        ModelAndView view = new ModelAndView("marketing/tmr/detail");
        RowBounds bounds = new RowBounds(page, limit);

        TMRPerformanceVO tmrInfo = tmrService.getTMRinfo(investUserForm.getId());
        List<TMRInvestUserVO> tmrList = tmrService.getTMRInvesterList(investUserForm,bounds);
        List<TMRInvestUserVO> tmrList2 = tmrService.getTMRInvesterTotal(investUserForm,new RowBounds());
        if (tmrList != null && tmrList.size() > 0){
            for (TMRInvestUserVO tmr:tmrList) {
                String investDate = tmr.getInvestDate();
                String[] dateArr = investDate.split("_");
                if("M".equals(dateArr[1])){
                    tmr.setInvestDate(dateArr[0].concat("月"));
                }else if("D".equals(dateArr[1])){
                    tmr.setInvestDate(dateArr[0].concat("天"));
                }
            }
        }

        TMRPerformanceVO tmrTotal = new TMRPerformanceVO();
        if (tmrList2 != null && tmrList2.size() > 0){
            tmrTotal.setNumbers(tmrList2.size() + "");
            BigDecimal tmpInvest  = new BigDecimal(0);
            BigDecimal tmpRed = new BigDecimal(0);
            for (TMRInvestUserVO invester: tmrList2) {
                tmpInvest = tmpInvest.add(invester.getInvestMoney());
                tmpRed = tmpRed.add(invester.getActivateRedBag());
            }
            tmrTotal.setInvestTotal(tmpInvest);
            tmrTotal.setActivateTotal(tmpRed);
        }

        PageInfo<TMRInvestUserVO> paginator = new PageInfo<>(tmrList);
        view.addObject("list", tmrList);
        view.addObject("tmrPerformanceForm", investUserForm);
        view.addObject("tmrInfo", tmrInfo);
        view.addObject("tmrTotal", tmrTotal);
        view.addObject("paginator", paginator);
        view.addObject("total", paginator.getPageSize());
        return view;
    }

    @RequiresPermissions("marketingDetail:import")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void export(HttpServletResponse response, TMRInvestUserForm tmrInvestUserForm) {
        RowBounds bounds = new RowBounds();
        List<TMRInvestUserVO> tmrList = tmrService.getTMRInvesterList(tmrInvestUserForm,bounds);

        for (TMRInvestUserVO tmr: tmrList) {
            tmr.setInvestTimeStr(DateUtil.getDateTime(tmr.getInvestTime()));
        }
        String headers[] = {"手机号码", "姓名", "借款标题", "借款期限", "投资日期", "投资金额","激活返现券金额"};
        String fieldNames[] = {"investPhone", "investUser", "investBid", "investDate", "investTimeStr", "investMoney","activateRedBag"};
        POIUtil.export(response, headers, fieldNames, tmrList);
    }
}
