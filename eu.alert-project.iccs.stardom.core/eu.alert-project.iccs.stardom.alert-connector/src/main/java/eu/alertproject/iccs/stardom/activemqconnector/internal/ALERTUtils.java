package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.Author;
import eu.alertproject.iccs.events.alert.KesiSCM;
import eu.alertproject.iccs.events.alert.MdServiceSCM;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 15:22
 */
public class ALERTUtils {


    public  static Profile extractProfile(Author author,String uri,String source){

        String name = author.getName();
        String[] split = new String[]{"",""};
        if(name != null ){
            split =name.split(" ");
        }


        String name1 = StringUtils.defaultIfEmpty(split[0], null);
        String lastname = split.length >= 2 ? StringUtils.substringAfter(name, " ") : null;
        String id = author.getId();
        String email = author.getEmail();
        return new Profile(
                name1 == null ? null :
                        ("none".equals(name1.toLowerCase()) ? null : name1),
                lastname == null ? null:
                        ("none".equals(lastname.toLowerCase()) ? null : lastname),
                id == null ? null :
                        ("none".equals(id.toLowerCase()) ? null : id),
                email == null ? null :
                        ("none".equals(email.toLowerCase()) ?null :email),
                uri,
                source
        );


    }


    public static Profile extractProfile(KesiSCM kesi,MdServiceSCM mdservice,String source){

        Author author = kesi.getAuthor();
        String authorUri = mdservice.getAuthorUri();


        if(author == null){
            author = kesi.getCommitter();
            authorUri = mdservice.getCommitterUri();
        }


        return extractProfile(author,authorUri,source);

        
    }

    public static int extractDays(MetricTemporal metric) {

        Date temporal = metric.getTemporal();
        return Days.daysBetween(new DateTime(temporal),new DateTime()).getDays();
    }
}
