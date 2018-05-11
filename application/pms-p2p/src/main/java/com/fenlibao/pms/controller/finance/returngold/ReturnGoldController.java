package com.fenlibao.pms.controller.finance.returngold;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.finance.form.ReturnGoldForm;
import com.fenlibao.model.pms.da.finance.vo.ReturnExperienceGoldVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.finance.returngold.ReturnGoldService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("finance/returngold")
public class ReturnGoldController {

    @Resource
    private ReturnGoldService returnGoldService;

    @RequiresPermissions("returnGold:view")
    @RequestMapping
    public ModelAndView list(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                             ReturnGoldForm returnGoldForm) {
        ModelAndView view = new ModelAndView("finance/returngold");
        RowBounds bounds = new RowBounds(page, limit);

        List<ReturnExperienceGoldVO> list = returnGoldService.getExperienceGoldCostList(returnGoldForm,bounds);
        BigDecimal total = returnGoldService.getExperienceGoldTotal(returnGoldForm);
        PageInfo<ReturnExperienceGoldVO> paginator = new PageInfo<>(list);
        view.addObject("list", list);
        view.addObject("returnGoldForm", returnGoldForm);
        view.addObject("paginator", paginator);
        view.addObject("total", total);
        return view;
    }

    @RequiresPermissions("returnGold:details")
    @RequestMapping(value = "details", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView details(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                @RequestParam(required = true) int gid,
                                @RequestParam(required = true) String startTime,
                                @RequestParam(required = true) String endTime,
                                @RequestParam(required = false) String telphone) {
        ModelAndView view = new ModelAndView("finance/returngold/details");
        RowBounds bounds = new RowBounds(page, limit);

        List<ReturnExperienceGoldVO> list = returnGoldService.getExperienceGolddetails(gid,startTime,endTime,telphone,bounds);
        PageInfo<ReturnExperienceGoldVO> paginator = new PageInfo<>(list);
        view.addObject("list", list);
        view.addObject("paginator", paginator);
        view.addObject("gid",gid);
        view.addObject("telphone",telphone);
        view.addObject("startTime",startTime);
        view.addObject("endTime",endTime);
        return view;
    }

    @RequiresPermissions("returnGold:export")
    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response, ReturnGoldForm returnGoldForm) {
        RowBounds bounds = new RowBounds();
        List<ReturnExperienceGoldVO> experienceGoldCostList = returnGoldService.getExperienceGoldCostList(returnGoldForm, bounds);
        BigDecimal total = returnGoldService.getExperienceGoldTotal(returnGoldForm);

        //总成本
        ReturnExperienceGoldVO returnExperienceGoldVO = new ReturnExperienceGoldVO();
        returnExperienceGoldVO.setActivityCode("总成本");
        returnExperienceGoldVO.setExperienceGold(total);
        experienceGoldCostList.add(returnExperienceGoldVO);

        String headers[] = {"体验金代码", "体验金金额", "体验金有效期", "年化收益", "到账人数", "产生成本"};
        String fieldNames[] = {"activityCode", "experienceGold", "effectDay", "yearYield", "usersCount", "userspay"};
        POIUtil.export(response, headers, fieldNames, experienceGoldCostList);
    }

    @RequiresPermissions("returnGoldDetails:export")
    @RequestMapping(value = "exportDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public void exportDetails(HttpServletResponse response,
                                @RequestParam(required = true) int gid,
                                @RequestParam(required = true) String startTime,
                                @RequestParam(required = true) String endTime,
                                @RequestParam(required = false) String telphone) {
        RowBounds bounds = new RowBounds();
        List<ReturnExperienceGoldVO> list = returnGoldService.getExperienceGolddetails(gid,startTime,endTime,telphone, bounds);
        String headers[] = {"产生日期", "用户手机号", "体验金利息"};
        String fieldNames[] = {"earningsDate", "telphone", "userspay"};
        POIUtil.export(response, headers, fieldNames, list);
    }
}
