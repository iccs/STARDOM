package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;

import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:44
 */
public class ScmTemporalAnalyzer extends AbstractScmAnalyzer{


    @Override
    public void analyze(Identity identity, ScmAction action) {

        Date date = action.getDate();

        ScmTemporalMetric stm = new ScmTemporalMetric();
        //check if the metric exists
        if(!getMetricsService().hasMetric(identity, ScmTemporalMetric.class)){

            //create
            stm = getMetricsService().<ScmTemporalMetric>getMetric(identity, ScmTemporalMetric.class);

        }

        stm.setWhen(date);
        getMetricsService().save(identity, stm);


    }
}
