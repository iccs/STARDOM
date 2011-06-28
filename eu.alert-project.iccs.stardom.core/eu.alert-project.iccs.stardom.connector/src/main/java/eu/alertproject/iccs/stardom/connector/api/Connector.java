package eu.alertproject.iccs.stardom.connector.api;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public interface Connector {


    public Boolean match(
            String firstNameA, String lastNameA, String usernameA, String emailA,
            String firstNameB, String lastNameB, String usernameB, String emailB,
            double threshold,
            double ulWeight,
            double vpuWeight,
            double mlWeight,
            double firstNameUl, double firstNameVpu,double firstNameMl,
            double lastNameUl, double lastNameVpu,double lastNameMl,
            double usernameNameUl, double usernameVpu,double usernameMl,
            double emailUl, double emailVpu,double eailMl);



    public Identity find(String firstName, String lastName, String userName, String email);
    public List<Identity> identify(List<Profile> profile);

    public List<Identity> getAll();

}
