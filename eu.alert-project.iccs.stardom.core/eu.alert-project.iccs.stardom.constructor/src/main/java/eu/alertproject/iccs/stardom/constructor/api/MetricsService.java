package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:49
 */

public interface MetricsService {

    public boolean hasMetric(Identity identity, Class<? extends Metric> metric);
    public <T extends Metric> T getMetric(Identity identity, Class<? extends Metric> metric);
    public Metric save(Identity identity, Metric stm);
}
