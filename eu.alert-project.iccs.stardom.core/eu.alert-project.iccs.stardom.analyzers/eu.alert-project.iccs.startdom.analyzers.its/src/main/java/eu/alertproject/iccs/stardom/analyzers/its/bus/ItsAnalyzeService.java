package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.stardom.bus.api.ItsEvent;
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
public class ItsAnalyzeService {
    private Logger logger = LoggerFactory.getLogger(ItsAnalyzeService.class);

    @EventSubscriber(eventClass = ItsEvent.class)
    public void event(ItsEvent event){
        logger.trace("void event() {}",event);
    }

}
