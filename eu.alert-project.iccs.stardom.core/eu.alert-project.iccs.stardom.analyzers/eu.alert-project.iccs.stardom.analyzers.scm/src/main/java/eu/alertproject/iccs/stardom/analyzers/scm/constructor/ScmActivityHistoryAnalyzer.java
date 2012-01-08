package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class ScmActivityHistoryAnalyzer extends AbstractScmAnalyzer {

    private Logger logger = LoggerFactory.getLogger(ScmActivityHistoryAnalyzer.class);
    
    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity == null ){
            return;
        }

        ScmActivityMetric sqm = getMetricDao().<ScmActivityMetric>getMostRecentMetric(identity,ScmActivityMetric.class);

        logger.trace("void analyze() Handling {} -> {} ",identity.getUuid(),action.getDate());

        ScmActivityMetric newMetric = new ScmActivityMetric();
        newMetric.setCreatedAt(action.getDate());
        newMetric.setIdentity(identity);
        newMetric.setQuantity(sqm == null ? 1 : sqm.getQuantity() + 1);
        newMetric = (ScmActivityMetric) getMetricDao().insert(newMetric);

        logger.trace("void analyze() {} = {} -> {} ",
                new Object[]{identity.getUuid(),(sqm ==null ?0:sqm.getQuantity()),newMetric.getQuantity()});
    }
}
