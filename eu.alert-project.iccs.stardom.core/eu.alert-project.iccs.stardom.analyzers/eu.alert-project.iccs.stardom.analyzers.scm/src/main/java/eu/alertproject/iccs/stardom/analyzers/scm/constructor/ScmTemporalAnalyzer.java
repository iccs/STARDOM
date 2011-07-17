package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:44
 */
public class ScmTemporalAnalyzer extends AbstractScmAnalyzer{


    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity == null){
            return;
        }

        Date date = action.getDate();

        ScmTemporalMetric stm = getMetricDao().<ScmTemporalMetric>getForIdentity(identity,ScmTemporalMetric.class);

        //check if the metric exists
        if(stm == null){

            stm = new ScmTemporalMetric();
            stm.setIdentity(identity);

            //create
            stm = (ScmTemporalMetric) getMetricDao().insert(stm);

        }

        stm.setTemporal(date);

        getMetricDao().update(stm);

    }
}
