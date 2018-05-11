package com.fenlibao.pms.controller.system;

import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.service.pms.idmt.permit.PmsPermitService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lullaby on 2015-11-04 10:53
 */
@RestController
@RequestMapping("system/pmsmenu")
public class PmsMenuController {
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PmsPermitService pmsPermitService;

    @RequestMapping(value = "/menu/{type}", method = RequestMethod.GET)
    public List<PermitVO> getMenu(@PathVariable(value = "type") String type) {
        Subject subject = SecurityUtils.getSubject();
        String username = subject.getPrincipal().toString();
        PmsUser user = userDetailsService.findByUsername(username);
        List<PermitVO> menus = pmsPermitService.getPermitsByUser(user.getId(), type);
        return menus;
    }

}
