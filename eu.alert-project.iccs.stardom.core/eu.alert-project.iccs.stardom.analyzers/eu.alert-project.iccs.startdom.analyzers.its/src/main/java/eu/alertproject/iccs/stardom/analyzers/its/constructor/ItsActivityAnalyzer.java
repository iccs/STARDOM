package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsActivityAnalyzer extends AbstractItsAnalyzer{

    @Override
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null ){
            return;
        }

        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getForIdentity(identity, ItsActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new ItsActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (ItsActivityMetric) getMetricDao().insert(sqm);

        }

        sqm.increaseQuantity();

        getMetricDao().update(sqm);


    }
}
