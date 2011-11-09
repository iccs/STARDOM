package eu.alertproject.iccs.stardom.analyzers.mailing.api;

import eu.alertproject.iccs.stardom.domain.api.Profile;

/**
 * User: fotis
 * Date: 09/11/11
 * Time: 23:58
 */
public interface ProfileFromMailFromService {
    Profile generate(String mailFrom);
}
