package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import java.io.IOException;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:12
 */
@Component("mailNewMailListener")
public class MailNewMailListener implements Subscriber {

    private Logger logger = LoggerFactory.getLogger(MailNewMailListener.class);

    @PostConstruct
    public void loaded(){
        logger.info("Loaded {}",this.getClass());
    }

    @Override
    public void onMessage(Message message) {

        logger.trace("void onMessage() {} ",message);

        if(!(message instanceof ActiveMQMessage)){

            logger.warn("I can't handle this message {} ",message);
            return;
        }


        try {
            MailingListConnectorContext context = null;


            ObjectMapper mapper = new ObjectMapper();
            context= mapper.readValue(
                            IOUtils.toInputStream((String) ((ActiveMQMessage) message).getProperty("message"))
                            ,MailingListConnectorContext.class);

            MailingEvent mailEvent = new MailingEvent(this,context);
            Bus.publish(mailEvent);

        } catch (IOException e) {
            logger.warn("Couldn't handle the message content ");
        } finally {
        }


    }
}
