package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 7/30/12
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class JmsMessagingService implements MessagingService
{

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void send(String topic, String message) {
        jmsTemplate.send(
                topic,
                new TextMessageCreator(message));

    }
}
