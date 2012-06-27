package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractTemporalAnalyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsTemporalAnalyzer implements Analyzer<ItsAction>{

    private Logger logger = LoggerFactory.getLogger(ItsTemporalAnalyzer.class);

    @Autowired
    private MetricDao metricDao;

    public ItsTemporalAnalyzer() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null){
            logger.warn("Can't work with a null identity");
            return;
//                throw new NullPointerException("Can't work with a null identity");
        }

        ItsTemporalMetric newMetrics = new ItsTemporalMetric();
        newMetrics.setIdentity(identity);
        newMetrics.setComponent(action.getComponent());
        newMetrics.setCreatedAt(new Date());
        newMetrics.setTemporal(action.getDate());

        Metric insert = metricDao.insert(newMetrics);

        logger.trace("void analyze() Created Metric {} ",insert);


    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ItsAction;
    }
}
