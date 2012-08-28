package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 8/28/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthenticationService {
    void remind(ProfileBean identity);
}
