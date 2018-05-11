package com.fenlibao.platform.util.file;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

/**
 * Created by Administrator on 2017/7/15.
 */
public class HttpHeadersUtil {

    public static  Header[] getgetHeaders(){
        Header headerVersion = new Header() {
            @Override
            public String getName() {
                return "version";
            }

            @Override
            public String getValue() {
                return "4.0.0";
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        Header headerAuthorization = new Header() {
            @Override
            public String getName() {
                return "Authorization";
            }

            @Override
            public String getValue() {
                return "dEtOblZ0aTdmMGY3M2dNUE51SHlBZFRZOStLQ012ZUFCQkkvbUpnSjg5cUVJWS9GQkxkbER2QUJkUlQzalBMZQ";
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        Header[] headersArray = {headerVersion,headerAuthorization};
        return headersArray;
    }



}
