package com.fenlibao.pms.controller.weixin;


import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.model.pms.weixin.RobotMsg;
import com.fenlibao.model.pms.weixin.form.RobotMsgForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.weixin.component.JSONUtil;
import com.fenlibao.service.weixin.defines.Env;
import com.fenlibao.service.weixin.message.Button;
import com.fenlibao.service.weixin.message.WxMsg;
import com.fenlibao.service.weixin.service.WeixinService;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bogle on 2016/3/1.
 */
@RestController
@RequestMapping("weixin")
public class WeixinController {

    private static final Logger LOG = LoggerFactory.getLogger(WeixinController.class);
    private static final String BUTTONS = "BUTTONS";

    @Autowired
    private WeixinService weixinService;

    /**
     * 微信管理首页
     * @return
     */
    @RequestMapping("index")
    @RequiresPermissions("weixin:view")
    public ModelAndView index() {
        return new ModelAndView("weixin/index");
    }

    /**
     * 微信菜单页面
     * @return
     */
    @RequestMapping("menu/index")
    @RequiresPermissions("weixinmenu:view")
    public ModelAndView showMenu() {
        ModelAndView modelAndView = new ModelAndView("weixin/menu/index");
        Map<String, Object> top = new HashMap<>();
        try {
            List<Button> buttons = weixinService.getButton(Env.test);
            top.put("name", "微信菜单");
            top.put("open", "true");
            top.put("icon", "/static/component/zTree_v3/css/zTreeStyle/img/diy/7.png");
            top.put("sub_button", buttons);
        } catch (Exception e) {
           LOG.error(e.getMessage());
            top.put("name", e.getMessage());
            top.put("open", "true");
            top.put("icon", "/static/component/zTree_v3/css/zTreeStyle/img/diy/7.png");
            modelAndView.setViewName("weixin/menu/error");
            modelAndView.addObject("error",e.getMessage());
        }
        return modelAndView.addObject("menu", JSONUtil.writeValueAsString(top));
    }

    /**
     * 发布测试环境微信菜单
     * @param buttons
     * @param session
     * @return
     */
    @RequiresPermissions("weixinmenu:edit")
    @RequestMapping("menu/publishTestMenu")
    public Object publishTestMenu(@RequestBody List<Button> buttons, HttpSession session) {
        WxMsg wxMsg = null;
        try {
        	//session.setAttribute(BUTTONS, buttons);
            wxMsg = this.weixinService.publishMenu(Env.test, buttons);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            wxMsg = new WxMsg();
            wxMsg.setErrcode(-1);
            wxMsg.setErrmsg(e.getMessage());
        }

        return wxMsg;

    }

    /**
     * 发布正式环境微信菜单
     * @param session
     * @return
     */
    @RequiresPermissions("weixinmenu:edit")
    @RequestMapping("menu/publishProdMenu")
    public Object publishProdMenu(@RequestBody List<Button> buttons,HttpSession session) {
        //List<Button> buttons = (List<Button>) session.getAttribute(BUTTONS);
        if (buttons == null) {
            WxMsg wxMsg = new WxMsg();
            wxMsg.setErrcode(-1);
            wxMsg.setErrmsg("请按选发布到测试公众查看");
            return wxMsg;
        }

        WxMsg wxMsg = null;
        try {
            wxMsg = this.weixinService.publishMenu(Env.prod, buttons);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            wxMsg = new WxMsg();
            wxMsg.setErrcode(-1);
            wxMsg.setErrmsg(e.getMessage());
        }
        return wxMsg;

    }

    /**
     * 自动回复关键字消息列表
     * @param robotMsgForm
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("msg/index")
    @RequiresPermissions("weixinkeyword:view")
    public ModelAndView showMsg(
            RobotMsgForm robotMsgForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit
    ) {
        RowBounds bounds = new RowBounds(page, limit);
        robotMsgForm.setType(RobotMsg.Type.CONTENT);
        List<RobotMsg> list = this.weixinService.selectRobotMsgByType(robotMsgForm, bounds);
        PageInfo<RobotMsg> paginator = new PageInfo<>(list);
        return new ModelAndView("weixin/msg/index")
                .addObject("list", list)
                .addObject("paginator", paginator);
    }

    /**
     * 跳转到关键字消息编辑页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/msg/edit", method = RequestMethod.GET)
    @RequiresPermissions(value = {"weixinkeyword:view", "weixinkeyword:edit"}, logical = Logical.OR)
    public ModelAndView editMsg(@RequestParam(required = false, defaultValue = "0") int id) {
        RobotMsg robotMsg = new RobotMsgForm();
        String title = "新增";
        if (id > 0) {
            title = "编辑";
            robotMsg = weixinService.selectByPrimaryKey(id);
        }
        return new ModelAndView("weixin/msg/edit").addObject("msg", robotMsg).addObject("title", title);
    }

    /**
     * 新增/编辑关键字消息
     * @param robotMsgForm
     * @param result
     * @return
     */
    @RequestMapping(value = "/msg/edit", method = RequestMethod.POST)
    @RequiresPermissions(value = {"weixinkeyword:view", "weixinkeyword:edit"}, logical = Logical.OR)
    public ModelAndView editMsg(@Valid @ModelAttribute("msg") RobotMsgForm robotMsgForm, BindingResult result) {
        int id;
        String title = "新增";
        if (robotMsgForm.getId() > 0) {
            title = "编辑";
        }
        if (result.hasFieldErrors() && result.getFieldError("name") != null) {
            LOG.error("提交数据格式有错误");
            return new ModelAndView("/weixin/msg/edit").addObject("title", title);
        }
        robotMsgForm.setType(RobotMsg.Type.CONTENT);
        if (robotMsgForm.getId() > 0) {
            id = weixinService.updateByPrimaryKeySelective(robotMsgForm);
        } else {
            robotMsgForm.setCreateTime(new Date());
            robotMsgForm.setMsgType("text");
            id = weixinService.insertSelective(robotMsgForm);
        }
        String url = "redirect:/weixin/msg/edit?success=" + (id > 0 ? true : false);
        if (id <= 0) {
            url = "/weixin/msg/edit";
        }
        ModelAndView modelAndView = new ModelAndView(url).addObject("title", title).addObject("code", id);
        return modelAndView;
    }

    /**
     * 删除关键字消息
     * @param msgs
     * @param result
     * @return
     */
    @RequiresPermissions("weixinkeyword:delete")
    @RequestMapping(value = "/msg/delete", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity deleteChild(@RequestBody @NotEmpty List<Integer> msgs, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        boolean flag = weixinService.deleteByPrimaryKeys(msgs) > 0 ? true : false;
        return new ResponseEntity<>(flag, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
