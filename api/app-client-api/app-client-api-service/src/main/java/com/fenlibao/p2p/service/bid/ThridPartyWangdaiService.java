package com.fenlibao.p2p.service.bid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */
public interface ThridPartyWangdaiService {

    /**
     * 统计当天上上签金额总量
     * @param map
     * @return
     * @throws Exception
     */
    BigDecimal getBidInfoSumAmout( Map<String,Object> map) throws  Exception;

    /**
     * 获取第一个标发标日期
     * @param map
     * @return
     * @throws Exception
     */
    Date getThridPartyFristDate(Map<String,Object> map) throws  Exception;

    /**
     * 获取某一天标ids
     * @param map
     * @return
     * @throws Exception
     */
    List<HashMap> getThridPartyRandomDayBids(Map<String,Object> map) throws  Exception;

}
