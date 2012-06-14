package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.*;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.DefaultMailingListAction;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:12
 */
@Component("mailNewListener")
public class MailNewListener extends ALERTActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(MailNewListener.class);

    @Override
    public void processXml(String text) {

        MailingListNewEnvelope envelope = EventFactory.<MailingListNewEnvelope>fromXml(
                text,
                MailingListNewEnvelope.class);

        MailingListNewPayload.EventData eventData = envelope
                .getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData();

        MailingList mailingList = eventData.getMailingList();



        DefaultMailingListAction defaultMailingListAction = new DefaultMailingListAction();
        defaultMailingListAction.setDate(mailingList.getDate());
        defaultMailingListAction.setFrom(mailingList.getFrom());
        defaultMailingListAction.setMessageId(mailingList.getMessageId());
        defaultMailingListAction.setInReplyTo(mailingList.getInReplyTo());
        defaultMailingListAction.setSubject(mailingList.getSubject());
        defaultMailingListAction.setText(mailingList.getContent());

        MailingListConnectorContext context  = new MailingListConnectorContext();
        context.setAction(defaultMailingListAction);
        context.setProfile(new Profile());

        if(isIgnoredBasedOnDate(context.getAction().getDate())){return;}
        fixProfile(context);


        MailingEvent mailEvent = new MailingEvent(this,context);
        logger.trace("void onMessage() {} ",mailEvent);

        Bus.publish(mailEvent);

    }

}
