package com.fenlibao.service.pms.common.publicity.impl;

import com.fenlibao.dao.pms.common.publicity.KnowEarlyMapper;
import com.fenlibao.model.pms.common.publicity.KnowEarly;
import com.fenlibao.service.pms.common.publicity.KnowEarlyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
@Service
public class KnowEarlyServiceImpl implements KnowEarlyService {

    @Resource
    private KnowEarlyMapper knowEarlyMapper;


    @Override
    public List<KnowEarly> getKnowEarlyList(KnowEarly KnowEarly, RowBounds bound, String startTime, String endTime) {
        if(StringUtils.isNotBlank(startTime)){
            startTime=startTime+" 00:00:00";
        }
        if(StringUtils.isNotBlank(endTime)){
            endTime=endTime+" 23:59:99";
        }

        return knowEarlyMapper.getKnowEarlyList(KnowEarly,bound,startTime,endTime);
    }

    @Override
    public void editKnowEarly(KnowEarly KnowEarly) {
        knowEarlyMapper.editKnowEarly(KnowEarly);
    }

    @Override
    public void deleteKnowEarly(String[] idList) {
        knowEarlyMapper.deleteKnowEarly(idList);
    }

    @Override
    public List<KnowEarly> getKnowEarlyListByIds(String[] idList) {
        return knowEarlyMapper.getKnowEarlyListByIds(idList);
    }

    @Override
    public void updateAllPicUploade(KnowEarly knowEarly) {
        knowEarlyMapper.updateAllPicUploade(knowEarly);
    }
}
