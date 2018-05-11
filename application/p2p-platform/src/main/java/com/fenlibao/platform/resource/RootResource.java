package com.fenlibao.platform.resource;

import javax.ws.rs.*;

/**
 * Created by Lullaby on 2016/1/29.
 */
@Path("/")
public class RootResource {

    @GET
    public String get() {
        return "hello, world";
    }

    @POST
    public String post() {
        return "hello, world";
    }

    @PUT
    public String put() {
        return "hello, world";
    }

    @DELETE
    public String delete() {
        return "hello, world";
    }

    @HEAD
    public String head() {
        return "hello, world";
    }

    @OPTIONS
    public String options() {
        return "hello, world";
    }

}
