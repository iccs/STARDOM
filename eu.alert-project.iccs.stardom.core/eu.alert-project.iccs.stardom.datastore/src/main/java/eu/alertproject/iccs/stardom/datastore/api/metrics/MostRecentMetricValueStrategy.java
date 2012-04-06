package eu.alertproject.iccs.stardom.datastore.api.metrics;

import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsIssuesResolvedMetric;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 19:51
 */
public class MostRecentMetricValueStrategy<T extends MetricQuantitative> extends AbstractMetricValueStrategy<T>{

    public MostRecentMetricValueStrategy(Class<T> metricClass) {
        super(metricClass);
    }

    @Override
    public Integer getValue(MetricDao metricDao, Identity identity) {
        T mostRecentMetric = metricDao.<T>getMostRecentMetric(identity, getMetricClass());
        if(mostRecentMetric == null ){
            return 0;
        }

        return mostRecentMetric.getQuantity();
    }

}
