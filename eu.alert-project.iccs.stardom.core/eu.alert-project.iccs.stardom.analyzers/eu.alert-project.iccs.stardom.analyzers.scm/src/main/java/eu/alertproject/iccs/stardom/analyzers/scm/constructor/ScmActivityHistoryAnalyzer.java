package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractQuantitativeHistoryAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class ScmActivityHistoryAnalyzer extends AbstractQuantitativeHistoryAnalyzer<ScmAction,ScmActivityMetric> {

    private Logger logger = LoggerFactory.getLogger(ScmActivityHistoryAnalyzer.class);

    protected ScmActivityHistoryAnalyzer() {
        super(ScmActivityMetric.class);
    }


//    @Override
//    @Transactional
//    public void analyze(Identity identity, ScmAction action) {
//
//        if(identity == null ){
//            return;
//        }
//
//        List<ScmActivityMetric> forIdentity = getMetricDao().getForIdentityAfer(identity, action.getDate(), ScmActivityMetric.class);
//
//        //introduce a new metric and increase the quantity of the rest of the metrics
//        if(forIdentity.size() <= 0 ){
//
//            logger.trace("void analyze() The date is after the most recent one");
//            ScmActivityMetric sqm = getMetricDao().<ScmActivityMetric>getMostRecentMetric(identity,ScmActivityMetric.class);
//            logger.trace("void analyze() Handling {} -> {} ",identity.getUuid(),action.getDate());
//            ScmActivityMetric newMetric = new ScmActivityMetric();
//            newMetric.setCreatedAt(action.getDate());
//            newMetric.setIdentity(identity);
//            newMetric.setQuantity(sqm == null ? 1 : sqm.getQuantity() + 1);
//            newMetric = (ScmActivityMetric) getMetricDao().insert(newMetric);
//
//            logger.trace("void analyze() {} = {} -> {} ",
//                    new Object[]{identity.getUuid(),(sqm ==null ?0:sqm.getQuantity()),newMetric.getQuantity()});
//
//        }else{
//            logger.trace("void analyze() The date is between and we need to correct the metrics");
//
//            ScmActivityMetric metric = (ScmActivityMetric) forIdentity.get(0);
//            Integer quantity = metric.getQuantity();
//
//            ScmActivityMetric newMetric = new ScmActivityMetric();
//            newMetric.setCreatedAt(action.getDate());
//            newMetric.setIdentity(identity);
//
//            newMetric.setQuantity(quantity);
//            newMetric = (ScmActivityMetric) getMetricDao().insert(newMetric);
//
//
//            quantity++;
//            for(ScmActivityMetric m : forIdentity){
//
//                m.setQuantity(quantity);
//                quantity++;
//
//                getMetricDao().update(m);
//            }
//
//        }
//
//    }


    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ScmAction;
    }
}
