package eu.alertproject.iccs.stardom.alertconnector.internal;

import eu.alertproject.iccs.stardom.alertconnector.api.IdentificationService;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.identifier.api.IdentifierWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.internal.DefaultIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */

@Service("identificationService")
public class  IdentificationServiceIF implements IdentificationService {


    @Autowired
    IdentityDao identityDao;

    @Autowired
    Identifier identifier;

    private DefaultIdentifier runtimeIdentifier;

    @PostConstruct
    private void init(){

        this.runtimeIdentifier = new DefaultIdentifier();



    }


    @Override
    public boolean match(
            Profile a,
            Profile b,
            double threshold,
            LevelWeightConfiguration configuration,
            PropertyWeightConfiguration firstname,
            PropertyWeightConfiguration lastname,
            PropertyWeightConfiguration username,
            PropertyWeightConfiguration email) {


        this.runtimeIdentifier.setWeightConfiguration(
            new IdentifierWeightConfiguration(
              threshold,
              configuration,
              firstname,
              lastname,
              username,
              email
            )
        );

        return this.runtimeIdentifier.match(a,b);
    }

    @Override
    public Identity findIdentity(Profile profile) {
        return this.identifier.find(profile);
    }

    @Override
    public List<Identity> findAll() {
        return identityDao.findAll();
    }
}
