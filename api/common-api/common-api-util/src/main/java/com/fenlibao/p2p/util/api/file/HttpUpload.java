package com.fenlibao.p2p.util.api.file;

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
 * Created by Lullaby on 2015/7/27.
 */
public class HttpUpload {

    private String filename;

    private String filepath;

    public HttpUpload(String filepath, String filename) {
        super();
        this.filename = filename;
        this.filepath = filepath;
    }

    public boolean upladFile(InputStream input) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut put = new HttpPut(filepath + filename);
        HttpEntity httpEntity = new InputStreamEntity(input);
        CloseableHttpResponse response = null;
        try {
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
            put.setEntity(bufferedHttpEntity);
            response = httpclient.execute(put);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).startsWith("2")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(input, response);
        }
    }

    public static void main(String[] args) {
        HttpUpload delete = new HttpUpload("http://192.168.27.236/resources", "/client/startup/fEoNa5i7Ddleqt6Tx6Ang7m5m6ux5n2s.png");
        System.out.println(delete.deleteFile());
    }

    public boolean deleteFile() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(filepath + filename);
        System.out.println(filepath + filename);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(delete);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            if (String.valueOf(statusCode).startsWith("2")) {
                return true;
            } else {
                return false;
            }
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
