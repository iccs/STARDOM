package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ItsMlDao;
import eu.alertproject.iccs.stardom.domain.api.ml.ItsMl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * User: fotis
 * Date: 27/08/11
 * Time: 18:27
 */
@Repository("itsMlDao")
public class JpaItsMlDao extends JpaCommonDao<ItsMl> implements ItsMlDao {

    private Logger logger = LoggerFactory.getLogger(JpaItsMlDao.class);

    protected JpaItsMlDao() {
        super(ItsMl.class);
    }

    @Override
    public ItsMl findByBugId(Integer bugId) {

        Query query = getEntityManager().createQuery("SELECT i FROM ItsMl i WHERE i.bugId =:bugId");

        query.setParameter("bugId",bugId);


        ItsMl ml = null;

        try {
            ml = (ItsMl) query.getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Couldn't find ItsMl for bugId = {} ",bugId);
        }

        return ml;

    }

    @Override
    public ItsMl findLastestForBugId(Integer bugId) {

        Query query = getEntityManager().createQuery("SELECT i FROM ItsMl i WHERE i.bugId =:bugId ORDER BY i.when DESC");
        query.setMaxResults(1);
        query.setParameter("bugId",bugId);

        ItsMl ml = null;
        try {
            ml = (ItsMl) query.getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Couldn't find ItsMl for bugId = {} ",bugId);
        }

        return ml;


    }
}
