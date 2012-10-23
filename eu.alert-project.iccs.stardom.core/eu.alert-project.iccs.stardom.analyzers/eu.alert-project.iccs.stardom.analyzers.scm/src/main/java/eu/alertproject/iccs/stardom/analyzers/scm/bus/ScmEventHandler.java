package eu.alertproject.iccs.stardom.analyzers.scm.bus;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.bus.api.*;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:52
 */
@EventHandler
public class ScmEventHandler {

    private int events= 0;

    private Logger logger = LoggerFactory.getLogger(ScmEventHandler.class);

    @Autowired
    Identifier identifier;

    @Autowired
    MetricDao metricDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    Analyzers analyzers;



    @EventSubscriber(eventClass = ScmEvent.class)
    public synchronized void event(ScmEvent event){

        //add the event to the queue and process one by one
        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ScmConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ScmConnectorContext context = (ScmConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());

        logger.trace("Processed {} events ",events++);
//        queue.add(context);
//        logger.trace("Processed {} queue size ",queue.size());

        //do your magic
        Identity identity = identifier.find(context.getProfile(),"scm");
        logger.trace("void event() Identity {}",identity.getUuid());


        logger.debug("Memory {}/{} ",Runtime.getRuntime().freeMemory(),Runtime.getRuntime().totalMemory());
        //whatever your do, do it here
        int changes =0;
        try {

            for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                //if you are wondering how on earth this
                //is not breaking, it is because if context.getAction()
                // is not an instance of ScmAction, it will throw
                // a class cast exception.
                if(a.canHandle(context.getAction())){
                    a.analyze(identity,context.getAction());
                    changes++;
                }
            }

        } catch (Exception e) {
            logger.error("Error analying action ", e);
        }finally {
            if(changes > 0){
                Bus.publish(STARDOMTopics.IdentityUpdated, new AnnotatedUpdateEvent(this,identity,context.getAction().getConcepts()));

                for(ScmFile f : context.getAction().getFiles()){
                    for(String module : f.getModules()){
                        Bus.publish(STARDOMTopics.ComponentUpdated, new AnnotatedUpdateEvent(this,
                                new Component(
                                        module,
                                        f.getName()),
                                context.getAction().getConcepts()));
                        //components here
                    }

                }
            }
        }

    }

}
