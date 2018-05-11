//package com.fenlibao.platform.test;
//
//import org.eclipse.jetty.server.Server;
//import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//
//import javax.ws.rs.core.UriBuilder;
//import java.net.URI;
//
///**
// * Created by Lullaby on 2016/2/2.
// */
//public class Test {
//
//    @org.junit.Test
//    public void bootstrap() throws Exception {
//        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
//        ResourceConfig config = new ResourceConfig().packages("com.fenlibao.platform.resource");
//        Server server = JettyHttpContainerFactory.createServer(baseUri, config);
//        server.start();
//    }
//
//}
