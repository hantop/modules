package com.fenlibao.pms.controller.system;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.common.enums.LogUrlEnum;
import com.fenlibao.model.pms.da.channel.form.ChannelStatisticsForm;
import com.fenlibao.model.pms.da.channel.vo.ChannelStatisticsVO;
import com.fenlibao.model.pms.da.system.form.PmsLogFrom;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsGroupForm;
import com.fenlibao.model.pms.idmt.log.PmsLog;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.idmt.group.PmsGroupService;
import com.fenlibao.service.pms.idmt.log.PmsLogService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 组织机构管理
 * Created by chenzhiliangon 2016/4/18.
 */
@RestController
@RequestMapping("system/pmslog")
public class PmsLoginLogController {

    @Resource
    private PmsLogService pmsLogService;
    @Resource
    private PmsGroupService pmsGroupService;
    @Resource
    private UserDetailsService userDetailsService;

    @RequiresPermissions("organization:view")
    @RequestMapping
    public ModelAndView pmsGroupPage(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit, PmsLogFrom logForm) {
        ModelAndView view = new ModelAndView("system/log/index");
        RowBounds bounds = new RowBounds(page, limit);
        String user = logForm.getUserName();
        String name = logForm.getRealName();
        String startDateStr = logForm.getStartDate();
        String endDateStr = logForm.getEndDate();
        String status = logForm.getStatus();
        Date startDate = null;
        Date endDate = null;
        String defaultStatus="";
        if(StringUtils.isEmpty(startDateStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            logForm.setStartDate(todayDateStr);
            startDate = DateUtil.StringToDate(todayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isEmpty(endDateStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            logForm.setEndDate(todayDateStr);
            endDate = DateUtil.StringToDate(todayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        if (status == null){
            status = "1";
        }
        defaultStatus = status;
        List<PmsLog> pmsLogList = pmsLogService.getPmsLog(user,name,status,startDate,endDate,bounds);

        for (PmsLog pmsLog : pmsLogList){
            if ("1".equals(status)){
                if ("login".equals(pmsLog.getOperation())){
                    pmsLog.setOperation("登入");
                }else if ("logout".equals(pmsLog.getOperation())){
                    pmsLog.setOperation("登出");
                }
            }else {
                pmsLog.setOperation(LogUrlEnum.parse(Integer.parseInt(pmsLog.getOperation())).getName());
            }
        }
        PageInfo<PmsLog> paginator = new PageInfo<>(pmsLogList);
        return view.addObject("pmsLogList",pmsLogList)
                .addObject("paginator", paginator)
                .addObject("logForm",logForm)
                .addObject("defaultStatus",defaultStatus);
    }

    @RequiresPermissions("channelStatistics:export")
    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void channelStatisticsListExport(HttpServletResponse response, PmsLogFrom logForm) {
        RowBounds bounds = new RowBounds();
        String user = logForm.getUserName();
        String name = logForm.getRealName();
        String startDateStr = logForm.getStartDate();
        String endDateStr = logForm.getEndDate();
        String status = logForm.getStatus();
        Date startDate = null;
        Date endDate = null;
        if(!StringUtils.isEmpty(startDateStr)){
            startDate = DateUtil.StringToDate(startDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(endDateStr)){
            endDate = DateUtil.StringToDate(endDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        if (!StringUtils.isEmpty(status)&&status.equals("1")){
            status="login";
        }
        if (!StringUtils.isEmpty(status)&&status.equals("2")){
            status="logout";
        }
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 获取渠道统计数据
        List<PmsLog> pmsLogList =  pmsLogService.getPmsLog(user,name,status,startDate,endDate,bounds);
        if(pmsLogList != null && pmsLogList.size() > 0) {
            List<PmsLogExportObject> exportList=new ArrayList<PmsLogExportObject>();
            for(int i = 0;i < pmsLogList.size(); i++){
                PmsLogExportObject item=new PmsLogExportObject();
                item.time=dateFormater.format(pmsLogList.get(i).getTime());
                item.userName=""+pmsLogList.get(i).getUserName()==null?"":pmsLogList.get(i).getUserName();
                item.realName=""+pmsLogList.get(i).getRealName();
                item.ip=""+pmsLogList.get(i).getIp();
                item.phone=""+pmsLogList.get(i).getPhone();
                String operation=null;
                if(pmsLogList.get(i).getOperation().equals("login")){
                    operation="登入";
                }
                else{
                    operation="登出";
                }
                item.status=""+operation;
                exportList.add(item);
            }
            String headers[]={"时间","登录账号","真实姓名","手机号码","登录状态","地址"};
            String fieldNames[]={"time", "userName","realName","phone","status","ip"};
            POIUtil.export(response, headers,fieldNames, exportList);
        }
    }

    private class PmsLogExportObject{
        private String userName;
        private String realName;
        private String time;
        private String ip;
        private String status;
        private String phone;
    }

    private class status{
        private String id;
        private String name;
    }
}

