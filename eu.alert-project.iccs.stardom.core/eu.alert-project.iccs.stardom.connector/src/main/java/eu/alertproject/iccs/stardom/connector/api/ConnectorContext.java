package eu.alertproject.iccs.stardom.connector.api;

import eu.alertproject.iccs.stardom.domain.api.Profile;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:17
 */
public interface ConnectorContext<T extends ConnectorAction>  {


    public void setProfile(Profile profile);
    public Profile getProfile();

    public void setAction(T action);
    public T getAction();
}
