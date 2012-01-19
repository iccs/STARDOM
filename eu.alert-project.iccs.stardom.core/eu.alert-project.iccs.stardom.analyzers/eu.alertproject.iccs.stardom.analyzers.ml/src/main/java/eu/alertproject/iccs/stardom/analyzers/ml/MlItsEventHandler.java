package eu.alertproject.iccs.stardom.analyzers.ml;

import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsHistoryEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsHistoryAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsHistoryConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.internal.ProfileFromItsKdeWhoService;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
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
public class MlItsEventHandler {


    private int reopened=0;
    private int resolved=0;
    private int others=0;

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

    private Logger logger = LoggerFactory.getLogger(MlItsEventHandler.class);

    @EventSubscriber(eventClass = ItsHistoryEvent.class)
    public void event(ItsHistoryEvent event){
        logger.trace("void event() {}",event);

        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ItsHistoryConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ItsHistoryConnectorContext context = (ItsHistoryConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());


        DefaultItsHistoryAction action = context.getAction();

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
        if(!StringUtils.isEmpty(context.getProfile().getEmail())){
            who = identifier.find(context.getProfile(),"ml");
        }

        try {
            itsMlService.recordItsHistory(who,context.getAction());
        } catch (Exception e) {

            logger.error("Something is up here ",e);
        }


    }

}
