package com.fenlibao.p2p.weixin.service;


import com.fenlibao.p2p.weixin.message.Message;
import com.fenlibao.p2p.weixin.message.template.TemplateMsg;

/**
 * 消息处理类，要求之类实现该接口实现直接的业务
 * Created by Administrator on 2015/6/18.
 */
public interface IMessageHandler {


    /**
     * 点击事件
     *
     * @param message
     * @param wxApi
     * @return
     */
    Message clickEvent(Message message, IWxApi wxApi);

    /**
     * 用户关注事件
     *
     * @param message
     * @return
     */
    Message subscribe(Message message);

    /**
     * 多客户消息
     *
     * @param message
     * @return
     */
    Message multiService(Message message);

    /**
     * 客服关闭会话通知
     *
     * @param message
     * @return
     */
    Message kfCloseSessionEvent(Message message);


    /**
     * 发送完模板消息后的后置处理，可以保存到数据库或者做其他操作
     *
     * @param results
     * @return
     */
    void completeTemplateMsg(TemplateMsg results);

    /**
     * 模板消息接收状态回调
     * @param message
     */
    void completeStatusTemplateMsg(Message message);
}
