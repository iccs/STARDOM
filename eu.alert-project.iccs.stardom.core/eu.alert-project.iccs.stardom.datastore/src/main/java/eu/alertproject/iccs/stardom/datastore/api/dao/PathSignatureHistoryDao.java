package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.PathSignatureHistory;

/**
 * User: fotis
 * Date: 18/12/11
 * Time: 13:42
 */
public interface PathSignatureHistoryDao extends CommonDao<PathSignatureHistory>{

    PathSignatureHistory findByIdentityPathAndSignature(Identity identity, String path, String signature);

}
