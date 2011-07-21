package eu.alertproject.iccs.stardom.analyzers.its.internal;

import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 12:46
 */
@Service("profileFromItsKdeWhoService")
public class ProfileFromItsKdeWhoService {

    private Logger logger = LoggerFactory.getLogger(ProfileFromItsKdeWhoService.class);


    public Profile generate(String name, String email){

        Profile profile = new Profile();
        /**
         * 1.) Names are composed of a single word, the rest is the last name
         */
        Pattern p = Pattern.compile("([\\p{L}\\w\\?-]+) *([\\p{L}\\w\\?\\.\\d=\\\\ -]+)?");

        Matcher matcher = p.matcher(name);

        logger.trace("Profile generate() {} ",name,email);

        if(matcher.matches()){
            logger.trace("Profile generate() ************* MATCHES ***************");

            profile.setName(matcher.group(1));
            profile.setLastname(matcher.group(2));

        }else{
            logger.trace("Profile generate() ----------------   NOT MATCHED");
        }



        profile.setEmail(email);
        logger.trace("Profile generate() Returning {} ",profile);
        return profile;

    }
}
