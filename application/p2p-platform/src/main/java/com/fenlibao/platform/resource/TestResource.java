package com.fenlibao.platform.resource;

import com.fenlibao.platform.service.TestService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Iterator;
import java.util.Map;

@Path("test")
public class TestResource {

    @Inject
    private TestService dependency;

    @GET
    @Path("depend")
    @Produces("application/json")
    public String helloWorld() {
        return this.dependency.getMessage();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @POST
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    public String post() {
        return "hello";
    }

    @POST
    @Path("consume/text")
    @Consumes(MediaType.TEXT_PLAIN)
    public void postClichedMessage(String message) {
        System.out.println(message);
    }

    @GET
    @Path("smooth")
    public String smooth(
            @DefaultValue("1024") @QueryParam("step") int step,
            @DefaultValue("true") @QueryParam("min-m") boolean hasMin,
            @DefaultValue("true") @QueryParam("max-m") boolean hasMax,
            @DefaultValue("true") @QueryParam("last-m") boolean hasLast) {
        System.out.println(step);
        System.out.println(hasMin);
        System.out.println(hasMax);
        System.out.println(hasLast);
        return "204 - No Content";
    }

    @POST
    @Path("header")
    public void header(@Context HttpHeaders headers) {
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        Map<String, Cookie> cookies = headers.getCookies();
        Iterator<String> iterator = headerParams.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = headers.getHeaderString(key);
            System.out.println(key + " -> " + value);
        }
        System.out.println("------- 我是调皮的分割线 -------");
        Iterator<Map.Entry<String, Cookie>> ite = cookies.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Cookie> map = ite.next();
            String key = map.getKey();
            Cookie cookie = map.getValue();
            System.out.println("cookie -> " + key);
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
            System.out.println(cookie.getDomain());
            System.out.println(cookie.getPath());
            System.out.println(cookie.getVersion());
        }
    }

    @GET
    @Path("user/{username}")
    public String user(@PathParam("username") String username) {
        System.out.println(username);
        return username;
    }

}
