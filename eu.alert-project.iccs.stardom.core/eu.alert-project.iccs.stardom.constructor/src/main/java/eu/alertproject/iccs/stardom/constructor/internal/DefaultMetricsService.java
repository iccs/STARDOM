package eu.alertproject.iccs.stardom.constructor.internal;

import eu.alertproject.iccs.stardom.constructor.api.MetricsService;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import org.springframework.stereotype.Service;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:49
 */

@Service("metricsService")
public class DefaultMetricsService implements MetricsService{

    @Override
    public boolean hasMetric(Identity identity, Class<? extends Metric> metric) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Metric> T getMetric(Identity identity, Class<? extends Metric> metric) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Metric save(Identity identity, Metric stm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
