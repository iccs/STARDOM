package eu.alertproject.iccs.stardom.analyzers.mailing.constructor;

import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractQuantitativeHistoryAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:57
 */

public class MailingListActivityHistoryAnalyzer extends AbstractQuantitativeHistoryAnalyzer<MailingListAction,MailingListActivityMetric> {

    private Logger logger = LoggerFactory.getLogger(MailingListActivityHistoryAnalyzer.class);

    public MailingListActivityHistoryAnalyzer() {
        super(MailingListActivityMetric.class);
    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof MailingListAction;
    }



}
