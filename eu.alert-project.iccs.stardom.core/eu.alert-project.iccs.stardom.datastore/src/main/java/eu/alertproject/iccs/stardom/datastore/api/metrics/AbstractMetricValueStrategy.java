package eu.alertproject.iccs.stardom.datastore.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.Metric;

/**
 * User: fotis
 * Date: 06/04/12
 * Time: 12:53
 */
public abstract class AbstractMetricValueStrategy<T extends Metric> implements MetricValueStrategy<T>{

    private Class<T> metricClass;

    public AbstractMetricValueStrategy(Class<T> metricClass){
           this.metricClass = metricClass;
    }


    public Class<T> getMetricClass() {
        return metricClass;
    }
}
