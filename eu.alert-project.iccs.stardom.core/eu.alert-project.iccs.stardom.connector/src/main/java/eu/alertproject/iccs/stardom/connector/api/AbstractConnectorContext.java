package eu.alertproject.iccs.stardom.connector.api;

import eu.alertproject.iccs.stardom.domain.api.Profile;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:20
 */
public abstract class AbstractConnectorContext<T extends ConnectorAction> implements ConnectorContext<T> {


    private Profile profile;
    private T action;

    @Override
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public void setAction(T action) {
        this.action= action;
    }

    @Override
    public T getAction() {
        return this.action;
    }
}
