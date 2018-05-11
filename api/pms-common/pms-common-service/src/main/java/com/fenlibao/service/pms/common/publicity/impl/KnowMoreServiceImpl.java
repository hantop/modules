package com.fenlibao.service.pms.common.publicity.impl;

import com.fenlibao.dao.pms.common.publicity.KnowMoreMapper;
import com.fenlibao.model.pms.common.publicity.KnowMore;
import com.fenlibao.service.pms.common.publicity.KnowMoreService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
@Service
public class KnowMoreServiceImpl implements KnowMoreService {

    @Resource
    private KnowMoreMapper knowMoreMapper;


    @Override
    public List<KnowMore> getKnowMoreList(KnowMore knowMore, RowBounds bound, String startTime, String endTime) {
        if(StringUtils.isNotBlank(startTime)){
            startTime=startTime+" 00:00:00";
        }
        if(StringUtils.isNotBlank(endTime)){
            endTime=endTime+" 23:59:99";
        }

        return knowMoreMapper.getKnowMoreList(knowMore,bound,startTime,endTime);
    }

    @Override
    public void editKnowMore(KnowMore knowMore) {
        knowMoreMapper.editKnowMore(knowMore);
    }

    @Override
    public void deleteKnowMore(String[] idList) {
        knowMoreMapper.deleteKnowMore(idList);
    }

    @Override
    public void updateAllPicUploade(KnowMore knowMore) {
        knowMoreMapper.updateAllPicUploade(knowMore);
    }

    @Override
    public List<KnowMore> getKnowEarlyListByIds(String[] idList) {
        return knowMoreMapper.getKnowEarlyListByIds(idList);
    }
}
