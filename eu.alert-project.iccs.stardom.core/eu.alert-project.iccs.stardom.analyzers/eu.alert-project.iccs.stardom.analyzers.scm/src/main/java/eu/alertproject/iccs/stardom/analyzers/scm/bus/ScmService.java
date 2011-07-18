package eu.alertproject.iccs.stardom.analyzers.scm.bus;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
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

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:52
 */
@EventHandler
public class ScmService {

    private Logger logger = LoggerFactory.getLogger(ScmService.class);

    @Autowired
    Identifier findIdentifier;

    @Autowired
    MetricDao metricDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    Analyzers analyzers;



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
        for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
            //if you are wondering how on earth this
            //is not breaking, it is because if context.getAction()
            // is not an instance of ScmAction, it will throw
            // a class cast exception.

            // I don't know how correct this is but for now
            // I am leaving this as is
            a.analyze(identity,context.getAction());
        }

    }

}
