package com.fenlibao.p2p.controller.v_1.v_1_0_0.notice;

import com.fenlibao.p2p.model.entity.notice.FriendLink;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.notice.FriendLinkVO;
import com.fenlibao.p2p.service.notice.NoticeService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */
@RestController("v_1_0_0/FriendLinkController")
@RequestMapping("friendLink")
public class FriendLinkController {

    @Resource
    NoticeService noticeService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    HttpResponse filesList(@ModelAttribute BaseRequestForm paramForm) throws Exception {
        HttpResponse response = new HttpResponse();
        //参数验证
        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

       List<FriendLink> accessoryList = noticeService.getFriendLink();

        if (accessoryList == null) {
            return response;
        }
        List<FriendLinkVO> voList = new ArrayList<FriendLinkVO>();

        for (FriendLink friendLink : accessoryList) {
            FriendLinkVO vo = new FriendLinkVO();
            vo.setFriendLinkId(friendLink.getFkId());
            vo.setFriendLinkName(friendLink.getName());
            vo.setFriendLinkUrl(friendLink.getUrl());
            vo.setCreateTime(friendLink.getCreateTime().getTime()/1000);
            voList.add(vo);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", voList);
        response.setData(map);
        return response;
    }
}
