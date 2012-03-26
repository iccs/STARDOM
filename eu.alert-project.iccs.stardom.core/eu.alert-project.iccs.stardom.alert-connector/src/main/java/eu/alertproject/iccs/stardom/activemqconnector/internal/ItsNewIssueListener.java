package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
@Service("itsNewIssueListener")
public class ItsNewIssueListener extends ALERTActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(ItsNewIssueListener.class);

    @Autowired
    Properties systemProperties;


    @Override
    public void process(Message message) throws IOException, JMSException {

        ItsConnectorContext context =null;
        ObjectMapper mapper = new ObjectMapper();

        String text = ((TextMessage) message).getText();

        logger.trace("void onMessage() Text to parse {} ",text);
        context= mapper.readValue(IOUtils.toInputStream(text),ItsConnectorContext.class);

        String filterDate = systemProperties.getProperty("analyzers.filterDate");

        Date when = null;
        try {
            when = DateUtils.parseDate(filterDate, new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            //nothing
        }


        if (when != null && context.getAction().getDate().before(when)) {
            logger.trace("void action() Ignoring action because date {} is before {}", context.getAction().getDate(), when);
            return;
        }

        fixProfile(context);
        ItsEvent itsEvent = new ItsEvent(this,context);
        logger.trace("void onMessage() {} ",itsEvent);

        Bus.publish(itsEvent);
    }
}
