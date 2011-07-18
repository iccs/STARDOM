package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListTemporalMetric;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class MailingListTemporalAnalyzer extends AbstractMailingListAnalyzer{

    @Override
    public void analyze(Identity identity, MailingListAction action) {

        if(identity == null){
            return;
        }

        Date date = action.getDate();

        MailingListTemporalMetric itm = getMetricDao().<MailingListTemporalMetric>getForIdentity(identity,MailingListTemporalMetric.class);

        //check if the metric exists
        if(itm== null){

            itm = new MailingListTemporalMetric();
            itm.setIdentity(identity);

            //create
            itm = (MailingListTemporalMetric) getMetricDao().insert(itm);
        }

        itm.setTemporal(date);
        getMetricDao().update(itm);

    }
}
