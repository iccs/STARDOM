package eu.alertproject.iccs.stardom.ui.service;

import javax.jms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 7/30/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessagingService {

    void send(String topic, String message);
}
