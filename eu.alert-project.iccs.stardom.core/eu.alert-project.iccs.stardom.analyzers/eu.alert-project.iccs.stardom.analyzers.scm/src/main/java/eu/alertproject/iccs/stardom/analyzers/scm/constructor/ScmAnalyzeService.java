package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.bus.api.ScmEvent;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        //get the current values of the metrics
        List<Metric> forIdentity = metricDao.getForIdentity(identity);

        for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
            a.analyze(identity,context.getAction());

        }

    }

}
