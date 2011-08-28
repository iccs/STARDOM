package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsTemporalAnalyzer extends AbstractItsAnalyzer{

    private Logger logger = LoggerFactory.getLogger(ItsTemporalAnalyzer.class);

    @Override
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null){
            return;
        }


        ItsTemporalMetric newMetrics  = new ItsTemporalMetric();
        newMetrics.setIdentity(identity);
        newMetrics.setCreatedAt(new Date());
        newMetrics.setTemporal(action.getDate());


        ItsTemporalMetric metric = (ItsTemporalMetric) getMetricDao().insert(newMetrics);

        logger.trace("void analyze() {} ", metric);

    }
}
