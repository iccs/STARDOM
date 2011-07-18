package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:36
 */
public abstract class AbstractItsAnalyzer  implements Analyzer<ItsAction> {

    @Autowired
    private MetricDao metricDao;

    public MetricDao getMetricDao() {
        return metricDao;
    }
}
