package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 15:09
 */
public class IssueNewAnnotated extends ALERTActiveMQListener{

    @Override
    public void process(Message message) throws IOException, JMSException {

        //process the issue here

    }


}
