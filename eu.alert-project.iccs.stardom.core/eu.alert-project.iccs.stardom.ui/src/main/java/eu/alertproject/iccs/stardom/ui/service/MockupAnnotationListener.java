package eu.alertproject.iccs.stardom.ui.service;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.jsi.TextToAnnotateRequestEnvelope;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * User: fotis
 * Date: 15/03/12
 * Time: 00:53
 */
public class MockupAnnotationListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(MockupAnnotationListener.class);
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Override
    public void onMessage(Message message) {

        try {

            Thread.sleep(5000);

            String s = IOUtils.toString(MockupAnnotationListener.class.getResourceAsStream("/ALERT.KEUI.TextToAnnotate.Annotated.xml"));

            logger.trace("void onMessage() replying with {} ",s);
            jmsTemplate.send(
                    Topics.ALERT_KEUI_TextToAnnotate_Annotated,
                    new TextMessageCreator(s)
            );


        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
        }


    }
}
