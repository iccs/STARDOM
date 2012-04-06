package eu.alertproject.iccs.stardom.datastore.api.metrics;

import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 19:47
 */
public interface MetricValueStrategy<T extends Metric>{

    public Integer getValue(MetricDao metricDao,Identity identity);
}
