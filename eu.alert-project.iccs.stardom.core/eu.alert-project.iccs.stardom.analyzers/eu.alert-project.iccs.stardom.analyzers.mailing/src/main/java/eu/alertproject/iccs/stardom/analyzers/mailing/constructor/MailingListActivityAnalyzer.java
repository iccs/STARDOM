package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class MailingListActivityAnalyzer extends AbstractMailingListAnalyzer {

    @Override
    public void analyze(Identity identity, MailingListAction action) {

        if(identity == null ){
            return;
        }

        MailingListActivityMetric sqm = getMetricDao().<MailingListActivityMetric>getForIdentity(identity,MailingListActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new MailingListActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (MailingListActivityMetric) getMetricDao().insert(sqm);

        }

        sqm.increaseQuantity();

        getMetricDao().update(sqm);

    }
}
