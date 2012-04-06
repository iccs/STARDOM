package eu.alertproject.iccs.stardom.datastore.api.metrics;

import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 19:47
 */
public class TemporalMetricValueStrategy<T extends MetricTemporal> extends AbstractMetricValueStrategy<T>{

    public TemporalMetricValueStrategy(Class<T> metricClass) {
        super(metricClass);
    }

    @Override
    public Integer getValue(MetricDao metricDao, Identity identity) {
        T mostRecentMetric = metricDao.<T>getMostRecentMetric(
                identity,
                getMetricClass()
        );
        
        if(mostRecentMetric == null){
            return 0;
        }
        return Days
                .daysBetween(
                        new DateTime(
                                mostRecentMetric.getTemporal()
                        ),
                        new DateTime()
                ).getDays();
    }

}
