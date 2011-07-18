package eu.alertproject.iccs.stardom.analyzers.forums.constructor;

import eu.alertproject.iccs.stardom.analyzers.forums.connector.ForumAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:55
 */
public abstract class AbstractForumAnalyzer implements Analyzer<ForumAction> {


    @Autowired
    private MetricDao metricDao;

    public MetricDao getMetricDao() {
        return metricDao;
    }


}
