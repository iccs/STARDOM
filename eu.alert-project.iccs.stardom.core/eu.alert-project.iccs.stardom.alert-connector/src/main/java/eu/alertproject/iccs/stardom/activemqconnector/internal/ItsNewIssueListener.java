package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.AbstractActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
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
@Service("itsNewIssueListener")
public class ItsNewIssueListener extends AbstractActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(ItsNewIssueListener.class);

    @Override
    public void process(Message message) throws IOException, JMSException {

        ItsConnectorContext context =null;
        ObjectMapper mapper = new ObjectMapper();

        String text = ((TextMessage) message).getText();

        logger.trace("void onMessage() Text to parse {} ",text);
        context= mapper.readValue(
                IOUtils.toInputStream(text)
                ,ItsConnectorContext.class);

        ItsEvent itsEvent = new ItsEvent(this,context);
        logger.trace("void onMessage() {} ",itsEvent);

        Bus.publish(itsEvent);
    }
}
