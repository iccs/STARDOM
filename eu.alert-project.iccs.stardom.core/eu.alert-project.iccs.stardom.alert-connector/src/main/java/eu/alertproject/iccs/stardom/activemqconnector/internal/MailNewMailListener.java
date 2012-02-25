package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * Time: 19:12
 */
@Component("mailNewMailListener")
public class MailNewMailListener extends ALERTActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(MailNewMailListener.class);

    @Autowired
    Properties systemProperties;

    @Override
    public void process(Message message) throws IOException, JMSException {

        MailingListConnectorContext context = null;

        ObjectMapper mapper = new ObjectMapper();

        String text = ((TextMessage) message).getText();
        if(StringUtils.isEmpty(text)){
            logger.warn("A message received doesn't contain no information so I am ignoring");
            return;
        }

        logger.trace("void onMessage() Text to parse {} ",text);
        context= mapper.readValue(IOUtils.toInputStream(text),MailingListConnectorContext.class);


        String filterDate = systemProperties.getProperty("analyzers.filterDate");
	logger.trace("System property filter date = ({})",filterDate);
        Date when = null;
        try {
            when = DateUtils.parseDate(filterDate, new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            //nothing
            logger.error("void process() Couldn't parse filterDate = ({}) ",filterDate);
        }

	logger.trace("Testing against date = ({})",when);

        if (when == null || ( context.getAction().getDate() != null && context.getAction().getDate().before(when) ) ) {
            logger.trace("void action() Ignoring action because date {} is before {}", context.getAction().getDate(), when);
            return;
        }

        fixProfile(context);
        MailingEvent mailEvent = new MailingEvent(this,context);
        logger.trace("void onMessage() {} ",mailEvent);

        Bus.publish(mailEvent);

    }

}
