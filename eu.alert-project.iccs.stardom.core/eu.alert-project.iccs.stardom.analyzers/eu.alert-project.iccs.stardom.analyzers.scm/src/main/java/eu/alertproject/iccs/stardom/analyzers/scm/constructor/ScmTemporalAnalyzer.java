package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
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
 * Date: 15/07/11
 * Time: 22:44
 */
public class ScmTemporalAnalyzer extends AbstractTemporalAnalyzer<ScmAction, ScmTemporalMetric> {

    public ScmTemporalAnalyzer() {
        super(ScmTemporalMetric.class);
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ScmAction;
    }
}
