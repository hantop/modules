package com.fenlibao.p2p.controller.v_1.v_1_0_0.privatemessage;

import com.fenlibao.p2p.model.entity.PrivateMessage;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.PrivateMessageVo;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 站内信
 *
 * Created by chenzhixuan on 2016/7/28.
 */
@RestController("v_1_0_0/PrivateMessageController")
@RequestMapping("user/message")
public class PrivateMessageController {
    private static final Logger logger = LogManager.getLogger(PrivateMessageController.class);

    @Resource
    private PrivateMessageService privateMessageService;

    /**
     * 用户消息标记已读
     *
     * @param paramForm
     * @param token
     * @param userId     用户ID（需要）
     * @param messageIds 用户消息ID，多个ID用户英文逗号拼接
     * @return
     */
    @RequestMapping(value = "read", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
    public HttpResponse updateUserMessageRead(BaseRequestForm paramForm,
                                          @RequestParam(required = false, value = "token") String token,
                                          @RequestParam(required = false, value = "userId") String userId,
                                          @RequestParam(required = false, value = "messageIds") String messageIds) {
        logger.info("request paramter[userId:{}]", new Object[]{userId, messageIds});
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(messageIds)) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }
            // 修改用户消息状态
            updateUserMessageStatus(userId, messageIds, response, Status.YD.name(), Status.WD.name());
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[PrivateMessageController.deleteUserMessage]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 修改用户消息状态
     *
     * @param userId
     * @param messageIds
     * @param response
     * @param status
     * @param whereStatus
     */
    private void updateUserMessageStatus(String userId, String messageIds, HttpResponse response, String status, String whereStatus) {
        // 分隔传入参数并加入List
        List<Integer> messageIdList = new ArrayList<>();
        try {
            for (String messageId : messageIds.split(",")) {
                messageIdList.add(Integer.valueOf(messageId));
            }
            if (messageIdList.size() > 0) {
                privateMessageService.updateUserMessageStatus(messageIdList, Integer.parseInt(userId), status, whereStatus);
            } else {
                response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
            }
        } catch (NumberFormatException e) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
        }
    }

    /**
     * 删除用户消息
     *
     * @param paramForm
     * @param token
     * @param userId     用户ID（需要）
     * @param messageIds 用户消息ID，多个ID用户英文逗号拼接
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, headers = APIVersion.V_1_0_0)
    public HttpResponse deleteUserMessage(BaseRequestForm paramForm,
                                          @RequestParam(required = false, value = "token") String token,
                                          @RequestParam(required = false, value = "userId") String userId,
                                          @RequestParam(required = false, value = "messageIds") String messageIds) {
        logger.info("request paramter[userId:{}]", new Object[]{userId, messageIds});
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(messageIds)) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }
            // 修改用户消息状态
            updateUserMessageStatus(userId, messageIds, response, Status.SC.name(), null);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[PrivateMessageController.deleteUserMessage]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 获取用户消息接口
     *
     * @param pageRequestForm
     * @param userId
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse messageList(PageRequestForm pageRequestForm,
                                    @RequestParam(required = false, value = "token") String token,
                                    @RequestParam(required = false, value = "userId") String userId,
                                    @RequestParam(required = false, value = "versionType") String versionType) {
        HttpResponse response = new HttpResponse();
        try {
            if (!pageRequestForm.validate()) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }

            VersionTypeEnum versionTypeEnum = VersionTypeEnum.parse(versionType);
            versionTypeEnum = versionTypeEnum != null ? versionTypeEnum : VersionTypeEnum.PT;

            PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
            List<PrivateMessageVo> voList = new ArrayList<>();
            List<PrivateMessage> list = privateMessageService.getUserMessages(userId, versionTypeEnum, pageBounds);

            for (PrivateMessage mes : list) {
                PrivateMessageVo vo = new PrivateMessageVo();
                vo.setMessageId(mes.getId());
                vo.setTitle(mes.getTitle());
                vo.setContent(mes.getContent());
                vo.setTimestamp(mes.getSendTime().getTime());
                if (mes.getStatus().equals(Status.WD.name())) {
                    vo.setStatus(1);
                } else {
                    vo.setStatus(0);
                }
                voList.add(vo);
            }
            Pager pager = new Pager(list);
            pager.setItems(voList);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[PrivateMessageController.messageList]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 用户未读消息数量
     *
     * @param paramForm
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "unread/count", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse messageUnderCount(BaseRequestForm paramForm,
                                          @RequestParam(required = false, value = "token") String token,
                                          @RequestParam(required = false, value = "userId") String userId) {
        logger.info("request paramter[userId:{}]", new Object[]{userId});
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(userId)) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }

            //获取用户未读消息数量
            int unreadCount = privateMessageService.getUserMessageCount(userId, Status.WD.name());
            response.getData().put("unreadCount", unreadCount);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[PrivateMessageController.messageUnderCount]" + ex.getMessage(), ex);
        }
        return response;
    }

}
