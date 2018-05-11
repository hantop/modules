package com.fenlibao.pms.controller.riskcontrol;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.constant.RedisConst;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.tool.SerializationUtils;
import com.fenlibao.model.pms.da.riskcontrol.*;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.encrypt.AES;
import com.fenlibao.service.pms.da.riskcontrol.RiskcontrolService;
import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 风控管理控制器
 *
 * Created by Administrator on 2017/11/17.
 */

@RestController
@RequestMapping("riskcontrol")
public class RiskcontrolCotroller {

    private static final Logger logger = LoggerFactory.getLogger(RiskcontrolCotroller.class);


    @Resource
    RiskcontrolService riskcontrolService;



    @RequestMapping("index")
//    @RequiresPermissions("riskcontrol:view")
    public ModelAndView index() {

        return new ModelAndView("riskcontrol/index");
    }


    /**
     * 进单审核
     * @param page
     * @param limit
     * @param consumerBid 查询条件
     * @return
     */
    @RequestMapping("consumerBidList")
    public ModelAndView consumerList(@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit, ConsumerBid consumerBid) {

        ModelAndView view = new ModelAndView("riskcontrol/consumer_list");

        RowBounds bounds = new RowBounds(page, limit);
        List<ConsumerBid> consumerBidList = riskcontrolService.getConsumerBidList(bounds,consumerBid);

        PageInfo<ConsumerBid> paginator = new PageInfo<ConsumerBid>(consumerBidList);
        view.addObject("paginator", paginator);
        view.addObject("consumerBid", consumerBid);
        view.addObject("consumerBidList",consumerBidList);

        return view;
    }

    @RequestMapping("baseInfo")
    public ModelAndView baseInfo(ConsumerBid consumerBid, HttpServletRequest request){
        ModelAndView view = new ModelAndView("riskcontrol/base_info");
        RiskBaseInfo riskBaseInfo = riskcontrolService.getRiskBaseInfo(consumerBid);
        this.getConsumerBid(consumerBid,view);

        view.addObject("riskBaseInfo",riskBaseInfo);
        return view;
    }

    private void getConsumerBid(ConsumerBid consumerBid,ModelAndView view) {
//        String sessionKey = "sessionConsumerBid"+consumerBid.getId();
//        Jedis jedis = RedisFactory.getResource();
//        Object obj = jedis.get(sessionKey);
//
//
//        ConsumerBid sessionConsumerBid =(ConsumerBid)obj;
            RowBounds bounds = new RowBounds(0, 1);
        consumerBid.setBankNo("1111");
            ConsumerBid sessionConsumerBid  = riskcontrolService.getConsumerBidList(bounds,consumerBid).get(0);



            view.addObject("consumerBid", sessionConsumerBid);
    }


    @RequestMapping("workInfo")
    public ModelAndView workInfo(ConsumerBid consumerBid){
        ModelAndView view = new ModelAndView("riskcontrol/work_info");
        RiskWorkInfo riskWorkInfo = riskcontrolService.getRiskWorkInfo(consumerBid);
        this.getConsumerBid(consumerBid,view);
        view.addObject("riskWorkInfo",riskWorkInfo);
        return view;
    }

    @RequestMapping("antiFraud")
    public ModelAndView antiFraud(ConsumerBid consumerBid){
        ModelAndView view = new ModelAndView("riskcontrol/anti_fraud");

        RiskAntiFraud riskAntiFraud = riskcontrolService.getRiskAntiFraud(consumerBid);

        this.transAntiFraud(riskAntiFraud);
        this.getConsumerBid(consumerBid,view);
        view.addObject("riskAntiFraud",riskAntiFraud);
        return view;
    }

    @RequestMapping("mutiplyBorrow")
    public ModelAndView mutiplyBorrow(ConsumerBid consumerBid){
        ModelAndView view = new ModelAndView("riskcontrol/mutiply_borrow");

        RiskMutiBorrow riskMutiBorrow = riskcontrolService.getRiskMutiBorrow(consumerBid);
        this.getConsumerBid(consumerBid,view);
        view.addObject("riskMutiBorrow",riskMutiBorrow);
        return view;
    }

    /**
     * 转换属性值枚举型转为对应的值
     * @param riskAntiFraud
     */
    private void transAntiFraud(RiskAntiFraud riskAntiFraud)  {

        if(riskAntiFraud==null){
            logger.info("riskAntiFraud为空，没有对应的欺诈信息");
            return;
        }


        Field[] fields = riskAntiFraud.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Class clazz = riskAntiFraud.getClass();
            String name = fields[i].getName(); // 获取属性的名字
            name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
            try {
                Method getmethod = clazz.getMethod("get"+name);//方法

                Object value = getmethod.invoke(riskAntiFraud);//属性值
                if("0".equals(value)){
                    Method setmethod = clazz.getMethod("set"+name,String.class);
                    setmethod.setAccessible(true);
                    setmethod.invoke(riskAntiFraud,"直接命中");

                }
                if("1".equals(value)){
                    Method setmethod = clazz.getMethod("set"+name,String.class);
                    setmethod.setAccessible(true);
                    setmethod.invoke(riskAntiFraud,"命中1度人脉");

                }
                if("2".equals(value)){
                    Method setmethod = clazz.getMethod("set"+name,String.class);
                    setmethod.setAccessible(true);
                    setmethod.invoke(riskAntiFraud,"命中2度人脉");

                }

                if("空".equals(value)||value==null||"".equals(value)){
                    Method setmethod = clazz.getMethod("set"+name,String.class);
                    setmethod.setAccessible(true);
                    setmethod.invoke(riskAntiFraud,"未命中");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
