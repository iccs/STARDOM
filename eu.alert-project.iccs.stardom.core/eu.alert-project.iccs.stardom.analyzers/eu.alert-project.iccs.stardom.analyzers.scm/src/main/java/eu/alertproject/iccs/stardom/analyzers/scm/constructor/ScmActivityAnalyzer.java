package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */
public class ScmActivityAnalyzer extends AbstractScmAnalyzer {

    @Override
    public void analyze(Identity identity, ScmAction action) {

        ScmActivityMetric sqm = new ScmActivityMetric();

        //check if the metric exists
        if(!getMetricsService().hasMetric(identity, ScmTemporalMetric.class)){

            //create
            sqm = getMetricsService().<ScmActivityMetric>getMetric(identity, ScmActivityMetric.class);
        }

        sqm.increaseQuantity();
        getMetricsService().save(identity, sqm);
    }
}
