package com.fenlibao.pms.controller.channel.channel;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.da.channel.vo.ChannelVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.pms.controller.channel.dto.ChannelDTO;
import com.fenlibao.service.pms.da.channel.channel.ChannelService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lullaby on 2015-11-18 20:52
 */
@RestController
@RequestMapping("channel")
public class ChannelController {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private ChannelService channelService;

    @RequestMapping("index")
    public ModelAndView channelIndex() {
        ModelAndView view = new ModelAndView("channel/index");
        return view;
    }


    @RequestMapping(value = "firstChannels", method = RequestMethod.GET)
    public List<ChannelVO> getFirstChannels() {
        return channelService.getParentChannels();
    }

    @RequestMapping(value = "getChannelsByParent", method = RequestMethod.GET)
    public List<ChannelVO> getChannelsByParent(int parentId) {
        return channelService.getChannelsByParent(parentId);
    }

    @RequiresPermissions("channelType:view")
    @RequestMapping("/parent/list-parent")
    public ModelAndView listParent(
            ChannelDTO channelDTO,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {

        RowBounds bounds = new RowBounds(page, limit);
        List<ChannelVO> list = channelService.findParentPager(channelDTO, bounds);
        PageInfo<ChannelVO> paginator = new PageInfo<>(list);
        return new ModelAndView("channel/parent/list-parent")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("channelDTO", channelDTO);
    }

    @RequiresPermissions(value={"channelType:create","channelType:edit"},logical=Logical.OR)
    @RequestMapping(value = "/parent/edit-parent", method = RequestMethod.GET)
    public ModelAndView editParent(@RequestParam(required = false, defaultValue = "0") int id) {
        ChannelVO channelVO = new ChannelDTO();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            channelVO = channelService.getChannelById(id);
        }
        return new ModelAndView("channel/parent/edit-parent").addObject("channel", channelVO).addObject("title", title);
    }

    /**
     * 新增，修改1级渠道
     *
     * @param channel
     * @param result
     * @return
     */
    @RequiresPermissions(value={"channelType:create","channelType:edit"},logical=Logical.OR)
    @RequestMapping(value = "/parent/edit-parent", method = RequestMethod.POST)
    public ModelAndView editParent(@Valid @ModelAttribute("channel") ChannelVO channel, BindingResult result) {
        int id;
        String title = "新增";
        if (channel.getId() > 0) {
            title = "编辑";
        }
        if (result.hasFieldErrors() && result.getFieldError("name") != null) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("channel/parent/edit-parent").addObject("title", title);
        }
        if (channel.getId() > 0) {
            id = channelService.updateByPrimaryKeySelective(channel);
        } else {
            id = channelService.insertSelective(channel);
        }
        String url = "redirect:/channel/parent/edit-parent?success=" + (id > 0 ? true : false);
        if (id <= 0) {
            url = "channel/parent/edit-parent";
        }
        ModelAndView modelAndView = new ModelAndView(url).addObject("title", title).addObject("code", id);
        return modelAndView;
    }

    @RequiresPermissions("channelType:delete")
    @RequestMapping(value = "/parent/deleteParent", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity deleteParent(@RequestBody @NotEmpty List<Integer> channelVOs, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        int count = channelService.deleteParent(channelVOs);
        Map<String, Serializable> body = new HashMap<>();
        boolean flag = count > 0 ? true : false;
        if (count < 0) {
            body.put("message", "存在渠道信息，不能删除");
        } else if (count == 0) {
            body.put("message", "删除失败，请联系管理员");
        } else {
            body.put("message", "删除成功");
        }
        body.put("success", flag);

        return new ResponseEntity<>(body, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    //---------------------------------------------------------------------二级渠道管理---------------------------------------

    /**
     * 渠道列表
     *
     * @param channelDTO
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("channelManage:view")
    @RequestMapping("/child/list-child")
    public ModelAndView listChild(
            ChannelDTO channelDTO,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<ChannelVO> list = channelService.findChildPager(channelDTO, bounds);
        PageInfo<ChannelVO> paginator = new PageInfo<>(list);
        return new ModelAndView("channel/child/list-child")
                .addObject("list", list)
                .addObject("paginator", paginator)
                .addObject("channelDTO", channelDTO);
    }

    /**
     * 新增，修改二级渠道
     *
     * @param id
     * @return
     */
    @RequiresPermissions(value={"channelManage:create","channelManage:edit"},logical=Logical.OR)
    @RequestMapping(value = "/child/edit-child", method = RequestMethod.GET)
    public ModelAndView editChild(@RequestParam(required = false, defaultValue = "0") int id) {
        ChannelVO channelVO = new ChannelDTO();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            channelVO = channelService.getChannelById(id);
        }
        List<ChannelVO> parentVOs = this.channelService.getParentChannels();
        return new ModelAndView("channel/child/edit-child")
                .addObject("parent", parentVOs)
                .addObject("channel", channelVO)
                .addObject("title", title);
    }

    @RequiresPermissions(value={"channelManage:create","channelManage:edit"},logical=Logical.OR)
    @RequestMapping(value = "/child/edit-child", method = RequestMethod.POST)
    public ModelAndView editChild(@Valid @ModelAttribute("channel") ChannelVO channel, BindingResult result) {
        String title = "新增";
        if (channel.getId() > 0) {
            title = "编辑";
        }
        if (result.hasErrors()) {
            LOG.error("提交数据格式有错误");
            List<ChannelVO> parentVOs = this.channelService.getParentChannels();
            return new ModelAndView("channel/child/edit-child")
                    .addObject("parent", parentVOs)
                    .addObject("title", title);
        }
        int id;

        if (channel.getId() > 0) {
            id = channelService.updateByPrimaryKeySelective(channel);
        } else {
            id = channelService.insertSelective(channel);
        }
        String url = "redirect:/channel/child/edit-child?success=" + (id > 0 ? true : false);
        ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("code", id);
        if(id < 0) {
            url = "channel/child/edit-child";
            List<ChannelVO> parentVOs = this.channelService.getParentChannels();
            modelAndView.addObject("parent", parentVOs);
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequiresPermissions("channelManage:delete")
    @RequestMapping(value = "/child/deleteChild", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity deleteChild(@RequestBody @NotEmpty List<Integer> channelVOs, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        boolean flag = channelService.deleteChild(channelVOs) > 0 ? true : false;
        return new ResponseEntity<>(flag, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
