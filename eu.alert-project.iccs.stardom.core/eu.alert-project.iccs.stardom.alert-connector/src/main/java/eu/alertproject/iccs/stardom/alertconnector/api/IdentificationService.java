package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.domain.api.Person;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public interface IdentificationService {

    public boolean match(
            Person a,
            Person b,
            double threshold,
            LevelWeightConfiguration configuration,
            PropertyWeightConfiguration firstname,
            PropertyWeightConfiguration lastname,
            PropertyWeightConfiguration username, PropertyWeightConfiguration email);
}
