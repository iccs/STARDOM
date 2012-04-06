package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.stardom.analyzers.its.api.ResolutionAdapter;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsChangeAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ItsMlDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsIssuesResolvedMetric;
import eu.alertproject.iccs.stardom.domain.api.ml.ItsMl;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: fotis
 * Date: 27/08/11
 * Time: 18:45
 */
@Service
public class ItsMlService {

    private Logger logger = LoggerFactory.getLogger(ItsMlService.class);


    @Autowired
    ResolutionAdapter resolutionAdapter;

    @Autowired
    IdentityDao identityDao;
    
    @Autowired
    MetricDao metricDao;

    @Autowired
    ItsMlDao itsMlDao;

    @Autowired
    Identifier identifier;


    @Transactional
    public void recordItsHistory(Identity who, DefaultItsChangeAction action) {

        String what = action.getWhat();
        logger.trace("void recordItsHistory() {}",action);
        if(StringUtils.equalsIgnoreCase(what, "Status")){


//            ItsMl itsMl = itsMlDao.findLastestForBugId(action.getBugId());

            ItsMl newBugAction = new ItsMl();

//            if(itsMl != null){
//                newBugAction.setUuid(itsMl.getUuid());
//            }

            newBugAction.setUuidWho(who == null ? "none"  :who.getUuid());
            newBugAction.setBugId(action.getBugId());
            newBugAction.setStatus(action.getAdded());
            newBugAction.setWhen(action.getDate());
//            ItsMl insert = itsMlDao.insert(newBugAction);
//            logger.trace("void event() Status Change {} ",insert);


        }else if(StringUtils.endsWithIgnoreCase(what,"AssignedTo")){

            //get the identity
            Profile p = new Profile();
            p.setEmail(action.getAdded());
            Identity identity = identifier.find(p,"ml-assigned-to");


            //find the bug
            ItsMl byBugId = itsMlDao.findLastestForBugId(action.getBugId());
            ItsMl newBugAction = new ItsMl();

            if(byBugId !=null ){
                newBugAction.setStatus(byBugId.getStatus());
            }


            newBugAction.setUuidWho(who == null ? "none"  :who.getUuid());
            newBugAction.setUuid(identity.getUuid());
            newBugAction.setBugId(action.getBugId());
            newBugAction.setWhen(action.getDate());

            ItsMl insert = itsMlDao.insert(newBugAction);
            logger.trace("void event() Assigned to Changed {}",insert);

        }else if(StringUtils.endsWithIgnoreCase(what,"CC")){

            logger.trace("void recordItsHistory() cc added");


            //this person was added as cc
            Profile p = new Profile();
            p.setEmail(action.getAdded());
            Identity identity = identifier.find(p,"ml-cc");

            ItsMl newBugAction = new ItsMl();

            newBugAction.setUuidWho(who == null ? "none"  :who.getUuid());
            newBugAction.setStatus("CC");
            newBugAction.setUuid(identity.getUuid());
            newBugAction.setBugId(action.getBugId());
            newBugAction.setWhen(action.getDate());


            ItsMl insert = itsMlDao.insert(newBugAction);
            logger.trace("void event() CC Changed {}",insert);

        }

    }
}
