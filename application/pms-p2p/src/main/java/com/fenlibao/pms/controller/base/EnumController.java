package com.fenlibao.pms.controller.base;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.common.global.ResponseEnum;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.common.base.EnumService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lullaby on 2015-09-22 15:30
 */
@RestController
@RequestMapping("setting/enum")
public class EnumController extends BaseController {

    @Resource
    private EnumService enumService;

    @RequiresPermissions("settingEnum:view")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView enumlist(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            TEnum tenum) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView view = new ModelAndView("setting/enum/index");
        List<TEnum> enumlist = enumService.getEnumList(tenum, bounds);
        PageInfo<TEnum> paginator = new PageInfo<>(enumlist);
        view.addObject("list", enumlist)
            .addObject("paginator", paginator)
            .addObject("tenum", tenum);
        return view;
    }

    @RequiresPermissions("settingEnum:update")
    @RequestMapping(method = RequestMethod.PUT)
    public HttpResponse updateEnum(@ModelAttribute TEnum tenum) {
        HttpResponse response = new HttpResponse();
        if (StringUtils.isNotEmpty(tenum.getEnumTable()) &&
            StringUtils.isNotEmpty(tenum.getEnumColumn()) &&
            StringUtils.isNotEmpty(tenum.getEnumKey()) &&
            StringUtils.isNotEmpty(tenum.getEnumValue())) {
            int result = enumService.updateEnum(tenum);
            if (result < 1) {
                response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

    @RequiresPermissions("settingEnum:delete")
    @RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
    public HttpResponse deleteEnum(@PathVariable(value = "ids") String ids) {
        System.out.println(ids);
        HttpResponse response = new HttpResponse();
        if (StringUtils.isNotBlank(ids)) {
            String[] idArray = ids.split(",");
            int length = idArray.length;
            if (length > 0) {
                int result = enumService.deleteEnum(idArray);
                System.out.println(result);
                if (length != result) {
                    response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
                }
            } else {
                response.setCodeMessage(ResponseEnum.WRONG_PARAMETER.getCode(), ResponseEnum.WRONG_PARAMETER.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public TEnum getEnum(@RequestParam(required = true) int id) {
        return enumService.getEnumById(id);
    }

    @RequiresPermissions("settingEnum:create")
    @RequestMapping(value = "new", method = RequestMethod.POST)
    public HttpResponse addEnum(@ModelAttribute TEnum tenum) {
        HttpResponse response = new HttpResponse();
        if (StringUtils.isNotEmpty(tenum.getEnumTable()) &&
                StringUtils.isNotEmpty(tenum.getEnumColumn()) &&
                StringUtils.isNotEmpty(tenum.getEnumKey()) &&
                StringUtils.isNotEmpty(tenum.getEnumValue())) {
            int result = enumService.addEnum(tenum);
            if (result < 1) {
                response.setCodeMessage(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
            }
        } else {
            response.setCodeMessage(ResponseEnum.EMPTY_PARAMETER.getCode(), ResponseEnum.EMPTY_PARAMETER.getMessage());
        }
        return response;
    }

}
