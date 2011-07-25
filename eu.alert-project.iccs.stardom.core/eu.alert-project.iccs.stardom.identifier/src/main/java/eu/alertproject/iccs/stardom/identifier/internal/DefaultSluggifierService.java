package eu.alertproject.iccs.stardom.identifier.internal;

import eu.alertproject.iccs.stardom.identifier.api.SluggifierService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * User: fotis
 * Date: 22/07/11
 * Time: 17:56
 */
@Service("sluggifierService")
public class DefaultSluggifierService implements SluggifierService{
    /**
     * @see SluggifierService
     */
    @Override
    public String sluggify(String string) {

        String str = StringUtils.trimToEmpty(string);
        if(StringUtils.isEmpty(str)){ return str; }

        String s = str.replaceAll("[-_\\+\\.@]", " ");
        return s.trim();


    }
}
