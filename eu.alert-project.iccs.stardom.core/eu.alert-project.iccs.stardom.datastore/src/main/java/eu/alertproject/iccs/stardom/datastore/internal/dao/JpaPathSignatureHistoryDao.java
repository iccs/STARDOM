package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.PathSignatureHistoryDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.PathSignatureHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * User: fotis
 * Date: 18/12/11
 * Time: 13:43
 */
@Repository("pathSignatureHistoryDao")
public class JpaPathSignatureHistoryDao extends JpaCommonDao<PathSignatureHistory> implements PathSignatureHistoryDao{

    private Logger logger = LoggerFactory.getLogger(JpaPathSignatureHistoryDao.class);

    protected JpaPathSignatureHistoryDao() {
        super(PathSignatureHistory.class);
    }

    @Override
    public PathSignatureHistory findByIdentityPathAndSignature(Identity identity, String path, String signature) {

        Query query = getEntityManager().createQuery(
                " SELECT p FROM PathSignatureHistory p " +
                " WHERE p.identity.uuid = :uuid " +
                " AND p.path = :path " +
                " AND p.signature = :signature"
        );


        query.setParameter("uuid",identity.getUuid());
        query.setParameter("path",path);
        query.setParameter("signature",signature);

        PathSignatureHistory pathSignatureHistory = null;
        try {
            pathSignatureHistory = (PathSignatureHistory) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("Could find signature {} for {} in {} ", new Object[]{signature,identity.getUuid(),path});
        }

        return pathSignatureHistory;
    }
}
