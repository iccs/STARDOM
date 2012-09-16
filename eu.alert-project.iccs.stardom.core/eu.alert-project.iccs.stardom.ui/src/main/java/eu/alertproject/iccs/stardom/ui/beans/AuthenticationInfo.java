package eu.alertproject.iccs.stardom.ui.beans;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/17/12
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationInfo {

    private String email;
    private boolean isAuthenticated;
    private boolean  isAdmin;
    private String uuid;

    public AuthenticationInfo(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
