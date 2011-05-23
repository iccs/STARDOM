package eu.alertproject.iccs.stardom.alertconnector.internal;

import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.identifier.api.IdentifierWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.alertconnector.api.IdentificationService;
import eu.alertproject.iccs.stardom.domain.api.Person;
import eu.alertproject.iccs.stardom.identifier.internal.DefaultIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */

@Service("identificationService")
public class  IdentificationServiceIF implements IdentificationService {


    private DefaultIdentifier identifier;

    @PostConstruct
    private void init(){

        this.identifier = new DefaultIdentifier();

    }


    @Override
    public boolean match(
            Person a,
            Person b,
            double threshold,
            LevelWeightConfiguration configuration,
            PropertyWeightConfiguration firstname,
            PropertyWeightConfiguration lastname,
            PropertyWeightConfiguration username,
            PropertyWeightConfiguration email) {


        this.identifier.setWeightConfiguration(
            new IdentifierWeightConfiguration(
              threshold,
              configuration,
              firstname,
              lastname,
              username,
              email
            )
        );

        return this.identifier.match(a,b);
    }
}
