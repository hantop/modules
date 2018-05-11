package com.fenlibao.p2p.weixin.repository;

import com.fenlibao.p2p.weixin.domain.TemplateMsg;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bogle on 2015/12/11.
 */
public interface TemplateMsgRepository extends CrudRepository<TemplateMsg, Long> {

    /**
     * 根据msgId修改发送结果状态
     *
     * @param msgId
     * @param status
     */
    @Modifying
    @Transactional
    @Query(value = "update com.fenlibao.p2p.weixin.domain.TemplateMsg set status = ?2 where msgid = ?1", nativeQuery = false)
    void setStatusFor(String msgId, String status);
}
