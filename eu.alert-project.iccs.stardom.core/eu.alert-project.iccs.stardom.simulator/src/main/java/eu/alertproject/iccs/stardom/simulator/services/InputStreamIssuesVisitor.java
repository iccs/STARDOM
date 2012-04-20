package eu.alertproject.iccs.stardom.simulator.services;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.alert.IssueNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.IssueUpdateAnnotatedEnvelope;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 18:04
 */
public class InputStreamIssuesVisitor implements InputStreamVisitor {

    private Logger logger = LoggerFactory.getLogger(InputStreamIssuesVisitor.class);

    private JmsTemplate template;

    int counter = 0;
    
    public InputStreamIssuesVisitor(JmsTemplate template) {
        this.template = template;
    }

    @Override
    public void handle(InputStream inputStream) throws IOException {

        String s = IOUtils.toString(inputStream);
        logger.trace("void handle() {} ",s);


        s = s.replaceAll("<s:issueComputerSystemUri>","<o:issueComputerSystemUri>");
        s = s.replaceAll("</s:issueComputerSystemUri>","</o:issueComputerSystemUri>");

        IssueUpdateAnnotatedEnvelope envelope = EventFactory.<IssueUpdateAnnotatedEnvelope>fromXml(
                s,
                IssueUpdateAnnotatedEnvelope.class
        );


        template.send(
                Topics.ALERT_KEUI_IssueUpdate_Annotated,
                new TextMessageCreator(s));


        logger.trace("void handle() sent {} ",counter++);
    }
}
