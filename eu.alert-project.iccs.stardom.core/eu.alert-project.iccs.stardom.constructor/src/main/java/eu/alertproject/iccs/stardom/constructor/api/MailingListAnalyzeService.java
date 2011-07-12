package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.bus.api.MailingEvent;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:53
 */
@EventHandler
public class MailingListAnalyzeService {

    private Logger logger = LoggerFactory.getLogger(MailingListAnalyzeService.class);

    @EventSubscriber(eventClass = MailingEvent.class)
    public void event(MailingEvent event){
        logger.trace("void event() {}",event);



    }
}
