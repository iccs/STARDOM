package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.connector.api.Connector;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class AlertConnector implements Connector {

    private Logger logger = LoggerFactory.getLogger(AlertConnector.class);

    @Autowired
    IdentificationService identificationService;

    @Override
    @RequestMapping(value = "/identification/match", method = RequestMethod.POST)
    public @ResponseBody Boolean match(
            @RequestParam String firstNameA,
            @RequestParam String lastNameA,
            @RequestParam String usernameA,
            @RequestParam String emailA,
            @RequestParam String firstNameB,
            @RequestParam String lastNameB,
            @RequestParam String usernameB,
            @RequestParam String emailB,
            @RequestParam double threshold,
            @RequestParam double ulWeight,
            @RequestParam double vpuWeight,
            @RequestParam double mlWeight,
            @RequestParam double firstNameUl,
            @RequestParam double firstNameVpu,
            @RequestParam double firstNameMl,
            @RequestParam double lastNameUl,
            @RequestParam double lastNameVpu,
            @RequestParam double lastNameMl,
            @RequestParam double usernameNameUl,
            @RequestParam double usernameVpu,
            @RequestParam double usernameMl,
            @RequestParam double emailUl,
            @RequestParam double emailVpu,
            @RequestParam double emailMl){


            logger.trace("Boolean match() firstNameA={}",firstNameA);
            logger.trace("Boolean match() lastNameA={}",lastNameA);
            logger.trace("Boolean match() usernameA={}",usernameA);
            logger.trace("Boolean match() emailA={}",emailA);
            logger.trace("Boolean match() firstNameB={}",firstNameB);
            logger.trace("Boolean match() lastNameB={}",lastNameB);
            logger.trace("Boolean match() usernameB={}",usernameB);
            logger.trace("Boolean match() emailB={}",emailB);
            logger.trace("Boolean match() threshold={}",threshold);
            logger.trace("Boolean match() ulWeight={}",ulWeight);
            logger.trace("Boolean match() vpuWeight={}",vpuWeight);
            logger.trace("Boolean match() mlWeight={}",mlWeight);
            logger.trace("Boolean match() firstNameUl={}",firstNameUl);
            logger.trace("Boolean match() firstNameVpu={}",firstNameVpu);
            logger.trace("Boolean match() firstNameMl={}",firstNameMl);
            logger.trace("Boolean match() lastNameUl={}",lastNameUl);
            logger.trace("Boolean match() lastNameVpu={}",lastNameVpu);
            logger.trace("Boolean match() lastNameMl={}",lastNameMl);
            logger.trace("Boolean match() usernameNameUl={}",usernameNameUl);
            logger.trace("Boolean match() usernameVpu={}",usernameVpu);
            logger.trace("Boolean match() usernameMl={}",usernameMl);
            logger.trace("Boolean match() emailUl={}",emailUl);
            logger.trace("Boolean match() emailVpu={}",emailVpu);
            logger.trace("Boolean match() emailMl={}",emailMl);



        return identificationService.match(
                new Profile(firstNameA,lastNameA,usernameA,emailA),
                new Profile(firstNameB,lastNameB,usernameB,emailB),
                threshold,
                new LevelWeightConfiguration(ulWeight,vpuWeight,mlWeight),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.FIRSTNAME,firstNameUl,firstNameVpu,firstNameMl),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.LASTNAME,lastNameUl,lastNameVpu,lastNameMl),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.USERNAME,usernameNameUl,usernameVpu,usernameMl),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.EMAIL,emailUl,emailVpu,emailMl)
        );

    }

    @Override
    @RequestMapping(value = "/identification/find", method = RequestMethod.POST)
    public @ResponseBody Identity find(
                            @RequestParam String firstName ,
                            @RequestParam String lastName ,
                            @RequestParam String userName ,
                            @RequestParam String email
                            ) {

        Identity identity = identificationService.findIdentity(new Profile(firstName,lastName,userName,email));

        return identity;

    }

    @Override
    @RequestMapping(value = "/identification/list", method = RequestMethod.GET)
    public @ResponseBody List<Identity> getAll() {
        return identificationService.findAll();
    }

    @Override
    public List<Identity> identify(List<Profile> profile) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
