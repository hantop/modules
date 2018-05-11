package com.fenlibao.pms.launch.init;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.common.pms.util.loader.XINWANG_PRO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Lullaby on 2015/7/20.
 */
public class InitServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        Config.loadProperties();
        Message.loadProperties();
        XINWANG_PRO.loadProperties();
    }

}
