package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

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

        MailingListActivityMetric sqm = getMetricDao().<MailingListActivityMetric>getMostRecentMetric(identity, MailingListActivityMetric.class);

        MailingListActivityMetric newMetric = new MailingListActivityMetric();
        newMetric.setCreatedAt(action.getDate());
        newMetric.setIdentity(identity);
        newMetric.setQuantity(sqm == null ? 1 : sqm.getQuantity());

        newMetric.increaseQuantity();

        newMetric = (MailingListActivityMetric) getMetricDao().insert(newMetric);

        logger.trace("void analyze() {} ", newMetric);

    }
}
