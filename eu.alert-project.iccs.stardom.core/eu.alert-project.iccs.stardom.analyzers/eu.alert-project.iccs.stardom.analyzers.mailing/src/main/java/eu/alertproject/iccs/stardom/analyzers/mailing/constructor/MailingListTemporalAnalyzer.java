package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class MailingListTemporalAnalyzer extends AbstractMailingListAnalyzer{
    private Logger logger = LoggerFactory.getLogger(MailingListTemporalAnalyzer.class);

    @Override
    public void analyze(Identity identity, MailingListAction action) {

        if(identity == null){
            return;
        }


        MailingListTemporalMetric newMetrics  = new MailingListTemporalMetric();
        newMetrics.setIdentity(identity);
        newMetrics.setCreatedAt(new Date());
        newMetrics.setTemporal(action.getDate());


        MailingListTemporalMetric metric = (MailingListTemporalMetric) getMetricDao().insert(newMetrics);

        logger.trace("void analyze() {} ", metric);

    }
}
