package eu.alertproject.iccs.stardom.analyzers.forums.constructor;

import eu.alertproject.iccs.stardom.analyzers.forums.connector.ForumAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ForumActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class ForumActivityAnalyzer extends AbstractForumAnalyzer {

    private Logger logger = LoggerFactory.getLogger(ForumActivityAnalyzer.class);

    @Override
    @Transactional
    public void analyze(Identity identity, ForumAction action) {

        if(identity == null ){
            return;
        }


        ForumActivityMetric sqm = getMetricDao().<ForumActivityMetric>getMostRecentMetric(identity, ForumActivityMetric.class);

        ForumActivityMetric newMetric = new ForumActivityMetric();
        newMetric.setCreatedAt(action.getDate());
        newMetric.setIdentity(identity);
        newMetric.setQuantity(sqm == null ? 0 : sqm.getQuantity());

        newMetric.increaseQuantity();

        newMetric = (ForumActivityMetric) getMetricDao().insert(newMetric);

        logger.trace("void analyze() {} ", newMetric);


    }
}
