package com.fenlibao.common.pms.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lullaby on 2015-07-27 14:37
 */
public class HttpUpload {

    private static final String SUCCESS_PREFIX = "2";

    private String filename;

    private String filepath;

    private String fullpath;

    public HttpUpload(String fullpath) {
        super();
        this.fullpath = fullpath;
    }

    public HttpUpload(String filepath, String filename) {
        super();
        this.filename = filename;
        this.filepath = filepath;
    }

    public boolean upladFile(InputStream input) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut put = new HttpPut(filepath.concat(filename));
        HttpEntity httpEntity = new InputStreamEntity(input);
        CloseableHttpResponse response = null;
        try {
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
            put.setEntity(bufferedHttpEntity);
            response = httpclient.execute(put);
            int statusCode = response.getStatusLine().getStatusCode();
            return String.valueOf(statusCode).startsWith(SUCCESS_PREFIX);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(input, response);
        }
    }

    public boolean deleteFile() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(fullpath);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(delete);
            int statusCode = response.getStatusLine().getStatusCode();
            return String.valueOf(statusCode).startsWith(SUCCESS_PREFIX);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, response);
        }
    }

    private void closeResources(InputStream input, CloseableHttpResponse response) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
