package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsCommentAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsActivityAnalyzer extends AbstractItsAnalyzer{

    private Logger logger = LoggerFactory.getLogger(ItsActivityAnalyzer.class);


    //Idenity here is null carefull
    @Override
    @Transactional
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null ){
            return;
        }

        ItsActivityMetric metric = getMetricDao().<ItsActivityMetric>getMostRecentMetric(identity, ItsActivityMetric.class);

        //check if the metric exists
        if( metric == null){
            //create

            metric  = new ItsActivityMetric();
            metric.setIdentity(identity);
            metric.setQuantity(0);
            metric = (ItsActivityMetric) getMetricDao().insert(metric);

        }

        metric.increaseQuantity();

        getMetricDao().update(metric);

        logger.trace("void analyze() {} ",metric);

    }

    private void handleItsAction(Identity identity, DefaultItsAction action){

        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getMostRecentMetric(identity, ItsActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new ItsActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (ItsActivityMetric) getMetricDao().insert(sqm);

        }

        sqm.increaseQuantity();

        getMetricDao().update(sqm);



    }

    private void handleItsCommentAction(Identity identity, DefaultItsCommentAction action){


        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getMostRecentMetric(identity, ItsActivityMetric.class);

        //check if the metric exists
        if( sqm == null){
            //create

            sqm  = new ItsActivityMetric();
            sqm.setIdentity(identity);
            sqm.setQuantity(0);
            sqm = (ItsActivityMetric) getMetricDao().insert(sqm);

        }

        sqm.increaseQuantity();

        getMetricDao().update(sqm);

    }

}


