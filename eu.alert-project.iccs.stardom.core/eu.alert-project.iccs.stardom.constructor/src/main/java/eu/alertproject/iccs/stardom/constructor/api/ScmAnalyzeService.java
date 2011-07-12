package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.bus.api.ScmEvent;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:52
 */
@EventHandler
public class ScmAnalyzeService {

    private Logger logger = LoggerFactory.getLogger(ScmAnalyzeService.class);

    @EventSubscriber(eventClass = ScmEvent.class)
    public void event(ScmEvent event){
        logger.trace("void event() {}",event);
    }

}
