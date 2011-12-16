package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEventHandler;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.constructor.ScmActivityAnalyzer;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
@Component("scmNewCommitListener")
public class ScmNewCommitListener implements Subscriber{

    private Logger logger = LoggerFactory.getLogger(ScmNewCommitListener.class);

    private AtomicInteger messageCount = new AtomicInteger(0);

//    @Autowired
//    private ScmEventHandler scmEventHandler;

    @Override
    public void onMessage(Message message) {

        logger.trace("void onMessage() {} ",message);

        if(!(message instanceof TextMessage)){

            logger.warn("I can't handle this message {} ",message);
            return;
        }


        int count = messageCount.incrementAndGet();

        try {


            ScmConnectorContext context =null;


            ObjectMapper mapper = new ObjectMapper();


            String text = ((TextMessage) message).getText();

            logger.trace("void onMessage() Text to parse {} ",text);
            context= mapper.readValue(
                    IOUtils.toInputStream(text)
                    ,ScmConnectorContext.class);

            ScmEvent scmEvent = new ScmEvent(this,context);
            logger.trace("void onMessage() {} ",scmEvent);

            Bus.publish(scmEvent);
//            scmEventHandler.event(scmEvent);


            logger.debug("Sending message {} ",count);

        } catch (IOException e) {
            logger.warn("Couldn't handle and translate the message content {}",e);
        } catch (JMSException e) {
            logger.warn("Couldn't retrieve the message content {}", e);
        } finally {

        }
    }

    public Integer getMessageCount() {
        return messageCount.get();
    }
}
