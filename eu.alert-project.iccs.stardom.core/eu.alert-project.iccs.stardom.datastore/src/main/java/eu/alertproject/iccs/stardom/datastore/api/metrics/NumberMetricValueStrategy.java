package eu.alertproject.iccs.stardom.datastore.api.metrics;

import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 19:51
 */
public class NumberMetricValueStrategy<T extends MetricTemporal> extends AbstractMetricValueStrategy<T>{


    public NumberMetricValueStrategy(Class<T> metricClass) {
        super(metricClass);
    }

    @Override
    public Integer getValue(MetricDao metricDao, Identity identity) {
        return metricDao.<T>getNumberForIdentity(identity, getMetricClass());
    }
}
