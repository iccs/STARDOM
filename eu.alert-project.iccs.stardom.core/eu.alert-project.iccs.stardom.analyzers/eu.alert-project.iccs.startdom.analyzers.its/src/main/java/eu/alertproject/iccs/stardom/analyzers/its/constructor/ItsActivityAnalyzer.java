package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsCommentAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsActivityAnalyzer extends AbstractItsAnalyzer{

    //Idenity here is null carefull
    @Override
    public void analyze(Identity identity, ItsAction action) {

        if(identity == null ){
            return;
        }

        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getForIdentity(identity, ItsActivityMetric.class);


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

//        if(action instanceof DefaultItsAction){
//            handleItsAction(identity, (DefaultItsAction) action);
//        }else if(action instanceof DefaultItsCommentAction){
//            handleItsCommentAction(identity, (DefaultItsCommentAction) action);
//        }
    }

    private void handleItsAction(Identity identity, DefaultItsAction action){

        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getForIdentity(identity, ItsActivityMetric.class);

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


        ItsActivityMetric sqm = getMetricDao().<ItsActivityMetric>getForIdentity(identity, ItsActivityMetric.class);

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


