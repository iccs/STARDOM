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
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_METADATA_MailNew_Updated
 *
 * User: fotis
 * Date: 05/11/11
 * Time: 19:12
 */
@Component("mailNewAnnotatedListener")
public class MailNewAnnotatedListener extends ALERTActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(MailNewAnnotatedListener.class);
    @Override
    public void processXml(String text) {


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


        String fromUri = eventData.getMdService().getFromUri();
        Keui keui = eventData.getKeui();
        List<Keui.Concept> contentConcepts = keui.getContentConcepts();
        contentConcepts.addAll(keui.getSubjectConcepts());


        DefaultMailingListAction defaultMailingListAction = new DefaultMailingListAction();
        defaultMailingListAction.setDate(mailingList.getDate());
        defaultMailingListAction.setFrom(mailingList.getFrom());
        defaultMailingListAction.setFromUri(fromUri);
        defaultMailingListAction.setMessageId(mailingList.getMessageId());
        defaultMailingListAction.setInReplyTo(mailingList.getInReplyTo());
        defaultMailingListAction.setSubject(mailingList.getSubject());
        defaultMailingListAction.setText(mailingList.getContent());
        defaultMailingListAction.setConcepts(contentConcepts);

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
