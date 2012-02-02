package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.AbstractActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEventHandler;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.constructor.ScmActivityAnalyzer;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.commons.io.IOUtils;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
@Component("scmNewCommitListener")
public class ScmNewCommitListener extends AbstractActiveMQListener{

    @Autowired
    Properties systemProperties;

    private Logger logger = LoggerFactory.getLogger(ScmNewCommitListener.class);

    @Override
    public void process(Message message) throws IOException, JMSException {



        ScmConnectorContext context =null;
        ObjectMapper mapper = new ObjectMapper();

        String text = ((TextMessage) message).getText();

        logger.trace("void onMessage() Text to parse {} ",text);
        context= mapper.readValue(
                IOUtils.toInputStream(text)
                ,ScmConnectorContext.class);


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

        //fix profile



        fixProfile(context);
        ScmEvent scmEvent = new ScmEvent(this,context);
        logger.trace("void onMessage() {} ",scmEvent);



        Bus.publish(scmEvent);

    }

}
