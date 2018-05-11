package com.fenlibao.p2p.launch;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.fenlibao.p2p.launch.filter.CORSFilter;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.CleanupMybatisPaginatorListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.ServletRegistration.Dynamic;
import java.util.EnumSet;
import java.util.Set;

import static com.fenlibao.p2p.util.loader.Config.load;
import static com.fenlibao.p2p.util.loader.Message.message;
import static com.fenlibao.p2p.util.loader.Payment.payment;
import static com.fenlibao.p2p.util.loader.Sender.sender;

/**
 * Initializer
 *
 * @author kop_still
 */
public class ServletRouterInitializer implements ServletContainerInitializer {

    private static final Logger logger = LogManager.getLogger(ServletRouterInitializer.class.getName());

    private static final String MULTIPART_CONFIG_LOCATION = "/tmp";
    private static final long MULTIPART_CONFIG_MAX_FILE_SIZE = 20848820;
    private static final long MULTIPART_CONFIG_MAX_REQUEST_SIZE = 418018841;
    private static final int MULTIPART_CONFIG_FILE_SIZE_THRESHOLD = 1048576;

    private static final String HTTP_ENCODING = "UTF-8";

    private static final String SPRING_SERVLET_NAME = "spring";

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        long start = System.currentTimeMillis();
        try {
            load();

            message();

            sender();

            payment();

            servletContext.setInitParameter("log4jConfiguration", "classpath:log4j2.xml");

            servletContext.setInitParameter("contextConfigLocation", "classpath:spring-config.xml");

            servletContext.addListener(ContextLoaderListener.class);

            //Mybitas异步查询，由于默认分页查询使用了线程池，所以在使用时就要加入清理监听器，以便在停止服务时关闭线程池 add by laubrence 2016-5-5 15:02:24
            servletContext.addListener(CleanupMybatisPaginatorListener.class);

            CharacterEncodingFilter springEncodingFilter = new CharacterEncodingFilter();
            springEncodingFilter.setEncoding(HTTP_ENCODING);
            springEncodingFilter.setForceEncoding(true);
            servletContext.addFilter("springEncodingFilter", springEncodingFilter)
                    .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

            String suffix = Config.get("interface.suffix");

//            FilterRegistration.Dynamic apiFilter = servletContext.addFilter("apiFilter", APIFilter.class);
//            apiFilter.addMappingForUrlPatterns(
//                    EnumSet.allOf(DispatcherType.class), true,
//                    StringUtils.isBlank(suffix) ? "/" : "*." + suffix);

            FilterRegistration.Dynamic hiddenHttpMethodFilter = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
            hiddenHttpMethodFilter.setInitParameter("encoding", HTTP_ENCODING);
            hiddenHttpMethodFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), true, SPRING_SERVLET_NAME);

            FilterRegistration.Dynamic httpPutFormContentFilter = servletContext.addFilter("httpPutFormContentFilter", HttpPutFormContentFilter.class);
            httpPutFormContentFilter.setInitParameter("encoding", HTTP_ENCODING);
            httpPutFormContentFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), true, SPRING_SERVLET_NAME);

//            FilterRegistration.Dynamic authFilter = servletContext.addFilter("authFilter", AuthFilter.class);
//            authFilter.setInitParameter("encoding", HTTP_ENCODING);
//            authFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), true, SPRING_SERVLET_NAME);

            FilterRegistration.Dynamic druidWebStatFilter = servletContext.addFilter("druidWebStatFilter", WebStatFilter.class);
            druidWebStatFilter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
            druidWebStatFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

            FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
            corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
            
//            FilterRegistration.Dynamic versionControllerFilter = servletContext.addFilter("versionControllerFilter", VersionControllerFilter.class);
//            versionControllerFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

            XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
            applicationContext.setConfigLocation("classpath:spring-mvc.xml");
            Dynamic spring = servletContext.addServlet(SPRING_SERVLET_NAME, new DispatcherServlet(applicationContext));
            spring.setLoadOnStartup(0);
            spring.addMapping(StringUtils.isBlank(suffix) ? "/" : "*." + suffix);
            MultipartConfigElement multipartConfig = new MultipartConfigElement(
                    MULTIPART_CONFIG_LOCATION,
                    MULTIPART_CONFIG_MAX_FILE_SIZE,
                    MULTIPART_CONFIG_MAX_REQUEST_SIZE,
                    MULTIPART_CONFIG_FILE_SIZE_THRESHOLD);
            spring.setMultipartConfig(multipartConfig);

            Dynamic druid = servletContext.addServlet("druid", StatViewServlet.class);
            druid.setInitParameter("loginUsername", Config.get("druid.monitor.username"));
            druid.setInitParameter("loginPassword", Config.get("druid.monitor.password"));
            druid.setLoadOnStartup(1);
            druid.addMapping("/druid/*");
        } catch (Exception e) {
            logger.error("`ServletContainerInitializer init error`", e);
        }
        long end = System.currentTimeMillis();
        long spend = end - start;
        logger.info("`ServletContainerInitializer init in " + spend + " ms`");
    }

}
