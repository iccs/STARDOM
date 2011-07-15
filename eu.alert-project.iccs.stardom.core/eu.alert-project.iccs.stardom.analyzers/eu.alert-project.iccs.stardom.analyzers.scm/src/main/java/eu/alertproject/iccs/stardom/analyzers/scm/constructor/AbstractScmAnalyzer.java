package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:55
 */
public abstract class AbstractScmAnalyzer implements Analyzer<ScmAction> {

    @Autowired
    private MetricsService metricsService;

    public MetricsService getMetricsService() {
        return metricsService;
    }
}
