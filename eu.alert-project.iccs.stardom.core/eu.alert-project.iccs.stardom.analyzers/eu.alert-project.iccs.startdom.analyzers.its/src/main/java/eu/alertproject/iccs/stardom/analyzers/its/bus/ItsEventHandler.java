package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.events.internal.IssueUpdated;
import eu.alertproject.iccs.stardom.analyzers.its.connector.*;
import eu.alertproject.iccs.stardom.analyzers.its.internal.ProfileFromItsKdeWhoService;
import eu.alertproject.iccs.stardom.bus.api.*;
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
public class ItsEventHandler {

    private Logger logger = LoggerFactory.getLogger(ItsEventHandler.class);

    @Autowired
    Identifier identifier;

    @Autowired
    Analyzers analyzers;

    @Autowired
    ProfileFromItsKdeWhoService profileFromItsKdeWhoService;


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


        DefaultItsAction action = context.getAction();

        /**
         * If this becomes more complicated this needs to change
         *
         * Our analyze interface sucks at the moment, it needs to
         * become more versatile.
         *
         * Giving the option of the implementor to pass any argument
         * and carry any action.
         *
         * A several affected parties such as creator, assigned,
         * cc and so on.
         *
         * Comments are handled in another event.
         *
         * Therefore we need to create an action for each one.
         *
         * We are using the CleanItsAction which should be removed later
         *
         *
         */

        handleDirtyProfile(action.getAssigned(),action);
        handleDirtyProfile(action.getReporter(), action);

        IssueUpdated iu = new IssueUpdated();
        iu.setDate(action.getDate());
        iu.setSubject(action.getSubject());
        iu.setConcepts(action.getConcepts());
        iu.setId(String.valueOf(action.getBugId()));

        Bus.publish(STARDOMTopics.IssueUpdated,new AnnotatedUpdateEvent(this,iu,action.getConcepts()));

        Bus.publish(STARDOMTopics.ComponentUpdated, new AnnotatedUpdateEvent(this,
                new Component(
                        action.getComponent(),
                        action.getBugId()),
                action.getConcepts()));

    }

    @EventSubscriber(eventClass = ItsCommentEvent.class)
    public void commentEvent(ItsCommentEvent event){
        logger.trace("void event() {}",event);

        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ItsCommentConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ItsCommentConnectorContext context = (ItsCommentConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());


        //we need to create or

        /**
         * The profiles comming in the comments is all messed up and we need to
         * handle this here before sending it over to the analyzer
         *
         *
         * The name and email of the comments follow this convention
         * <who name="Random name">cgiboudeaux gmx com</who>
         *
         */

        DefaultItsCommentAction action = context.getAction();
        Identity identity = identifier.find(context.getProfile(),"its-comment");
        logger.trace("void event() Identity {}",identity.getUuid());


        try {
            //whatever your do, do it here
            for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                if(a.canHandle(context.getAction())){
                    a.analyze(identity,context.getAction());
                }
            }
        } catch (Exception e) {
            logger.error("Error in handler ",e);
        } finally {

            Bus.publish(STARDOMTopics.IdentityUpdated,new AnnotatedUpdateEvent(this,identity,action.getConcepts()));

            IssueUpdated iu = new IssueUpdated();
            iu.setDate(action.getDate());
            iu.setSubject(action.getSubject());
            iu.setConcepts(action.getConcepts());
            iu.setId(String.valueOf(action.getBugId()));

            Bus.publish(STARDOMTopics.IssueUpdated,new AnnotatedUpdateEvent(this,iu,action.getConcepts()));
            Bus.publish(STARDOMTopics.ComponentUpdated, new AnnotatedUpdateEvent(this,
                    new Component(
                            action.getComponent(),
                            action.getBugId()),
                            action.getConcepts()));

        }


    }

    private void handleDirtyProfile(Profile dirty, DefaultItsAction action ){

        Identity identity = identifier.find(dirty,"its");

        //whatever your do, do it here
        try {
            for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                if(a.canHandle(action)){
                    a.analyze(identity,action);
                }
            }
        } catch (Exception e) {
            logger.error("Error in handler ",e);
        } finally {

            Bus.publish(STARDOMTopics.IdentityUpdated,new AnnotatedUpdateEvent(this,identity,action.getConcepts()));
        }
    }

}
