//package com.fenlibao.platform.test.jetty;
//
//import com.fenlibao.platform.resource.TestResource;
//import org.eclipse.jetty.server.Server;
//import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.UriBuilder;
//import java.net.URI;
//
///**
// * Created by Lullaby on 2016/1/28.
// */
//public class JettyServerTest {
//
//    private Server server;
//
//    private WebTarget target;
//
//    private URI uri;
//
//    @Before
//    public void before() throws Exception {
//        uri = UriBuilder.fromUri("http://localhost/").port(9999).build();
//        ResourceConfig config = new ResourceConfig(TestResource.class);
//        server = JettyHttpContainerFactory.createServer(uri, config);
//        server.start();
//        Client client = ClientBuilder.newClient();
//        target = client.target(uri);
//    }
//
//    @After
//    public void after() throws Exception {
//        server.stop();
//    }
//
//    @Test
//    public void test() {
//        String response = target.path("test").request().get(String.class);
//        Assert.assertEquals("Got it!", response);
//    }
//
//}
