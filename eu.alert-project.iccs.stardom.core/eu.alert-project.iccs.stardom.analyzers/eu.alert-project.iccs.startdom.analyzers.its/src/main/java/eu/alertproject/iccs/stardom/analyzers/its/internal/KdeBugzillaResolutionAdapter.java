package eu.alertproject.iccs.stardom.analyzers.its.internal;

import eu.alertproject.iccs.stardom.analyzers.its.api.ResolutionAdapter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * User: fotis
 * Date: 04/02/12
 * Time: 19:26
 */

public class KdeBugzillaResolutionAdapter implements ResolutionAdapter{


    @Override
    public boolean isResolved(String value) {
        return StringUtils.equalsIgnoreCase(value,"RESOLVED");
    }

    @Override
    public boolean isReopened(String value) {
        return StringUtils.equalsIgnoreCase(value,"REOPENED");
    }
}
