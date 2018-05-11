package com.fenlibao.platform.listener;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lullaby on 2016/1/29.
 */
public class RequestListener implements RequestEventListener {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

    private final int requestNumber;

    private final long startTime;

    public RequestListener(int requestNumber) {
        this.requestNumber = requestNumber;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                logger.debug("Resource method "
                        + event.getUriInfo().getMatchedResourceMethod()
                        .getHttpMethod()
                        + " started for request " + requestNumber);
                break;
            case FINISHED:
                logger.debug("Request " + requestNumber
                        + " finished. Processing time "
                        + (System.currentTimeMillis() - startTime) + " ms.");
                break;
        }
    }

}
