package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractTemporalAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListTemporalMetric;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class MailingListTemporalAnalyzer extends AbstractTemporalAnalyzer<MailingListAction,MailingListTemporalMetric> {

    public MailingListTemporalAnalyzer() {
        super(MailingListTemporalMetric.class);
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof MailingListAction;
    }
}
