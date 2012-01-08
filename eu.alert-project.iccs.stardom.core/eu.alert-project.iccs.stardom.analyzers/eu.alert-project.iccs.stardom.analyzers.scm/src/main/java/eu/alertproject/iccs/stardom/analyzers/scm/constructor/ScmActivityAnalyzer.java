package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class ScmActivityAnalyzer extends AbstractScmAnalyzer {

    private Logger logger = LoggerFactory.getLogger(ScmActivityAnalyzer.class);
    
    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity == null ){
            return;
        }

        List<ScmActivityMetric> forIdentity = getMetricDao().<ScmActivityMetric>getForIdentity(identity, ScmActivityMetric.class);

        ScmActivityMetric metric = null;
        if(forIdentity ==null || forIdentity.size() <=0){

            metric = new ScmActivityMetric();
            metric.setQuantity(0);
            metric.setIdentity(identity);
            metric = (ScmActivityMetric) getMetricDao().insert(metric);

        }else{
            metric = forIdentity.get(0);
        }

        metric.setCreatedAt(action.getDate());
        metric.increaseQuantity();

        metric = (ScmActivityMetric)getMetricDao().update(metric);

        logger.trace("void analyze() {} ",metric);

    }
}
