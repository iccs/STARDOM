package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsTemporalAnalyzer extends AbstractItsAnalyzer{

    @Override
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null){
            return;
        }

        Date date = action.getDate();

        ItsTemporalMetric itm = getMetricDao().<ItsTemporalMetric>getForIdentity(identity,ItsTemporalMetric.class);

        //check if the metric exists
        if(itm== null){

            itm = new ItsTemporalMetric();
            itm.setIdentity(identity);

            //create
            itm = (ItsTemporalMetric) getMetricDao().insert(itm);

        }

        itm.setTemporal(date);
        getMetricDao().update(itm);

    }
}
