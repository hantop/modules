package com.fenlibao.p2p.common.util.http;

import com.fenlibao.p2p.common.util.http.defines.HttpResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by lenovo on 2015/9/4.
 */
public class CustomResponseHandler implements ResponseHandler<HttpResult> {

    @Override
    public HttpResult handleResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        HttpResult result = new HttpResult(statusCode);
        if (statusCode != HttpStatus.SC_OK) {
            result.setMessage("ERROR");
            throw new HttpResponseException(statusCode, null);
        } else {
            HttpEntity entity = response.getEntity();
            byte[] bytes = entity != null ? EntityUtils.toByteArray(entity) : null;
            result.setBytes(bytes);
            result.setMessage("OK");
        }
        return result;
    }
}
