package eu.alertproject.iccs.stardom.analyzers.its.internal;

import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;
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


    public Profile generate(Profile dirty){

        String name = StringUtils.trimToEmpty(dirty.getName());
        String email = StringUtils.trimToEmpty(dirty.getEmail());

        Profile profile = new Profile();
        profile.setUsername(dirty.getUsername());
        profile.setEmail(email);
        profile.setSourceId(dirty.getSourceId());

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
            profile.setName(null);
            profile.setLastname(null);
        }



        //check none
        if(StringUtils.trimToEmpty(profile.getName()).toLowerCase().equals("none")){
            profile.setName(null);
        }
        if(StringUtils.trimToEmpty(profile.getLastname()).toLowerCase().equals("none")){
            profile.setLastname(null);
        }

        if(StringUtils.trimToEmpty(profile.getUsername()).toLowerCase().equals("none")){
            profile.setUsername(null);
        }

        if(StringUtils.trimToEmpty(profile.getEmail()).toLowerCase().equals("none")){
            profile.setEmail(null);
        }


        logger.trace("Profile generate() Returning {} ",profile);
        return profile;

    }
}
