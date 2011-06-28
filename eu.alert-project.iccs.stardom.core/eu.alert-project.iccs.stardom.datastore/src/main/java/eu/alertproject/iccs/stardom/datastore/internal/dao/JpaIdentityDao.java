package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 09:43
 * To change this template use File | Settings | File Templates.
 */
@Repository("identityDao")
public class JpaIdentityDao extends JpaCommonDao<Identity> implements IdentityDao{
    protected JpaIdentityDao() {
        super(Identity.class);
    }

    @Override
    @Transactional
    public Identity insert(Identity bean) {
        Identity insert = super.insert(bean);//To change body of overridden methods use File | Settings | File Templates.
        insert.setUuid(org.apache.commons.codec.digest.DigestUtils.md5Hex(String.valueOf(insert.getId())));
        return insert;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Identity> findPossibleMatches(Profile profile) {

        Query query = getEntityManager().createQuery(
                "SELECT DISTINCT i FROM Identity i " +
                        " LEFT JOIN i.profiles p"+
                        " WHERE    " +
                        "       UPPER(p.email) = :email " +
                        " OR    UPPER(p.name) = :name" +
                        " OR    UPPER(p.lastname) = :lastname " +
                        " OR    UPPER(p.username) = :username");


        query.setParameter("email",profile.getEmail().toUpperCase());
        query.setParameter("name",profile.getName().toUpperCase());
        query.setParameter("lastname",profile.getLastname().toUpperCase());
        query.setParameter("username",profile.getUsername().toUpperCase());


        return query.getResultList();
    }
}
