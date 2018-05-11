package com.fenlibao.p2p.weixin;

import com.fenlibao.p2p.weixin.defines.MsgType;
import com.fenlibao.p2p.weixin.message.Message;
import com.fenlibao.p2p.weixin.xstream.XStreamUtil;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.Socket;

/**
 * Elemental example for executing multiple POST requests sequentially.
 */
public class ElementalHttpPost {

//    @Test
    public void testThread() throws Exception {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestExpectContinue(true)).build();

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor(1);

        HttpCoreContext coreContext = HttpCoreContext.create();
        HttpHost host = new HttpHost("localhost", 8081);
        coreContext.setTargetHost(host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

        try {

            Message message = new Message();
            message.setFromUserName("gh_d5557832dff1");
            message.setToUserName("oWMXht_7-xA_7MRVvDaKu1wm0DrM");
            message.setMsgType(MsgType.text);
            message.setContent("msg content");

            for (long i = 0; i < Long.MAX_VALUE; i++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket);
                }
                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
                        "/weixin/process");
                System.out.println(">> Request URI: " + request.getRequestLine().getUri());

                StringEntity stringEntity = new StringEntity(XStreamUtil.toXML(message).toString(), ContentType.create("text/plain", Consts.UTF_8));
                request.setEntity(stringEntity);


                httpexecutor.preProcess(request, httpproc, coreContext);
                HttpResponse response = httpexecutor.execute(request, conn, coreContext);
                httpexecutor.postProcess(response, httpproc, coreContext);

                System.out.println("<< Response: " + response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("==============");
                if (!connStrategy.keepAlive(response, coreContext)) {
                    conn.close();
                } else {
                    System.out.println("Connection kept alive...");
                }
            }
        } finally {
            conn.close();
        }
    }


    @Test
    public void testLong() {
        System.out.println(System.currentTimeMillis());
    }
}