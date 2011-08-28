package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:44
 */
public class ScmTemporalAnalyzer extends AbstractScmAnalyzer{

    private Logger logger = LoggerFactory.getLogger(ScmTemporalAnalyzer.class);


    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity == null){
            return;
        }


        ScmTemporalMetric newMetrics  = new ScmTemporalMetric();
        newMetrics.setIdentity(identity);
        newMetrics.setCreatedAt(new Date());
        newMetrics.setTemporal(action.getDate());


        ScmTemporalMetric metric = (ScmTemporalMetric) getMetricDao().insert(newMetrics);

        logger.trace("void analyze() {} ",metric);

    }
}
