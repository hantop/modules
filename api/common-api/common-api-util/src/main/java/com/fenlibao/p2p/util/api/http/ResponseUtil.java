package com.fenlibao.p2p.util.api.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class ResponseUtil {

    private static final String RESPONSE_APPLICATION_JSON = "application/json; charset=utf-8";

    private static final String RESPONSE_TEXT_PLAIN = "text/plain; charset=utf-8";

    public static void response(Object data, HttpServletResponse response) {
        response.setContentType(RESPONSE_APPLICATION_JSON);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseText(Object data, HttpServletResponse response) {
        response.setContentType(RESPONSE_TEXT_PLAIN);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
