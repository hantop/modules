package com.fenlibao.pms.controller.cs.replacePhone;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.cs.ReplacePhoneInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.form.ReplacePhoneForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.ReplacePhoneService;
import com.fenlibao.service.pms.da.exception.ExcelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cs/phone")
public class ReplacePhoneController {

    @Resource
    private ReplacePhoneService replacePhoneService;

    //更换手机号码查询列表
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                ReplacePhoneForm replacePhoneForm) {
        ModelAndView view = new ModelAndView("cs/phone");
        RowBounds bounds = new RowBounds(page, limit);

        List<ReplacePhoneInfo> replacePhoneList = replacePhoneService.getReplacePhoneList(replacePhoneForm, bounds);
        PageInfo<ReplacePhoneInfo> paginator = new PageInfo<>(replacePhoneList);
        view.addObject("list", replacePhoneList);
        view.addObject("replacePhoneForm", replacePhoneForm);
        view.addObject("paginator", paginator);
        return view;
    }

    //要更换的手机号码验证
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Map<String, Object> search(ReplacePhoneForm replacePhoneForm) {

        //必须是实名认证
        Map<String, Object> resultMap = new HashMap<>();
        String newPhone = replacePhoneForm.getPhoneNum();
        String sysCode = "0000";

        try {
            if (!StringUtils.isBlank(newPhone)){
                resultMap.put("sysCode",sysCode);
                //1.当前用户是否注册过
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("phoneNum",newPhone.trim());
                UserAuthInfo userRegiterInfo = replacePhoneService.getUserInfoByPhone(paramMap);
                if(userRegiterInfo == null){
                    resultMap.put("regCode","0");
                    return resultMap;
                }

                //2.查询当前手机是否实名认证过
                UserAuthInfo userAuthInfo = replacePhoneService.getUserAuthByPhone(newPhone.trim());
                if(userAuthInfo != null){
                    String idcardNum = userAuthInfo.getIdcard();
                    if(!StringUtils.isBlank(idcardNum)){
                        resultMap.put("authCode","1");
                        resultMap.put("userId", userAuthInfo.getUserId());
                        resultMap.put("phoneNum", userAuthInfo.getPhoneNum());
                        resultMap.put("idcard", idcardNum);
                    }else {
                        resultMap.put("authCode","0");
                    }
                }else {
                    resultMap.put("authCode","0");
                }
            }else {
                sysCode = "1000";
                resultMap.put("sysCode",sysCode);
            }
        }catch (ExcelException e){
            e.printStackTrace();
            sysCode = "1001";
            resultMap.put("sysCode",sysCode);
        }

        return resultMap;
    }

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public Map<String, Object> auth(@RequestParam(required = true) String rePhone) {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        if(!StringUtils.isBlank(rePhone)){
            paramMap.put("phoneNum",rePhone.trim());
        }
        UserAuthInfo userInfo = replacePhoneService.getUserInfoByPhone(paramMap);
        if (userInfo != null){
            UserAuthInfo userAuthInfo = replacePhoneService.getUserAuthByPhone(userInfo.getPhoneNum());
            if(userAuthInfo != null){   //新号码注册未实名认证
                resultMap.put("doCode","1004");
                return resultMap;
            }
            resultMap.put("doCode","1000");
        }
        return resultMap;
    }

    /**
     * @author wangyunjing
     * @date 20151228
     * @todo 更换手机号码 更换条件 1.新的手机没有注册；2.用户的手机号码和传递过来旧号码是否相同;
     * @todo 验证通过，更新数据库，跟新顺序：
     * @todo 用户表(t6101),资金账号(t6101),用户推广信息(t6111),历史站内信，发放工资用户信息(t6193),电信流量(flb.t_flow)
     */
    @RequestMapping(value = "replace", method = RequestMethod.POST)
    public Map<String, Object> replace(
                                @RequestParam(required = true) String rePhone,
                                @RequestParam(required = true) int userId,
                                @RequestParam(required = true) String oldPhone) {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        paramMap.put("phoneNum",rePhone.trim());
        UserAuthInfo userInfo = replacePhoneService.getUserInfoByPhone(paramMap);
        if(userInfo != null){   //已经注册过来
            // TODO: 2016/7/6  @Jing 如果没有实名认证还是可以更换的
            UserAuthInfo userAuthInfo = replacePhoneService.getUserAuthByPhone(userInfo.getPhoneNum());
            if(userAuthInfo != null){   //新号码注册未实名认证
                resultMap.put("regCode","1");
                return resultMap;
            }
        }
        paramMap.clear();
        paramMap.put("userId",userId);
        UserAuthInfo userAuthInfo = replacePhoneService.getUserInfoByPhone(paramMap);
        if(userInfo != null){
            String phoneTmp = userAuthInfo.getPhoneNum();
            if(!oldPhone.equals(phoneTmp)){
                resultMap.put("verifyCode","1");
                return resultMap;
            }
        }

        //更新手机号码
        try {
            replacePhoneService.replacePhoneLogic(userId,rePhone,oldPhone,userInfo);
            resultMap.put("doCode","1000");
        }catch (Exception e){
            resultMap.put("doCode","1001");
        }

        return resultMap;
    }


    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response,ReplacePhoneForm replacePhoneForm) {
        RowBounds bounds = new RowBounds();
        List<ReplacePhoneInfo> replacePhoneList = replacePhoneService.getReplacePhoneList(replacePhoneForm, bounds);

        List<ReplacePhoneExport> ReplacePhoneExportList = new ArrayList<>();
        for (ReplacePhoneInfo replacePhone : replacePhoneList) {
            ReplacePhoneExport rp = new ReplacePhoneExport();
            rp.operatorTime = DateUtil.getDateTime(replacePhone.getOperatorTime());
            rp.operator = replacePhone.getOperator();
            rp.oldPhone = replacePhone.getOldPhone();
            rp.newPhone = replacePhone.getNewPhone();
            ReplacePhoneExportList.add(rp);
        }
        String headers[] = {"更换日期", "操作人", "原手机号码", "新手机号码"};
        String fieldNames[] = {"operatorTime", "operator", "oldPhone", "newPhone"};
        POIUtil.export(response, headers, fieldNames, ReplacePhoneExportList);
    }

    private class ReplacePhoneExport{
        String operatorTime;
        String operator;
        String oldPhone;
        String newPhone;
    }
}
