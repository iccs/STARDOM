package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:53
 */
@EventHandler
public class ItsAnalyzeService {

    private Logger logger = LoggerFactory.getLogger(ItsAnalyzeService.class);

    @Autowired
    Identifier findIdentifier;

    @Autowired
    Analyzers analyzers;



    @EventSubscriber(eventClass = ItsEvent.class)
    public void event(ItsEvent event){
        logger.trace("void event() {}",event);

        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ItsConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ItsConnectorContext context = (ItsConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());


        //do your magic
        Identity identity = findIdentifier.find(context.getProfile());
        logger.trace("void event() Identity {}",identity.getUuid());

        //whatever your do, do it here
        for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
            a.analyze(identity,context.getAction());
        }

    }

}
