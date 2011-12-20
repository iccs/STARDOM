package eu.alertproject.iccs.stardom.activemqconnector.api;

import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 16/12/11
 * Time: 22:14
 */
public abstract class AbstractActiveMQListener implements Subscriber{

    private Logger logger = LoggerFactory.getLogger(AbstractActiveMQListener.class);


    private AtomicInteger messageSent = new AtomicInteger(0);
    private AtomicInteger messageTotal = new AtomicInteger(0);


    @Override
    public void onMessage(Message message) {

        logger.trace("void onMessage() {} ",message);

        if(!(message instanceof TextMessage)){

            logger.warn("I can't handle this message {} ",message);
            return;
        }

        try {

            int count = messageSent.incrementAndGet();
            process(message);
            logger.debug("Sending message {} ",count);

        } catch (IOException e) {
            logger.warn("Couldn't handle and translate the message content {}",e);
        } catch (JMSException e) {
            logger.warn("Couldn't retrieve the message content {}", e);
        } finally {
            messageTotal.incrementAndGet();
        }
    }

    public abstract void process(Message message) throws IOException, JMSException;

    public Integer getMessageCount() {
        return messageTotal.get();
    }

    public Integer getMessageSentCount(){
        return messageSent.get();
    }
}
