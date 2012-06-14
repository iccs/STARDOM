package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.DefaultMailingListAction;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractTemporalAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class MailingListTemporalAnalyzer extends AbstractTemporalAnalyzer<MailingListAction,MailingListTemporalMetric> {


    private Logger logger = LoggerFactory.getLogger(MailingListTemporalAnalyzer.class);


    public MailingListTemporalAnalyzer() {
        super(MailingListTemporalMetric.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyze(Identity identity, MailingListAction action) {


        if(identity == null){
            logger.warn("Can't work with a null identity");
            return;
//                throw new NullPointerException("Can't work with a null identity");
        }

        MailingListTemporalMetric newMetrics = new MailingListTemporalMetric();

        newMetrics.setIdentity(identity);
        newMetrics.setCreatedAt(new Date());
        newMetrics.setTemporal(action.getDate());
        newMetrics.setInReplyTo(action.getInReplyTo());
        newMetrics.setMessageId(action.getMessageId());


        Metric insert = getMetricDao().insert(newMetrics);

        logger.trace("void analyze() Created Metric {} ",insert);




    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof DefaultMailingListAction;
    }
}
