package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class ScmActivityAnalyzer extends AbstractScmAnalyzer {

    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity == null ){
            return;
        }

        ScmActivityMetric sqm = getMetricDao().<ScmActivityMetric>getForIdentity(identity,ScmActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new ScmActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (ScmActivityMetric) getMetricDao().insert(sqm);

        }

        sqm.increaseQuantity();

        getMetricDao().update(sqm);

    }
}
