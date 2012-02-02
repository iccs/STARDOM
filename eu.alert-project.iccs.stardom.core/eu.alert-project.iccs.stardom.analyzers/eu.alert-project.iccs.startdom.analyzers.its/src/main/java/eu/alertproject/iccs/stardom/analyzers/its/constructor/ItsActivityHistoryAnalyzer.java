package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractQuantitativeHistoryAnalyzer;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 09:37
 */
public class    ItsActivityHistoryAnalyzer extends AbstractQuantitativeHistoryAnalyzer<ItsAction,ItsActivityMetric>{

    private Logger logger = LoggerFactory.getLogger(ItsActivityHistoryAnalyzer.class);

    public ItsActivityHistoryAnalyzer() {
        super(ItsActivityMetric.class);
    }


    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ItsAction;
    }
}


