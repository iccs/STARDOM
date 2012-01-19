package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
@Repository("profileDao")
public class JpaProfileDao extends JpaCommonDao<Profile> implements ProfileDao{
    protected JpaProfileDao() {
        super(Profile.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Profile> findByAny(String query) {
        
        Query q = getEntityManager().createQuery(
                " SELECT p FROM Profile  p " +
                        " WHERE LOWER(p.name)     LIKE :name " +
                        " OR    LOWER(p.lastname) LIKE :name " +
                        " OR    LOWER(p.email)    LIKE :name " +
                        " OR    LOWER(p.username) LIKE :name" +
                " ORDER BY p.id "
        );


        q.setParameter("name","%"+ query.toLowerCase()+"%");

        return q.getResultList();

    }
}
