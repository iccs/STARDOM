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
     *
     * @deprecated This will be removed in the future
     */
    public Identity find(Profile profile);

    /**
     * The following methods searches the local store for
     * an Identity which matches this profile.
     *
     * I case a profile is not found an new Identity should
     * be created.
     *
     * @param profile
     * @param source The source of the profile for tracking purposes
     * @return
     */
    public Identity find(Profile profile,String source);



    public List<Identity> identify(List<Profile> profile);

}
