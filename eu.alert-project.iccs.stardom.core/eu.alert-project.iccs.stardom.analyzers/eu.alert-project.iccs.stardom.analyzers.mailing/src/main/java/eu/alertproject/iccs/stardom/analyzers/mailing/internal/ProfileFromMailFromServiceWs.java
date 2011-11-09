package eu.alertproject.iccs.stardom.analyzers.mailing.internal;

import eu.alertproject.iccs.stardom.analyzers.mailing.api.ProfileFromMailFromService;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 16:43
 */
public class ProfileFromMailFromServiceWs implements ProfileFromMailFromService {

    /**
     * <p>The following method generates a mail from header such as "Fotis"&lt; a @ b .com &gt;"
     * into a proper {@link Profile}</p>
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
            2.) Names are composed of a single work, the rest is the last name

            Alex Fiestas <afiestas@kde.org>
         */
        Pattern p = Pattern.compile("\"?([\\w\\?-]+) ([\\w\\?\\.\\d=\\\\ -]+)?\"?\\s*<(.*)>");

        Matcher matcher = p.matcher(mailFrom);

        if(!matcher.matches()){
            return null;
        }
        if(matcher.groupCount() < 2){
            return null;
        }

        Profile profile = new Profile();


        profile.setEmail(matcher.group(3).trim());
        profile.setName(matcher.group(1).trim());
        profile.setLastname(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(matcher.group(2)),""));

        return profile;

    }

}
