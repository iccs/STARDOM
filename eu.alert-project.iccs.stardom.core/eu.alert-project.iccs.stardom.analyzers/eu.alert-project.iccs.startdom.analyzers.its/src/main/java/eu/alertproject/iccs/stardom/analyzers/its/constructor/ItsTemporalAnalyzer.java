package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractTemporalAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class ItsTemporalAnalyzer extends AbstractTemporalAnalyzer<ItsAction,ItsTemporalMetric>{

    public ItsTemporalAnalyzer() {
        super(ItsTemporalMetric.class);
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ItsAction;
    }
}
