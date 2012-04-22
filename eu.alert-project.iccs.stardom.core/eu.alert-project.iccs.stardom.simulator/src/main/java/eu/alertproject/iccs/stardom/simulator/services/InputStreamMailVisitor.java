package eu.alertproject.iccs.stardom.simulator.services;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.alert.IssueUpdateAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.MailingListAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.MailingListNewEnvelope;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 18:04
 */
public class InputStreamMailVisitor implements InputStreamVisitor {

    private Logger logger = LoggerFactory.getLogger(InputStreamMailVisitor.class);

    private JmsTemplate template;

    int counter = 0;
    int errors=0;

    public InputStreamMailVisitor(JmsTemplate template) {
        this.template = template;
    }

    @Override
    public void handle(InputStream inputStream) throws IOException {

        String s = IOUtils.toString(inputStream);
        logger.trace("void handle() {} ",s);

        try{

        MailingListAnnotatedEnvelope envelope = EventFactory.<MailingListAnnotatedEnvelope>fromXml(
                s,
                MailingListAnnotatedEnvelope.class);


        template.send(
                Topics.ALERT_METADATA_MailNew_Updated,
                new TextMessageCreator(s));

            logger.trace("void handle() sent {} ",counter++);
        }catch (Exception e){
            logger.warn("Ignoring event {}",errors++);
        }



    }
}
