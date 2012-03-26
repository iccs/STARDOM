package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.alert.MailingList;
import eu.alertproject.iccs.events.alert.MailingListAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.MailingListAnnotatedPayload;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.DefaultMailingListAction;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.domain.api.Profile;
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

        String text = ((TextMessage) message).getText();
        MailingListAnnotatedEnvelope envelope = EventFactory.<MailingListAnnotatedEnvelope>fromXml(
                text,
                MailingListAnnotatedEnvelope.class);

        MailingListAnnotatedPayload.EventData eventData = envelope
                .getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData();

        MailingList mailingList = eventData.getMailingList();
        Keui keui = eventData.getKeui();



        DefaultMailingListAction defaultMailingListAction = new DefaultMailingListAction();
        defaultMailingListAction.setDate(mailingList.getDate());
        defaultMailingListAction.setFrom(mailingList.getFrom());
        defaultMailingListAction.setSubject(mailingList.getSubject());
        defaultMailingListAction.setText(mailingList.getContent());


        MailingListConnectorContext context  = new MailingListConnectorContext();
        context.setAction(defaultMailingListAction);
        context.setProfile(new Profile());

        //TODO store annotations
        //TODO store uris


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
