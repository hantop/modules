package com.fenlibao.p2p.weixin.repository;

import com.fenlibao.p2p.weixin.domain.Channel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Bogle on 2015/12/9.
 */
public interface ChannelRepository extends CrudRepository<Channel, Integer> {

    Channel findById(Integer id);

    List<Channel> findByIdBetween(Integer min, Integer max);
}
