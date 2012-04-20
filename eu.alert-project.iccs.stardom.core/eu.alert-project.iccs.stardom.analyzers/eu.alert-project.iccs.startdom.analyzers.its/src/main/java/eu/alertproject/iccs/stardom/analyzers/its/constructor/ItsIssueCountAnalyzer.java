package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.api.ResolutionAdapter;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsChangeAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ItsMlDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsIssuesResolvedMetric;
import eu.alertproject.iccs.stardom.domain.api.ml.ItsMl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: fotis
 * Date: 04/02/12
 * Time: 21:04
 */
public class ItsIssueCountAnalyzer implements Analyzer<DefaultItsChangeAction> {

    private Logger logger = LoggerFactory.getLogger(ItsIssueCountAnalyzer.class);

    ReentrantLock lock;

    @Autowired
    MetricDao metricDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    ResolutionAdapter resolutionAdapter;

    @Autowired
    ItsMlDao itsMlDao;

    @PostConstruct
    public void init(){
        lock = new ReentrantLock();
    }

    @PreDestroy
    public void destroy(){
        lock.unlock();
    }


    @Override
    @Transactional
    public void analyze(Identity who, DefaultItsChangeAction action) {

        logger.trace("void recordItsHistory() Locking");
        lock.lock();
        try{

            logger.trace("void analyze() {} ",action);
            String what = action.getWhat();
            if(StringUtils.equalsIgnoreCase(what,"status")){

                if(resolutionAdapter.isResolved(action.getAdded())){
                    //record change

                    ItsIssuesResolvedMetric metric = metricDao.getMostRecentMetric(who, ItsIssuesResolvedMetric.class);

                    if(metric == null){
                        metric = new ItsIssuesResolvedMetric();
                        metric.setCreatedAt(action.getDate());
                        metric.setQuantity(1);
                        metric.setIdentity(who);
                        metric = (ItsIssuesResolvedMetric) metricDao.insert(metric);
                    }else{
                        metric.increaseQuantity();
                        metric = (ItsIssuesResolvedMetric) metricDao.update(metric);
                    }
                    logger.trace("void recordItsHistory() {} ",metric);

                }else if(resolutionAdapter.isReopened(action.getAdded())){

                    /*
                        If a NullPointerException is thrown here, we are doing something very wrong!!!
                     */
                    ItsMl byBugId = itsMlDao.findByBugId(action.getBugId());
                    if(byBugId == null){
                        logger.warn("void analyze() ItsML = {} was never opened", action.getBugId());
                        return;
                    }
                    logger.trace("void analyze() ItsML = {} ",byBugId);




                    Identity byProfileUuid = identityDao.findByProfileUuid(byBugId.getUuidWho());
                    if(byProfileUuid==null){
                        logger.trace("void analyze() ItsML= {} is orphan skipping",byBugId.getUuidWho());
                        return;
                    }

                    logger.trace("void analyze() Profile = {} ",byProfileUuid);

                    ItsIssuesResolvedMetric metric = metricDao.getMostRecentMetric(byProfileUuid, ItsIssuesResolvedMetric.class);
                    logger.trace("void analyze() Looking for most rescent record for bugId={} and identity  {} = {}",
                                                                                        new Object[]{
                                                                                            action.getBugId(),
                                                                                            byBugId.getUuidWho(),
                                                                                            metric});

                    if(metric != null){
                        metric.decreaseQuantity();
                        metric.setCreatedAt(action.getDate());

                        metricDao.update(metric);
                        logger.trace("void recordItsHistory() Decreased History");

                    }else{
                        logger.warn("void recordItsHistory() Couldn't find a metric where one should be there");

                    }


                }
            }
        }finally{
            logger.trace("void analyze() Unlocking");
            lock.unlock();

        }
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof DefaultItsChangeAction;
    }
}
