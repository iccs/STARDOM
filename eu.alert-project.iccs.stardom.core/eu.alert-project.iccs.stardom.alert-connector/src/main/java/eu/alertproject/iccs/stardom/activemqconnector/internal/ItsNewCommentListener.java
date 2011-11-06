package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.Message;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
public class ItsNewCommentListener implements Subscriber {

    @Override
    public void onMessage(Message message) {
        if(message instanceof ActiveMQMessage){
            ActiveMQMessage mqmessage = (ActiveMQMessage) message;

        }


    }
}
