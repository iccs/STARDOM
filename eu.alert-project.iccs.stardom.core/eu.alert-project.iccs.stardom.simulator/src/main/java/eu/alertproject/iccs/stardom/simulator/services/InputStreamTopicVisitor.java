package eu.alertproject.iccs.stardom.simulator.services;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/12/12
 * Time: 3:53 PM
 */
public class InputStreamTopicVisitor implements InputStreamVisitor {

    private Logger logger = LoggerFactory.getLogger(InputStreamTopicVisitor.class);

    private String topic;
    private JmsTemplate template;
    private boolean enabled;


    public InputStreamTopicVisitor(String topic,boolean enabled, JmsTemplate template) {
        this.topic=topic;
        this.enabled = enabled;
        this.template =template;
    }

    @Override
    public void handle(InputStream inputStream) throws IOException {

        String s = IOUtils.toString(inputStream);
        logger.trace("void handle() {} ",s);

        if(enabled){
            this.template.send(
                    this.topic,
                    new TextMessageCreator(s)
            );
        }
    }
}
