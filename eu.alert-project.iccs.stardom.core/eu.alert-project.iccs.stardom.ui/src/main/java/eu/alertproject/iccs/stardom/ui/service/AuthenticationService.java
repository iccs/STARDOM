package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.ui.beans.AuthenticationInfo;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 8/28/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthenticationService {
    boolean remind(ProfileBean identity);
    boolean authenticate(ProfileBean identity);
    void logout(String email);
    AuthenticationInfo checkAuthenticationInfo(String email);
    void initSession(String email);

}
