package eu.alertproject.iccs.stardom.analyzers.forums.constructor;

import eu.alertproject.iccs.stardom.analyzers.forums.connector.ForumAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ForumActivityMetric;
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

        ForumActivityMetric sqm = getMetricDao().<ForumActivityMetric>getForIdentity(identity,ForumActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new ForumActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (ForumActivityMetric) getMetricDao().insert(sqm);

        }


        sqm.increaseQuantity();

        logger.trace("void analyze() {}",sqm);
        getMetricDao().update(sqm);

    }
}
