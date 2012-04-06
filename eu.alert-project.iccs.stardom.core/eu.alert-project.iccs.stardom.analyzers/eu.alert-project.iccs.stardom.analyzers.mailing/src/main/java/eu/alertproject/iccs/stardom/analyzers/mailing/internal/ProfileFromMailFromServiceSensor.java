package eu.alertproject.iccs.stardom.analyzers.mailing.internal;

import eu.alertproject.iccs.stardom.analyzers.mailing.api.ProfileFromMailFromService;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 16:43
 */
public class ProfileFromMailFromServiceSensor implements ProfileFromMailFromService {

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

        /*
        We are making the following assumptions that we have
            1.) Email format Name Lastname <email>
            2.) Names are composed of a single word, the rest is the last name

            Alex Fiestas <afiestas@kde.org>
         */
        Pattern p = Pattern.compile("([\\w\\.\\?-]+) at ([\\w\\?\\.\\d=\\-]+) (\\((.*( .*)?)\\))?");

        Matcher matcher = p.matcher(mailFrom);

        if(!matcher.matches()){
            return null;
        }
        if(matcher.groupCount() < 3){
            return null;
        }

        Profile profile = new Profile();

        profile.setEmail(matcher.group(1).trim()+" "+matcher.group(2).trim());
        profile.setName(StringUtils.substringBefore(matcher.group(4).trim()," "));
        profile.setLastname(StringUtils.defaultIfEmpty(StringUtils.substringAfter(matcher.group(4).trim()," ").trim(),""));

        return profile;

    }

}
