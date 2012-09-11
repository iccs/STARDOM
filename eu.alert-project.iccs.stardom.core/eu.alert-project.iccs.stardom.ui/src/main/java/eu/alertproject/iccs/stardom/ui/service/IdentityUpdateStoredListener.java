package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.api.AbstractActiveMQListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/12/12
 * Time: 12:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class IdentityUpdateStoredListener extends AbstractActiveMQListener {

    @Autowired
    MergeService mergeService;

    @Override
    public void process(Message message) throws IOException, JMSException {


    }
}
