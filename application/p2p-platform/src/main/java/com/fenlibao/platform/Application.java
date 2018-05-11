package com.fenlibao.platform;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.aeonbits.owner.ConfigFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fenlibao.platform.common.config.SystemConfig;
import com.fenlibao.platform.filter.IPConfigFilter;
import com.fenlibao.platform.filter.ResourceFilter;
import com.fenlibao.platform.filter.SignFilter;
import com.fenlibao.platform.module.BusinessModule;
import com.fenlibao.platform.resource.BaseResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class Application {

    private static final String RESOURCE_PACKAGE_NAME = "com.fenlibao.platform";

    private static final String SERVLET_CONTEXT_PATH = "/*";

    private static final String GENERAL_FILTER_PATH = "/*";
    
    private static final String COMMON_RESOURCE_FILTER_PATH = "/common*";

    private static final String MYBATIS_CONFIG_PATH = "mybatis-config.xml";

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {

        logger.info("Server starting...");

        final SystemConfig config = ConfigFactory.create(SystemConfig.class);

        final ServletContainer servlet = new ServletContainer(
                new ResourceConfig().packages(RESOURCE_PACKAGE_NAME));

        final Injector injector = Guice.createInjector(new ServletModule() {

            protected void configureServlets() {
                filter(GENERAL_FILTER_PATH).through(IPConfigFilter.class);
                filter(COMMON_RESOURCE_FILTER_PATH).through(ResourceFilter.class);
                filter(GENERAL_FILTER_PATH).through(SignFilter.class);
                serve(SERVLET_CONTEXT_PATH).with(servlet);
            }

        }, new XMLMyBatisModule() {

            protected void initialize() {
                install(JdbcHelper.MySQL);
                setEnvironmentId(config.datasourceEnvironment());
                setClassPathResource(MYBATIS_CONFIG_PATH);
            }

        }, new BusinessModule());

        ServletContextHandler handler = new ServletContextHandler();

        handler.addEventListener(new ServletContextListener() {

            public void contextInitialized(ServletContextEvent servletContextEvent) {
                logger.info("Servlet context initialized.");
            }

            public void contextDestroyed(ServletContextEvent servletContextEvent) {
                logger.info("Servlet context destroyed.");
            }

        });
        handler.addEventListener(new GuiceServletContextListener() {

            protected Injector getInjector() {
                return injector;
            }

        });
        handler.setBaseResource(new BaseResource());
        handler.setContextPath(config.contextPath());

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        handler.addFilter(GuiceFilter.class, GENERAL_FILTER_PATH, dispatcherTypes);

        Server server = new Server(config.port());
        server.setHandler(handler);
        server.start();

        ServiceLocator serviceLocator = servlet.getApplicationHandler().getServiceLocator();
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        serviceLocator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);

        logger.info("Server started.");

        server.join();
    }

}
