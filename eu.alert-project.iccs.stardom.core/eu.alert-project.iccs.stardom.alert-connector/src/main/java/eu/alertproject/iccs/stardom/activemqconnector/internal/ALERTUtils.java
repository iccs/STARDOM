package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.Author;
import eu.alertproject.iccs.events.converters.KESIDateConverter;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 15:22
 */
public class ALERTUtils {
    
    public static Profile extractProfile(Author author,String uri,String source){

        String name = author.getName();
        
        
        String[] split = name.split(" ");

        return new Profile(
            StringUtils.defaultIfEmpty(split[0], ""),
            (split.length >=2 ? StringUtils.substringAfter(name," ") : ""),
            author.getId(),
            author.getEmail(),
            uri,
            source
        );
        
        
    }

    public static int extractDays(MetricTemporal metric) {

        Date temporal = metric.getTemporal();
        return Days.daysBetween(new DateTime(temporal),new DateTime()).getDays();
    }
}
