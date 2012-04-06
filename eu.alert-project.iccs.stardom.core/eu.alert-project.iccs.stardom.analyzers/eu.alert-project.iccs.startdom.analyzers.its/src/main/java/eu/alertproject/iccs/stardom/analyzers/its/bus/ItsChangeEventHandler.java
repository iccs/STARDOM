package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsChangeAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsChangeConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.internal.ProfileFromItsKdeWhoService;
import eu.alertproject.iccs.stardom.bus.api.AnnotatedUpdateEvent;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.bus.api.Event;
import eu.alertproject.iccs.stardom.bus.api.STARDOMTopics;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.datastore.api.dao.ItsMlDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 24/08/11
 * Time: 21:27
 */
@EventHandler
public class ItsChangeEventHandler {


    @Autowired
    Identifier identifier;

    @Autowired
    ItsMlDao itsMlDao;

    @Autowired
    ItsMlService itsMlService;

    @Autowired
    Analyzers analyzers;

    @Autowired
    ProfileFromItsKdeWhoService profileFromItsKdeWhoService;

    private Logger logger = LoggerFactory.getLogger(ItsChangeEventHandler.class);

    @EventSubscriber(eventClass = ItsChangeEvent.class)
    public void event(ItsChangeEvent event){
        logger.trace("void event() {}",event);

        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ItsChangeConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ItsChangeConnectorContext context = (ItsChangeConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());

        /**
         * We need to track who is assigned to a specific bug, at a specific
         * time and each time the bug changes the action
         *
         * The logic is that if the bug exists in our database and the assignment
         * changes we create a new entry
         *
         * If the bug doesn't exist, which means this is a new bug, we set
         * the assigned person in the action
         *
         * Each time the status change, we retrieve the bug from our database
         * create a copy and save it
         *
         */

        Identity who = null;
        try {
            if(!StringUtils.isEmpty(context.getProfile().getEmail())){

                if(context.getProfile().getName() !=null && context.getProfile().getName().trim().toLowerCase().equals("none")){
                    context.getProfile().setName(null);
                    context.getProfile().setLastname(null);
                }

                who = identifier.find(context.getProfile(),"its-change");
            }

            itsMlService.recordItsHistory(who, context.getAction());

            //whatever your do, do it here
            for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                if(a.canHandle(context.getAction())){
                    try{
                        a.analyze(who,context.getAction());
                    }catch (Exception e){
                        logger.error("Something is up in the analyzer",e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Something is up here ",e);
        }finally {
            if(who !=null){
                Bus.publish(STARDOMTopics.IdentityUpdated,new AnnotatedUpdateEvent(this,who,null));
            }
        }
    }

}
