package eu.alertproject.iccs.stardom.ui.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 7/30/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockMessagingService implements MessagingService
{
    private Logger logger = LoggerFactory.getLogger(MockMessagingService.class);

    @Override
    public void send(String topic, String message) {

        logger.trace("Sending {} to {} ",topic,message);


    }
}
