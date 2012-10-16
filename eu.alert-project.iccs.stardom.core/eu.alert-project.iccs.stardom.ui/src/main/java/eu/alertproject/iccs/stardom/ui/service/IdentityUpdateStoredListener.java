package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.api.AbstractActiveMQHandler;
import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/12/12
 * Time: 12:30 AM
 */
public class IdentityUpdateStoredListener extends AbstractActiveMQHandler {

    @Autowired
    MergeService mergeService;

    @Override
    public void process(ActiveMQMessageBroker broker, Message message) throws IOException, JMSException {
    }
}
