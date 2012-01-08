package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
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

public class MailingListActivityAnalyzer extends AbstractMailingListAnalyzer {

    private Logger logger = LoggerFactory.getLogger(MailingListActivityAnalyzer.class);

    @Override
    @Transactional
    public void analyze(Identity identity, MailingListAction action) {

        if(identity == null ){
            return;
        }

        List<MailingListActivityMetric> forIdentity = getMetricDao().<MailingListActivityMetric>getForIdentity(identity, MailingListActivityMetric.class);

        MailingListActivityMetric metric = null;
        if(forIdentity ==null || forIdentity.size() <=0){

            metric = new MailingListActivityMetric();
            metric.setQuantity(0);
            metric.setIdentity(identity);
            metric = (MailingListActivityMetric) getMetricDao().insert(metric);

        }else{
            metric = forIdentity.get(0);
        }

        metric.setCreatedAt(action.getDate());
        metric.setCreatedAt(action.getDate());
        metric.increaseQuantity();

        metric = (MailingListActivityMetric)getMetricDao().update(metric);

        logger.trace("void analyze() {} ", metric);

    }
}
