package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public interface IdentificationService {

    public boolean match(
            Profile a,
            Profile b,
            double threshold,
            LevelWeightConfiguration configuration,
            PropertyWeightConfiguration firstname,
            PropertyWeightConfiguration lastname,
            PropertyWeightConfiguration username, PropertyWeightConfiguration email);

    public Identity findIdentity(Profile profile);

    @Transactional(readOnly = true)
    public List<Identity> findAll();
}
