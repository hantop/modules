//package com.fenlibao.platform.test.guice;
//
//import com.fenlibao.platform.service.TestService;
//import com.fenlibao.platform.service.impl.TestServiceImpl;
//import com.google.inject.Injector;
//import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
//import org.junit.Before;
//import org.junit.Test;
//import org.mybatis.guice.MyBatisModule;
//import org.mybatis.guice.datasource.c3p0.C3p0DataSourceProvider;
//import org.mybatis.guice.datasource.helper.JdbcHelper;
//
//import java.util.Properties;
//
//import static com.google.inject.Guice.createInjector;
//import static com.google.inject.name.Names.bindProperties;
//
///**
// * Created by Lullaby on 2016/1/30.
// */
//public class GuiceTest {
//
//    private Injector injector;
//
//    private TestService testService;
//
//    @Before
//    public void setupMyBatisGuice() throws Exception {
//        this.injector = createInjector(new MyBatisModule() {
//               @Override
//               protected void initialize() {
//                   install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
//
//                   bindDataSourceProviderType(C3p0DataSourceProvider.class);
//                   bindTransactionFactoryType(JdbcTransactionFactory.class);
//                   addMapperClasses("com.fenlibao.platform.mapper");
//
//                   bindProperties(binder(), createTestProperties());
//                   bind(TestService.class).to(TestServiceImpl.class);
//               }
//           }
//        );
//        this.testService = this.injector.getInstance(TestService.class);
//    }
//
//    @Test
//    public void test() {
//        System.out.println(testService.getMessage());
//    }
//
//    protected static Properties createTestProperties() {
//        Properties myBatisProperties = new Properties();
//        myBatisProperties.setProperty("mybatis.environment.id", "test");
//        myBatisProperties.setProperty("JDBC.username", "sa");
//        myBatisProperties.setProperty("JDBC.password", "");
//        myBatisProperties.setProperty("JDBC.autoCommit", "false");
//        return myBatisProperties;
//    }
//
//}
