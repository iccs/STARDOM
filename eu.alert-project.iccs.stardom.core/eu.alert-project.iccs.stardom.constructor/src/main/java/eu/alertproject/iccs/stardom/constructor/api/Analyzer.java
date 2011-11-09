package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:34
 */
public interface Analyzer<T extends ConnectorAction> {

    public void analyze(Identity identity, T action);
    public boolean canHandle(ConnectorAction action);

}
