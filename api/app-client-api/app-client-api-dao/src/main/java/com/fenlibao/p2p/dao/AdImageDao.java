package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.AdImage;

import java.util.List;
import java.util.Map;

public interface AdImageDao {

	List<AdImage> getAdImg(Map<String, Object> map);
}
