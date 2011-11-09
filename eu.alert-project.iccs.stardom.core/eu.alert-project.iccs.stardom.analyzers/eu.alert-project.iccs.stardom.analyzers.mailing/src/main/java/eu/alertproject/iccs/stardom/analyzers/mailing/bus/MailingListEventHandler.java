package eu.alertproject.iccs.stardom.analyzers.mailing.bus;

import eu.alertproject.iccs.stardom.analyzers.mailing.api.ProfileFromMailFromService;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.mailing.constructor.AbstractMailingListAnalyzer;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
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
public class MailingListEventHandler {

    private Logger logger = LoggerFactory.getLogger(MailingListEventHandler.class);

    @Autowired
    Identifier identifier;

    @Autowired
    Analyzers analyzers;


    @Autowired
    ProfileFromMailFromService profileFromMailFromService;

    @EventSubscriber(eventClass = MailingEvent.class)
    public void event(MailingEvent event){

        logger.trace("void event() {}",event);

        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof MailingListConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        MailingListConnectorContext context = (MailingListConnectorContext) payload;
        logger.trace("void event() {}",context.getAction());


        //we need to do some processing

        //try to generate a profile from the e-mail
        Profile profile = profileFromMailFromService.generate(context.getAction().getFrom());

        if(profile == null){
            logger.warn("Couldn' generate profile from {} - ignoring context",context.getAction().getFrom());
            return;
        }

        context.setProfile(profile);

//        //do your magic
        Identity identity = identifier.find(profile);
        logger.trace("void event() Identity {}",identity.getUuid());
//


        //whatever your do, do it here
        try{
            for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                if(a.canHandle(context.getAction())){
                  a.analyze(identity,context.getAction());
                }
            }
        }catch (ClassCastException e){
            logger.warn("Something happened in the analyzer {} ",e);
        }

    }
}
