package com.fenlibao.p2p.weixin.repository;

import com.fenlibao.p2p.weixin.domain.Subscribe;
import org.springframework.data.repository.CrudRepository;

public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    Subscribe findByOpendId(String openId);
}