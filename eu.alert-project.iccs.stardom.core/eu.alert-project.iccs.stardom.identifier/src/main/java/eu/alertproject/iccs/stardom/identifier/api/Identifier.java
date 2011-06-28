package eu.alertproject.iccs.stardom.identifier.api;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */
public interface Identifier {

    public boolean match(Profile a, Profile b);

    /**
     * The following methods searches the local store for
     * an Identity which matches this profile.
     *
     * I case a profile is not found an new Identity should
     * be created.
     *
     * @param profile
     * @return
     */
    public Identity find(Profile profile);
    public List<Identity> identify(List<Profile> profile);

}
