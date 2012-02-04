package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 09:41
 * To change this template use File | Settings | File Templates.
 */
public interface    IdentityDao extends CommonDao<Identity> {

    List<Identity> findPossibleMatches(Profile profile);
    List<Identity> findAllPaginableOrderByLastName(int page, int pageSize);
    Identity findByProfileId(Integer profileId);
    Identity findByProfileUuid(String uuid);
}
