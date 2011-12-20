package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.AbstractActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsCommentEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsHistoryEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsCommentConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsHistoryConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
@Service("itsNewCommentListener")
public class ItsNewCommentListener extends AbstractActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(ItsNewCommentListener.class);

    @Override
    public void process(Message message) throws IOException, JMSException {

        ItsCommentConnectorContext  context =null;
        ObjectMapper mapper = new ObjectMapper();
        String text = ((TextMessage) message).getText();

        logger.trace("void onMessage() Text to parse {} ",text);
        context= mapper.readValue(
                IOUtils.toInputStream(text)
                ,ItsCommentConnectorContext .class);

        ItsCommentEvent event = new ItsCommentEvent(this,context);
        logger.trace("void onMessage() {} ",event);

        Bus.publish(event);
    }
}
