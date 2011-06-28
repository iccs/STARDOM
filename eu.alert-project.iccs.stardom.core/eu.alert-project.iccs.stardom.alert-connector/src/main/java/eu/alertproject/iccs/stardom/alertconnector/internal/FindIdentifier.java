package eu.alertproject.iccs.stardom.alertconnector.internal;

import eu.alertproject.iccs.stardom.identifier.api.IdentifierWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.internal.DefaultIdentifier;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
@Component("findIdentifier")
public class FindIdentifier extends DefaultIdentifier{

    public FindIdentifier(){

        super(
            new IdentifierWeightConfiguration(
                0.74,
                new LevelWeightConfiguration(0.45,0.35, 0.20),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.FIRSTNAME,0.2, 0.8,0.2),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.LASTNAME,0.8, 0.8,0.8),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.USERNAME,0.8, 0.2,0.2),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.EMAIL,1, 0.4,0.4)
        ));

    }

}
