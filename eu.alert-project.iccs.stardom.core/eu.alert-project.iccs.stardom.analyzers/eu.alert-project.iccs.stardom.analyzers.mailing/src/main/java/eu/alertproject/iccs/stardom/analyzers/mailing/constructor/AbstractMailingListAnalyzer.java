package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:55
 */
public abstract class AbstractMailingListAnalyzer implements Analyzer<MailingListAction> {


    @Autowired
    private MetricDao metricDao;

    public MetricDao getMetricDao() {
        return metricDao;
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof MailingListAction;
    }


}
