package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.bus.api.ScmEvent;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.DefaultScmAction;
import eu.alertproject.iccs.stardom.connector.api.ScmConnectorContext;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:52
 */
@EventHandler
public class ScmAnalyzeService {

    private Logger logger = LoggerFactory.getLogger(ScmAnalyzeService.class);

    @Autowired
    Identifier findIdentifier;

    @EventSubscriber(eventClass = ScmEvent.class)
    public void event(ScmEvent event){


        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ScmConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ScmConnectorContext context = (ScmConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());


        //do your magic
        Identity identity = findIdentifier.find(context.getProfile());

        logger.trace("void event() Identity {}",identity.getUuid());

        //whatever your do, do it here


    }

}
