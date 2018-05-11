package com.fenlibao.p2p.controller.v_3.v_3_1_0.pageset;

import com.fenlibao.p2p.controller.v_3.v_3_1_0.user.UserApiController;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2016/12/27 9:35
 */
@RestController("v_3_1_0/PageSetController")
@RequestMapping(value = "pageSet", headers = APIVersion.v_3_1_0)
public class PageSetController {
    private static final Logger logger= LogManager.getLogger(UserApiController.class);

    /**
     * 用户頁面控制
     * @param formExtend
     * @return
     */
    @RequestMapping(value = "showTopup", method = RequestMethod.GET)
    public HttpResponse showTopup(@ModelAttribute BaseRequestForm formExtend) {
        HttpResponse response = new HttpResponse();

        if (!formExtend.validate()) {
            logger.debug("request param is empty");
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
        }

        Map<String, Object> data = new HashMap<>(1);
        String showLLRechargeStr = Config.get("pageSet.lianlian.recharge");
        String showLLWithdrawStr = Config.get("pageSet.lianlian.withdraw");
        String showStr = Config.get("pageSet.cg.show");
        int showLLRecharge = 0;
        int showLLWithdraw = 0;
        try {
            showLLRecharge = Integer.valueOf(showLLRechargeStr);
            showLLWithdraw = Integer.valueOf(showLLWithdrawStr);
        } catch (Throwable ex) {
            showLLRecharge = 1;
            showLLWithdraw = 1;
        }

        int show = 0;
        try {
            show = Integer.valueOf(showStr);
        } catch (Throwable ex) {
            show = 1;
        }

        data.put("showLLRecharge", showLLRecharge);
        data.put("showLLWithdraw", showLLWithdraw);

        data.put("show", show);

        response.setData(data);
        return response;
    }

}
