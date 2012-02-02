package eu.alertproject.iccs.stardom.activemqconnector.monitor;

import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;

/**
 * User: fotis
 * Date: 27/01/12
 * Time: 01:02
 */
public class MqStatusListener implements Subscriber {

    private Logger logger = LoggerFactory.getLogger(MqStatusListener.class);

    @Override
    public void onMessage(Message message) {
        logger.trace("void onMessage() {} ",message);
    }
}
