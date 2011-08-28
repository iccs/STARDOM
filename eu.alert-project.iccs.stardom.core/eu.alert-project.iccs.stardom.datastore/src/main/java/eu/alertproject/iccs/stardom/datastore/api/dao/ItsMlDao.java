package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.ml.ItsMl;

/**
 * User: fotis
 * Date: 27/08/11
 * Time: 17:32
 */
public interface ItsMlDao extends CommonDao<ItsMl> {

    ItsMl findByBugId(Integer bugId);

    ItsMl findLastestForBugId(Integer bugId);
}
