package eu.alertproject.iccs.stardom.analyzers.mailing.internal;

import eu.alertproject.iccs.stardom.analyzers.mailing.api.ProfileFromMailFromService;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 16:43
 */
public class ProfileFromMailFromServiceSensor implements ProfileFromMailFromService {

    private Logger logger = LoggerFactory.getLogger(ProfileFromMailFromServiceSensor.class);

    /**
     * <p>The following method generates a mail from header such as "Fotis"&lt; a @ b .com &gt;"
     * into a proper {@link eu.alertproject.iccs.stardom.domain.api.Profile}</p>
     * <p>
     *     It attempts to extract the firstname and the last name from the e-mail.
     * </p>
     *
     *
     * @param mailFrom
     * @return
     */
    @Override
    public Profile generate(String mailFrom){

        String[] regexes = new String[]{
          "\"?([\\p{L}\\?-_]+)\\s?([\\p{L}\\?\\.\\d=\\\\ _-]+)?\"?\\s*<(.*)>", //  Alex Fiestas <afiestas@kde.org>
          "([\\p{L}\\.\\?-_]+) at ([\\p{L}\\?\\.\\d=\\-_]+) (\\((.*( .*)?)\\))?"
        };

        String regex = null;

        for(String reg:regexes){

            if(mailFrom.matches(reg)){
                regex = reg;
                break;
            }
        }

        if(regex == null){
            logger.error("Could not find a suitable regular expression for {} ",mailFrom);
            return null;
        }

        logger.debug("Found a match for {} ",mailFrom);

        /*
        We are making the following assumptions that we have
            1.) Email format Name Lastname <email>
            2.) Names are composed of a single word, the rest is the last name

            Alex Fiestas <afiestas@kde.org>
         */
        Pattern p = Pattern.compile(regex);

        int i = ArrayUtils.indexOf(regexes, regex);
        Profile profile = new Profile();

        Matcher matcher = p.matcher(mailFrom);
        matcher.matches();

        switch (i){
            case 0:   //  Alex Fiestas <afiestas@kde.org>

                if(matcher.groupCount() <= 2){
                    //single name
                    profile.setName(matcher.group(1).trim());
                    profile.setEmail(matcher.group(2).trim());
                    profile.setUsername(matcher.group(2).trim());
                }else if( matcher.groupCount()>2){
                    profile.setName(matcher.group(1).trim());
                    profile.setLastname(StringUtils.trimToEmpty(matcher.group(2)));
                    profile.setEmail(matcher.group(3).trim());
                    profile.setUsername(matcher.group(3).trim());
                }
                break;
            case 1:    //  afiestas at kde com
                profile.setEmail(matcher.group(1).trim()+" "+matcher.group(2).trim());
                profile.setName(StringUtils.substringBefore(matcher.group(4).trim()," "));
                profile.setLastname(StringUtils.defaultIfEmpty(StringUtils.substringAfter(matcher.group(4).trim()," ").trim(),""));
                break;
        }
        return profile;

    }

}
